package com.example.kevengao.godairy.person;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kevengao.godairy.R;

public class LightActivity extends Activity {
    private CameraManager manager;// 声明CameraManager对象
    private Camera camera = null;// 声明Camera对象
    private ImageButton light_on;
    private ImageButton light_off;
    private ImageButton light_close;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        light_on = (ImageButton) findViewById(R.id.light_on);
        light_off = (ImageButton) findViewById(R.id.light_off);
        light_close = (ImageButton) findViewById(R.id.light_close);
        light_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LightActivity.this, myhome.class));
            }
        });
    }
}