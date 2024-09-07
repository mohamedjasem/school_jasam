package com.School.Entity;



import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudentWithImageUrl {
    private Long id;
    private String rollNo;
    private String name;
    private String dob;
    private String gender;
    private String email;
    private String standard;
    private String mobileNumber;
    private String address;
    private String profileImageUrl;  // The URL of the profile image

    public StudentWithImageUrl(Student student, String profileImageUrl) {
        this.id = student.getRegisterNo();
        this.rollNo = student.getRollNo();
        this.name = student.getName();
        this.dob = student.getDob();
        this.gender = student.getGender();
        this.email = student.getEmail();
        this.standard = student.getStandard();
        this.mobileNumber = student.getMobileNumber();
        this.address = student.getAddress();
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and setters
}
