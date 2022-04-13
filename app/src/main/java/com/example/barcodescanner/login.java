package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

public class login extends AppCompatActivity {
    private Button login;
    private View parent_view;
    private TextInputLayout usernamelayout, passwordlayout;
    private TextInputEditText username_ed, password_ed;
    private LottieAnimationView fireworks;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.signin);
        username_ed = findViewById(R.id.username_ed);
        usernamelayout = findViewById(R.id.usernamelay);
        fireworks=findViewById(R.id.fireworks);



        //******
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        password_ed = findViewById(R.id.ed_pass);
        passwordlayout = findViewById(R.id.passwordil);

        login.setOnClickListener(view -> {
            if (TextUtils.isEmpty(username_ed.getText().toString()) || TextUtils.isEmpty(password_ed.getText().toString())) {
            textboxcheck();

            } else {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }

        });
//******************login***************************
        ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(login.this, SignUp.class);
                startActivity(intent);
            }
        });
        //****************forget password************
        ((View) findViewById(R.id.forgetpassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    public void textboxcheck() {
        if (TextUtils.isEmpty(username_ed.getText().toString())) {
            usernamelayout.setError("this field cant be empty!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    usernamelayout.setError("");
                    usernamelayout.setHelperText("*Required");

                }
            }, 4000);
        } else {
            usernamelayout.setHelperText("");
        }
        if (TextUtils.isEmpty(password_ed.getText().toString())) {
            passwordlayout.setError("this field cant be empty!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    passwordlayout.setError("");
                    passwordlayout.setHelperText("*Required");

                }
            }, 4000);
        } else {
            passwordlayout.setHelperText("");

        }
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

                if (fireworks.isAnimating()){
                    fireworks.cancelAnimation();

                }else{
                    fireworks.playAnimation();
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
}