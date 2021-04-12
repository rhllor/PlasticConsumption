package com.github.rhllor.pc.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.github.rhllor.pc.library.Consumption;

@Component
class ConsumptionModelAssembler implements RepresentationModelAssembler<Consumption, EntityModel<Consumption>> {

  @Override
  public EntityModel<Consumption> toModel(Consumption consumption) {

    return EntityModel.of(consumption, //
        linkTo(methodOn(PlasticConsumptionController.class).one(consumption.getWeekNumber())).withSelfRel(),
        linkTo(methodOn(PlasticConsumptionController.class).all()).withRel(""));
  }
}