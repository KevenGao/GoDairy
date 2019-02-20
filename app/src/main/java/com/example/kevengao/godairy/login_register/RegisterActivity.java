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

import com.example.kevengao.godairy.R;

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

public class RegisterActivity extends Activity {
    private EditText username;
    private EditText psd;
    private Button register;
    private String name1,pwd1;
    private List<UserBean> list;
    private TextView tologin;


    private static final String TAG = "LoginActivity";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                JSONObject obj = null;
                int i = Integer.parseInt(ReturnMessage);

                if(i != 0){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("i", i);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "去登录吧 " + i, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "此用户名已被注册 ", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        psd = findViewById(R.id.psd);
        register = findViewById(R.id.register);
        tologin = findViewById(R.id.tologin);
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name1=username.getText().toString().trim();
                pwd1=psd.getText().toString().trim();
                if ("".equals(name1)||"".equals(pwd1))
                    Toast.makeText(RegisterActivity.this,"请输入用户名或密码",Toast.LENGTH_LONG).show();
                //通过okhttp发起post请求
                postRequest(name1,pwd1);
            }
        });
    }
    /**
     * post请求后台
     * @param username
     * @param password
     */
    private void postRequest(String username,String password)  {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
                .add("username",name1)
                .add("password",pwd1)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http:// 10.0.2.2:8085/DL_Db/RegisterServlet?")
                .post(formBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    OkHttpClient client = new OkHttpClient();

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
        private int i;

        public int getUserid() {
            return i;
        }

        public void setUserid(int i) {
            this.i = i;
        }

        public UserBean() {
        }

        public UserBean(int i) {

            this.i = i;
        }

    }
}

