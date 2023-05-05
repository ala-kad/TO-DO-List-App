package com.example.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.miniproject.Adapter.ToDoAdapter;
import com.example.miniproject.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FloatingActionButton Fabtn;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> toDoModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        Fabtn = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        Fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        toDoModelList = new ArrayList<>();
        adapter = new ToDoAdapter(MainActivity.this, toDoModelList);

        recyclerView.setAdapter(adapter);
        showData();
    }

    private void showData(){
        firestore.collection("task").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                        toDoModelList.add(toDoModel);
                        //Notify adapter that data in the toDoModel list has changed
                        adapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(toDoModelList);
            }
        });
    }
}