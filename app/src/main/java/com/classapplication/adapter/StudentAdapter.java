package com.classapplication.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.classapplication.R;
import com.classapplication.activity.ChatActivity;
import com.classapplication.data.StudentListItem;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter{
    ArrayList<StudentListItem> items = new ArrayList();
    String professor;
    String name_;
    public StudentAdapter( ArrayList<StudentListItem> items,String professor,String name){
        this.items = items;
        this.professor = professor;
        this.name_ = name;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        }

        Button btn1 = convertView.findViewById(R.id.studentBtn1);
        Button btn2 = convertView.findViewById(R.id.studentBtn2);
        Button btn3 = convertView.findViewById(R.id.studentBtn3);
        final TextView name = convertView.findViewById(R.id.studentTv);
        final StudentListItem data = items.get(position);
        name.setText(data.getName());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(position,true);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(position,false);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(),ChatActivity.class);
                intent.putExtra("name",name_);
                intent.putExtra("mytoken",data.getMytoken());
                intent.putExtra("professor",professor);
                intent.putExtra("student",false);
                intent.putExtra("classname","");
                intent.putExtra("yourtoken",data.getToken());
                parent.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    public void check(int position,boolean bool){
        if(bool){
            items.get(position).setAttendance("1");
        }else{
            items.get(position).setAttendance("0");
        }

        Log.e("check",items.get(position).getAttendance());
    }

    public String attendance(){
        String attendance = "";

        for(int i = 0;i< items.size();i++){
            attendance+= items.get(i).getAttendance();
        }

        return attendance;
    }
}
