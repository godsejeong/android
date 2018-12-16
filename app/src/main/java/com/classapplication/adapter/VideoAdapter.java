package com.classapplication.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.classapplication.R;
import com.classapplication.activity.VideoActivity;
import com.classapplication.data.VideoListItem;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    ArrayList<VideoListItem> items = new ArrayList<>();

    public VideoAdapter(ArrayList<VideoListItem> items){
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videoitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        VideoListItem data = items.get(i);

        if(data.isBl()){
            viewHolder.deleteBtn.setVisibility(View.VISIBLE);
        }else{
            viewHolder.deleteBtn.setVisibility(View.INVISIBLE);
        }

        viewHolder.DateTv.setText(data.getTitle());
        viewHolder.DateTv.setText(data.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), VideoActivity.class);
                intent.putExtra("videolink",data.getLink());
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView TitleTv,DateTv;
        Button deleteBtn;

        ViewHolder(View view) {
            super(view);
            TitleTv = view.findViewById(R.id.videoTitle);
            DateTv = view.findViewById(R.id.videoDate);
            deleteBtn = view.findViewById(R.id.videoDelete);
        }
    }
}
