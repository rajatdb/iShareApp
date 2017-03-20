package com.example.rajat_pc.project_ishare.constant;


import com.example.rajat_pc.project_ishare.MyApplication;
import com.example.rajat_pc.project_ishare.R;



public class Constant
{
    public static final String WIFI_HOT_SPOT_SSID_PREFIX = MyApplication.getInstance()
            .getString(R.string.app_name);
    public static final String FREE_SERVER = "192.168.43.1";

    public interface MSG
    {
        public static final int PICTURE_OK = 0;
        public static final int APP_OK = 1;
    }

}
