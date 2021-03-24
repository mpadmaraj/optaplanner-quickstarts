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

import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.spring.boot.autoconfigure.SolverManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.schooltimetabling.TimeTableSpringBootApp;
import com.example.schooltimetabling.domain.TimeTable;
import com.example.schooltimetabling.persistence.TimeTableRepository;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private SolverManager<TimeTable, Long> solverManager;
    @Autowired
    private ScoreManager<TimeTable, HardSoftScore> scoreManager;

    // To try, GET http://localhost:8080/timeTable
    @GetMapping()
    public TimeTable getTimeTable() {
        // Get the solver status before loading the solution
        // to avoid the race condition that the solver terminates between them
        SolverStatus solverStatus = getSolverStatus();
        TimeTable solution = timeTableRepository.findById(TimeTableRepository.SINGLETON_TIME_TABLE_ID);        
        scoreManager.updateScore(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        //System.out.println(scoreManager.explainScore(solution));
        return solution;
    }

    @GetMapping("/reset")
    public void  Reset() {
      TimeTableSpringBootApp.restart();
    }

    @PostMapping("/solve")
    public void solve( @RequestBody ConstraintWeight constraintWeight) {

        solverManager.solveAndListen(TimeTableRepository.SINGLETON_TIME_TABLE_ID,
                problemId -> {
                    TimeTable timeTable = timeTableRepository.findById(problemId);                    
                   
                    if(null != constraintWeight.getRoomWeight()) {
                        timeTable.getConstraintConfiguration().setRoomConflict(HardSoftScore.ofHard(constraintWeight.getRoomWeight()));
                    }
                    System.out.println("weight::"+  timeTable.getConstraintConfiguration().getRoomConflict());
                    return timeTable;
                },
                timeTableRepository::save);
    }
    
    
    @PostMapping("/solve/stop")
    public void solveStop() {
        
        solverManager.terminateEarly(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

    @PostMapping("/reason")
    public ScoreExplanation<TimeTable, HardSoftScore> reason() {
        TimeTable solution = timeTableRepository.findById(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
        return scoreManager.explainScore(solution);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

    public static class ConstraintWeight {
        private Integer roomWeight;
        private Integer teacherWeight;
        private Integer studentWeight;
        
        public Integer getRoomWeight() {
            return roomWeight;
        }

        public void setRoomWeight(Integer weight) {
            this.roomWeight = weight;
        }

        public Integer getTeacherWeight() {
            return teacherWeight;
        }

        public void setTeacherWeight(Integer weight) {
            this.teacherWeight = weight;
        }

        public Integer getStudentWeight() {
            return studentWeight;
        }

        public void setStudentWeight(Integer weight) {
            this.studentWeight = weight;
        }
    }
}
