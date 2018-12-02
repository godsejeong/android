package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;

import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserinfoChangeActivity extends AppCompatActivity {
    ImageView back;
    EditText name,passwd;
    Button finishBtn;
    PultusORM orm;
    TextView userChange,userWithdrawal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_change);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);
        List<Object> userlist = orm.find(new UserData());
        UserData user = (UserData) userlist.get(userlist.size() - 1);

        back = findViewById(R.id.changeBack);
        name = findViewById(R.id.changeName);
        passwd = findViewById(R.id.changePd);
        finishBtn = findViewById(R.id.changeBtn);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name.setText(user.getName());
        passwd.setText(user.getPasswd());

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwd.getText().toString().equals(user.getPasswd())){
                    Toast.makeText(getApplicationContext(), "이전 패스워드와 동일합니다. 다른 패스워드를 입력해주세요", Toast.LENGTH_LONG).show();
                }else if(name.getText().toString().isEmpty() || passwd.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_LONG).show();
                }else{
                    Call<UserData> res = new Utils().postservice.SignModify(user.getToken(),name.getText().toString(),passwd.getText().toString());
                    res.enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if(response.code() == 200){
                                orm.save(response.body());
                                Toast.makeText(getApplicationContext(), "회원정보 수정이 정상적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                Log.e("code", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }
}
