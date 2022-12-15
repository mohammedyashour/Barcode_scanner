package com.example.barcodescanner.Adapter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.Data.User;
import com.example.barcodescanner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class All_users extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseFirestore db;
    private StorageReference storageReference;
    UserAdapter userAdapter;
    ArrayList<User> list;
    DatabaseReference ref;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        progressDialog.setTitle("Loading Data....");
        progressDialog.show();
        recyclerView = findViewById(R.id.recusers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        list = new ArrayList<User>();
        userAdapter = new UserAdapter(this, list);
        recyclerView.setAdapter(userAdapter);

        EventChangeLisnter();

    }

    private void EventChangeLisnter() {
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(All_users.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        list.add(dc.getDocument().toObject(User.class));
                    }
                    userAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}