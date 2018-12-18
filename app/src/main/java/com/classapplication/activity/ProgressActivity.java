package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.VideoAdapter;
import com.classapplication.data.VideoData;
import com.classapplication.data.VideoListItem;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressActivity extends AppCompatActivity {
    ArrayList<VideoListItem> item = new ArrayList<>();
    VideoAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new VideoAdapter(item);
        recyclerView.setAdapter(adapter);

        Call<VideoData> res = new Utils().postservice.VideoList(getIntent().getStringExtra("token"));
        res.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getList().size(); i++) {
                        Log.e("list", new Gson().toJson(response.body().getList().get(i)));
                        item.add(new VideoListItem(response.body().getList().get(i).getTitle(),
                                response.body().getList().get(i).getDate(),
                                response.body().getList().get(i).getLink(),
                                getIntent().getStringExtra("token"),
                                true));
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
