package com.example.schooltimetabling.domain;

public class UserConstraints {
    private Integer roomWeight;
    private Integer teacherWeight;
    private Integer studentWeight;
    private Boolean enableTuesday;
    private Boolean smithPrefersTuesday;
    private Boolean smithHatesTuesday;
    private Boolean smithTwoClassOnTuesday;

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

        public boolean getEnableTuesday() {
        return enableTuesday;
    }

    public void setEnableTuesday(Boolean enableTuesday) {
            this.enableTuesday = enableTuesday;
    }

    public Boolean getSmithPrefersTuesday() {
        return smithPrefersTuesday;
    }

    public void setSmithPrefersTuesday(Boolean smithPrefersTuesday) {
        this.smithPrefersTuesday = smithPrefersTuesday;
    }

    public Boolean getSmithHatesTuesday() {
        return smithHatesTuesday;
    }

    public void setSmithHatesTuesday(Boolean smithHatesTuesday) {
        this.smithHatesTuesday = smithHatesTuesday;
    }

    
    public Boolean getSmithTwoClassOnTuesday() {
        return smithTwoClassOnTuesday;
    }

    public void setSmithTwoClassOnTuesday(Boolean smithTwoClassOnTuesday) {
        this.smithTwoClassOnTuesday = smithTwoClassOnTuesday;
    }
}
