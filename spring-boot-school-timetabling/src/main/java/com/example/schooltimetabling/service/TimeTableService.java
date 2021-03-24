package com.example.schooltimetabling.service;

import com.example.schooltimetabling.domain.ConstraintWeights;
import com.example.schooltimetabling.domain.TimeTable;
import com.example.schooltimetabling.persistence.TimeTableRepository;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;
    
    public TimeTable getTimeTable (Long problemId, ConstraintWeights constraintWeight){
        return timeTableRepository.findByIdAndConstraints(problemId, constraintWeight);                            
    }
}
