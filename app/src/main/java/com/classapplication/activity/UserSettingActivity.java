package com.classapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.activity.LoginActivity;
import com.classapplication.activity.MainActivity;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.service.ChatService;
import com.classapplication.utils.Utils;

import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingActivity extends AppCompatActivity {
    PultusORM orm;
    TextView userChange,userWithdrawal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);
        List<Object> userlist = orm.find(new UserData());
        UserData user = (UserData) userlist.get(userlist.size() - 1);

        userChange = findViewById(R.id.userChangeTv);
        userWithdrawal = findViewById(R.id.WithdrawalTv);

        userChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserinfoChangeActivity.class));
            }
        });

        userWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<BasicData> res = new Utils().postservice.SignDel(user.getToken());
                res.enqueue(new Callback<BasicData>() {
                    @Override
                    public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                            orm.delete(new UserData());
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                            Log.e("code", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("DelError",t.getMessage());
                    }
                });

            }
        });
    }
}
