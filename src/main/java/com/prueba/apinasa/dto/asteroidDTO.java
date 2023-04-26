package com.prueba.apinasa.dto;

public class asteroidDTO {

    private String name;
    private String diameter;
    private String  speed;
    private String  date;
    private String  planet;

    public asteroidDTO(String name, String diameter, String speed, String date, String planet) {
        this.name = name;
        this.diameter = diameter;
        this.speed = speed;
        this.date = date;
        this.planet = planet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }
}
