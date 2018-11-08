package com.classapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView resigerTv;
    Button loginBtn;
    EditText id,pd;
    PultusORM orm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resigerTv = findViewById(R.id.loginRegisterTv);
        loginBtn = findViewById(R.id.loginBtn);
        id = findViewById(R.id.loginId);
        pd = findViewById(R.id.loginPd);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db",path);

        resigerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!id.getText().toString().isEmpty() && !pd.getText().toString().isEmpty()){
                    login(id.getText().toString(),pd.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"모든항목을 기입해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String id,String pd){
        Call<UserData> res = new Utils().postservice.Login(id,pd);
        res.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.code() == 200){
                    orm.save(response.body());
                    Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else if(response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "서버에러", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("loginError",t.getMessage());
            }
        });
    }
}
