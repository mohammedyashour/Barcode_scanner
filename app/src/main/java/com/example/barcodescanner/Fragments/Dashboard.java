package com.example.barcodescanner.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.barcodescanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

public class Dashboard extends Fragment {
    private FlowingDrawer mDrawer;

    FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;
TextView name,email;
ImageView profileimage;
LinearLayout inventory;
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

      //  Toast.makeText(getContext(), "Scanned : " +firebaseAuth.getCurrentUser().getEmail().toString(),Toast.LENGTH_LONG).show();


        Thread thread = new Thread() {
            @Override
            public void run() {
                firebaseFirestore.collection("users" )
                        .whereEqualTo("Email",firebaseAuth.getCurrentUser().getEmail().toString())
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot q : task.getResult()) {
                                        name.setText(q.getData().get("Username").toString());
                                        email.setText(q.getData().get("Email").toString());
                                        profileimage.setImageURI(  Uri.parse(q.getData().get("imageuri").toString()));

                                    }
                                }
                                else{
                                    Toast.makeText(getContext(), "failed " ,Toast.LENGTH_LONG).show();

                                }
                            }
                        });



            }
        };
        thread.start();

inventory.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


    }
});

        return view;
    }





    private void initViews(View view) {
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        profileimage=view.findViewById(R.id.image);
        inventory=view.findViewById(R.id.inventory);

    }
    }