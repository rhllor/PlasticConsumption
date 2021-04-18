package com.github.rhllor.pc.library.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Consumption {

    public Consumption() {}
    public Consumption(Long userId, int year, int weekNumber, float weight) { 
        this.setUserId(userId);
        this.setYear(year);
        this.setWeekNumber(weekNumber);
        this.setWeight(weight);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull(message = "ID Utente deve essere valorizzato")
    private Long userId;
    @NotNull(message = "L'Anno deve essere valorizzato")
    private int year;
    @NotNull(message = "Il numero della settimana deve essere valorizzato")
    private int weekNumber;
    @NotNull(message = "Il peso deve essere valorizzato")
    @Min(value = 0, message = "Il peso può essere solo un valore positivo.")
    private float weight;

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeekNumber() {
        return this.weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
    
    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    
    @Override
    public String toString() {
        return "Consumption{" + "userId=" + this.userId + ", year=" + this.year+ ", week=" + this.weekNumber + ", weight=" + this.weight + "}";
    }
}