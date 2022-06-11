package com.example.barcodescanner.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.barcodescanner.All_users;
import com.example.barcodescanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Admin_page extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore;
    private TextView users, products;
    private ImageView instagram, behance, github, facebook;
    private CardView users_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        initViews();


        GetCollectionLength("Products", "error while getting all product number",products);
        GetCollectionLength("users", "error while getting users number",users);


    }

    private void initViews() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        users = findViewById(R.id.users_tv);
        products = findViewById(R.id.products_tv);
        instagram = findViewById(R.id.instabtn);
        behance = findViewById(R.id.behancebtn);
        github = findViewById(R.id.githubbtn);
        facebook = findViewById(R.id.facebookbtn);
        users_card = findViewById(R.id.userscardview);
        users_card.setOnClickListener(this);
        instagram.setOnClickListener(this);
        behance.setOnClickListener(this);
        github.setOnClickListener(this);
        facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.githubbtn:
                gotowebsite("https://github.com/moudyash");
                break;


            case R.id.behancebtn:
                gotowebsite("https://www.behance.net/moudyash");
                break;
            case R.id.facebookbtn:
                gotowebsite("https://www.facebook.com/mohammed.ashour.3726613");
                break;
            case R.id.instabtn:
                gotowebsite("https://www.instagram.com/moudy_ashour");
                break;
            case R.id.userscardview:
                startActivity(new Intent(Admin_page.this, All_users.class));
                break;
        }
    }

    public void gotowebsite(String websitelink) {
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse(websitelink));
        startActivity(openURL);
    }

    public void GetCollectionLength(String CollectionPath, String ErrorMessage,TextView Number_tv ) {
        //this method working by pass CollectionPath & ErrorMessage&Number_tv to get number of document inside the collection
        firebaseFirestore.collection(CollectionPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;

                            }
                            Number_tv.setText(String.valueOf(count));
                        } else {
                            Toast.makeText(Admin_page.this, ErrorMessage, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}