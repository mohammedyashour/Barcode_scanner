package com.example.barcodescanner.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcodescanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Dashboard extends Fragment {
FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;
TextView name,email;

    public Dashboard() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        Toast.makeText(getContext(), "Scanned : " +firebaseAuth.getCurrentUser().getEmail().toString(),Toast.LENGTH_LONG).show();
name.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        firebaseFirestore.collection("users" )
                .whereEqualTo("Email",firebaseAuth.getCurrentUser().getEmail().toString())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot q : task.getResult()) {
                        Toast.makeText(getContext(), "Scanned : " +q.getData().get("Username").toString(),Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "failed " ,Toast.LENGTH_LONG).show();

                }
            }
        });

    }
});


        return view;
    }
    private void initViews(View view) {
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);

    }
    }