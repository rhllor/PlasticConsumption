package com.github.rhllor.pc.service.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.service.controller.UserController;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<Consumption, EntityModel<Consumption>> {

    @Override
    public EntityModel<Consumption> toModel(Consumption consumption) {
  
      return EntityModel.of(consumption, //
          linkTo(methodOn(UserController.class).one(consumption.getUserId(), consumption.getYear(), consumption.getWeekNumber())).withSelfRel(),
          linkTo(methodOn(UserController.class).all(null, null)).withSelfRel());
    }
}
