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

package com.example.schooltimetabling.solver;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.example.schooltimetabling.domain.Lesson;
import com.example.schooltimetabling.domain.Room;
import com.example.schooltimetabling.domain.TimeTableSolution;
import com.example.schooltimetabling.domain.Timeslot;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimeTableConstraintProviderTest {

    private static final Room ROOM1 = new Room(1, "Room1");
    private static final Room ROOM2 = new Room(2, "Room2");
    private static final Timeslot TIMESLOT1 = new Timeslot(1, DayOfWeek.MONDAY, LocalTime.NOON);
    private static final Timeslot TIMESLOT2 = new Timeslot(2, DayOfWeek.TUESDAY, LocalTime.NOON);
    private static final Timeslot TIMESLOT3 = new Timeslot(3, DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(1));
    private static final Timeslot TIMESLOT4 = new Timeslot(4, DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(3));

    @Autowired
    ConstraintVerifier<TimeTableConstraintProvider, TimeTableSolution> constraintVerifier;

    @Test
    void roomConflict() {
        Lesson firstLesson = new Lesson(1, "Subject1", "Teacher1", "Group1", TIMESLOT1, ROOM1);
        Lesson conflictingLesson = new Lesson(2, "Subject2", "Teacher2", "Group2", TIMESLOT1, ROOM1);
        Lesson nonConflictingLesson = new Lesson(3, "Subject3", "Teacher3", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void teacherConflict() {
        String conflictingTeacher = "Teacher1";
        Lesson firstLesson = new Lesson(1, "Subject1", conflictingTeacher, "Group1", TIMESLOT1, ROOM1);
        Lesson conflictingLesson = new Lesson(2, "Subject2", conflictingTeacher, "Group2", TIMESLOT1, ROOM2);
        Lesson nonConflictingLesson = new Lesson(3, "Subject3", "Teacher2", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void studentGroupConflict() {
        String conflictingGroup = "Group1";
        Lesson firstLesson = new Lesson(1, "Subject1", "Teacher1", conflictingGroup, TIMESLOT1, ROOM1);
        Lesson conflictingLesson = new Lesson(2, "Subject2", "Teacher2", conflictingGroup, TIMESLOT1, ROOM2);
        Lesson nonConflictingLesson = new Lesson(3, "Subject3", "Teacher3", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void teacherRoomStability() {
        String teacher = "Teacher1";
        Lesson lessonInFirstRoom = new Lesson(1, "Subject1", teacher, "Group1", TIMESLOT1, ROOM1);
        Lesson lessonInSameRoom = new Lesson(2, "Subject2", teacher, "Group2", TIMESLOT1, ROOM1);
        Lesson lessonInDifferentRoom = new Lesson(3, "Subject3", teacher, "Group3", TIMESLOT1, ROOM2);
        /*constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomStability)
                .given(lessonInFirstRoom, lessonInDifferentRoom, lessonInSameRoom)
                .penalizesBy(2); */
    }

    @Test
    void teacherTimeEfficiency() {
        String teacher = "Teacher1";
        Lesson singleLessonOnMonday = new Lesson(1, "Subject1", teacher, "Group1", TIMESLOT1, ROOM1);
        Lesson firstTuesdayLesson = new Lesson(2, "Subject2", teacher, "Group2", TIMESLOT2, ROOM1);
        Lesson secondTuesdayLesson = new Lesson(3, "Subject3", teacher, "Group3", TIMESLOT3, ROOM1);
        Lesson thirdTuesdayLessonWithGap = new Lesson(4, "Subject4", teacher, "Group4", TIMESLOT4, ROOM1);
        /* constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherTimeEfficiency)
                .given(singleLessonOnMonday, firstTuesdayLesson, secondTuesdayLesson, thirdTuesdayLessonWithGap)
                .rewardsWith(1); */ // Second tuesday lesson immediately follows the first.
    }

    @Test
    void studentGroupSubjectVariety() {
        String studentGroup = "Group1";
        String repeatedSubject = "Subject1";
        Lesson mondayLesson = new Lesson(1, repeatedSubject, "Teacher1", studentGroup, TIMESLOT1, ROOM1);
        Lesson firstTuesdayLesson = new Lesson(2, repeatedSubject, "Teacher2", studentGroup, TIMESLOT2, ROOM1);
        Lesson secondTuesdayLesson = new Lesson(3, repeatedSubject, "Teacher3", studentGroup, TIMESLOT3, ROOM1);
        Lesson thirdTuesdayLessonWithDifferentSubject = new Lesson(4, "Subject2", "Teacher4", studentGroup, TIMESLOT4, ROOM1);
        Lesson lessonInAnotherGroup = new Lesson(5, repeatedSubject, "Teacher5", "Group2", TIMESLOT1, ROOM1);
/*        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupSubjectVariety)
                .given(mondayLesson, firstTuesdayLesson, secondTuesdayLesson, thirdTuesdayLessonWithDifferentSubject,
                        lessonInAnotherGroup)
                .penalizesBy(1); */ // Second tuesday lesson immediately follows the first.
    }

}
