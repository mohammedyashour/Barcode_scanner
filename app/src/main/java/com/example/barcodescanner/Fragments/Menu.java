package com.example.barcodescanner.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.barcodescanner.MainActivity;
import com.example.barcodescanner.R;
import com.example.barcodescanner.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends Fragment {

    Button btn;
    private TextInputEditText edname, edemail, eddate;
    private TextInputLayout layname, layemail, laydate;
    private CircleImageView profileimage;

    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public Menu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initViews(v);
        edname.setEnabled(false);
        eddate.setEnabled(false);
        edemail.setEnabled(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Sign out your account...");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("users" )
                .whereEqualTo("Email",firebaseAuth.getCurrentUser().getEmail().toString())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                edname.setText(q.getData().get("Username").toString());
                                edemail.setText(q.getData().get("Email").toString());
                                eddate.setText(q.getData().get("dateofbirth").toString());
                                profileimage.setImageURI(  Uri.parse(q.getData().get("imageuri").toString()));

                            }
                        }
                        else{
                            Toast.makeText(getContext(), "failed " ,Toast.LENGTH_LONG).show();

                        }
                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                firebaseAuth.signOut();

                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finishAffinity();

            }
        });
        return v;
    }

    private void initViews(View v) {
        edname = v.findViewById(R.id.edname);
        edemail = v.findViewById(R.id.edemail);
        eddate = v.findViewById(R.id.eddate);
        layname = v.findViewById(R.id.layname);
        layemail = v.findViewById(R.id.layemail);
        laydate = v.findViewById(R.id.laydate);
        btn = v.findViewById(R.id.btnlogout);
        profileimage=v.findViewById(R.id.profile_image);
        //User info

    }
}