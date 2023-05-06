package com.example.miniproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.AddNewTask;
import com.example.miniproject.MainActivity;
import com.example.miniproject.Model.ToDoModel;
import com.example.miniproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder>{

    private List<ToDoModel> toDoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    //Class constructor & initalizing propreties
    public ToDoAdapter(MainActivity mainActivity, List<ToDoModel> toDoList){
        this.toDoList = toDoList;
        activity =mainActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task, parent, false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }
    public void deleteTask (int position){
        ToDoModel toDoModel = toDoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        notifyItemRemoved(position);
    }
    public Context getContext() {
        return activity;
    }
    public void editTask (int position){
        ToDoModel toDoModel = toDoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = toDoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());
        holder.mDueDate.setText("Due on" + toDoModel.getDue());
        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status",0);
                }else {
                    firestore.collection("task").document(toDoModel.TaskId).update("status",1);
                }
            }
        });
    }
    //converting status proprety to boolean if 0 false, true if 1
    private boolean toBoolean (int status){
        return status !=0 ;
    }

    //return the todolist number of items
    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDate;
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            mDueDate = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mycheckbox);
        }
    }

}
