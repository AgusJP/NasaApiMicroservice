package com.prueba.apinasa.dto;

public class AsteroidDTO {

    private String name;
    private double averageDiameter;
    private String speed;
    private String date;
    private String orbitingPlanet;

    public AsteroidDTO(String name, double averageDiameter, String speed, String date, String orbitingPlanet) {
        this.name = name;
        this.averageDiameter = averageDiameter;
        this.speed = speed;
        this.date = date;
        this.orbitingPlanet = orbitingPlanet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageDiameter() {
        return averageDiameter;
    }

    public void setAverageDiameter(double averageDiameter) {
        this.averageDiameter = averageDiameter;
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

    public String getOrbitingPlanet() {
        return orbitingPlanet;
    }

    public void setOrbitingPlanet(String orbitingPlanet) {
        this.orbitingPlanet = orbitingPlanet;
    }

    @Override
    public String toString() {
        return "asteroidDTO{" +
                "name='" + name + '\'' +
                ", averageDiameter=" + averageDiameter +
                ", speed='" + speed + '\'' +
                ", date='" + date + '\'' +
                ", orbitingPlanet='" + orbitingPlanet + '\'' +
                '}';
    }

}