package com.github.rhllor.pc.service.controller;

import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.entity.User;
import com.github.rhllor.pc.library.ConsumptionRepository;
import com.github.rhllor.pc.library.ConsumptionSpecification;
import com.github.rhllor.pc.library.SearchCriteria;
import com.github.rhllor.pc.library.UserRepository;
import com.github.rhllor.pc.library.SearchCriteria.TypeSearch;
import com.github.rhllor.pc.service.model.UserModelAssembler;
import com.github.rhllor.pc.service.NotFoundException;

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
public class UserController implements ISecuredController {
    
    private final ConsumptionRepository _consumptionRepository;
    private final UserRepository _userRepository;
    private final UserModelAssembler _assembler;
    
    public UserController(ConsumptionRepository consumptionRepository, UserRepository userRepository, UserModelAssembler assembler) {
        this._consumptionRepository = consumptionRepository;
        this._userRepository = userRepository;
        this._assembler = assembler;
    }
    
    @GetMapping("/")    
    @Operation(summary = "Estrae le utenze.", tags = { "Users" })
    public CollectionModel<EntityModel<User>> allUsers() {
        List<EntityModel<User>> users = this._userRepository.findAll().stream()
            .map(user -> EntityModel.of(user,
                linkTo(methodOn(UserController.class).oneUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsers()).withSelfRel()))
            .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera singolo utente utilizzando il suo ID.", tags = { "Users" })
    public EntityModel<User> oneUser(@PathVariable Long id) {
        User user = this._userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());

        return EntityModel.of(user, //
            linkTo(methodOn(UserController.class).oneUser(user.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @GetMapping("/consumptions")
    @Operation(summary = "Estrae i consumi di tutti gli utenti.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date fromDate, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date toDate) 
    {
        Specification<Consumption> specDate = manageFromAndToDate(fromDate, toDate);

        List<EntityModel<Consumption>> consumptions = this._consumptionRepository.findAll(specDate).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("/consumptions/{year}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in un singolo anno.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        List<EntityModel<Consumption>> consumptions = this._consumptionRepository.findAll(specYear).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("/consumptions/{year}/{weekNumber}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in una determinata settimana di uno specifico anno.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year, @PathVariable int weekNumber) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", year));
        List<EntityModel<Consumption>> consumptions = this._consumptionRepository.findAll(Specification.where(specYear).and(specWeekNumber)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> allByUser(@PathVariable Long id, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date fromDate, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date toDate) 
    {
        Specification<Consumption> specDate = manageFromAndToDate(fromDate, toDate);        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        List<EntityModel<Consumption>> consumptions = this._consumptionRepository.findAll(
            Specification.where(specUserId).and(specDate)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions/{year}")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente in un determinato anno.", tags = { "Users", "Consumption" })
    public CollectionModel<EntityModel<Consumption>> allByUser(@PathVariable Long id, @PathVariable int year) {
        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        List<EntityModel<Consumption>> consumptions = this._consumptionRepository.findAll(Specification.where(specUserId).and(specYear)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("{id}/consumptions/{year}/{weekNumber}")
    @Operation(summary = "Estrae tutti i consumi di uno specifico utente in una determinata settimana di uno specifico anno.", description = "Ritorna Token di autenticazione.", tags = { "Users", "Consumption" })
    public EntityModel<Consumption> one(@PathVariable Long id, @PathVariable int year, @PathVariable int weekNumber) {
        
        ConsumptionSpecification specUserId = new ConsumptionSpecification(new SearchCriteria("userId", id));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));
        Consumption consumption = this._consumptionRepository.findOne(Specification.where(specUserId).and(specYear).and(specWeekNumber))
            .orElseThrow(() -> new NotFoundException());

        return this._assembler.toModel(consumption);
    }

    private Specification<Consumption> manageFromAndToDate(Date from, Date to) {

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
