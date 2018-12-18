package com.classapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.classapplication.R;
import com.classapplication.activity.ChatActivity;
import com.classapplication.adapter.VideoAdapter;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.data.VideoData;
import com.classapplication.data.VideoListItem;
import com.classapplication.utils.LectureExistCheck;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecutureInfoActivity extends AppCompatActivity {
    PultusORM orm;
    Button btn1, btn2, btn3, btn4;
    TextView lecutureName, lecutureName2, lecutureSort, lecutureDate, lecutureMaxStudent, lecutureCurrentStudent, lecuturePeriod;
    String date, mytoken, professor, classname, professorname, Category, yourtoken, time, term;
    int maxmember, currentMember;
    RecyclerView recyclerView;
    ArrayList<VideoListItem> item = new ArrayList<>();
    VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuture_info);

        btn1 = findViewById(R.id.infoChat);
        btn2 = findViewById(R.id.infoQA);
        btn3 = findViewById(R.id.infoAttendance);
        btn4 = findViewById(R.id.infoDel);

        recyclerView = findViewById(R.id.lecutureRecycler);

        lecutureName = findViewById(R.id.lecutureName);
        lecutureName2 = findViewById(R.id.lecutureName2);
        lecutureSort = findViewById(R.id.lecutureSort);
        lecutureDate = findViewById(R.id.lecutureDate);
        lecutureMaxStudent = findViewById(R.id.lecutureMaxStudent);
        lecutureCurrentStudent = findViewById(R.id.lecutureCurrentStudent);
        lecuturePeriod = findViewById(R.id.lecuturePeriod);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);
        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);

        mytoken = getIntent().getStringExtra("mytoken");
        professor = getIntent().getStringExtra("professor");
        classname = getIntent().getStringExtra("classname");
        professorname = getIntent().getStringExtra("professorname");
        yourtoken = getIntent().getStringExtra("yourtoken");
        Category = getIntent().getStringExtra("Category");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        maxmember = getIntent().getIntExtra("maxmember", 0);
        currentMember = getIntent().getIntExtra("currentMember", 0);
        term = getIntent().getStringExtra("term");

        lecutureName.setText(classname);
        lecutureName2.setText(professorname);
        lecutureSort.setText(Category);
        lecutureDate.setText(date + " " + time);
        lecutureMaxStudent.setText(String.valueOf(maxmember));
        lecutureCurrentStudent.setText(String.valueOf(currentMember));
        lecuturePeriod.setText(term);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new VideoAdapter(item,user.getToken());
        recyclerView.setAdapter(adapter);

        Call<VideoData> res = new Utils().postservice.VideoList(professor);
        retrofit2.Call<BasicData> res2 = new Utils().postservice.LectureExist(mytoken,professor);
        res2.enqueue(new Callback<BasicData>() {
            @Override
            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                if(response.code() == 401){

                    res.enqueue(new Callback<VideoData>() {
                        @Override
                        public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                            if (response.code() == 200) {
                                for (int i = 0; i < response.body().getList().size(); i++) {
                                    Log.e("list", new Gson().toJson(response.body().getList().get(i)));
                                    item.add(new VideoListItem(response.body().getList().get(i).getTitle(),
                                            response.body().getList().get(i).getDate(),
                                            response.body().getList().get(i).getLink(),
                                            professor,
                                            false));
                                }
                                adapter.notifyDataSetChanged();
                                Log.e("lecutureVideo", "Update");
                            } else {
                                Log.e("lecutureVideo", String.valueOf(response.code()));
                                Toast.makeText(getApplicationContext(), "동영상을 불러오는 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<VideoData> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                            Log.e("lecuturevideoError", t.getMessage());
                        }
                    });

                }else{
                    item.add(new VideoListItem("수강신청을 해야지 동영상을 보실수 있습니다.","","1",professor,false));
                    adapter.notifyDataSetChanged();
                    Log.e("codecode", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<BasicData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("LectureExist2Error",t.getMessage());
            }
        });



        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("mytoken", mytoken);
            intent.putExtra("professor", professor);
            intent.putExtra("student", true);
            intent.putExtra("classname", classname);
            intent.putExtra("yourtoken", yourtoken);
            startActivity(intent);
        });

        btn2.setOnClickListener(v -> lecutruejoin());

        btn3.setOnClickListener(v -> {
            retrofit2.Call<BasicData> res3 = new Utils().postservice.LectureExist(mytoken,professor);
            res3.enqueue(new Callback<BasicData>() {
                @Override
                public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                    if(response.code() == 401){
                        Intent intent = new Intent(getApplicationContext(),ProgressActivity.class);
                        intent.putExtra("lecuturetoken",professor);
                        intent.putExtra("token",user.getToken());
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
                deleteshow(professor,mytoken);
            }
        });

    }

    void deleteshow(String professortoken,String token)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("탈퇴하기");
        builder.setMessage("탈퇴하시겠습니까?");

        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<BasicData> res = new Utils().postservice.leaveLecture(professortoken,token);
                        res.enqueue(new Callback<BasicData>() {
                            @Override
                            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "강의 탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                    Log.e("code", String.valueOf(response.code()));
                                }
                            }

                            @Override
                            public void onFailure(Call<BasicData> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                                Log.e("leaveLectureError", t.getMessage());
                            }
                        });
                    }
                });
        builder.setPositiveButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void lecutruejoin() {
        Call<BasicData> res = new Utils().postservice.Lecturejoin(professor, mytoken);
        res.enqueue(new Callback<BasicData>() {
            @Override
            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                if (response.code() == 200) {
                    finish();
                    Toast.makeText(getApplicationContext(), "수강신청이 완료되었습니다. 강의창에 다시 들어와주세요", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "이미 수강중인 강의 입니다.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("responcecode", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "수강신청에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BasicData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("LecutruejoinError", t.getMessage());
            }
        });
    }
}
