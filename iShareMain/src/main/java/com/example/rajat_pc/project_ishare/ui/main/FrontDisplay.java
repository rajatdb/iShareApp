package com.example.rajat_pc.project_ishare.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.rajat_pc.p2pmanager.p2ptimer.OSTimer;
import com.example.rajat_pc.p2pmanager.p2ptimer.Timeout;
import com.example.rajat_pc.project_ishare.R;

public class FrontDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_display);
        Timeout timeout = new Timeout()
        {
            @Override
            public void onTimeOut()
            {
                startActivity(new Intent(FrontDisplay.this, FrontNextActivity.class));
                finish();
            }
        };

        new OSTimer(null, timeout, 2 * 1000).start();
    }
}
