package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hackerearth.fullstack.backend.controller.DoctorController;
import com.hackerearth.fullstack.backend.model.Doctor;
import com.hackerearth.fullstack.backend.repository.DoctorRepository;

@SpringBootTest
class sampleTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorController doctorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDoctor_ValidDoctor_ReturnsSavedDoctor() {
        // Arrange
        Doctor doctor = new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.createDoctor(doctor);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctor.getName(), response.getBody().getName());
        assertEquals(doctor.getSpecialty(), response.getBody().getSpecialty());
        assertEquals(doctor.getHospitalAffiliation(), response.getBody().getHospitalAffiliation());
        assertEquals(doctor.getContactPhone(), response.getBody().getContactPhone());

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void getAllDoctors_ReturnsListOfDoctors() {
        // Arrange
        List<Doctor> doctorList = List.of(
                new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112"),
                new Doctor("Jane Smith", "Neurologist", "XYZ Clinic", "7774443331"));

        when(doctorRepository.findAll()).thenReturn(doctorList);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getAllDoctors();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctorList.size(), response.getBody().size());
    }
}
