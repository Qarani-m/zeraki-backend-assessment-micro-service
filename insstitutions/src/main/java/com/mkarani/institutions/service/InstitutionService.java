package com.mkarani.institutions.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mkarani.institutions.dto.InstitutionRequest;
import com.mkarani.institutions.entity.InstitutionEntity;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface InstitutionService {
    InstitutionEntity addInstitution(InstitutionRequest institution);

    List<InstitutionEntity> getAllInstitutions();

    List<InstitutionEntity> sortInstitutionsByName(String direction);

    List<InstitutionEntity> searchInstitutionsByName(String name);

    String deleteInstitution(Long id);

    InstitutionEntity editInstitutionName(Long id, String name) throws JsonProcessingException;

    Object getCourses(Long institutionId) throws Exception;

    Optional<InstitutionEntity> findById(Long id);
}
