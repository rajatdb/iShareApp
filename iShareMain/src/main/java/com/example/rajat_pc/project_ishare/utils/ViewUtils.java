package com.example.rajat_pc.project_ishare.utils;


import android.view.View;


public class ViewUtils
{

    private final static String tag = ViewUtils.class.getSimpleName();


    public static int[] getViewItemLocation(View view)
    {
        int[] location = new int[2]; //each item location
        view.getLocationInWindow(location);

        return location;
    }

}
