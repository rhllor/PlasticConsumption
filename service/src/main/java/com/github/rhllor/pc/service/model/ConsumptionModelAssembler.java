package com.github.rhllor.pc.service.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.service.controller.ConsumptionController;

@Component
public class ConsumptionModelAssembler implements RepresentationModelAssembler<Consumption, EntityModel<Consumption>> {

  @Override
  public EntityModel<Consumption> toModel(Consumption consumption) {

    return EntityModel.of(consumption, //
        linkTo(methodOn(ConsumptionController.class).one(consumption.getId())).withSelfRel(),
        linkTo(methodOn(ConsumptionController.class).all()).withSelfRel());
  }
}