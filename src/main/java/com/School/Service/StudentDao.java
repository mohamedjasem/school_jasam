package com.School.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.School.Entity.Student;

public interface StudentDao {
	public String storeFile(MultipartFile file) throws IOException ;
	 public String getFileUrl(String fileName);
	 public boolean deleteStudentById(String rollNo);
}
