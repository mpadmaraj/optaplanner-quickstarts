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

package com.example.schooltimetabling;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.schooltimetabling.domain.Lesson;
import com.example.schooltimetabling.domain.Room;
import com.example.schooltimetabling.domain.Timeslot;
import com.example.schooltimetabling.persistence.LessonRepository;
import com.example.schooltimetabling.persistence.RoomRepository;
import com.example.schooltimetabling.persistence.TimeslotRepository;
import org.springframework.boot.ApplicationArguments;

@SpringBootApplication
public class TimeTableSpringBootApp {
 private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(TimeTableSpringBootApp.class, args);
    }

    @Value("${timeTable.demoData:SMALL}")
    private DemoData demoData;

    @Bean
    public CommandLineRunner demoData(
            TimeslotRepository timeslotRepository,
            RoomRepository roomRepository,
            LessonRepository lessonRepository) {
        return (args) -> {
            if (demoData == DemoData.NONE) {
                return;
            }

            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
/*            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));*/
            if (demoData == DemoData.LARGE) {
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
            }

            roomRepository.save(new Room("Room 1"));
            roomRepository.save(new Room("Room 2"));
            roomRepository.save(new Room("Room 3"));
            if (demoData == DemoData.LARGE) {
                roomRepository.save(new Room("Room 4"));
                roomRepository.save(new Room("Room 5"));
                roomRepository.save(new Room("Room 6"));
            }

            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Math", "Mr. Smith", "10th grade"));
            lessonRepository.save(new Lesson("Physics", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("Chemistry", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("French", "Mr. Rajan", "10th grade"));
            lessonRepository.save(new Lesson("Geography", "Mr. Harris", "10th grade"));
            lessonRepository.save(new Lesson("History", "Mr. D'Souza", "10th grade"));
            if (demoData == DemoData.LARGE) {
                lessonRepository.save(new Lesson("Math", "A. Jones", "9th grade"));
                lessonRepository.save(new Lesson("Math", "A. Jones", "9th grade"));
                lessonRepository.save(new Lesson("Math", "A. Jones", "9th grade"));
                lessonRepository.save(new Lesson("ICT", "A. Jones", "9th grade"));
                lessonRepository.save(new Lesson("Physics", "M. Lal", "9th grade"));
                lessonRepository.save(new Lesson("Geography", "C. Corey", "9th grade"));
                lessonRepository.save(new Lesson("Geology", "C. Darwin", "9th grade"));
                lessonRepository.save(new Lesson("History", "I. Singh", "9th grade"));
                lessonRepository.save(new Lesson("English", "I. Singh", "9th grade"));
                lessonRepository.save(new Lesson("Drama", "I. Singh", "9th grade"));
                lessonRepository.save(new Lesson("Art", "S. Dali", "9th grade"));
                lessonRepository.save(new Lesson("Art", "S. Dali", "9th grade"));
                lessonRepository.save(new Lesson("Physical education", "C. Kumar", "9th grade"));
                lessonRepository.save(new Lesson("Physical education", "C. Kumar", "9th grade"));
                lessonRepository.save(new Lesson("Physical education", "C. Kumar", "9th grade"));
            }
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
        };
    }

    public enum DemoData {
        NONE,
        SMALL,
        LARGE
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(TimeTableSpringBootApp.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }

}
