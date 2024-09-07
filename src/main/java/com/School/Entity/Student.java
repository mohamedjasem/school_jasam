package com.School.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registerNo;
    @NotBlank(message = "Roll number is mandatory")
    private String rollNo;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String dob;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Standard is mandatory")
    private String standard;



    private String mobileNumber;

    @NotBlank(message = "Address is mandatory")
    private String address;

    private String profileImagePath;
}
