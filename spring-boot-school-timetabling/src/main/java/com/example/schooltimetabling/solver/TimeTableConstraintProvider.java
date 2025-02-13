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

import java.time.Duration;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import com.example.schooltimetabling.domain.Lesson;

import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.ROOM_CONFLICT;
import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.TEACHER_CONFLICT;
import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.STUDENT_CONFLICT;
import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.SMITH_PREFERS_TUESDAY;
import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.SMITH_HATES_TUESDAY;
import static com.example.schooltimetabling.domain.SchoolTimeTableConstraintConfiguration.SMITH_WANTS_TWO_CLASS;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        
        return new Constraint[] {
                // Hard constraints
                roomConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                smithPrefersTuesday(constraintFactory),
                smithHatesTuesday(constraintFactory),
                smithPrefersConscecutiveClassOnTuesday(constraintFactory)
                // Soft constraints
                // teacherRoomStability(constraintFactory),
                // teacherTimeEfficiency(constraintFactory),
                // studentGroupSubjectVariety(constraintFactory)
        };
    }

    Constraint roomConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                // Select each pair of 2 different lessons ...
                .fromUniquePair(Lesson.class,
                        // ... in the same timeslot ...
                        Joiners.equal(Lesson::getTimeslot),
                        // ... in the same room ...
                        Joiners.equal(Lesson::getRoom))
                        
                // ... and penalize each pair with a hard weight.
                .penalizeConfigurable(ROOM_CONFLICT);
                //.penalize("Room conflict", HardSoftScore.ONE_HARD);

      
    }


    Constraint teacherConflict(ConstraintFactory constraintFactory) {
        // A teacher can teach at most one lesson at the same time.
        return constraintFactory
                .fromUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getTeacher))
                .penalizeConfigurable(TEACHER_CONFLICT);
                //.penalize("Teacher conflict", HardSoftScore.ONE_HARD);
    }

    Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        // A student can attend at most one lesson at the same time.
        return constraintFactory
                .fromUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getStudentGroup))
                .penalizeConfigurable(STUDENT_CONFLICT);
                //.penalize("Student group conflict", HardSoftScore.ONE_HARD);
    }

     Constraint smithPrefersTuesday(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach in a single room.
        return constraintFactory               
                .from(Lesson.class)
                .filter((lesson) -> lesson.getTeacher().equals("Mr. Smith") && lesson.getTimeslot().getDayOfWeek().getValue() == 2)
                .rewardConfigurable(SMITH_PREFERS_TUESDAY);
    }

    Constraint smithHatesTuesday(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach in a single room.
        return constraintFactory               
                .from(Lesson.class)
                .filter((lesson) -> lesson.getTeacher().equals("Mr. Smith") && lesson.getTimeslot().getDayOfWeek().getValue() == 2)
                .penalizeConfigurable(SMITH_HATES_TUESDAY);
    }

    Constraint smithPrefersConscecutiveClassOnTuesday(ConstraintFactory constraintFactory) {
        // A student group dislikes sequential lessons on the same subject.
        return constraintFactory
                .from(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getSubject),
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0 && 
                        lesson1.getTimeslot().getDayOfWeek().getValue() == 2 && 
                        lesson1.getStudentGroup().equals(lesson2.getStudentGroup()) &&
                        lesson1.getRoom().getId().equals(lesson2.getRoom().getId()) &&
                        lesson1.getTeacher().equals("Mr. Smith") &&
                        lesson1.getTeacher().equals(lesson2.getTeacher()) ;
                })
                .rewardConfigurable(SMITH_WANTS_TWO_CLASS);
    }
/*    Constraint teacherRoomStability(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach in a single room.
        return constraintFactory
                .fromUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTeacher))
                .filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom())
                .penalize("Teacher room stability", HardSoftScore.ONE_SOFT);
    }

    Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach sequential lessons and dislikes gaps between lessons.
        return constraintFactory
                .from(Lesson.class)
                .join(Lesson.class, Joiners.equal(Lesson::getTeacher),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward("Teacher time efficiency", HardSoftScore.ONE_SOFT);
    }

    Constraint studentGroupSubjectVariety(ConstraintFactory constraintFactory) {
        // A student group dislikes sequential lessons on the same subject.
        return constraintFactory
                .from(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getSubject),
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .penalize("Student group subject variety", HardSoftScore.ONE_SOFT);
    } */

}
