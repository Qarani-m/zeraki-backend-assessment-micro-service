package com.mkarani.institutions.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkarani.institutions.dto.InstitutionRequest;

import com.mkarani.institutions.entity.InstitutionEntity;
import com.mkarani.institutions.exceptions.DeletionErrorException;
import com.mkarani.institutions.exceptions.InstitutionExistsException;
import com.mkarani.institutions.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionServiceImpl implements InstitutionService{

    @Autowired
    private InstitutionRepository institutionRepository;

    public InstitutionEntity addInstitution(InstitutionRequest institutionRequest) {
        Optional<InstitutionEntity> existingInstitution = institutionRepository.findByName(institutionRequest.getName());
        if (existingInstitution.isPresent()) {
            throw new InstitutionExistsException(institutionRequest.getName());
        }
        InstitutionEntity institutionEntity = InstitutionEntity.builder()
                .name(institutionRequest.getName())
                .address(institutionRequest.getAddress())
                .city(institutionRequest.getCity())
                .county(institutionRequest.getCounty())
                .country(institutionRequest.getCountry())
                .phoneNumber(institutionRequest.getPhoneNumber())
                .studentReg(new ArrayList<>())
                .studentCount(0L)
                .coursesOffered(institutionRequest.getCoursesOffered())
                .build();
        return institutionRepository.save(institutionEntity);
    }

    @Override
    public List<InstitutionEntity> getAllInstitutions() {
        List<InstitutionEntity> institutionEntities = institutionRepository.findAll();
        if (institutionEntities.isEmpty(){
            return new ArrayList<>();
        }
        return institutionEntities;
    }
    @Override
    public List<InstitutionEntity> sortInstitutionsByName(String direction) {
        if(direction.toUpperCase().contains("DESC")){
            return institutionRepository.findAllOrderedByNameDesc();
        }
        return institutionRepository.findAllOrderedByNameAsc();
    }
    @Override
    public List<InstitutionEntity> searchInstitutionsByName(String name) {
        return institutionRepository.findByNameContaining(name);
    }

    @Override
    public String deleteInstitution(Long id) {
        try{
            institutionRepository.deleteById(id);
            return "Institution with Id "+id+" deleted Sucessfull";
        }catch (Exception e){
            throw new DeletionErrorException(id);

        }
    }

    @Override
    public InstitutionEntity editInstitutionName(Long id, String newInstitutionName) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(newInstitutionName);
        String nameNode = node.get("name").asText();

        Optional<InstitutionEntity> existingInstitutionByName = institutionRepository.findByName(nameNode);
        Optional<InstitutionEntity> existingInstitutionById = institutionRepository.findById(id);
        if (existingInstitutionByName.isPresent()) {
            throw new InstitutionExistsException(nameNode);
        }
        if (existingInstitutionById.isEmpty()) {
            throw new IllegalArgumentException("Institution with the Id "+id+" does not exist");
        }
        InstitutionEntity institutionEntity = existingInstitutionById.get();
        institutionEntity.setName(nameNode);
        return institutionRepository.save(institutionEntity);
    }

    @Override
    public Object getCourses(Long institutionId) throws Exception {
        Optional<InstitutionEntity> institution =institutionRepository.findById(institutionId);
        if (institution.isEmpty()){
            throw  new Exception("No institution with Id"+ institutionId.toString());
        }
        return institution.get().getCoursesOffered();
    }


}
