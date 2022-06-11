package com.example.barcodescanner.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.barcodescanner.Admin.Admin_page;
import com.example.barcodescanner.Admin.Settings;
import com.example.barcodescanner.R;
import com.example.barcodescanner.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import tyrantgit.explosionfield.ExplosionField;

public class Menu extends Fragment {
    private ExplosionField explosionField;

    Button btn;
    private TextView username, email;
    private CircularImageView profileimage;

    ProgressDialog progressDialog;
    private LinearLayout admin, logout, profile,settings;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String currentUserID;
String uri;
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
        CheckAdmin();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Sign out your account...");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("users")
                .whereEqualTo("Email", firebaseAuth.getCurrentUser().getEmail().toString())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                username.setText(q.getData().get("Username").toString());
                                email.setText(q.getData().get("Email").toString());
                                // eddate.setText(q.getData().get("dateofbirth").toString());
                                profileimage.setImageURI(Uri.parse(q.getData().get("imageuri").toString()));
     uri= String.valueOf(Uri.parse(q.getData().get("imageuri").toString()));
                            }
                        } else {
                            Toast.makeText(getContext(), "failed ", Toast.LENGTH_LONG).show();

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
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Admin_page.class);
                startActivity(intent);

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        return v;
    }
    public void showImage(){
        Dialog builder = new Dialog(getActivity(),R.style.PauseDialog);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(350, 450);
imageView.setLayoutParams(layoutParams);
        imageView.setImageURI(Uri.parse(uri));
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        explosionField.explode(imageView);
        builder.dismiss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);
                         }




});
        builder.show();
    }

    private void CheckAdmin() {

        //check Admin
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserID = user.getEmail();
            if (currentUserID.equals("medo.ash.2019@gmail.com")) {
                admin.setVisibility(View.VISIBLE);
            } else
                admin.setVisibility(View.GONE);
        }

    }

    private void initViews(View v) {
        username = v.findViewById(R.id.menuusername);
        email = v.findViewById(R.id.menuemail);
        // btn = v.findViewById(R.id.btnlogout);
        profileimage = v.findViewById(R.id.image);
        //User info
        profileimage.setBorderWidth(3);
        logout = v.findViewById(R.id.logout);
        admin = v.findViewById(R.id.adminpage);
        profile = v.findViewById(R.id.usernameandemail);
        settings = v.findViewById(R.id.Settings);
        explosionField = ExplosionField.attach2Window(getActivity());

    }
}