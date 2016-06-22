package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import java.util.HashMap;

public class Building {

    private int buildingId;
    private String name;
    private String description;
    private double temperature;
    private boolean status;
    private HashMap<String, Double> buildingGPS;

    public Building() {
    }

    public Building(int buildingId, String name, String description, double temperature, boolean status, HashMap<String, Double> buildingGPS) {
        this.buildingId = buildingId;
        this.name = name;
        this.description = description;
        this.temperature = temperature;
        this.status = status;
        this.buildingGPS = buildingGPS;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HashMap<String, Double> getBuildingGPS() {
        return buildingGPS;
    }

    public void setBuildingGPS(HashMap<String, Double> buildingGPS) {
        this.buildingGPS = buildingGPS;
    }
}
