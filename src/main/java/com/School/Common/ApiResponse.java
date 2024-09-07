package com.School.Common;

import com.School.Entity.Student;

public class ApiResponse {
    private Student student;
    private String message;

    public ApiResponse(Student student, String message) {
        this.student = student;
        this.message = message;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
