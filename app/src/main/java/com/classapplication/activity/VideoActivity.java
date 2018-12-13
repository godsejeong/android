package com.classapplication.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.classapplication.R;
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

        String token = getIntent().getStringExtra("lecuturetoken");
        Log.e("lecuturetoken",token);

        Call<VideoData> res = new Utils().postservice.VideoList(token);
        res.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                if(response.code() == 200){
                    MediaController mediaController = new MediaController(VideoActivity.this);
                    mediaController.setAnchorView(infoVideo);
                    infoVideo.setMediaController(mediaController);
                    infoVideo.setVideoURI(Uri.parse(response.body().getList().get(0).getLink()));
                    infoVideo.start();
                }else{
                    Toast.makeText(getApplicationContext(), "동영상을 불러오는 도중에 오류가 발생하였습ㄴ디ㅏ.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("videoError",t.getMessage());
            }
        });


    }
}
