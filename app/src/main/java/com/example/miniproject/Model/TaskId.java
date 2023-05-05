package com.example.miniproject.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TaskId {
    //exclude field from DB persistence
    @Exclude
    public String TaskId;

    public  <T extends  TaskId> T withId(@NonNull final String id){
        this.TaskId = id;
        return (T) this;
    }
}
