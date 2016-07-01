package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataProvidingFromFirebase {

    public static List<Building> buildingList = new ArrayList<>();
    public static Map<Integer, Building> buildingMap = new HashMap<>();

    public static List<Building> getBuildingList() {
        return buildingList;
    }

    public static void setBuildingList(List<Building> buildingList) {
        DataProvidingFromFirebase.buildingList = buildingList;
    }

    public static Map<Integer, Building> getBuildingMap() {
        return buildingMap;
    }

    public static void setBuildingMap(Map<Integer, Building> buildingMap) {
        DataProvidingFromFirebase.buildingMap = buildingMap;
    }

    public static void clearBuildingListandMap() {
        if (!buildingList.isEmpty()) buildingList.clear();
        if (!buildingMap.isEmpty()) buildingMap.clear();
    }

    public static void addBuilding(List<Building> buildings) {


        if (buildingList.isEmpty()) {
            buildingList.addAll(buildings);
            for (Building building : buildings) {
                buildingMap.put(building.getBuildingId(), building);
            }
        }

    }


//
//    public static List<String> getBulidingNames() {
//        List<String> list = new ArrayList<>();
//        for (Building building : buildingList) {
//            list.add(building.getName());
//        }
//        return list;
//    }
//
//    public static List<Building> getFilteredList(int bulidingId) {
//
//        List<Building> filteredList = new ArrayList<>();
//        for (Building building : buildingList) {
//            if (building.getBuildingId() == bulidingId) {
//                filteredList.add(building);
//            }
//        }
//
//        return filteredList;
//
//    }

}
