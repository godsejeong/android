package com.classapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.data.BasicData;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;

import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecutureAddActivity extends AppCompatActivity {
    PultusORM orm;
    EditText lecutureAddName,lecutureAddName2,lecutureAddSort,lecutureAddDate,lecutureMaxAddStudent,lecutureAddPeriod,lecutureAddtime;
    Button addBtn;
    boolean check;
    String lecuturetoken,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecuture_add);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);
        token = user.getToken();

        lecutureAddName = findViewById(R.id.lecutureAddName);
        lecutureAddName2 = findViewById(R.id.lecutureAddName2);
        lecutureAddSort = findViewById(R.id.lecutureAddSort);
        lecutureAddDate = findViewById(R.id.lecutureAddDate);
        lecutureMaxAddStudent = findViewById(R.id.lecutureMaxAddStudent);
        lecutureAddPeriod = findViewById(R.id.lecutureAddPeriod);
        lecutureAddtime =findViewById(R.id.lecutureAddtime);
        addBtn = findViewById(R.id.addBtn);
        check = getIntent().getBooleanExtra("add",false);

        if(!check){
            addBtn.setText("수정하기");
            lecuturetoken = getIntent().getStringExtra("token");
            String professorname = getIntent().getStringExtra("professorname");
            String courseTitle = getIntent().getStringExtra("courseTitle");
            String date = getIntent().getStringExtra("date");
            String maxmember = getIntent().getStringExtra("maxmember");
            String Category = getIntent().getStringExtra("Category");
            String term = getIntent().getStringExtra("term");
            String time = getIntent().getStringExtra("time");
            lecutureAddName.setText(courseTitle);
            lecutureAddName2.setText(professorname);
            lecutureAddSort.setText(Category);
            lecutureAddDate.setText(date);
            lecutureMaxAddStudent.setText(maxmember);
            lecutureAddPeriod.setText(term);
            lecutureAddtime.setText(time);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check){
                    if(!lecutureAddName.getText().toString().isEmpty() &&
                       !lecutureAddName2.getText().toString().isEmpty() &&
                       !lecutureAddSort.getText().toString().isEmpty() &&
                       !lecutureAddDate.getText().toString().isEmpty() &&
                       !lecutureAddtime.getText().toString().isEmpty() &&
                       !lecutureMaxAddStudent.getText().toString().isEmpty() &&
                       !lecutureAddPeriod.getText().toString().isEmpty()){


                        Call<BasicData> res = new Utils().postservice.newLecture(
                                lecutureAddName.getText().toString(),
                                lecutureAddName2.getText().toString(),
                                token,
                                lecutureAddSort.getText().toString(),
                                lecutureAddDate.getText().toString(),
                                lecutureAddtime.getText().toString(),
                                Integer.parseInt(lecutureMaxAddStudent.getText().toString()),
                                lecutureAddPeriod.getText().toString()
                        );
                        res.enqueue(new Callback<BasicData>() {
                            @Override
                            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                                if(response.code() == 200){
                                    finish();
                                    Toast.makeText(getApplicationContext(), "강의가 추가가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                }else{
                                    Log.e("responsecode : ", String.valueOf(response.code()));
                                    Toast.makeText(getApplicationContext(), "강의가 추가되지 않았습니다", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BasicData> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                                Log.e("LectureaddError", t.getMessage());
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "모든항목을 기입해주세요", Toast.LENGTH_LONG).show();

                    }
                }else {
                    if(!lecutureAddName.getText().toString().isEmpty() &&
                            !lecutureAddName2.getText().toString().isEmpty() &&
                            !lecutureAddSort.getText().toString().isEmpty() &&
                            !lecutureAddDate.getText().toString().isEmpty() &&
                            !lecutureAddtime.getText().toString().isEmpty() &&
                            !lecutureMaxAddStudent.getText().toString().isEmpty() &&
                            !lecutureAddPeriod.getText().toString().isEmpty()) {


                        Call<BasicData> res = new Utils().postservice.setLecture(
                                lecutureAddName.getText().toString(),
                                lecutureAddName2.getText().toString(),
                                token,
                                lecutureAddSort.getText().toString(),
                                lecutureAddDate.getText().toString(),
                                lecutureAddtime.getText().toString(),
                                Integer.parseInt(lecutureMaxAddStudent.getText().toString()),
                                lecutureAddPeriod.getText().toString(),
                                lecuturetoken
                        );
                        res.enqueue(new Callback<BasicData>() {
                            @Override
                            public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                                if(response.code() == 200){
                                    Intent intent = new Intent();
                                    intent.putExtra("courseTitle",lecutureAddName.getText().toString());
                                    intent.putExtra("Category",lecutureAddSort.getText().toString());
                                    intent.putExtra("professorname",lecutureAddName2.getText().toString());
                                    intent.putExtra("date",lecutureAddDate.getText().toString());
                                    intent.putExtra("time",lecutureAddtime.getText().toString());
                                    intent.putExtra("maxmember",lecutureMaxAddStudent.getText().toString());
                                    intent.putExtra("term",lecutureAddPeriod.getText().toString());
                                    setResult(Activity.RESULT_OK,intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "강의 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                }else{
                                    Log.e("responsecode : ", String.valueOf(response.code()));
                                    Toast.makeText(getApplicationContext(), "강의가 수정되지 않았습니다", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BasicData> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                                Log.e("LecturesetError", t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }
}
