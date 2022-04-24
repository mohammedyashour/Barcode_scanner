package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private SwitcherX switcher;
TextView switchtv;
    CircleImageView addprofileimg;
    private int check;
    private TextInputEditText usernamesignup,passwordsignup,dateofbirthsignup,confirmpasswordsignup;
  private   TextInputLayout confirmpass;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
         switcher =findViewById(R.id.switcher);


        switchtv=findViewById(R.id.robotchecktextview);
        addprofileimg=findViewById(R.id.addprofileimage);
        confirmpass=findViewById(R.id.lay_conpassword);
        passwordsignup=findViewById(R.id.ed_password);

        confirmpasswordsignup=findViewById(R.id.ed_conpassword);
        String password,confirm;
        password=passwordsignup.getText().toString();
        confirm=confirmpasswordsignup.getText().toString();

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
                    if (passwordsignup.getText().toString().equals(confirmpasswordsignup.getText().toString())&& !TextUtils.isEmpty(passwordsignup.getText().toString())){
                        confirmpass.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));}else{
                        confirmpass.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                    }
                }else{
                    switchtv.setTextColor(Color.parseColor("#ff4651"));
                    //color start icon mode

                    if (passwordsignup.getText().toString().equals(confirmpasswordsignup.getText().toString())&& !TextUtils.isEmpty(passwordsignup.getText().toString())){
                        confirmpass.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));}else{
                        confirmpass.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
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
}