package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.util.Locale;
import java.util.Objects;

public class login extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
    private static final String TAG = "moudy";
    TextView tv_english,tv_arabic,tv_forget,tv_Signintocontinue,tv_newuser,tv_SignUp;
    private Button login;
    private View parent_view;
    private TextInputLayout usernamelayout, passwordlayout;
    private TextInputEditText username_ed, password_ed;
    private LottieAnimationView fireworks;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static SharedPreferences sp ;
    String current_Device_language,current_app_language;
    SharedPreferences sharedPreferences ;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        Log.v(TAG, "current language"+(Locale.getDefault().getDisplayLanguage().toString()));
        initViews();
        tv_arabic=findViewById(R.id.tv_arabic);
        tv_english=findViewById(R.id.tv_english);

        fireworks=findViewById(R.id.fireworks);
        tv_forget=findViewById(R.id.tv_forget);
        tv_Signintocontinue=findViewById(R.id.tv_Signintocontinue);
        tv_newuser=findViewById(R.id.tv_newuser);
        tv_SignUp=findViewById(R.id.tv_SignUp);

        //******
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        sharedPreferences = getSharedPreferences("MySP",MODE_PRIVATE); //open
       if((Locale.getDefault().getDisplayLanguage().toString()).equals("العربية")&&sharedPreferences.getString("language","").isEmpty()){
           allarabic();
       }else if((Locale.getDefault().getDisplayLanguage().toString()).equalsIgnoreCase("English")&&sharedPreferences.getString("language","").isEmpty()){
           allenglish();
       }

        if (sharedPreferences.getString("language","").equals("ar")){
            allarabic();
            Toast.makeText(context, "ar " , Toast.LENGTH_LONG).show();
        }else if(sharedPreferences.getString("language","").equals("en")){
            allenglish();
            Toast.makeText(context, "en " , Toast.LENGTH_LONG).show();

        }
        tv_arabic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        allarabic();
    }


});
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allenglish();
            }


        });


        login.setOnClickListener(view -> {
            LogIn();

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

    private void initViews() {
        login = findViewById(R.id.signin);
        username_ed = findViewById(R.id.username_ed);
        usernamelayout = findViewById(R.id.usernamelay);
        password_ed = findViewById(R.id.ed_pass);
        passwordlayout = findViewById(R.id.passwordil);
    }

    public void LogIn() {
        String email,password;
        email=username_ed.getText().toString();
        password=password_ed.getText().toString();

        if (TextUtils.isEmpty(username_ed.getText().toString())||TextUtils.isEmpty(password_ed.getText().toString())){
        if (TextUtils.isEmpty(username_ed.getText().toString())) {
            usernamelayout.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    usernamelayout.setError("");
                    usernamelayout.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        } else {
            usernamelayout.setHelperText("");
        }
        if (TextUtils.isEmpty(password_ed.getText().toString())) {
            passwordlayout.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    passwordlayout.setError("");
                    passwordlayout.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        } else {
            passwordlayout.setHelperText("");

        }}else{
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.v(TAG, "Successfully login");

                 Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        Log.v(TAG, "Successfully intent");

                        startActivity(intent);
                        Log.v(TAG, "Successfully start");

                    }
                });
                t1.start();

                         }else{

            }
            }
        });
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
    private void allarabic() {
        current_app_language="ar";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",current_app_language);
        // editor.remove("language");

        editor.commit();

        context = LocaleHelper.setLocale(login.this, "ar");
        resources = context.getResources();
        usernamelayout.setHint(resources.getString(R.string.Username));
        login.setText(resources.getString(R.string.signin));
        tv_forget.setText(resources.getString(R.string.forgetpasswoed));
        passwordlayout.setHint(resources.getString(R.string.Password));
        tv_Signintocontinue.setText(resources.getString(R.string.Signintocontinue));
        tv_newuser.setText(resources.getString(R.string.newuser));
        tv_SignUp.setText(resources.getString(R.string.SignUp));
        passwordlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        usernamelayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tv_arabic.setTextColor(getResources().getColor(R.color.green_300));
        tv_english.setTextColor(getResources().getColor(R.color.default_text_view_color));
    }
    private void allenglish() {
        current_app_language="en";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",current_app_language);
        // editor.remove("language");


        editor.commit();
        context = LocaleHelper.setLocale(login.this, "en");
        resources = context.getResources();
        usernamelayout.setHint(resources.getString(R.string.Username));
        login.setText(resources.getString(R.string.signin));
        tv_forget.setText(resources.getString(R.string.forgetpasswoed));
        passwordlayout.setHint(resources.getString(R.string.Password));
        tv_Signintocontinue.setText(resources.getString(R.string.Signintocontinue));
        tv_newuser.setText(resources.getString(R.string.newuser));
        tv_SignUp.setText(resources.getString(R.string.SignUp));
        usernamelayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        passwordlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        tv_english.setTextColor(getResources().getColor(R.color.green_300));
        tv_arabic.setTextColor(getResources().getColor(R.color.default_text_view_color));
    }



}