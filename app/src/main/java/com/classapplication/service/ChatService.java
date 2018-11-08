package com.classapplication.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.support.v4.app.NotificationCompat.Builder;

import com.classapplication.R;
import com.classapplication.activity.ChatActivity;
import com.classapplication.data.ChatData;
import com.classapplication.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class ChatService extends Service {
    io.socket.client.Socket socket;
    String token;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        noti("노티피케이션이 실행중입니다.","실행중...");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            token = intent.getStringExtra("mytoken");
//            Log.e("token",token);
            Log.d("test", "액티비티-서비스");
            try {
                socket = IO.socket(new Utils().url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            socket.on("receive message", onNewMessage);
            socket.connect();
            return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("serviceresiveroom","resive");
            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            String mytoken;
            String youtoken;

            try {
                username = data.getString("name");
                message = data.getString("index");
                mytoken = data.getString("token");
                youtoken = data.getString("your");
                Log.e("name",username);
                Log.e("message",message);
                Log.e("mytoken",mytoken);
                Log.e("youtoken",youtoken);
                    String date = new SimpleDateFormat("a hh:mm").format(new Date());
                    if(token.equals(youtoken)){
                        Chatload(new ChatData("", message,username,date,false),youtoken+mytoken);
                        noti(username,message);


                    }

            } catch (JSONException e) {
                return;
            }
        }
    };

        void noti(String name,String message){
            Intent notificationIntent = new Intent(ChatService.this,ChatActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(ChatService.this, 0, notificationIntent, 0);

            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= 26) {
                String CHANNEL_ID = name;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        name+"Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                        .createNotificationChannel(channel);

                builder = new Builder(ChatService.this,CHANNEL_ID);
            } else {
                builder = new Builder(ChatService.this);
            }
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(name)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL);

            startForeground(1, builder.build());

            Log.e("Asdfasdf","noti");
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDestroy() {
        try {
            Log.e("foregrouind","files");
            stopForeground(true);
            socket.disconnect();
            socket.off("receive message",onNewMessage);
        }catch (Exception e){

        }
    }

    //내토큰 + 상대토큰

    void Chatload(ChatData data,String savedata) {
        Gson gson = new Gson();
        SharedPreferences pref = getSharedPreferences("chat", MODE_PRIVATE);
        String json = pref.getString(savedata, "");
        ArrayList<ChatData> items_ = new ArrayList<>();
        if(json != "")
            items_ = gson.fromJson(json, new TypeToken<ArrayList<ChatData>>(){}.getType());
        items_.add(data);
        Log.e("servicejson",new Gson().toJson(items_));
        SharedPreferences.Editor editor = pref.edit();
        String savejson = new Gson().toJson(items_);
        editor.putString(savedata,savejson);
        editor.commit();
    }
}
