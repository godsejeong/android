package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.VideoAdapter;
import com.classapplication.data.AchiveData;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.data.VideoData;
import com.classapplication.data.VideoListItem;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressActivity extends AppCompatActivity {
    TextView textview;
    ArrayList<VideoListItem> item = new ArrayList<>();
    VideoAdapter adapter;
    RecyclerView recyclerView;
    String lecuturetoken,token;
    UserData user;
    PultusORM orm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        recyclerView = findViewById(R.id.progressRecycler);
        textview = findViewById(R.id.textView);

        lecuturetoken = getIntent().getStringExtra("lecuturetoken");
        token = getIntent().getStringExtra("token");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);
        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);


        adapter = new VideoAdapter(item,user.getToken());
        recyclerView.setAdapter(adapter);

        Call<AchiveData> res = new Utils().postservice.returnAchive(token,lecuturetoken);
        res.enqueue(new Callback<AchiveData>() {
            @Override
            public void onResponse(Call<AchiveData> call, Response<AchiveData> response) {
                if(response.code() == 200){
                    textview.setText(String.valueOf(response.body().getAchive())+"%");
                }else{
                    Log.e("code", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "진행도를 불러오는 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AchiveData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("lreturnAchiveError", t.getMessage());
            }
        });


        Call<VideoData> res2 = new Utils().postservice.VideoList(lecuturetoken);
        res2.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getList().size(); i++) {
                        Log.e("list", new Gson().toJson(response.body().getList().get(i)));
                        item.add(new VideoListItem(response.body().getList().get(i).getTitle(),
                                response.body().getList().get(i).getDate(),
                                response.body().getList().get(i).getLink(),
                                getIntent().getStringExtra("token"),
                                false));
                    }
                    adapter.notifyDataSetChanged();
                    Log.e("lecutureVideo", "Update");
                } else {
                    Log.e("lecutureVideo", "Fail");
                    Toast.makeText(getApplicationContext(), "동영상을 불러오는 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("lecuturevideoError", t.getMessage());
            }
        });


    }
}
