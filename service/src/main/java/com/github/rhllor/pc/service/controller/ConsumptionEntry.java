package com.github.rhllor.pc.service.controller;

import javax.validation.constraints.Min;

public class ConsumptionEntry {

    @Min(value = 0, message = "Il valore deve essere positivo.")
    private Float weight;

    public Float getWeight() {
        return this.weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
