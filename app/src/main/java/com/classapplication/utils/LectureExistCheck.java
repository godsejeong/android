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

    retrofit2.Call<BasicData> res = new Utils().postservice.LectureExist(mytoken,lecuturetokrn);

    @Override
    public void run() {
        super.run();
        try {
            switch (res.execute().code()) {
                case 200:
                    check =  true;
                    Log.e("ASDadf","200");
                    break;
                case 401:
                    check =  false;
                    Log.e("ASDadf","401");
                    break;
                default:
                    check =  false;
                    Log.e("ASDadf","sdafadf");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Boolean checkdata(){
        return check;
    }

}
