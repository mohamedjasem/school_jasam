package com.School.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.School.Entity.Student;


@Repository
public interface SRepo extends JpaRepository<Student, Long>{
	 Student findByRollNo(String rollNo);
	 List<Student> findByStandard(String standard);
	 boolean existsByRollNo(String rollNo);
	 
	 @Modifying
	    @Transactional
	    @Query("DELETE FROM Student s WHERE s.rollNo = :rollNo")
	    void deleteByRollNo(@Param("rollNo") String rollNo);
}
