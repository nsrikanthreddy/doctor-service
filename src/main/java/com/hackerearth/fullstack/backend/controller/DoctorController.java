package com.hackerearth.fullstack.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.hackerearth.fullstack.backend.model.Doctor;
import com.hackerearth.fullstack.backend.repository.DoctorRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", exposedHeaders = {"Access-Control-Allow-Origin"}, methods  = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        
    	Doctor savedDoctor = doctorRepository.save(doctor);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        
    	Optional<Doctor> doctor = doctorRepository.findById(id);
    	if(doctor.isPresent())
    		return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
    	else
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor updatedDoctor) {
       
    	Optional<Doctor> doctor = doctorRepository.findById(id);
    	if(doctor.isEmpty())
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	
    	Doctor existedDoctorObj=doctor.get();
    	existedDoctorObj.setName(updatedDoctor.getName());
    	existedDoctorObj.setSpecialty(updatedDoctor.getSpecialty());
    	existedDoctorObj.setHospitalAffiliation(updatedDoctor.getHospitalAffiliation());
    	existedDoctorObj.setContactPhone(updatedDoctor.getContactPhone());
    	Doctor modifiedDoctor = doctorRepository.save(existedDoctorObj);
        return new ResponseEntity<>(modifiedDoctor, HttpStatus.OK);
    		
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        
    	Optional<Doctor> doctor = doctorRepository.findById(id);
    	if(!doctor.isPresent())
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	
    	doctorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return new ResponseEntity<>(doctorRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<Doctor>> searchDoctorsByName(@RequestParam("name") String name) {
        
    	return new ResponseEntity<>(doctorRepository.findByNameContaining(name), HttpStatus.OK);
    }
}
