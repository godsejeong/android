package com.classapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.classapplication.R;
import com.classapplication.activity.ChatActivity;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.utils.LectureExistCheck;
import com.classapplication.utils.Utils;

import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecutureInfoActivity extends AppCompatActivity {
    PultusORM orm;
    Button btn1,btn2,btn3,btn4,btn5;
    TextView teachername,lecutrename;
    ImageView imageView;
    String name,mytoken,professor,classname,professorname,imageurl,yourtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuture_info);

        btn1 = findViewById(R.id.infoChat);
        btn2 = findViewById(R.id.infoQA);
        btn3 = findViewById(R.id.infoAttendance);
        btn4 = findViewById(R.id.infoDel);
        btn5 = findViewById(R.id.infoVideo);
//        teachername = findViewById(R.id.infoteacher);
//        lecutrename = findViewById(R.id.infolectureName);
        imageView = findViewById(R.id.infoImage);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);
        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);

        mytoken = getIntent().getStringExtra("mytoken");
        professor = getIntent().getStringExtra("professor");
        classname = getIntent().getStringExtra("classname");
        professorname = getIntent().getStringExtra("professorname");
        imageurl = getIntent().getStringExtra("imageurl");
        yourtoken = getIntent().getStringExtra("yourtoken");
        lecutrename.setText(classname);
        teachername.setText(professorname);
        Glide.with(this).load(imageurl).into(imageView);

        btn1.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                            intent.putExtra("name",user.getName());
                            intent.putExtra("mytoken",mytoken);
                            intent.putExtra("professor",professor);
                            intent.putExtra("student",true);
                            intent.putExtra("classname",classname);
                            intent.putExtra("yourtoken",yourtoken);
                            startActivity(intent);
        });

        btn2.setOnClickListener(v -> lecutruejoin());

        btn3.setOnClickListener(v -> {
            retrofit2.Call<BasicData> res = new Utils().postservice.LectureExist(mytoken,professor);

            res.enqueue(new Callback<BasicData>() {
                @Override
                public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                    if(response.code() == 401){
                        Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
                        intent.putExtra("mytoken",mytoken);
                        intent.putExtra("lecturetoken",professor);
                        startActivity(intent);
                    }else{
                        Log.e("codecode", String.valueOf(response.code()));
                        Toast.makeText(getApplicationContext(), "수강하지 않는 강의입니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BasicData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                    Log.e("LectureExist2Error",t.getMessage());
                }
            });
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<BasicData> res = new Utils().postservice.leaveLecture(professor,mytoken);
                res.enqueue(new Callback<BasicData>() {
                    @Override
                    public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "강의 탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                            Log.e("code", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("leaveLectureError",t.getMessage());
                    }
                });
            }
        });

    btn5.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=  new Intent(getApplicationContext(),VideoActivity.class);
            intent.putExtra("lecuturetoken",professor);
            startActivity(intent);
        }
    });
    }

    void lecutruejoin(){
        Call<BasicData> res = new Utils().postservice.Lecturejoin(professor,mytoken);
        res.enqueue(new Callback<BasicData>() {
            @Override
            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(), "수강신청이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    Toast.makeText(getApplicationContext(), "이미 수강중인 강의 입니다.", Toast.LENGTH_LONG).show();
                }else{
                    Log.e("responcecode", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "수강신청에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BasicData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("LecutruejoinError",t.getMessage());
            }
        });
    }
}
