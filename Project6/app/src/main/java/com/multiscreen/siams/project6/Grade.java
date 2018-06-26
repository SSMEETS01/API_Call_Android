package com.multiscreen.siams.project6;

public class Grade {

    String CourseCode, Score, ResultDate;

    public Grade(String courseCode, String score, String resultDate) {
        CourseCode = courseCode;
        Score = score;
        ResultDate = resultDate;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public String getScore() {
        return Score;
    }

    public String getResultDate() {
        return ResultDate;
    }
}
