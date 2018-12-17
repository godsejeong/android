package com.classapplication.utils;

import android.util.Log;

import com.classapplication.data.BasicData;

import java.io.IOException;

public class LectureExistCheck extends Thread {
    String mytoken;
    String lecuturetokrn;
    Boolean check = false;

    public LectureExistCheck(String mytoken, String lecuturetokrn) {
        this.lecuturetokrn = lecuturetokrn;
        this.mytoken = mytoken;
    }

    retrofit2.Call<BasicData> res = new Utils().postservice.LectureExist(mytoken, lecuturetokrn);

    @Override
    public void run() {
        try {
            Log.e("asdf", "괴");
            if (res.execute().code() == 401) {
                check = true;
                Log.e("ASDadf", "401");
            } else if (res.execute().code() == 200) {
                check = false;
                Log.e("ASDadf", "200");
            } else {
                check = false;
                Log.e("ASDadf", "sdafadf");
            }
    } catch(
    IOException e)
    {
        Log.e("ASDadf",e.getMessage());
        e.printStackTrace();
    }

}


    public Boolean checkdata() {
        Log.e("check", String.valueOf(check));
        return check;
    }

}
