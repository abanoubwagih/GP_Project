package com.gmail.abanoubwagih.gp_project.BuildingHandle;

public class Building {

    private String BuildingId;
    private String name;
    private String description;
    private double temperature;
    private boolean status;


    public String getBuildingId() {
        return BuildingId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description + "\n";
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean getStatus() {
        return status;
    }

    public Building(String BuildingId, String name, String description, double temperature, boolean status) {
        this.BuildingId = BuildingId;
        this.name = name;
        this.description = description;
        this.temperature = temperature;
        this.status = status;
    }

}
