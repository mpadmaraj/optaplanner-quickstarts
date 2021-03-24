package com.example.schooltimetabling.domain;

public class ConstraintWeights {
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
