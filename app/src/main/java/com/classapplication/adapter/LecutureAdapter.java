package com.classapplication.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        TextView lecutureNameTv, lecutureTime, lecutureMember, lecutureIndex;

        MyViewHolder(View view) {
            super(view);
            lecutureNameTv = view.findViewById(R.id.lecutureNameTv);
            lecutureTime = view.findViewById(R.id.lecutureTime);
            lecutureMember = view.findViewById(R.id.lecutureMember);
            lecutureIndex = view.findViewById(R.id.lecutureIndex);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lecutureNameTv, lecutureTime, lecutureMember, lecutureIndex;

        ViewHolder(View view) {
            super(view);
            lecutureNameTv = view.findViewById(R.id.lecutureNameTv);
            lecutureTime = view.findViewById(R.id.lecutureTime);
            lecutureMember = view.findViewById(R.id.lecutureMember);
            lecutureIndex = view.findViewById(R.id.lecutureIndex);
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lecuture_item, viewGroup, false);
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
                holder.lecutureTime.setText("(" + data.getDate() + ") " + "(" + data.getTime() + ") " + data.getTerm());
                holder.lecutureIndex.setText(String.valueOf(data.getIndex()));
                holder.lecutureMember.setText(String.valueOf(data.getCurrentMember()));
                holder.lecutureNameTv.setText(data.getCourseTitle());

                Log.e("data.getCurrentMember()", String.valueOf(data.getCurrentMember()));

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
                myholder.lecutureTime.setText("(" + data.getDate() + ")" + "(" + data.getTime() + ") " + data.getTerm());
                myholder.lecutureIndex.setText(String.valueOf(data.getIndex()));
                myholder.lecutureMember.setText(String.valueOf(data.getCurrentMember()));
                myholder.lecutureNameTv.setText(data.getCourseTitle());

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
