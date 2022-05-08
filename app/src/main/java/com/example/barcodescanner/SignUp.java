package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bitvale.switcher.Switcher;
import com.bitvale.switcher.SwitcherX;
import com.example.barcodescanner.Fragments.Scanner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SignUp extends AppCompatActivity {
   private FirebaseAuth firebaseAuth;


    private SwitcherX switcher;
TextView switchtv;
    CircleImageView addprofileimg;
    private int check;
    private TextInputEditText username_et,password_et,dateofbirth_et,confirmpassword_et,email_et;
  private   TextInputLayout username_lay,password_lay,dateofbirth_lay,confirmpass_lay,email_lay;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
  private   String username,email,dateofbirth,password,confirmpassword;
    Resources resources;
private Button signupbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        initViews();

signupbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        signup();
    }
});

        addprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(SignUp.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

            }
        });

        shake();
check=0;
        switcher.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {


                if (switcher.isChecked()){
                    showDialogCongrat();
                    check+=1;
                    switchtv.setTextColor(Color.parseColor("#48ea8b"));
                    //color start icon mode
                    if (password_et.getText().toString().equals(confirmpassword_et.getText().toString())&& !TextUtils.isEmpty(password_et.getText().toString())){
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));}else{
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                    }
                }else{
                    switchtv.setTextColor(Color.parseColor("#ff4651"));
                    //color start icon mode

                    if (password_et.getText().toString().equals(confirmpassword_et.getText().toString())&& !TextUtils.isEmpty(password_et.getText().toString())){
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));}else{
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                    }
                }

                return null;
            }
        });

    }
    public void showDialogCongrat() {
        final Dialog dialogconfrim = new Dialog(this);
        dialogconfrim.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogconfrim.setContentView(R.layout.confirm_dialog);
        dialogconfrim.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogconfrim.setCancelable(true);
        ((Button) dialogconfrim.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogconfrim.dismiss();

            }
        });
        dialogconfrim.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogconfrim.dismiss();
            }
        },7000);

    }
    private void showDialogverify () {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.donedialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 12) {
                if (check==1) {
                    showDialogverify();
                }



            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    public void shake(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;}
    public static  boolean IsValidEmail(String email){
        //    String email_pattern ="[a-zA-0z-9._-]+@gmail.com";
    String email_pattern ="[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
    return email.matches(email_pattern);
    }
    private void initViews() {

        username_et=findViewById(R.id.ed_user);
        email_et=findViewById(R.id.ed_email);
        dateofbirth_et=findViewById(R.id.ed_date);
        password_et=findViewById(R.id.ed_password);
        confirmpassword_et=findViewById(R.id.ed_conpassword);
//**************************************
        username_lay=findViewById(R.id.lay_user);
        password_lay=findViewById(R.id.lay_password);
        email_lay=findViewById(R.id.lay_email);
        confirmpass_lay=findViewById(R.id.lay_conpassword);
        dateofbirth_lay=findViewById(R.id.lay_date);
        //****************************************
        resources = getApplicationContext().getResources();

        switcher =findViewById(R.id.switcher);
        signupbtn=findViewById(R.id.signup_btn);
        switchtv=findViewById(R.id.robotchecktextview);
        addprofileimg=findViewById(R.id.addprofileimage);

    }
        private void signup(){
        username=username_et.getText().toString();
            email=email_et.getText().toString()+"@gmail.com";
            dateofbirth=dateofbirth_et.getText().toString();
            password=password_et.getText().toString();
            confirmpassword=confirmpassword_et.getText().toString();

            if (username.isEmpty()){
                username_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        username_lay.setError("");
                        username_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }  if (email.isEmpty()){
                email_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        email_lay.setError("");
                        email_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }  if (dateofbirth.isEmpty()){
                dateofbirth_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dateofbirth_lay.setError("");
                        dateofbirth_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }  if (password.length()<6){
                password_lay.setError(resources.getString(R.string.lessthen6));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        password_lay.setError("");
                        password_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }  if (confirmpassword.isEmpty()){
                confirmpass_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confirmpass_lay.setError("");
                        confirmpass_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }  if (!password.equals(confirmpassword)){
                confirmpass_lay.setError(resources.getString(R.string.passerror));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confirmpass_lay.setError("");
                        confirmpass_lay.setHelperText(resources.getString(R.string.Required));

                    }
                }, 4000);
            }if(TextUtils.isEmpty(username_et.getText().toString())||TextUtils.isEmpty(email_et.getText().toString()) ||
            TextUtils.isEmpty(password_et.getText().toString())||TextUtils.isEmpty(dateofbirth_et.getText().toString())||
                    TextUtils.isEmpty(confirmpassword_et.getText().toString())){
            }else{
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            successfuldialog();
                        }else{
                            Snackbar.make(findViewById(android.R.id.content),"SignUp Field",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    private void successfuldialog () {

        final Dialog dialog = new Dialog(SignUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.successfuldialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }

}