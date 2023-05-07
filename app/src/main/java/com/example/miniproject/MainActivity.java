package com.example.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.miniproject.Adapter.ToDoAdapter;
import com.example.miniproject.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{


    private RecyclerView recyclerView;
    private FloatingActionButton Fabtn;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> toDoModelList;
    private  Query query;
    private ListenerRegistration listenerRegistration;
    private Button logoutBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Binding activity views to class attributes
        recyclerView = findViewById(R.id.recyclerView);
        Fabtn = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();
        logoutBtn = findViewById(R.id.logoutBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        //Logout Business Logic
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                //Navigating user to Login Activity after SigningOut
                startActivity(new Intent(MainActivity.this, LoginActivty.class));
                finish();
            }
        });

        //setting fixed size to recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Add new task button click
        Fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        //Adapting recycler view
        toDoModelList = new ArrayList<>();
        adapter = new ToDoAdapter(MainActivity.this, toDoModelList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);
    }

    //Getting all tasks and rendering them
    private void showData(){
       query = firestore.collection("task").orderBy("time", Query.Direction.DESCENDING);
       listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                //destructing listenerRegitration
                listenerRegistration.remove();
            }
        });
    }

    //clearing the taskList after closing the model
    @Override
    public void onDialogCLose(DialogInterface dialogInterface) {
        toDoModelList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}