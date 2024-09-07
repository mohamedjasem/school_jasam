package com.School.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.School.Common.ApiResponse;
import com.School.Entity.Student;
import com.School.Entity.StudentWithImageUrl;
import com.School.Repository.SRepo;
import com.School.Service.StudentDao;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/api/student")
@PreAuthorize("hasRole('ADMIN')")
public class StudentController {
	@Autowired
	private SRepo repo;

@Autowired
	private StudentDao service;

@PostMapping("/save")
public ResponseEntity<ApiResponse> createStudent(
        @RequestParam("rollNo") String rollNo,
        @RequestParam("name") String name,
        @RequestParam("dob") String dob,
        @RequestParam("gender") String gender,
        @RequestParam("email") String email,
        @RequestParam("standard") String standard,
        @RequestParam("mobileNumber") String mobileNumber,
        @RequestParam("address") String address,
        @RequestParam("file") MultipartFile file) {

    // Validate required fields
    if (rollNo == null || rollNo.isEmpty() ||
        name == null || name.isEmpty() ||
        dob == null || dob.isEmpty() ||
        gender == null || gender.isEmpty() ||
        email == null || email.isEmpty() ||
        standard == null || standard.isEmpty() ||
        mobileNumber == null || mobileNumber.isEmpty() ||
        address == null || address.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse(null, "All fields are required."));
    }

    // Check if file is missing
    if (file.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse(null, "Profile image file is required."));
    }
    
 // Check if rollNo already exists
    if (repo.existsByRollNo(rollNo)) {
        return ResponseEntity.badRequest().body(new ApiResponse(null, "Roll number already exists. Please provide a unique roll number."));
    }

    try {
        // Store the file and get the path
        String profileImagePath = service.storeFile(file);

        // Create and save student entity
        Student student = new Student();
        student.setRollNo(rollNo);
        student.setName(name);
        student.setDob(dob);
        student.setGender(gender);
        student.setEmail(email);
        student.setStandard(standard);
        student.setMobileNumber(mobileNumber);
        student.setAddress(address);
        student.setProfileImagePath(profileImagePath);

        // Save the student entity
        Student savedStudent = repo.save(student);

        // Return response with student data and success message
        return ResponseEntity.ok(new ApiResponse(savedStudent, "Student created successfully"));

    } catch (IOException e) {
        // Handle file storage exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(null, "An error occurred while storing the file."));
    } catch (Exception e) {
        // Handle any other exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(null, "An unexpected error occurred."));
    }
}



@PutMapping("/update")
public ResponseEntity<ApiResponse> updateStudent(
        @RequestParam("rollNo") String rollNo,
        @RequestParam("name") String name,
        @RequestParam("dob") String dob,
        @RequestParam("gender") String gender,
        @RequestParam("email") String email,
        @RequestParam("standard") String standard,
        @RequestParam("mobileNumber") String mobileNumber,
        @RequestParam("address") String address,
        @RequestParam(value = "file", required = false) MultipartFile file) {

    // Validate required fields
    if (rollNo == null || rollNo.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse(null, "Roll number is required."));
    }

    try {
        // Check if student with the given rollNo exists
        Student existingStudent = repo.findByRollNo(rollNo);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(null, "Student with roll number " + rollNo + " not found."));
        }

        // Update student details
        existingStudent.setName(name != null ? name : existingStudent.getName());
        existingStudent.setDob(dob != null ? dob : existingStudent.getDob());
        existingStudent.setGender(gender != null ? gender : existingStudent.getGender());
        existingStudent.setEmail(email != null ? email : existingStudent.getEmail());
        existingStudent.setStandard(standard != null ? standard : existingStudent.getStandard());
        existingStudent.setMobileNumber(mobileNumber != null ? mobileNumber : existingStudent.getMobileNumber());
        existingStudent.setAddress(address != null ? address : existingStudent.getAddress());

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            String profileImagePath = service.storeFile(file);
            existingStudent.setProfileImagePath(profileImagePath);
        }

        // Save the updated student entity
        Student updatedStudent = repo.save(existingStudent);

        return ResponseEntity.ok(new ApiResponse(updatedStudent, "Updated successfully"));

    } catch (IOException e) {
        // Handle file storage exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(null, "An error occurred while storing the file."));
    } catch (Exception e) {
        // Handle any other exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(null, "An unexpected error occurred."));
    }
}




@GetMapping("/findRollNo")
public ResponseEntity<StudentWithImageUrl> findStudentBy( @RequestParam String rollNo) {
    // Fetch the student from the repository
    Student student = repo.findByRollNo(rollNo);

    // Check if the student is found
    if (student == null) {
        return ResponseEntity.notFound().build();
    }

    // Get the image URL
    String imageUrl = service.getFileUrl(student.getProfileImagePath());

    // Create a response with the student details and image URL
    StudentWithImageUrl studentWithImage = new StudentWithImageUrl(student, imageUrl);

    return ResponseEntity.ok(studentWithImage);
}



@GetMapping("/all")
public ResponseEntity<List<StudentWithImageUrl>> getAllStudents() {
    List<Student> students = repo.findAll();
    List<StudentWithImageUrl> studentsWithImages = students.stream()
        .map(student -> {
            String imageUrl = service.getFileUrl(student.getProfileImagePath());
            return new StudentWithImageUrl(student, imageUrl);
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(studentsWithImages);
}

@GetMapping("/getStudent")
public ResponseEntity<ApiResponse> getStudentByRollNo(
        @RequestParam("rollNo") String rollNo) {

    // Validate required fields
    if (rollNo == null || rollNo.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse(null, "Roll number is required."));
    }

    try {
        // Check if student with the given rollNo exists
        Student existingStudent = repo.findByRollNo(rollNo);

        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(null, "Student with roll number " + rollNo + " not found."));
        }

        // Return the student details
        return ResponseEntity.ok(new ApiResponse(existingStudent, "Student found successfully"));

    } catch (Exception e) {
        // Handle any other exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(null, "An unexpected error occurred."));
    }
}
@GetMapping("/checkRollNo")
public ResponseEntity<?> checkRollNo(@RequestParam String rollNo) {
    boolean exists = repo.existsByRollNo(rollNo);
    if (exists) {
        return ResponseEntity.ok().body("Roll number exists");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Roll number not found");
    }
}
@GetMapping("/by-standard/{standard}")
public ResponseEntity<List<StudentWithImageUrl>> getByStandard(@PathVariable("standard") String standard) {
    // Fetch students by standard from the repository
    List<Student> students = repo.findByStandard(standard);
    
    // Map students to StudentWithImageUrl with image URLs
    List<StudentWithImageUrl> studentsWithImages = students.stream()
        .map(student -> {
            String imageUrl = service.getFileUrl(student.getProfileImagePath());
            return new StudentWithImageUrl(student, imageUrl);
        })
        .collect(Collectors.toList());

    // Return the list wrapped in ResponseEntity
    return ResponseEntity.ok(studentsWithImages);
}
@GetMapping("/test")
public String welcome() {
    return "Welcome To ADMIN - DashBoard ";
}
@GetMapping("/Studenttest")
public String welcomeStudent() {
    return "Welcome To Student - DashBoard ";
}


@DeleteMapping("/delete")
public ResponseEntity<String> deleteStudent(@RequestParam String rollNo) {
    boolean deleted = service.deleteStudentById(rollNo);
    if (deleted) {
        return ResponseEntity.ok("Student deleted successfully");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
}



//@GetMapping("/checkRollNo")
//public ResponseEntity<?> checkRollNo(@RequestParam String rollNo) {
//    boolean exists = repo.existsByRollNo(rollNo);
//    if (exists) {
//        return ResponseEntity.ok().body("Roll number exists");
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Roll number not found");
//    }
//}


}

