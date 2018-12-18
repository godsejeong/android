package com.classapplication.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.classapplication.R;
import com.classapplication.data.BasicData;
import com.classapplication.data.VideoData;
import com.classapplication.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity {
    VideoView infoVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        infoVideo = findViewById(R.id.infoVideo);

        String link = getIntent().getStringExtra("videolink");
        String token = getIntent().getStringExtra("token");
        String lecuturetoken = getIntent().getStringExtra("lecuturetoken");
        boolean check = getIntent().getBooleanExtra("check",false);

        if(!check){
            Call<BasicData> res = new Utils().postservice.upAchive(token,lecuturetoken);
            res.enqueue(new Callback<BasicData>() {
                @Override
                public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                    if(response.code() == 200){
                        Log.e("finish",response.body().getMessage());
                    }else{
                        Log.e("code", String.valueOf(response.code()));
//                        Toast.makeText(getApplicationContext(), "진행도를 불러오는 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BasicData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                    Log.e("upAchiveError", t.getMessage());
                }
            });
        }
        Log.e("lecuturetoken",link);

        MediaController mediaController = new MediaController(VideoActivity.this);
        mediaController.setAnchorView(infoVideo);
        infoVideo.setMediaController(mediaController);
        infoVideo.setVideoURI(Uri.parse(link));
        infoVideo.start();

    }
}
