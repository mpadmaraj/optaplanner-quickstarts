package com.example.schooltimetabling.service;

import com.example.schooltimetabling.domain.UserConstraints;
import com.example.schooltimetabling.domain.TimeTableSolution;
import com.example.schooltimetabling.persistence.TimeTableRepository;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;
    
    public TimeTableSolution getTimeTable (Long problemId, UserConstraints constraintWeight){
        return timeTableRepository.findByIdAndConstraints(problemId, constraintWeight, true, false);                            
    }
}
