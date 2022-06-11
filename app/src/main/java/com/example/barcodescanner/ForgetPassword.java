package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;



public class ForgetPassword extends AppCompatActivity {
    private LottieAnimationView dialog;
    private ImageView lock;
    private TextInputEditText emailed;
    private TextInputLayout emaillay;
    private  Button resetbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        progressDialog = new ProgressDialog(ForgetPassword.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Send email to reset password...");
        progressDialog.setCanceledOnTouchOutside(false);

        lock = findViewById(R.id.lockpng);
        emailed = findViewById(R.id.forgetemailed);
        emaillay = findViewById(R.id.forgetemaillay);
        mAuth = FirebaseAuth.getInstance();
        resetbtn = findViewById(R.id.btn_restPass);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailed.getText() + "";
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emaillay.setError(getResources().getString(R.string.Invalidemailformat));

                }
                if (email.isEmpty()) {
                    emaillay.setError("Please enter email!");

                }
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()) {

                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                     progressDialog.dismiss();
                        resetdone();

                      } else {
                      Toast.makeText(ForgetPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                       progressDialog.dismiss();
                       }
                       }
                    }
                    );
                }


            }
        });
    }

    public void resetdone() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgetdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottie);


        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }
}