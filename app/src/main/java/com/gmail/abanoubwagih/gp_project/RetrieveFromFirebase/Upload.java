package com.gmail.abanoubwagih.gp_project.RetrieveFromFirebase;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.Building;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by RINA on 6/22/2016.
 */
public class Upload {

    private static DatabaseReference mDatabase;

    private static void uploadData(User user) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child("abanoubwagih").setValue(user);

    }

    public static void uploadToFirebase() {

        HashMap<String, Double> buildingGPS = new HashMap<>();
        buildingGPS.put("lat", 30.031094);
        buildingGPS.put("lon", 31.210595);
        List<Building> building = new ArrayList<>();

        String des = "faclty of computers and information " +
                "\n Cairo university \n The world has witnessed enormous and unprecedented developments in the " +
                "fields of communications and information technology during the past few years. These developments " +
                "redoubled the responsibility of the computers and information faculties in qualifying their students" +
                " in order to graduate the specialized People who are able to deal with the information technology and" +
                " communications revolution." +
                " Within the framework of Cairo University policy that aims to gain a model to the university of " +
                "the future through developing and updating its educational programs.\n" + "\n" +
                "Making an effort to achieve the authorized criteria of the international Academy. The Faculty " +
                "of Computers and Information attempts to develop the system of study and reevaluating the curricula" +
                " and applying the latest educational systems that provide more participation and choice for students to" +
                " study the subjects according to their abilities and desires. Within this framework, the Faculty endeavors" +
                " to apply the system of credit hours that is effective in the international universities.";

        building.add(new Building(1, "FCI", des, 35, true, buildingGPS));


        uploadData(new User("Cairo", "21 al hkim street", "abanoub", "abanoubwagih", 1, building));

    }
}
