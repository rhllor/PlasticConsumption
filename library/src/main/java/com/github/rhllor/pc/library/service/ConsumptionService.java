package com.github.rhllor.pc.library.service;

import java.util.List;
import java.util.Optional;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.repository.ConsumptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionService {

    @Autowired
    private ConsumptionRepository _repository;
    
    public Optional<Consumption> findById(Long id) {
        return this._repository.findById(id);
    }

    public List<Consumption> findAll(@Nullable Specification<Consumption> spec) {
        return this._repository.findAll(spec);
    }

    public Optional<Consumption> findOne(@Nullable Specification<Consumption> spec) {
        return this._repository.findOne(spec);
    }

    public Consumption save(Consumption consumption) {
        return this._repository.save(consumption);
    }

    public void delete(Consumption consumption) {
        this._repository.delete(consumption);
    }

}
