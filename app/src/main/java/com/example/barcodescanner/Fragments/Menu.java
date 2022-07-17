package com.example.barcodescanner.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.example.barcodescanner.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import tyrantgit.explosionfield.ExplosionField;

public class Menu extends Fragment implements View.OnClickListener{
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
    SharedPreferences sharedPreferences;
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
        profile = v.findViewById(R.id.profile);
        settings = v.findViewById(R.id.Settings);
        explosionField = ExplosionField.attach2Window(getActivity());

//**********************************************************************
        profileimage.setOnClickListener(this);
        settings.setOnClickListener(this);
        admin.setOnClickListener(this);
        logout.setOnClickListener(this);
        profile.setOnClickListener(this);

    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.image:
                showImage();

                break;
            case R.id.Settings:
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
                break;
            case R.id.adminpage:
                Intent adminintent = new Intent(getActivity(), Admin_page.class);
                startActivity(adminintent);
                break;
            case R.id.logout:
              logoutfun();
                break;
            case R.id.profile:
                Intent profileintent = new Intent(getActivity(), UserProfile.class);
                startActivity(profileintent);
                break;
        }

        }
    private void logoutfun() {
        progressDialog.show();

       // firebaseAuth.signOut();
          setDefaults("currentbadge","empty",getContext());

        String s=  getDefaults("currentbadge",getContext());
       //  sharedPreferences = getActivity().getSharedPreferences("Mysp", MODE_PRIVATE);


        Snackbar.make(getActivity().findViewById(android.R.id.content),s, Snackbar.LENGTH_LONG).show();

//        Intent logoutintent = new Intent(getActivity(), login.class);
//        startActivity(logoutintent);
//        getActivity().finishAffinity();
    }
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}