package com.example.kevengao.godairy.person;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.kevengao.godairy.MainActivity;
import com.example.kevengao.godairy.R;
import com.example.kevengao.godairy.login_register.LoginActivity;

import java.util.Calendar;

public class myhome  extends Activity {
    private Button bt_login;
    private ImageButton ib_back;
    private LinearLayout shoudiantong;
    private LinearLayout rijiye;
    private Button out;
    private LinearLayout rili;
    private EditText data=null;
    private EditText userid=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhome);
        data=(EditText) findViewById(R.id.data);
        out = (Button) findViewById(R.id.out);
        //取出用户名
        EditText userid=findViewById(R.id.userid);
        String username = getIntent().getStringExtra("username");
        userid.setText(username+"");

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myhome.this, MainActivity.class));
                finish();
            }
        });
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myhome.this, LoginActivity.class));
                finish();
            }
        });
        shoudiantong = (LinearLayout) findViewById(R.id.shoudiantong);
        shoudiantong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myhome.this, LightActivity.class));
            }
        });

        rijiye = (LinearLayout) findViewById(R.id.rijiye);
        rijiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myhome.this, MainActivity.class));
            }
        });
        //日历查看
        rili = (LinearLayout) findViewById(R.id.rili);
        rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                new DatePickerDialog(myhome.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        data.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}