package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.classapplication.R;
import com.classapplication.data.CalendarData;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CalendarActivity extends AppCompatActivity {
    String mytoken, lecturetoken;
    com.applandeo.materialcalendarview.CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        List<EventDay> events = new ArrayList<>();
        calendarView.setEvents(events);

        mytoken = getIntent().getStringExtra("mytoken");
        lecturetoken = getIntent().getStringExtra("lecturetoken");

        Call<CalendarData> res = new Utils().postservice.CalnedarAttendance(mytoken, lecturetoken);

        res.enqueue(new Callback<CalendarData>() {
            @Override
            public void onResponse(Call<CalendarData> call, Response<CalendarData> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getList().size(); i++) {
                        Calendar calendar1 = Calendar.getInstance();

                        calendar1.set(
                                response.body().getList().get(i).getYear(),
                                response.body().getList().get(i).getMonth() - 1,
                                response.body().getList().get(i).getDay()
                        );
                        if (response.body().getList().get(i).getAttendance().equals("0")) {
                            events.add(new EventDay(calendar1, R.drawable.ic_check_false_24dp));
                        } else if(response.body().getList().get(i).getAttendance().equals("1")){
                            events.add(new EventDay(calendar1, R.drawable.ic_check_true_24dp));
                        } else if(response.body().getList().get(i).getAttendance().equals("2")){
                            events.add(new EventDay(calendar1, R.drawable.ic_check_black_24dp));
                        }
                    }

                    calendarView.setEvents(events);
                } else {
                    Log.e("code", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CalendarData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("LectureError", t.getMessage());
            }
        });


//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.set(
//               2018,
//                10,
//                6
//        );
//        events.add(new EventDay(calendar1, R.mipmap.ic_launcher));

//        try {
//            calendarView.setDate(calendar);
//        } catch (OutOfDateRangeException e) {
//            e.printStackTrace();
//        }
    }
}
