package com.github.rhllor.pc.service.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import java.util.Calendar;
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
import com.github.rhllor.pc.service.model.ConsumptionModelAssembler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/consumptions")
@Tag(name = "Consumptions", description = "Raccolta delle API relative ai consumi di plastica.")
public class ConsumptionController extends AbstractConsumption implements ISecuredController {

    private final ConsumptionService _cService;
    private final UserService _uService;
    private final ConsumptionModelAssembler _assembler;

    public ConsumptionController(ConsumptionService cService, UserService uService, ConsumptionModelAssembler assembler) {
        this._cService = cService;
        this._uService = uService;
        this._assembler = assembler;
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Estrae uno specifico consumo.", tags = { "Consumption" })
    public EntityModel<Consumption> one(@PathVariable Long id) {
        Consumption consumption = this._cService.findById(id)
            .orElseThrow(() -> new NotFoundException());

        return this._assembler.toModel(consumption);
    }

    @GetMapping("/")
    @Operation(summary = "Estrae i consumi di tutti gli utenti.", description = "Questa API ?? paginata con una dimensione minima di 50 record visualizzati e massimo 500.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Parameter(description = "Formato accettato YYYY-MM-DD") 
        Date fromDate, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Parameter(description = "Formato accettato YYYY-MM-DD") 
        Date toDate,
        @RequestParam(required = false) 
        @Parameter(description = "Valore minimo: 0.") 
        Integer pageNo, 
        @RequestParam(required = false) 
        @Parameter(description = "Valore Minimo: 1. Valore Massimo: 500. Default: 50.") 
        Integer pageSize) {
            
        Specification<Consumption> specDate = manageFromAndToDate(fromDate, toDate);

        List<EntityModel<Consumption>> consumptions = this._cService.findAll(specDate, pageNo, pageSize).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(fromDate, toDate, pageNo, pageSize)).withSelfRel());
    }

    @GetMapping("/{year}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in un determinato anno.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));

        List<EntityModel<Consumption>> consumptions = this._cService.findAll(specYear).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(year)).withSelfRel());
    }
    
    @GetMapping("/{year}/{weekNumber}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in una determinata settimana di uno specifico anno.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year, @PathVariable int weekNumber) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", Calendar.getInstance().get(Calendar.YEAR)));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));

        List<EntityModel<Consumption>> consumptions = this._cService.findAll(Specification.where(specYear).and(specWeekNumber)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(year)).withSelfRel());
    }

    @PutMapping("/")
    @Operation(summary = "Censisce un nuovo consumo settimanale.", description = "Questa API censisce, o aggiorna se non presente, il consumo di plastica per la settimana corrente dell'utente autenticato al servizio." , tags = { "Consumption" })
    public ResponseEntity<?> replace(@RequestBody @Valid ConsumptionEntry consumptionEntry, Authentication authentication) throws Exception {

        Long userId = getUserAuthenticatedId(authentication);
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int weekNumber = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        ConsumptionSpecification specUser = new ConsumptionSpecification(new SearchCriteria("userId", userId));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", actualYear));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));

        Consumption updatetedValue = this._cService.findOne(Specification.where(specYear).and(specWeekNumber).and(specUser))
            .map(consumption -> {
                consumption.setWeight(consumptionEntry.getWeight());
                return this._cService.save(consumption);
            }).orElseGet(() ->{
                Consumption newConsumption = new Consumption(userId, actualYear, weekNumber, consumptionEntry.getWeight());
                return this._cService.save(newConsumption);
            });
        EntityModel<Consumption> entityModel = this._assembler.toModel(updatetedValue);
        return ResponseEntity 
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) 
            .body(entityModel);
    }

    @DeleteMapping("/")
    @Operation(summary = "Elimina il consumo della settimana corrente.", tags = { "Consumption" })
    public ResponseEntity<?> delete(Authentication authentication) throws Exception {
        
        Long userId = getUserAuthenticatedId(authentication);
        ConsumptionSpecification specUser = new ConsumptionSpecification(new SearchCriteria("userId", userId));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", Calendar.getInstance().get(Calendar.YEAR)));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
        Consumption consumption = this._cService.findOne(Specification.where(specYear).and(specWeekNumber).and(specUser))
            .orElseThrow(() -> new NotFoundException());

        this._cService.delete(consumption);

        return ResponseEntity.noContent().build();
    }

    private Long getUserAuthenticatedId(Authentication authentication) throws Exception {

        User userAuthenticated = this._uService.getByUsername(authentication.getName())
            .map(user -> user)
            .orElseThrow(() -> new Exception("L'utente autenticato non ?? stato trovato."));

        return userAuthenticated.getId();
    }

}