package com.classapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.classapplication.service.ChatService;
import com.classapplication.R;
import com.classapplication.adapter.ChatAdapter;
import com.classapplication.data.ChatData;
import com.classapplication.data.UserData;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import ninja.sakib.pultusorm.core.PultusORM;

public class ChatActivity extends AppCompatActivity {
    ChatAdapter adapter;
    RecyclerView chat;
    EditText chatEt;
    Button chatBtn;
    PultusORM orm;
    String mytoken, name, professor, classname, yourtoken;
    boolean student;
    io.socket.client.Socket socket;
    ArrayList<ChatData> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mytoken = getIntent().getStringExtra("mytoken");
        yourtoken = getIntent().getStringExtra("yourtoken");
        name = getIntent().getStringExtra("name");
        professor = getIntent().getStringExtra("professor");
        student = getIntent().getBooleanExtra("student", true);
        classname = getIntent().getStringExtra("classname");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (student)
            getSupportActionBar().setTitle(classname);
        else {
            getSupportActionBar().setTitle(name);
        }
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        try {
            socket = IO.socket(new Utils().url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        chat = findViewById(R.id.chatRecylcer);
        chatEt = findViewById(R.id.chatEt);
        chatBtn = findViewById(R.id.chatBtn);

        List<Object> userlist = orm.find(new UserData());
        final UserData user = (UserData) userlist.get(userlist.size() - 1);


        Log.e(mytoken, professor);

        socket.on("receive message", onNewMessage);
        socket.connect();

        Chatload(mytoken+yourtoken);

        chat.scrollToPosition(items.size());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chat.setLayoutManager(mLayoutManager);

        adapter = new ChatAdapter(items);
        chat.setAdapter(adapter);
        chat.scrollToPosition(adapter.getItemCount() - 1);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatEt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "메시지를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(student){
                        socket.emit("send message", user.getName(), chatEt.getText().toString(), mytoken, yourtoken);
                    }else{
                        socket.emit("professor message", user.getName(), chatEt.getText().toString(), mytoken, yourtoken);
                    }

                    String date = new SimpleDateFormat("a hh:mm").format(new Date());
                    items.add(new ChatData(chatEt.getText().toString(), "", "", date, true));
                    chat.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();
                    Chatsave(mytoken+yourtoken);
                    chatEt.setText(null);
                }
            }
        });
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                chat.scrollToPosition(adapter.getItemCount() - 1);
                adapter.notifyDataSetChanged();
            }
        };

        @Override
        public void call(final Object... args) {
            Log.e("resiveroom","risive");
            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            String resivemytoken;
            String youtoken;
            try {
                username = data.getString("name");
                message = data.getString("index");
                resivemytoken = data.getString("token");
                youtoken = data.getString("your");
                Log.e("name",username);
                Log.e("message",message);
                Log.e("mytoken",resivemytoken);
                Log.e("youtoken",youtoken);
                String date = new SimpleDateFormat("a hh:mm").format(new Date());
                if(mytoken.equals(youtoken)){
                    Chatsave(resivemytoken+youtoken);
                    items.add(new ChatData("",message,username,date,false));
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }

            } catch (JSONException e) {
                return;
            }
        }
    };

    void Chatload(String savecode) {
        Log.e("test", "test");
        Gson gson = new Gson();
        SharedPreferences pref = getSharedPreferences("chat", MODE_PRIVATE);
        String json = pref.getString(savecode, "");
        ArrayList<ChatData> items_ = new ArrayList<>();
        Log.e("loadjson", json);
        items_ = gson.fromJson(json, new TypeToken<ArrayList<ChatData>>() {
        }.getType());
        if (items_ != null) items.addAll(items_);
    }

    void Chatsave(String savecode) {
        SharedPreferences pref = getSharedPreferences("chat", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String json = new Gson().toJson(items);
        editor.putString(savecode, json);
        Log.e("savejson", json);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
