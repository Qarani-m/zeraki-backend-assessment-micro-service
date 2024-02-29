package com.mkarani.institutions.controller;


import com.mkarani.institutions.dto.InstitutionRequest;
import com.mkarani.institutions.entity.InstitutionEntity;
import com.mkarani.institutions.exceptions.DeletionErrorException;
import com.mkarani.institutions.exceptions.InstitutionExistsException;
import com.mkarani.institutions.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    // Endpoint to add a new institution

    @PostMapping
    public ResponseEntity<InstitutionEntity> addCourseToInstitution(){
        return null;
    }
    @PostMapping("/new")
    public ResponseEntity<?> addInstitution(@RequestBody InstitutionRequest institution) {
        try {
            InstitutionEntity savedInstitution = institutionService.addInstitution(institution);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstitution);
        } catch (InstitutionExistsException e) {
            // Return conflict status if the institution already exists
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    // Endpoint to retrieve all institutions
    @GetMapping("/all")
    public ResponseEntity<List<InstitutionEntity>> getAllInstitutions() {
        List<InstitutionEntity> institutions = institutionService.getAllInstitutions();
        return ResponseEntity.ok().body(institutions);
    }

    // Endpoint to search institutions by name
    @GetMapping("/search/{name}")
    public ResponseEntity<List<InstitutionEntity>> searchInstitutionsByName(@PathVariable String name) {
        List<InstitutionEntity> filteredInstitutions = institutionService.searchInstitutionsByName(name);
        return ResponseEntity.ok().body(filteredInstitutions);
    }

    // Endpoint to sort institutions by name
    @GetMapping("/sort/{direction}")
    public ResponseEntity<List<InstitutionEntity>> sortInstitutionsByName(@PathVariable String direction) {
        List<InstitutionEntity> sortedInstitutions = institutionService.sortInstitutionsByName(direction);
        return ResponseEntity.ok().body(sortedInstitutions);
    }

    // Endpoint to delete an institution
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInstitution(@PathVariable Long id) {
        try {
            institutionService.deleteInstitution(id);
            return ResponseEntity.noContent().build();
        } catch (DeletionErrorException e) {
            // Return internal server error if deletion fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //    endpont to get all the courses tought in this institution
    @GetMapping("/course/{institutionId}")
    public  ResponseEntity<?> getCoursesInInstitutoin(@PathVariable Long institutionId){
        try{
            return ResponseEntity.ok().body(
                    institutionService.getCourses(institutionId)
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }


    // Endpoint to update the name of an institution
    @PutMapping("/update-name/{id}")
    public ResponseEntity<?> editInstitutionName(@PathVariable Long id, @RequestBody String newInstitutionName) {
        try {
            InstitutionEntity updatedInstitution = institutionService.editInstitutionName(id, newInstitutionName);
            return ResponseEntity.ok().body(updatedInstitution);
        } catch (IllegalArgumentException e) {
            // Return bad request status if the provided institution id is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InstitutionExistsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


        catch (Exception e) {
            // Return internal server error if an unexpected error occurs during update
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Error updating institution."));
        }
    }
}
