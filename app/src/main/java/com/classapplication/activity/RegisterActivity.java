package com.classapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    ImageView backBtn;
    TextView name, id, pd;
    RadioButton professorRb,studentRb;
    RadioGroup rg;
    Boolean check = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backBtn = findViewById(R.id.registerBack);
        registerBtn = findViewById(R.id.registerBtn);
        name = findViewById(R.id.registerName);
        name = findViewById(R.id.registerName);
        id = findViewById(R.id.registerId);
        pd = findViewById(R.id.registerPd);
        rg = findViewById(R.id.regiserGrop);
        professorRb = findViewById(R.id.professorRb);
        studentRb = findViewById(R.id.studentRb);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioid = rg.getCheckedRadioButtonId();
                RadioButton rb =  findViewById(radioid);
                try {
                    if (rb.getText().toString().equals("학생")) {
                        check = false;
                    } else if (rb.getText().toString().equals("교수")) {
                        check = true;
                    }
                }catch (NullPointerException e){
                    check = null;
                }

                if (!id.getText().toString().isEmpty() && !pd.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && check != null) {
                    register(name.getText().toString(), pd.getText().toString(), id.getText().toString(), check);
                } else {
                    Toast.makeText(getApplicationContext(), "모든항목을 기입해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(String name,String pd,String id,Boolean type){
        Call<UserData> res = new Utils().postservice.Resiger(name,pd,id,type);
        res.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }else if(response.code() == 409) {
                    Toast.makeText(getApplicationContext(), "아이디가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "서버에러", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("resigerError",t.getMessage());
            }
        });
    }
}
