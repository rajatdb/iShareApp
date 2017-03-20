package com.example.rajat_pc.project_ishare.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.rajat_pc.p2pmanager.p2ptimer.OSTimer;
import com.example.rajat_pc.p2pmanager.p2ptimer.Timeout;
import com.example.rajat_pc.project_ishare.R;


public class FrontNextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_next);
        Timeout timeout = new Timeout()
        {
            @Override
            public void onTimeOut()
            {
                startActivity(new Intent(FrontNextActivity.this, MainActivity.class));
                finish();
            }
        };

        new OSTimer(null, timeout, 4 * 1000).start();
    }
}