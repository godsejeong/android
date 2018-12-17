package com.classapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.adapter.ChatAdapter;
import com.classapplication.adapter.LecutureAdapter;
import com.classapplication.data.LectureData;
import com.classapplication.data.LectureListData;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    PultusORM orm;
    RecyclerView recyclerView;
    LecutureAdapter adapter;
    EditText tv;
    ImageView btn;
    ArrayList<LectureListData> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.searchRecycler);
        tv = findViewById(R.id.searchTv);
        btn = findViewById(R.id.searchBtn);

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new LecutureAdapter(items,user.getName());
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    items.clear();
                    Call<LectureData> res = new Utils().postservice.SearchLecture(tv.getText().toString());
                    res.enqueue(new Callback<LectureData>() {
                        @Override
                        public void onResponse(Call<LectureData> call, Response<LectureData> response) {
                            if (response.code() == 200) {

                                LectureData obj = response.body();

                                Log.e("ASdfasdf", new Gson().toJson(obj.getList()));

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
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
