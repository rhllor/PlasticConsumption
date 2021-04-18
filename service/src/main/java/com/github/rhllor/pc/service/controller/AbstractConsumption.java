package com.github.rhllor.pc.service.controller;

import java.util.Date;

import com.github.rhllor.pc.library.ConsumptionSpecification;
import com.github.rhllor.pc.library.SearchCriteria;
import com.github.rhllor.pc.library.SearchCriteria.TypeSearch;
import com.github.rhllor.pc.library.entity.Consumption;

import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractConsumption {
    
    protected Specification<Consumption> manageFromAndToDate(Date from, Date to) {

        if (from == null && to == null)
            return null;

        ConsumptionSpecification specFrom;
        ConsumptionSpecification specTo;

        if (from != null && to == null) {
            specFrom = new ConsumptionSpecification(new SearchCriteria("fromDate", from, TypeSearch.greaterOrEqual));
            return Specification.where(specFrom);
        }
        
        if (from == null && to != null) {
            specTo = new ConsumptionSpecification(new SearchCriteria("toDate", to, TypeSearch.lessOrEqual));
            return Specification.where(specTo);
        }

        specFrom = new ConsumptionSpecification(new SearchCriteria("fromDate", from, TypeSearch.greaterOrEqual));
        specTo = new ConsumptionSpecification(new SearchCriteria("toDate", to, TypeSearch.lessOrEqual));
        return Specification.where(specFrom).and(specTo);
    }
    
}
