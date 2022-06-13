package com.example.barcodescanner;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserProfile extends AppCompatActivity {
   private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView username, email;
    private CircularImageView profileimage;
    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
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
                                profileimage.setImageURI(Uri.parse(q.getData().get("imageuri").toString()));
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "failed to get data from firebase ", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    private void initViews() {
        username = findViewById(R.id.profileusername);
        profileimage= findViewById(R.id.image);

    }

}