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

        String link = getIntent().getStringExtra("videolink");
        Log.e("lecuturetoken",link);

        MediaController mediaController = new MediaController(VideoActivity.this);
        mediaController.setAnchorView(infoVideo);
        infoVideo.setMediaController(mediaController);
        infoVideo.setVideoURI(Uri.parse(link));
        infoVideo.start();

    }
}
