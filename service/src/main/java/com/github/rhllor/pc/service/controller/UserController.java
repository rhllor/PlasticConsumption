package com.github.rhllor.pc.service.controller;

import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.entity.User;
import com.github.rhllor.pc.library.service.ConsumptionService;
import com.github.rhllor.pc.library.service.UserService;
import com.github.rhllor.pc.library.ConsumptionSpecification;
import com.github.rhllor.pc.library.SearchCriteria;
import com.github.rhllor.pc.service.error.NotFoundException;
import com.github.rhllor.pc.service.model.UserModelAssembler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.EntityModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Raccolta delle API relative agli utenti.")
public class UserController extends AbstractConsumption implements ISecuredController {
    
    private final ConsumptionService _cService;
    private final UserService _uService;
    private final UserModelAssembler _assembler;
    
    public UserController(ConsumptionService consumptionRepository, UserService userService, UserModelAssembler assembler) {
        this._cService = consumptionRepository;
        this._uService = userService;
        this._assembler = assembler;
    }
    
    @GetMapping("/")    
    @Operation(summary = "Estrae le utenze.", tags = { "Users" })
    public CollectionModel<EntityModel<User>> allUsers() {
        List<EntityModel<User>> users = this._uService.findAll().stream()
            .map(user -> EntityModel.of(user,
                linkTo(methodOn(UserController.class).oneUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsers()).withSelfRel()))
            .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera singolo utente utilizzando il suo ID.", tags = { "Users" })
    public EntityModel<User> oneUser(@PathVariable Long id) {
        User user = this._uService.findById(id)
            .orElseThrow(() -> new NotFoundException());

        return EntityModel.of(user, //
            linkTo(methodOn(UserController.class).oneUser(user.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente.", description = "Questa API è paginata con una dimensione minima di 50 record visualizzati e massimo 500.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> allByUser(@PathVariable Long id, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date fromDate, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date toDate,
        @RequestParam(required = false) Integer pageNo, 
        @RequestParam(required = false) Integer pageSize) 
    {
        Specification<Consumption> specDate = manageFromAndToDate(fromDate, toDate);        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        List<EntityModel<Consumption>> consumptions = this._cService.findAll(
            Specification.where(specUserId).and(specDate), pageNo, pageSize).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).allByUser(id, fromDate, toDate, pageNo, pageSize)).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions/{year}")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente in un determinato anno.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> allByUser(@PathVariable Long id, @PathVariable int year) {
        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        List<EntityModel<Consumption>> consumptions = this._cService.findAll(Specification.where(specUserId).and(specYear)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).allByUser(id, year)).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions/{year}/{weekNumber}")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente in una determinata settimana di uno specifico anno.", description = "Ritorna Token di autenticazione.", tags = { "Users", "Consumption" })
    public EntityModel<Consumption> one(@PathVariable Long id, @PathVariable int year, @PathVariable int weekNumber) {
        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));
        Consumption consumption = this._cService.findOne(Specification.where(specUserId).and(specYear).and(specWeekNumber))
            .orElseThrow(() -> new NotFoundException());

        return this._assembler.toModel(consumption);
    }

}
