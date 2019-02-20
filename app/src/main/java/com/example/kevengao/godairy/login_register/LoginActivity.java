package com.example.kevengao.godairy.login_register;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevengao.godairy.MainActivity;
import com.example.kevengao.godairy.R;
import com.example.kevengao.godairy.person.myhome;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {
    private EditText username;
    private EditText psd;
    private Button login;
    private String name1,pwd1;
    private List<UserBean> list;
    private TextView toregister;
    private Button skip;

   // final OkHttpClient client = new OkHttpClient();

    private static final String TAG = "LoginActivity";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                JSONObject obj = null;
                String username = null;
                try{
                    obj = new JSONObject(ReturnMessage);
                   username= obj.getString("username");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(username!= null){
                    Intent intent = new Intent(LoginActivity.this, myhome.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "欢迎来到精灵日记 " + username, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "抱歉，没有你的账号，去注册一个吧！ ", Toast.LENGTH_SHORT).show();

                }
                /***
                 * 在此处可以通过获取到的Msg值来判断
                 * 给出用户提示注册成功 与否，以及判断是否用户名已经存在
                 */
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        psd = findViewById(R.id.psd);
        login = findViewById(R.id.login);
        toregister = findViewById(R.id.toregister);
        skip=(Button)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(username == null) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                }
                else if(psd!=null){*/


                name1=username.getText().toString().trim();
                pwd1=psd.getText().toString().trim();
                if ("".equals(name1)||"".equals(pwd1)){
                    Toast.makeText(LoginActivity.this, "请输入用户名或密码", Toast.LENGTH_LONG).show();
                }else {
                //通过okhttp发起post请求
                postRequest(name1,pwd1);
                }

            }
        });
        toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    //post请求后台
    private void postRequest(String username,String password)  {
        //建立请求表单，添加上传服务器的参数

        RequestBody formBody = new FormBody.Builder()
                .add("username",name1)
                .add("password",pwd1)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http:// 10.0.2.2:8085/DL_Db/LoginServlet?")
                .post(formBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                OkHttpClient client = new OkHttpClient();
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        BufferedReader reader = null;
                        InputStream in = response.body().byteStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        String line ;
                        final StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null){
                            sb.append(line);
                            Log.i(TAG, "onResponse: " +sb.toString());
                        }

                        String str = sb.toString();
                        Message m = mHandler.obtainMessage();
                        m.what = 1;
                        m.obj = str;
                        mHandler.sendMessage(m);

                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public class UserBean {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserBean() {
        }

        public UserBean(int id) {

            this.id = id;
        }


    }
}
