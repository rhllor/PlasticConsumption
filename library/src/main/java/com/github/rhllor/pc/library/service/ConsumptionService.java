package com.github.rhllor.pc.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.repository.ConsumptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Consumption> findAll(@Nullable Specification<Consumption> spec,@Nullable Integer pageNo,@Nullable Integer pageSize) {

        if (pageNo == null)
            pageNo = 0;

        if (pageSize == null)
            pageSize = 50;

        if (pageSize > 500)
            pageSize = 500;

        if (pageNo < 0)
            return new ArrayList<Consumption>();
        if (pageSize <= 0)
            return new ArrayList<Consumption>();

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        Page<Consumption> pagedResult = this._repository.findAll(spec, paging);
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Consumption>();
        }
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
