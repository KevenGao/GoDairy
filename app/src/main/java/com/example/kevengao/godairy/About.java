package com.example.kevengao.godairy;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//关于页面
public class About extends Activity {
    private ImageView about_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about_close=(ImageView)findViewById(R.id.about_close);
        about_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                About.this.finish();
            }
        });

    }

}
