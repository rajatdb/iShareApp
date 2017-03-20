package com.example.rajat_pc.project_ishare.ui.setting;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.rajat_pc.project_ishare.R;
import com.example.rajat_pc.project_ishare.ui.common.BaseActivity;


public class AboutActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_about_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.menu_about));
;
    }

}
