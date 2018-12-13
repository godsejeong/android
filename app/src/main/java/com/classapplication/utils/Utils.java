package com.classapplication.utils;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    public String url = "http://18.222.180.31:3030";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Services postservice = retrofit.create(Services.class);

    public static MultipartBody.Part createMultipartBody(File file, String name) {
        final RequestBody mFile = RequestBody.create(MediaType.parse("video/*"), file);
        return MultipartBody.Part.createFormData(name, file.getName(), mFile);
    }
}
