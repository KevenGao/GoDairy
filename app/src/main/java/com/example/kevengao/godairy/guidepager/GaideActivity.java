package com.example.kevengao.godairy.guidepager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.kevengao.godairy.R;
import com.example.kevengao.godairy.login_register.LoginActivity;

public class GaideActivity extends AppCompatActivity {
    private static final long DELAY_TIME=3000L;//
    private TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
skip=(TextView)findViewById(R.id.skip);
skip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(GaideActivity.this,LoginActivity.class));
        finish();
    }
});
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GaideActivity.this, LoginActivity.class));
                finish();
            }
        },DELAY_TIME);

    }

}
