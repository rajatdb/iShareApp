package com.example.rajat_pc.project_ishare.ui.main;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.rajat_pc.project_ishare.R;
import com.example.rajat_pc.project_ishare.sdk.cache.Cache;
import com.example.rajat_pc.project_ishare.ui.setting.AboutActivity;
import com.example.rajat_pc.project_ishare.ui.setting.BlueToothActivity;
import com.example.rajat_pc.project_ishare.ui.setting.FileBrowseActivity;
import com.example.rajat_pc.project_ishare.ui.transfer.FileSelectActivity;
import com.example.rajat_pc.project_ishare.ui.transfer.ReceiveActivity;
import com.example.rajat_pc.project_ishare.utils.PreferenceUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int counter = 0;
    private EditText nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        Button send = (Button) findViewById(R.id.activity_main_i_send);
        send.setOnClickListener(this);
        Button receive = (Button) findViewById(R.id.activity_main_i_receive);
        receive.setOnClickListener(this);

        nameEdit = (EditText) findViewById(R.id.activity_main_name_edit);
        nameEdit.setText((String) PreferenceUtils.getParam(MainActivity.this, " String", Build.DEVICE));

        YoYo.with(Techniques.ZoomOut).duration(4000).delay(2000).playOn(findViewById(R.id.welcome));
        YoYo.with(Techniques.ZoomIn).duration(4000).playOn(findViewById(R.id.button));
        YoYo.with(Techniques.ZoomIn).duration(4000).playOn(findViewById(R.id.activity_main_i_send));
        YoYo.with(Techniques.ZoomIn).duration(4000).playOn(findViewById(R.id.activity_main_i_receive));
        YoYo.with(Techniques.BounceIn).duration(4000).duration(2000).playOn(findViewById(R.id.activity_main_name_edit));
        YoYo.with(Techniques.BounceInUp).duration(4000).duration(2000).playOn(findViewById(R.id.device));

    }

    @Override
    public void onPause()
    {
        super.onPause();
        PreferenceUtils.setParam(MainActivity.this, " String", nameEdit.getText().toString());
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.activity_main_i_receive :
                Cache.selectedList.clear();
                startActivity(new Intent(MainActivity.this, ReceiveActivity.class).putExtra(" name", nameEdit.getText().toString()));
                break;
            case R.id.activity_main_i_send :
                Cache.selectedList.clear();
                startActivity(new Intent(MainActivity.this, FileSelectActivity.class).putExtra(" name", nameEdit.getText().toString()));
                break;
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            switch (menuItem.getItemId())
            {
                case R.id.menu_item_receive_directory :
                    startActivity(new Intent(MainActivity.this, FileBrowseActivity.class));
                    break;
                case R.id.menu_item_about :
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case R.id.menu_item_bluetooth_device :
                    startActivity(new Intent(MainActivity.this, BlueToothActivity.class));
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(counter < 1){
            Toast.makeText(getApplicationContext(), "Press once again to Exit", Toast.LENGTH_SHORT).show();
            counter++;
            return;
        }
        else if(counter == 1){
            super.onBackPressed();
            finish();
        }
    }
}
