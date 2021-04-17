package com.github.rhllor.pc.service.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.ConsumptionRepository;
import com.github.rhllor.pc.library.ConsumptionSpecification;
import com.github.rhllor.pc.library.SearchCriteria;
import com.github.rhllor.pc.service.model.ConsumptionModelAssembler;
import com.github.rhllor.pc.service.NotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/consumptions")
@Tag(name = "Consumptions", description = "Raccolta delle API relative ai consumi di plastica.")
public class ConsumptionController extends AbstractConsumption implements ISecuredController {

    private final ConsumptionRepository _repository;
    private final ConsumptionModelAssembler _assembler;

    public ConsumptionController(ConsumptionRepository repository, ConsumptionModelAssembler assembler) {
        this._repository = repository;
        this._assembler = assembler;
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Estrae uno specifico consumo.", tags = { "Consumption" })
    public EntityModel<Consumption> one(@PathVariable Long id) {
        Consumption consumption = this._repository.findById(id)
            .orElseThrow(() -> new NotFoundException());

        return this._assembler.toModel(consumption);
    }

    @GetMapping("/")
    @Operation(summary = "Estrae i consumi di tutti gli utenti.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date fromDate, 
        @RequestParam(required = false)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        Date toDate) {
            
        Specification<Consumption> specDate = manageFromAndToDate(fromDate, toDate);

        List<EntityModel<Consumption>> consumptions = this._repository.findAll(specDate).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(null, null)).withSelfRel());
    }

    @GetMapping("/{year}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in un determinato anno.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", year));

        List<EntityModel<Consumption>> consumptions = this._repository.findAll(specYear).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(null, null)).withSelfRel());
    }
    
    @GetMapping("/{year}/{weekNumber}")
    @Operation(summary = "Estrae i consumi di tutti gli utenti in una determinata settimana di uno specifico anno.", tags = { "Consumption" })
    public CollectionModel<EntityModel<Consumption>> all(@PathVariable int year, @PathVariable int weekNumber) {

        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", Calendar.getInstance().get(Calendar.YEAR)));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));

        List<EntityModel<Consumption>> consumptions = this._repository.findAll(Specification.where(specYear).and(specWeekNumber)).stream()
            .map(this._assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(consumptions, linkTo(methodOn(ConsumptionController.class).all(null, null)).withSelfRel());
    }

    @PutMapping("/")
    @Operation(summary = "Censisce un nuovo consumo settimanale.", description = "Questa API censisce, o aggiorna se non presente, il consumo di plastica per la settimana corrente dell'utente autenticato al servizio." , tags = { "Consumption" })
    public ResponseEntity<?> replace(@RequestBody @Valid ConsumptionEntry consumptionEntry) {

        Long userId = 1L;
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int weekNumber = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        ConsumptionSpecification specUser = new ConsumptionSpecification(new SearchCriteria("userId", userId));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", actualYear));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", weekNumber));

        Consumption updatetedValue = this._repository.findOne(Specification.where(specYear).and(specWeekNumber).and(specUser))
            .map(consumption -> {
                consumption.setWeight(consumptionEntry.getWeight());
                return this._repository.save(consumption);
            }).orElseGet(() ->{

                Consumption newConsumption = new Consumption(userId, actualYear, weekNumber, consumptionEntry.getWeight());
                return this._repository.save(newConsumption);
            });
        EntityModel<Consumption> entityModel = this._assembler.toModel(updatetedValue);
        return ResponseEntity 
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) 
            .body(entityModel);
    }

    @DeleteMapping("/")
    @Operation(summary = "Elimina il consumo della settimana corrente.", tags = { "Consumption" })
    public ResponseEntity<?> delete() {
        
        Long userId = 1L;
        ConsumptionSpecification specUser = new ConsumptionSpecification(new SearchCriteria("userId", userId));
        ConsumptionSpecification specYear = new ConsumptionSpecification(new SearchCriteria("year", Calendar.getInstance().get(Calendar.YEAR)));
        ConsumptionSpecification specWeekNumber = new ConsumptionSpecification(new SearchCriteria("weekNumber", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
        Consumption consumption = this._repository.findOne(Specification.where(specYear).and(specWeekNumber).and(specUser))
            .orElseThrow(() -> new NotFoundException());

        this._repository.delete(consumption);

        return ResponseEntity.noContent().build();
    }
}