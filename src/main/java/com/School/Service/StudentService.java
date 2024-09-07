package com.School.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.School.Repository.SRepo;



@Service
public class StudentService implements StudentDao {
	@Autowired
	private SRepo repo;
	
	 @Value("${file.upload-dir}")
	    private String uploadDir;
	 @Override
	    public String storeFile(MultipartFile file) throws IOException {
	        Path uploadPath = Paths.get(uploadDir);
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        String originalFileName = file.getOriginalFilename();
	        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
	        Path filePath = uploadPath.resolve(uniqueFileName);

	        Files.write(filePath, file.getBytes());

	        return uniqueFileName;
	    }
@Override
	    public String getFileUrl(String fileName) {
	        // Implement logic to construct and return the URL for the file
	        return "/files/" + fileName; // Example URL path
	    }


@Transactional
public boolean deleteStudentById(String rollNo) {
    try {
        if (repo.existsByRollNo(rollNo)) {
            repo.deleteByRollNo(rollNo);
            return true;
        } else {
            return false;
        }
    } catch (Exception e) {
        // Log the exception
        throw new RuntimeException("Error occurred while deleting student", e);
    }
}









}
