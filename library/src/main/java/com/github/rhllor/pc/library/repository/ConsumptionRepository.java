package com.github.rhllor.pc.library.repository;

import com.github.rhllor.pc.library.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long>, JpaSpecificationExecutor<Consumption> {

}
