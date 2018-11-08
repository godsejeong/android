package com.classapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.classapplication.R;
import com.classapplication.data.ChatData;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ChatData> items;


    class ResiveViewHolder extends RecyclerView.ViewHolder {
        TextView resive,time,name;
        ResiveViewHolder(View view){
            super(view);
            resive = view.findViewById(R.id.reciveChat);
            name = view.findViewById(R.id.reciveName);
            time = view.findViewById(R.id.reciveTime);
        }
    }

    class SendViewHolder extends RecyclerView.ViewHolder {
        TextView send,time;
        SendViewHolder(View view) {
            super(view);
            send = view.findViewById(R.id.sendChat);
            time = view.findViewById(R.id.sendTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getViewtype()) {
            return 0;
        } else {
            return 1;
        }
    }

    public ChatAdapter(ArrayList<ChatData> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;

        if(i == 0){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.send_item,viewGroup, false);
            return new SendViewHolder(view);
        }else if(i == 1){
            view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.resive_item,viewGroup, false);
            return new ResiveViewHolder(view);
        }else{
            return new ResiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ChatData data = items.get(i);

        switch (viewHolder.getItemViewType()) {
            case 0:
                SendViewHolder viewHolder1 = (SendViewHolder) viewHolder;
                viewHolder1.send.setText(data.getSend());
                viewHolder1.time.setText(data.getTime());
                break;

            case 1:
                ResiveViewHolder viewHolder2 = (ResiveViewHolder) viewHolder;
                viewHolder2.resive.setText(data.getResive());
                viewHolder2.time.setText(data.getTime());
                viewHolder2.name.setText(data.getName());
                break;
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
