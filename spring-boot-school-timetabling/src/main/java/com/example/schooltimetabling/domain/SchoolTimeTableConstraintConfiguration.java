package com.example.schooltimetabling.domain;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
//import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;


@ConstraintConfiguration
public class SchoolTimeTableConstraintConfiguration {

    public static final String ROOM_CONFLICT = "Room conflict";

    public static final String ROOM_CONFLICT_IGNORE = "Room conflict ignore";

    public static final String TEACHER_CONFLICT = "Teacher conflict";

    public static final String STUDENT_CONFLICT = "Student group conflict";


    @ConstraintWeight(ROOM_CONFLICT)
    private HardSoftScore roomConflict = HardSoftScore.ofHard(10);

    @ConstraintWeight(TEACHER_CONFLICT)
    private HardSoftScore teacherConflict = HardSoftScore.ofHard(10);

    @ConstraintWeight(STUDENT_CONFLICT)
    private HardSoftScore studentConflict = HardSoftScore.ofHard(1);

    public HardSoftScore getRoomConflict() {
        return roomConflict;
    }

    public void setRoomConflict(HardSoftScore roomConflict) {
        if(null != roomConflict) {
            this.roomConflict = roomConflict;
        }
        
    }


    public HardSoftScore getTeacherConflict() {
        return teacherConflict;
    }

    public void setTeacherConflict(HardSoftScore teacherConflict) {
        this.teacherConflict = teacherConflict;
    }

    public HardSoftScore getStudentConflict() {
        return studentConflict;
    }

    public void setStudentConflict(HardSoftScore studentConflict) {
        this.studentConflict = studentConflict;
    }

}