package com.example.barcodescanner;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.Adapter.UsersAdapter;
import com.example.barcodescanner.modle.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class All_users extends AppCompatActivity {

    RecyclerView rec;
    ArrayList<User> users;

    UsersAdapter adapter;
    FirebaseFirestore store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        store=FirebaseFirestore.getInstance();
        rec=findViewById(R.id.recusers);
       users=new ArrayList<User>();
      adapter=new UsersAdapter(users,All_users.this);
        rec.setAdapter(adapter);
        rec.hasFixedSize();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rec.setLayoutManager(lm);
        EventChangeLister();

    }

    //users info
    private void EventChangeLister() {
        store.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(All_users.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        users.add(doc.getDocument().toObject(User.class));
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });


    }
}