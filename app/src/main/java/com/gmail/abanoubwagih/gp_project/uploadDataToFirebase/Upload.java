package com.gmail.abanoubwagih.gp_project.uploadDataToFirebase;

/**
 * Created by RINA on 6/22/2016.
 */
public class Upload {
//
//    private static DatabaseReference mDatabase;
//    private static Context context;
//
//    private static void uploadData(User user) {
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child("maro").setValue(user);
//
//    }
//
//    public static void uploadToFirebase() {
////
////        HashMap<String, Double> buildingGPS = new HashMap<>();
////        buildingGPS.put("lat", 30.031094);
////        buildingGPS.put("lon", 31.210595);
////        List<Building> building = new ArrayList<>();
////
////        String des = "faclty of computers and information " +
////                "\n Cairo university \n The world has witnessed enormous and unprecedented developments in the " +
////                "fields of communications and information technology during the past few years. These developments " +
////                "redoubled the responsibility of the computers and information faculties in qualifying their students" +
////                " in order to graduate the specialized People who are able to deal with the information technology and" +
////                " communications revolution." +
////                " Within the framework of Cairo University policy that aims to gain a model to the university of " +
////                "the future through developing and updating its educational programs.\n" + "\n" +
////                "Making an effort to achieve the authorized criteria of the international Academy. The Faculty " +
////                "of Computers and Information attempts to develop the system of study and reevaluating the curricula" +
////                " and applying the latest educational systems that provide more participation and choice for students to" +
////                " study the subjects according to their abilities and desires. Within this framework, the Faculty endeavors" +
////                " to apply the system of credit hours that is effective in the international universities.";
////
////
////        context = LaunchActivity.context;
////        String stringImage = getStringFromImage(R.drawable.buling1);
////
////
////        building.add(new Building(1, "FCI", des, stringImage, 35, true, buildingGPS));
////
////
//////        uploadData(new User("Cairo", "21 al hkim street", "abanoub", "abanoubwagih", 1, building));
////
////        stringImage = getStringFromImage(R.drawable.buling2);
////        building.add(new Building(2, "new Building", des, stringImage, 25, true, buildingGPS));
////
//////        uploadData(new User("Cairo", "21 al hkim street", "abanoub", "abanoubwagih", 2, building));
////
////        stringImage = getStringFromImage(R.drawable.buling3);
////        building.add(new Building(3, "mdarg kbire", des, stringImage, 25, true, buildingGPS));
//////        uploadData(new User("Cairo", "21 al hkim street", "abanoub", "abanoubwagih", 3, building));
////
////        stringImage = getStringFromImage(R.drawable.buling4);
////        building.add(new Building(4, "modrg ibrahib", des, stringImage, 25, true, buildingGPS));
////
//////        stringImage = getStringFromImage(R.drawable.buling4);
//////        building.add(new Building(5, "building 5", des, stringImage, 25, true, buildingGPS));
//////        stringImage = getStringFromImage(R.drawable.buling4);
//////        building.add(new Building(6, "building 6", des, stringImage, 25, true, buildingGPS));
//////        stringImage = getStringFromImage(R.drawable.buling4);
//////        building.add(new Building(7, "building 7", des, stringImage, 25, true, buildingGPS));
//////        stringImage = getStringFromImage(R.drawable.buling4);
//////        building.add(new Building(8, "building 8", des, stringImage, 25, true, buildingGPS));
////
////
////        uploadData(new User("Cairo", "21 al hkim street", "marina", "maro", building.size(), building));
////
////        Log.d("Upload", "done ");
//    }
//
//    public static String getStringFromImage(int imageID) {
//
//        try {
//            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), imageID);//your image
//            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
//            bmp.recycle();
//            byte[] byteArray = bYtE.toByteArray();
//            String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            return imageFile;
//        } catch (Exception e) {
//            Log.d("image read ", e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
}
