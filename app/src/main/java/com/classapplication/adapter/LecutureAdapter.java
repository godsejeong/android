package com.classapplication.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.classapplication.activity.LecutureInfoActivity;
import com.classapplication.R;
import com.classapplication.activity.LecuturePagerActivity;
import com.classapplication.activity.StudentActivity;
import com.classapplication.data.LectureListData;

import java.util.ArrayList;

public class LecutureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<LectureListData> items;
    String name;
    public LecutureAdapter(ArrayList<LectureListData> items,String name) {
        this.items = items;
        this.name = name;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayTv, NameTv;

        MyViewHolder(View view) {
            super(view);
            dayTv = view.findViewById(R.id.mylecuturedayTv);
            NameTv = view.findViewById(R.id.mylecutureNameTv);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView professorTv, roomTv, dayTv, NameTv;

        ViewHolder(View view) {
            super(view);
            professorTv = view.findViewById(R.id.lecutureprofessorTv);
            roomTv = view.findViewById(R.id.lecutureRoomTv);
            dayTv = view.findViewById(R.id.lecuturedayTv);
            NameTv = view.findViewById(R.id.lecutureNameTv);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if (i == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lecuture_item, viewGroup, false);
            return new ViewHolder(view);
        } else if (i == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_lecuture_item, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {


        final LectureListData data = items.get(i);

        switch (viewHolder.getItemViewType()) {
            case 0:
                final LecutureAdapter.ViewHolder holder = (ViewHolder) viewHolder;
                holder.dayTv.setText(data.getDate());
                holder.roomTv.setText(data.getCategory());
                holder.professorTv.setText(data.getProfessor());
                holder.NameTv.setText(data.getCourseTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), LecutureInfoActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("mytoken",data.getStudentToken());
                        intent.putExtra("professor",data.getToken());
                        intent.putExtra("student",true);
                        intent.putExtra("classname",data.getCourseTitle());
                        intent.putExtra("professorname",data.getProfessor());
                        intent.putExtra("yourtoken",data.getProfessorToken());

                        intent.putExtra("date",data.getDate());
                        intent.putExtra("time",data.getTime());
                        intent.putExtra("maxmember",data.getMaxMember());
                        intent.putExtra("Category",data.getCategory());
                        intent.putExtra("term",data.getTerm());
                        intent.putExtra("currentMember",data.getCurrentMember());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
                break;
            case 1:
                final LecutureAdapter.MyViewHolder myholder = (MyViewHolder) viewHolder;
                myholder.dayTv.setText(data.getDate());
                myholder.NameTv.setText(data.getCourseTitle());
                myholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myholder.itemView.getContext(), LecuturePagerActivity.class);
                        intent.putExtra("token", data.getToken());
                        intent.putExtra("courseTitle",data.getCourseTitle());
                        intent.putExtra("date",data.getDate());
                        intent.putExtra("time",data.getTime());
                        intent.putExtra("maxmember",data.getMaxMember());
                        intent.putExtra("Category",data.getCategory());
                        intent.putExtra("term",data.getTerm());
                        intent.putExtra("professorname",data.getProfessor());
                        intent.putExtra("currentMember",data.getCurrentMember());
//                        intent.putExtra("")
                        myholder.itemView.getContext().startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
