package com.github.rhllor.pc.service;

import java.util.List;
import com.github.rhllor.pc.library.Consumption;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/plastic_consumption")
public class PlasticConsumptionController {

    @GetMapping("/")
    public CollectionModel<EntityModel<Consumption>> all() {
        throw new NotFoundException();
    }

    @GetMapping("/{weekNumber}")
    public EntityModel<Consumption> one(@PathVariable int weekNumber) {
        throw new NotFoundException();
    }
    
    @GetMapping("/{year}/{weekNumber}")
    public EntityModel<Consumption> one(@PathVariable int year, @PathVariable int weekNumber) {
        throw new NotFoundException();
    }

    @PostMapping("/")
    public ResponseEntity<?> newConsumption(@RequestBody Consumption consumption) {
        throw new NotFoundException();
    }

    @PutMapping("/")
    public ResponseEntity<?> replace(@RequestBody Consumption consumption) {
        throw new NotFoundException();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> delete() {
        throw new NotFoundException();
    }
}