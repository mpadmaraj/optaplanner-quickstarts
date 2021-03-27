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

package com.example.schooltimetabling.persistence;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.example.schooltimetabling.domain.UserConstraints;
import com.example.schooltimetabling.domain.Lesson;
import com.example.schooltimetabling.domain.Room;
import com.example.schooltimetabling.domain.TimeTableSolution;
import com.example.schooltimetabling.domain.Timeslot;

@Service
@Transactional
public class TimeTableRepository {

    // There is only one time table, so there is only timeTableId (= problemId).
    public static final Long SINGLETON_TIME_TABLE_ID = 1L;

    @Autowired
    private TimeslotRepository timeslotRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private LessonRepository lessonRepository;

    
    public TimeTableSolution findById(Long id) {
        /*if (!SINGLETON_TIME_TABLE_ID.equals(id)) {
            throw new IllegalStateException("There is no timeTable with id (" + id + ").");
        } */
        // Occurs in a single transaction, so each initialized lesson references the same timeslot/room instance
        // that is contained by the timeTable's timeslotList/roomList.
        TimeTableSolution timeTable = new TimeTableSolution(
                timeslotRepository.findAll(),
                roomRepository.findAll(),
                lessonRepository.findAll());
                     
        return timeTable;
    }

    public TimeTableSolution findByIdAndConstraints(Long id, UserConstraints constraintWeight, boolean isSolver, boolean enableTuesday) {
        /* if (!SINGLETON_TIME_TABLE_ID.equals(id)) {
            throw new IllegalStateException("There is no timeTable with id (" + id + ").");
        } */
        // Occurs in a single transaction, so each initialized lesson references the same timeslot/room instance
        // that is contained by the timeTable's timeslotList/roomList.
        if(isSolver) {
            timeslotRepository.deleteAll();
            roomRepository.deleteAll();
            lessonRepository.deleteAll();
            generateDemoData(enableTuesday);
        }
        TimeTableSolution timeTable = new TimeTableSolution(
                timeslotRepository.findAll(),
                roomRepository.findAll(),
                lessonRepository.findAll());
        if(null != constraintWeight.getRoomWeight()) {
            timeTable.getConstraintConfiguration().setRoomConflict(HardSoftScore.ofHard(constraintWeight.getRoomWeight()));
        }

        if(null != constraintWeight.getTeacherWeight()) {
            timeTable.getConstraintConfiguration().setTeacherConflict(HardSoftScore.ofHard(constraintWeight.getTeacherWeight()));
        }

        if(null != constraintWeight.getStudentWeight()) {
            timeTable.getConstraintConfiguration().setStudentConflict(HardSoftScore.ofHard(constraintWeight.getStudentWeight()));
        }

        if(constraintWeight.getSmithPrefersTuesday()) {
            timeTable.getConstraintConfiguration().setSmithPrefersTuesday(HardSoftScore.ofHard(1000));
        }

        if(constraintWeight.getSmithHatesTuesday()) {
            timeTable.getConstraintConfiguration().setSmithHatesTuesday(HardSoftScore.ofHard(1000));
        }

        if(constraintWeight.getSmithTwoClassOnTuesday()) {
            timeTable.getConstraintConfiguration().setSmithWantsTwoClass(HardSoftScore.ofHard(1000));
        }
             
        return timeTable;
    }
    
    public void save(TimeTableSolution timeTable) {
        for (Lesson lesson : timeTable.getLessonList()) {
            // TODO this is awfully naive: optimistic locking causes issues if called by the SolverManager
            lessonRepository.save(lesson);
        }
    }

    private void generateDemoData(boolean enableTuesday) {

            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
            if(enableTuesday) {
                timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));                
            }

/*            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 15)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 15), LocalTime.of(10, 00)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 00), LocalTime.of(10, 45)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 15)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(14, 15), LocalTime.of(15, 00))); */
            

            roomRepository.save(new Room("Room 1"));
            roomRepository.save(new Room("Room 2"));
            roomRepository.save(new Room("Room 3"));

            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Physics", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("Chemistry", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("French", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("Geography", "Mr. Harris", "10th grade"));
            lessonRepository.save(new Lesson("History", "Mr. D'Souza", "10th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "9th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "9th grade"));
            lessonRepository.save(new Lesson("Physics", "Mr. Rajan", "9th grade"));
            lessonRepository.save(new Lesson("Chemistry", "Mr. Rajan", "9th grade"));
            lessonRepository.save(new Lesson("Biology", "Mr. Harris", "9th grade"));
            lessonRepository.save(new Lesson("History", "Mr. D'Souza", "9th grade"));
            

            Lesson lesson = lessonRepository.findAll(Sort.by("id")).iterator().next();
            lesson.setTimeslot(timeslotRepository.findAll(Sort.by("id")).iterator().next());
            lesson.setRoom(roomRepository.findAll(Sort.by("id")).iterator().next());

            lessonRepository.save(lesson);       
    }

}
