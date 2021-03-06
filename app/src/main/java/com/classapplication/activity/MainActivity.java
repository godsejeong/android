package com.classapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.LecutureAdapter;
import com.classapplication.data.MyLectureData;
import com.classapplication.data.LectureData;
import com.classapplication.data.LectureListData;
import com.classapplication.data.UserData;
import com.classapplication.service.ChatService;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    PultusORM orm;
    RecyclerView mainList;
    LecutureAdapter adapter;
    ArrayList<LectureListData> items = new ArrayList<>();
    ConstraintLayout serchLayout;
    LinearLayout studentLayout;
    ConstraintLayout professorLayout;
    Button lecutureAddBtn;
    UserData user;
    TextView tv1,tv2,tv3,tv4;

    @Override
    protected void onResume() {
        super.onResume();
        items.clear();
        if (user.getIsProfessor()) {
            studentLayout.setVisibility(View.INVISIBLE);
            professorLayout.setVisibility(View.VISIBLE);
            Call<MyLectureData> res = new Utils().postservice.Lecture(user.getToken());
            res.enqueue(new Callback<MyLectureData>() {
                @Override
                public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
                    MyLectureData obj = response.body();
                    Log.e("testtest", new Gson().toJson(obj.getList()));
                    for (int i = 0; i < obj.getList().size(); i++) {
                        Log.e("i", String.valueOf(i+1));
                        items.add(new LectureListData(
                                obj.getList().get(i).getCourseTitle(),
                                obj.getList().get(i).getDate(),
                                obj.getList().get(i).getCategory(),
                                obj.getList().get(i).getProfessor(),
                                obj.getList().get(i).getToken(),
                                obj.getList().get(i).getTime(),
                                obj.getList().get(i).getMaxMember(),
                                obj.getList().get(i).getTerm(),
                                user.getToken(),
                                obj.getList().get(i).getProfessorToken(),
                                obj.getList().get(i).getCurrentMember(),
                                i+1,
                                false));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MyLectureData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                    Log.e("MyLectureError", t.getMessage());
                }
            });
        } else {
            studentLayout.setVisibility(View.VISIBLE);
            professorLayout.setVisibility(View.INVISIBLE);

            Call<LectureData> res = new Utils().postservice.LectureList();

            res.enqueue(new Callback<LectureData>() {
                @Override
                public void onResponse(Call<LectureData> call, Response<LectureData> response) {
                    if (response.code() == 200) {

                        LectureData obj = response.body();

                        Log.e("ASdfasdf", new Gson().toJson(obj.getList()));


                        for (int i = 0; i < obj.getList().size(); i++) {
                            Log.e("i", String.valueOf(i));
                            items.add(new LectureListData(
                                    obj.getList().get(i).getCourseTitle(),
                                    obj.getList().get(i).getDate(),
                                    obj.getList().get(i).getCategory(),
                                    obj.getList().get(i).getProfessor(),
                                    obj.getList().get(i).getToken(),
                                    obj.getList().get(i).getTime(),
                                    obj.getList().get(i).getMaxMember(),
                                    obj.getList().get(i).getTerm(),
                                    user.getToken(),
                                    obj.getList().get(i).getProfessorToken(),
                                    obj.getList().get(i).getCurrentMember(),
                                    i+1,
                                    true));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<LectureData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                    Log.e("LectureError", t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainList = findViewById(R.id.mainList);
        serchLayout = findViewById(R.id.searchLayout);
        professorLayout = findViewById(R.id.professorLayout);
        studentLayout = findViewById(R.id.studentLayout);
        lecutureAddBtn = findViewById(R.id.lecutureAddBtn);
        tv1 = findViewById(R.id.myLecuture);
        tv2 = findViewById(R.id.Language);
        tv3 = findViewById(R.id.web);
        tv4 = findViewById(R.id.network);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                Call<MyLectureData> res = new Utils().postservice.Lecture(user.getToken());
                res.enqueue(new Callback<MyLectureData>() {
                    @Override
                    public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
                        MyLectureData obj = response.body();
                        items.clear();
                        for (int i = 0; i < obj.getList().size(); i++) {
                            items.add(new LectureListData(
                                    obj.getList().get(i).getCourseTitle(),
                                    obj.getList().get(i).getDate(),
                                    obj.getList().get(i).getCategory(),
                                    obj.getList().get(i).getProfessor(),
                                    obj.getList().get(i).getToken(),
                                    obj.getList().get(i).getTime(),
                                    obj.getList().get(i).getMaxMember(),
                                    obj.getList().get(i).getTerm(),
                                    user.getToken(),
                                    obj.getList().get(i).getProfessorToken(),
                                    obj.getList().get(i).getCurrentMember(),
                                    i+1,
                                    true));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<MyLectureData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("MyLectureError", t.getMessage());
                    }
                });
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                Call<MyLectureData> res = new Utils().postservice.getPro(user.getToken());
                res.enqueue(new Callback<MyLectureData>() {
                    @Override
                    public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
                        MyLectureData obj = response.body();
                        items.clear();
                        for (int i = 0; i < obj.getList().size(); i++) {
                            items.add(new LectureListData(
                                    obj.getList().get(i).getCourseTitle(),
                                    obj.getList().get(i).getDate(),
                                    obj.getList().get(i).getCategory(),
                                    obj.getList().get(i).getProfessor(),
                                    obj.getList().get(i).getToken(),
                                    obj.getList().get(i).getTime(),
                                    obj.getList().get(i).getMaxMember(),
                                    obj.getList().get(i).getTerm(),
                                    user.getToken(),
                                    obj.getList().get(i).getProfessorToken(),
                                    obj.getList().get(i).getCurrentMember(),
                                    i+1,
                                    true));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<MyLectureData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("MyLectureError", t.getMessage());
                    }
                });
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                Call<MyLectureData> res = new Utils().postservice.getWeb(user.getToken());
                res.enqueue(new Callback<MyLectureData>() {
                    @Override
                    public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
                        MyLectureData obj = response.body();
                        items.clear();
                        for (int i = 0; i < obj.getList().size(); i++) {
                            items.add(new LectureListData(
                                    obj.getList().get(i).getCourseTitle(),
                                    obj.getList().get(i).getDate(),
                                    obj.getList().get(i).getCategory(),
                                    obj.getList().get(i).getProfessor(),
                                    obj.getList().get(i).getToken(),
                                    obj.getList().get(i).getTime(),
                                    obj.getList().get(i).getMaxMember(),
                                    obj.getList().get(i).getTerm(),
                                    user.getToken(),
                                    obj.getList().get(i).getProfessorToken(),
                                    obj.getList().get(i).getCurrentMember(),
                                    i+1,
                                    true));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<MyLectureData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("MyLectureError", t.getMessage());
                    }
                });
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            items.clear();
            Call<MyLectureData> res = new Utils().postservice.getNet(user.getToken());
            res.enqueue(new Callback<MyLectureData>() {
                @Override
                public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
                    MyLectureData obj = response.body();
                    items.clear();
                    for (int i = 0; i < obj.getList().size(); i++) {
                        items.add(new LectureListData(
                                obj.getList().get(i).getCourseTitle(),
                                obj.getList().get(i).getDate(),
                                obj.getList().get(i).getCategory(),
                                obj.getList().get(i).getProfessor(),
                                obj.getList().get(i).getToken(),
                                obj.getList().get(i).getTime(),
                                obj.getList().get(i).getMaxMember(),
                                obj.getList().get(i).getTerm(),
                                user.getToken(),
                                obj.getList().get(i).getProfessorToken(),
                                obj.getList().get(i).getCurrentMember(),
                                i+1,
                                true));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MyLectureData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                    Log.e("MyLectureError", t.getMessage());
                }
            });
        }
        });

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        List<Object> userlist = orm.find(new UserData());
        user = (UserData) userlist.get(userlist.size() - 1);

        Intent serviceintent = new Intent(getApplicationContext(), ChatService.class);
        serviceintent.putExtra("mytoken", user.getToken());
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(serviceintent);
        } else {
            startService(serviceintent);
        }

        lecutureAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LecutureAddActivity.class);
                intent.putExtra("add", true);
                startActivity(intent);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainList.setLayoutManager(mLayoutManager);

        adapter = new LecutureAdapter(items, user.getName());
        mainList.setAdapter(adapter);
//        if (user.getIsProfessor()) {
//            studentLayout.setVisibility(View.INVISIBLE);
//            professorLayout.setVisibility(View.VISIBLE);
//            Call<MyLectureData> res = new Utils().postservice.Lecture(user.getToken());
//            res.enqueue(new Callback<MyLectureData>() {
//                @Override
//                public void onResponse(Call<MyLectureData> call, Response<MyLectureData> response) {
//                    MyLectureData obj = response.body();
//                    for (int i = 0; i < obj.getList().size(); i++) {
//                        items.add(new LectureListData(
//                                obj.getList().get(i).getCourseTitle(),
//                                obj.getList().get(i).getDate(),
//                                obj.getList().get(i).getCategory(),
//                                obj.getList().get(i).getProfessor(),
//                                obj.getList().get(i).getToken(),
//                                obj.getList().get(i).getTime(),
//                                obj.getList().get(i).getMaxMember(),
//                                obj.getList().get(i).getTerm(),
//                                user.getToken(),
//                                obj.getList().get(i).getProfessorToken(),
//                                obj.getList().get(i).getCurrentMember(),
//                                false));
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onFailure(Call<MyLectureData> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
//                    Log.e("MyLectureError", t.getMessage());
//                }
//            });
//        } else {
//            studentLayout.setVisibility(View.VISIBLE);
//            professorLayout.setVisibility(View.INVISIBLE);
//
//            Call<LectureData> res = new Utils().postservice.LectureList();
//
//            res.enqueue(new Callback<LectureData>() {
//                @Override
//                public void onResponse(Call<LectureData> call, Response<LectureData> response) {
//                    if (response.code() == 200) {
//
//                        LectureData obj = response.body();
//
//                        Log.e("ASdfasdf", new Gson().toJson(obj.getList()));
//
//
//                        for (int i = 0; i < obj.getList().size(); i++) {
//                            items.add(new LectureListData(
//                                    obj.getList().get(i).getCourseTitle(),
//                                    obj.getList().get(i).getDate(),
//                                    obj.getList().get(i).getCategory(),
//                                    obj.getList().get(i).getProfessor(),
//                                    obj.getList().get(i).getToken(),
//                                    obj.getList().get(i).getTime(),
//                                    obj.getList().get(i).getMaxMember(),
//                                    obj.getList().get(i).getTerm(),
//                                    user.getToken(),
//                                    obj.getList().get(i).getProfessorToken(),
//                                    obj.getList().get(i).getCurrentMember(),
//                                    true));
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<LectureData> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
//                    Log.e("LectureError", t.getMessage());
//                }
//            });
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                orm.delete(new UserData());
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;

            case R.id.userInfoChange:
                startActivity(new Intent(getApplicationContext(), UserSettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
