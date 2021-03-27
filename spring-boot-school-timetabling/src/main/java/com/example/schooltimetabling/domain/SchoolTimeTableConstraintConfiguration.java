package com.example.schooltimetabling.domain;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
//import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;


@ConstraintConfiguration
public class SchoolTimeTableConstraintConfiguration {

    public static final String ROOM_CONFLICT = "Room conflict";

    public static final String TEACHER_CONFLICT = "Teacher conflict";

    public static final String STUDENT_CONFLICT = "Student group conflict";

    public static final String SMITH_PREFERS_TUESDAY = "Smith Prefers Tuesday";

    public static final String SMITH_HATES_TUESDAY = "Smith Hates Tuesday";

    public static final String SMITH_WANTS_TWO_CLASS = "Smith Wants consecutive class";


    @ConstraintWeight(ROOM_CONFLICT)
    private HardSoftScore roomConflict = HardSoftScore.ofHard(10);

    @ConstraintWeight(TEACHER_CONFLICT)
    private HardSoftScore teacherConflict = HardSoftScore.ofHard(10);

    @ConstraintWeight(STUDENT_CONFLICT)
    private HardSoftScore studentConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(SMITH_PREFERS_TUESDAY)
    private HardSoftScore smithPrefersTuesday = HardSoftScore.ofHard(0);

    @ConstraintWeight(SMITH_HATES_TUESDAY)
    private HardSoftScore smithHatesTuesday = HardSoftScore.ofHard(0);

    @ConstraintWeight(SMITH_WANTS_TWO_CLASS)
    private HardSoftScore smithWantsTwoClass = HardSoftScore.ofHard(0);

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


    public HardSoftScore getSmithPrefersTuesday() {
        return smithPrefersTuesday;
    }

    public void setSmithPrefersTuesday(HardSoftScore smithPrefersTuesday) {
        this.smithPrefersTuesday = smithPrefersTuesday;
    }


    public HardSoftScore getSmithHatesTuesday() {
        return smithHatesTuesday;
    }

    public void setSmithHatesTuesday(HardSoftScore smithHatesTuesday) {
        this.smithHatesTuesday = smithHatesTuesday;
    }

    public HardSoftScore getSmithWantsTwoClass  () {
        return smithWantsTwoClass  ;
    }

    public void setSmithWantsTwoClass  (HardSoftScore smithWantsTwoClass  ) {
        this.smithWantsTwoClass   = smithWantsTwoClass  ;
    }

}