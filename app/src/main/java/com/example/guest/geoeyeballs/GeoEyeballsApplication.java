package com.example.guest.geoeyeballs;

import android.app.Application;

import com.parse.Parse;

public class GeoEyeballsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "65NlcfKu2fjtbgH6oQHN3kN7Ek5zrA829PJDttOy", "DRvNB2OiTC5Ezc6zSQcCTyp2GDcxVXrTF368k4Ow");
    }

}
