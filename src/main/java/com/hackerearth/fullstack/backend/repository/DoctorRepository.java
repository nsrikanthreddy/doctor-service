package com.hackerearth.fullstack.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hackerearth.fullstack.backend.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByNameContaining(String name);
}
