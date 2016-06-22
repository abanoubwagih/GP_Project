package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import java.util.ArrayList;
import java.util.List;

public final class DataProvidingFromFirebase {

    public static List<Building> buildingList = new ArrayList<>();
//    public static Map<String, Building> buildingMap = new HashMap<>();


    public static void addBuilding(List<Building> buildings) {

        buildingList.addAll(buildings);
//        for (Building building:buildings) {
//            buildingMap.put(building.getName(), building);
//        }

    }

    public static List<String> getBulidingNames() {
        List<String> list = new ArrayList<>();
        for (Building building : buildingList) {
            list.add(building.getName());
        }
        return list;
    }

    public static List<Building> getFilteredList(int searchString) {

        List<Building> filteredList = new ArrayList<>();
        for (Building building : buildingList) {
            if (building.getBuildingId() == searchString) {
                filteredList.add(building);
            }
        }

        return filteredList;

    }

}
