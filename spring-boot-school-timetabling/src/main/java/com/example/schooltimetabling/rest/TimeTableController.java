/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.schooltimetabling.rest;

import java.util.HashMap;
import java.util.Map;

import com.example.schooltimetabling.TimeTableSpringBootApp;
import com.example.schooltimetabling.domain.TimeTableSolution;
import com.example.schooltimetabling.domain.UserConstraints;
import com.example.schooltimetabling.persistence.TimeTableRepository;

import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    @Autowired
    private TimeTableRepository timeTableRepository;

    public static Map<String, Long> timeTableId;
    static {
        timeTableId = new HashMap<>();
        timeTableId.put("ROOM_CONFLICT", 1L);
        timeTableId.put("TEACHER_CONFLICT", 2L);
        timeTableId.put("STUDENT_CONFLICT", 3L);
        timeTableId.put("TEACH_DAY_PREFERENCE", 4L);
        timeTableId.put("TEACH_DAY_DISLIKE", 5L);
        timeTableId.put("ALL_CONFLICT", 5L);
        timeTableId.put("TEACH_DAY_DISLIKE", 5L);
    }

    @Autowired
    private SolverManager<TimeTableSolution, Long> solverManager;

    @Autowired
    private ScoreManager<TimeTableSolution, HardSoftScore> scoreManager;

    // To try, GET http://localhost:8080/timeTable
    @PostMapping("/{problemId}")
    public TimeTableSolution getTimeTable( @PathVariable Long problemId,  @RequestBody UserConstraints constraintWeight) {
        // Get the solver status before loading the solution
        // to avoid the race condition that the solver terminates between them
        SolverStatus solverStatus = getSolverStatus();
        TimeTableSolution solution = timeTableRepository.findByIdAndConstraints(problemId, constraintWeight, false, false);        
        scoreManager.updateScore(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        //System.out.println(scoreManager.explainScore(solution));
        return solution;
    }

    private Long generateProblemId() {
/*        long leftLimit = 1L;
        long rightLimit = 100L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return generatedLong; */
        return TimeTableRepository.SINGLETON_TIME_TABLE_ID;
    }

    @GetMapping("/reset")
    public void  Reset() {
      TimeTableSpringBootApp.restart();
    }

    @PostMapping("/solve")
    public Long solve( @RequestBody UserConstraints constraintWeight) {

        Long problemId = generateProblemId();
        SolverJob<TimeTableSolution, Long> solveAndListen = solverManager.solveAndListen(problemId,
                id -> {
                    if(constraintWeight.getEnableTuesday()) {
                        return timeTableRepository.findByIdAndConstraints(id, constraintWeight, true, true);
                    } else {
                        return timeTableRepository.findByIdAndConstraints(id, constraintWeight, true, false);
                    }
                    
                },
                timeTableRepository::save);      
        return problemId;          
    }
    
    
    @PostMapping("/solve/stop/{problemId}")
    public void solveStop(@PathVariable Long problemId) {
        
        solverManager.terminateEarly(problemId);
    }

    @PostMapping("/reason/{problemId}")
    public ScoreExplanation<TimeTableSolution, HardSoftScore> reason(@PathVariable Long problemId) {
        TimeTableSolution solution = timeTableRepository.findById(problemId);
        return scoreManager.explainScore(solution);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

}
