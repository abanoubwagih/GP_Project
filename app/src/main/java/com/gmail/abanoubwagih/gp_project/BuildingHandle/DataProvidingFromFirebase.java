package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataProvidingFromFirebase {

    public static List<Building> buildingList = new ArrayList<>();
    public static Map<String, Building> buildingMap = new HashMap<>();


    private static void addBuilding(String BuildingId, String name, String description, double temperature, boolean status) {
        Building building = new Building(BuildingId, name, description, temperature, status);
        buildingList.add(building);
        buildingMap.put(BuildingId, building);
    }

    public static List<String> getProductNames() {
        List<String> list = new ArrayList<>();
        for (Building product : buildingList) {
            list.add(product.getName());
        }
        return list;
    }

    public static List<Building> getFilteredList(String searchString) {

        List<Building> filteredList = new ArrayList<>();
        for (Building product : buildingList) {
            if (product.getBuildingId().contains(searchString)) {
                filteredList.add(product);
            }
        }

        return filteredList;

    }

}
