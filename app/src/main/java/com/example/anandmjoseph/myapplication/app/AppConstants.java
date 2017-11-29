package com.example.anandmjoseph.myapplication.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand M Joseph 30/11/17.
 */

public class AppConstants {

    public static String AUTH_KAY                                = "Bearer ";
    public static String[]		PERMISSIONS								= { android.Manifest.permission.INTERNET, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.CAMERA };
     public static String CONTENT_TYPE                            = "application/x-www-form-urlencoded";

     public static int PERMISSION_ACCESS_COURSE_LOCATION = 001;
    public static int PERMISSION_ACCESS_CAMERA = 50;
    public static int PERMISSION_ACCESS_COURSE_LOCATION_TIME = 002;
    public static int COUNTRY_CODE_INDIA = 142;


}