package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
class mainTest {

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
    void getDoctorById_DoctorExists_ReturnsDoctor() {
        // Arrange
        Doctor doctor = new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112");
        Long doctorId = doctor.getId();
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorById(doctorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctor.getSpecialty(), response.getBody().getSpecialty());
        assertEquals(doctor.getHospitalAffiliation(), response.getBody().getHospitalAffiliation());
        assertEquals(doctor.getContactPhone(), response.getBody().getContactPhone());
    }

    @Test
    void getDoctorById_DoctorDoesNotExist_ReturnsNotFound() {
        // Arrange
        Long doctorId = 1L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorById(doctorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
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

    @Test
    void updateDoctor_ValidDoctor_ReturnsUpdatedDoctor() {
        // Arrange
        Doctor existingDoctor = new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112");
        Doctor updatedDoctor = new Doctor("John Doe", "Neurologist", "Apollo Hospitals", "8885551112");

        Long doctorId = existingDoctor.getId();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(updatedDoctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.updateDoctor(doctorId, updatedDoctor);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedDoctor.getSpecialty(), response.getBody().getSpecialty());
    }

    @Test
    void updateDoctor_InvalidDoctor_ReturnsNotFound() {
        // Arrange
        Long doctorId = -999L;
        Doctor updatedDoctor = new Doctor("John Doe", "Neurologist", "Apollo Hospitals", "8885551112");

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Doctor> response = doctorController.updateDoctor(doctorId, updatedDoctor);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteDoctor_ValidDoctorId_DeletesDoctor() {
        // Arrange
        Doctor doctor = new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112");
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).deleteById(doctor.getId());
        Long doctorId = doctor.getId();
        
        // Act
        ResponseEntity<Void> response = doctorController.deleteDoctor(doctorId);
        
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorRepository, times(1)).deleteById(doctorId);
    }  

    @Test
    void searchDoctorsByName_ValidName_ReturnsListOfDoctors() {
        // Arrange
        String searchName = "John";
        List<Doctor> doctorList = List.of(
                new Doctor("John Doe", "Cardiologist", "Apollo Hospitals", "8885551112"),
                new Doctor("John Smith", "Neurologist", "XYZ Clinic", "7774443331"));

        when(doctorRepository.findByNameContaining(searchName)).thenReturn(doctorList);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.searchDoctorsByName(searchName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctorList.size(), response.getBody().size());
    }
}
