package com.classapplication.activity;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.VideoAdapter;
import com.classapplication.data.BasicData;
import com.classapplication.data.VideoData;
import com.classapplication.data.VideoItem;
import com.classapplication.data.VideoListItem;
import com.classapplication.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecuturePagerActivity extends AppCompatActivity {
    Button studentInfo, videoUpload, lecutureFix, lecutureRemove;
    RecyclerView recyclerView;
    ArrayList<VideoListItem> item = new ArrayList<>();
    VideoAdapter adapter;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuture_pager);

        studentInfo = findViewById(R.id.lecuturePagerStudentInfo);
        lecutureFix = findViewById(R.id.lecuturePagerFix);
        lecutureRemove = findViewById(R.id.lecuturePagerRemove);
        videoUpload = findViewById(R.id.lecuturePagerUpload);
        recyclerView  = findViewById(R.id.lecuturePagerRecycler);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new VideoAdapter(item);
        recyclerView.setAdapter(adapter);

        studentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                intent.putExtra("token", getIntent().getStringExtra("token"));
                startActivity(intent);
            }
        });

        videoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });

        Call<VideoData> res = new Utils().postservice.VideoList(getIntent().getStringExtra("token"));
        res.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getList().size(); i++) {
                        VideoItem data = response.body().getList().get(i);
                        item.add(new VideoListItem(data.getTitle(),data.getDate(),data.getLink(),true));
                    }
                    adapter.notifyDataSetChanged();
                    Log.e("lecutureVideo","Update");
                } else {
                    Log.e("lecutureVideo","Fail");
                    Toast.makeText(getApplicationContext(), "동영상을 불러오는 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("lecuturevideoError",t.getMessage());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("upload1","Result");
        if(resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Log.e("upload","OK");
                Log.e("uploadfile", String.valueOf(data.getData()));
                file = new File(getPath(getApplicationContext(),data.getData()));
                Log.e("file", String.valueOf(file));
                Upload(file,RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra("token")));
            }
        }
    }

    private void Upload(File file, RequestBody token) {
        Log.e("testtoken", String.valueOf(token));
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),file.getName());
        Call<BasicData> res = new Utils().postservice.addVideo(Utils.createMultipartBody(file,"video"),token,filename);
        res.enqueue(new Callback<BasicData>() {
            @Override
            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(), "업로드를 성공하였습니다.", Toast.LENGTH_LONG).show();
                    Log.e("videouploadserver","ok");
                }else{
                    Toast.makeText(getApplicationContext(), "업로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    Log.e("VideoUpload"+String.valueOf(response.code()),response.message());
                }
            }

            @Override
            public void onFailure(Call<BasicData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("UploadError",t.getMessage());
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void getPermission()
    {
        if ((ContextCompat.checkSelfPermission(LecuturePagerActivity.this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED))

            ActivityCompat.requestPermissions(LecuturePagerActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 0)
        {
            if (grantResults[0] == 0) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(i, 100);
            }else{
                Toast.makeText(this, "동영상을 등록하시려면 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


