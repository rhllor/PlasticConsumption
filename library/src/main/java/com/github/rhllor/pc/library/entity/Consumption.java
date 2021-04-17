package com.github.rhllor.pc.library.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Consumption {

    public Consumption() {}
    public Consumption(Long userId, int year, int weekNumber, float weight) { 
        this.setUserId(userId);
        this.setYear(year);
        this.setWeekNumber(weekNumber);
        this.setWeight(weight);
    }

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    
    private Long userId;
    private int year;
    private int weekNumber;
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