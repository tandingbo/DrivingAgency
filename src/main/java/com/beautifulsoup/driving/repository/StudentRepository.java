package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {
}
