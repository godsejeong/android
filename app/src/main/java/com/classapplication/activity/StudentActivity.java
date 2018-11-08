package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.StudentAdapter;
import com.classapplication.data.BasicData;
import com.classapplication.data.StudentData;
import com.classapplication.data.StudentListItem;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentActivity extends AppCompatActivity {
    PultusORM orm;
    ListView list;
    ArrayList<StudentListItem> item = new ArrayList<>();
    StudentAdapter adapter;
    String token,professor,name,mytoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        list = findViewById(R.id.studentList);

        token = getIntent().getStringExtra("token");
        professor = getIntent().getStringExtra("token");


        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);
        name = user.getName();
        mytoken = user.getToken();
        Call<StudentData> res = new Utils().postservice.Student(token);
        res.enqueue(new Callback<StudentData>() {
            @Override
            public void onResponse(Call<StudentData> call, Response<StudentData> response) {
                if (response.code() == 200) {
                    Log.e("asdfadf", new Gson().toJson(response.body().getList()));

                    for (int i = 0; i < response.body().getList().size(); i++) {
                        item.add(new StudentListItem(response.body().getList().get(i).getName(), response.body().getList().get(i).getToken(),mytoken,"0"));
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "서버에 문제가 있습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudentData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("StudentError", t.getMessage());
            }
        });

        adapter = new StudentAdapter(item,professor,name);
        list.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sendattendance(adapter.attendance());
    }

    public void Sendattendance(String attendance){
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd");
        Date data = new Date();
        String today = df.format(data);

        Call<BasicData> res = new Utils().postservice.Attendance(token,today,attendance);
        res.enqueue(new Callback<BasicData>() {
            @Override
            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                if(response.code() == 200){
                    Log.e("success",response.body().getMessage());
                }else{
                    Log.e("Fali", String.valueOf(response.message()));
                    Toast.makeText(getApplicationContext(), "서버에 문제가 있습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BasicData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                Log.e("AttendanceError", t.getMessage());
            }
        });
    }
}
