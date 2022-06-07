package com.example.barcodescanner.Fragments;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends Fragment {

    Button btn;
   private TextView username,email;
    private CircularImageView profileimage;

    ProgressDialog progressDialog;
private LinearLayout logout;
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
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);



        initViews(v);

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
                                username.setText(q.getData().get("Username").toString());
                                email.setText(q.getData().get("Email").toString());
                               // eddate.setText(q.getData().get("dateofbirth").toString());
                                profileimage.setImageURI(  Uri.parse(q.getData().get("imageuri").toString()));

                            }
                        }
                        else{
                            Toast.makeText(getContext(), "failed " ,Toast.LENGTH_LONG).show();

                        }
                    }
                });
        logout.setOnClickListener(new View.OnClickListener() {
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
        username = v.findViewById(R.id.menuusername);
        email = v.findViewById(R.id.menuemail);
       // btn = v.findViewById(R.id.btnlogout);
        profileimage=v.findViewById(R.id.image);
        //User info
profileimage.setBorderWidth(3);
logout=v.findViewById(R.id.logout);
    }
}