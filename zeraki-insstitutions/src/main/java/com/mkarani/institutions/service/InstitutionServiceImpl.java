package com.mkarani.institutions.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkarani.institutions.Dto.InstitutionRequest;
import com.mkarani.institutions.entity.InstitutionEntity;
import com.mkarani.institutions.exceptions.DeletionErrorException;
import com.mkarani.institutions.exceptions.InstitutionExistsException;
import com.mkarani.institutions.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .build();
        return institutionRepository.save(institutionEntity);
    }

    @Override
    public List<InstitutionEntity> getAllInstitutions() {
        return institutionRepository.findAll();
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
        return institutionRepository.findByCompanyNameContaining(name);
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
            System.out.println("=========================");
            throw new InstitutionExistsException(nameNode);
        }
        if (existingInstitutionById.isEmpty()) {
            throw new IllegalArgumentException("Institution with the Id "+id+" does not exist");
        }
        InstitutionEntity institutionEntity = existingInstitutionById.get();
        institutionEntity.setName(nameNode);
        return institutionRepository.save(institutionEntity);
    }


}
