package com.gmail.abanoubwagih.gp_project.UserHandler;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.Building;

import java.util.List;

/**
 * Created by RINA on 6/22/2016.
 */
public class User {

    private String address;
    private String city;
    private String name;
    private String loginName;
    private int numberOfBuilding;
    private List<Building> building;

    public User() {
    }


    public User(String city, String address, String name, String loginName, int numberOfBuilding, List<Building> building) {
        this.city = city;
        this.address = address;
        this.name = name;
        this.loginName = loginName;
        this.numberOfBuilding = numberOfBuilding;
        this.building = building;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfBuilding() {
        return numberOfBuilding;
    }

    public void setNumberOfBuilding(int numberOfBuilding) {
        this.numberOfBuilding = numberOfBuilding;
    }

    public List<Building> getBuilding() {
        return building;
    }

    public void setBuilding(List<Building> building) {
        this.building = building;
    }
}
