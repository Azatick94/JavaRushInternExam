package com.space.model;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NotNull
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "planet")
    private String planet;
    @Column(name = "shipType")
    private ShipType shipType;
    @Column(name = "prodDate")
    private Date prodDate;
    @Column(name = "isUsed")
    private Boolean isUsed;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "crewSize")
    private Integer crewSize;
    @Column(name = "rating")
    private Double rating;

    public Ship(Long id, String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.crewSize = crewSize;
        this.rating = calculateRating(speed, isUsed, prodDate);
    }

    // не нужный конструктор. необходим только для jpa.
    protected Ship() {
    }

    private Double calculateRating(Double speed, Boolean isUsed, Date prodDate) {
        Double k;
        if (isUsed)
            k=0.5;
        else
            k=1.0;
        int currentDate = 3019;

        Double rating = (80*speed*k)/(currentDate-prodDate.getYear()+1);
        return rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}