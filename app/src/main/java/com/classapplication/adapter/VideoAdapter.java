package com.classapplication.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.classapplication.R;
import com.classapplication.activity.CalendarActivity;
import com.classapplication.activity.VideoActivity;
import com.classapplication.data.BasicData;
import com.classapplication.data.VideoListItem;
import com.classapplication.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        viewHolder.TitleTv.setText(data.getTitle());
        viewHolder.DateTv.setText(data.getDate());

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<BasicData> res = new Utils().postservice.delVideo(data.getToken(),data.getTitle());
                res.enqueue(new Callback<BasicData>() {
                    @Override
                    public void onResponse(Call<BasicData> call, Response<BasicData> response) {
                        if(response.code() == 200){
                            items.remove(i);
                            notifyDataSetChanged();
                        } else {
                            Log.e("responsecode", String.valueOf(response.code()));
                            Toast.makeText(viewHolder.itemView.getContext(), "동영상을 삭제하는데 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicData> call, Throwable t) {
                        Toast.makeText(viewHolder.itemView.getContext(), "네트워크를 확인해주세요!", Toast.LENGTH_LONG).show();
                        Log.e("lecuturevideoError",t.getMessage());
                    }
                });
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getLink().equals("1")){
                 Toast.makeText(viewHolder.itemView.getContext(), "수강신청을 해야 동영상을 볼수 있습니다.", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(viewHolder.itemView.getContext(), VideoActivity.class);
                    intent.putExtra("videolink", data.getLink());
                    viewHolder.itemView.getContext().startActivity(intent);
                }
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
