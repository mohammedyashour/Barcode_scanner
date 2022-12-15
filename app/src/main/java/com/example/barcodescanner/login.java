package com.example.barcodescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.barcodescanner.explosionfield.ExplosionField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "moudy";
    TextView tv_english, tv_arabic, tv_forget, tv_Signintocontinue, tv_newuser, tv_SignUp;
    private Button login;
    private View parent_view;
    private TextInputLayout usernamelayout, passwordlayout;
    private TextInputEditText username_ed, password_ed;
    private LottieAnimationView fireworks;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    public ProgressDialog progressDialog;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static SharedPreferences sp;
    private ImageView imageView;
    String current_Device_language, current_app_language;
    SharedPreferences sharedPreferences;
    Context context;
    Resources resources;

    ExplosionField explosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        explosionField = new ExplosionField(this);
        explosionField = ExplosionField.attach2Window(this);
        imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explosionField.explode(imageView);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                explosionField.reset(imageView, ExplosionField.SEQUENTIAL);
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("password");
            password_ed.setText(value);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        Log.v(TAG, "current language" + (Locale.getDefault().getDisplayLanguage().toString()));
        initViews();
        tv_arabic = findViewById(R.id.tv_arabic);
        tv_english = findViewById(R.id.tv_english);

        fireworks = findViewById(R.id.fireworks);
        tv_forget = findViewById(R.id.tv_forget);
        tv_Signintocontinue = findViewById(R.id.tv_Signintocontinue);
        tv_newuser = findViewById(R.id.tv_newuser);
        tv_SignUp = findViewById(R.id.tv_SignUp);

        //******
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        sharedPreferences = getSharedPreferences("MySP", MODE_PRIVATE); //open
        if ((Locale.getDefault().getDisplayLanguage().toString()).equals("العربية") && sharedPreferences.getString("language", "").isEmpty()) {
            allarabic();
        } else if ((Locale.getDefault().getDisplayLanguage().toString()).equalsIgnoreCase("English") && sharedPreferences.getString("language", "").isEmpty()) {
            allenglish();
        }

        if (sharedPreferences.getString("language", "").equals("ar")) {
            allarabic();
            Toast.makeText(context, "ar ", Toast.LENGTH_LONG).show();
        } else if (sharedPreferences.getString("language", "").equals("en")) {
            allenglish();
            Toast.makeText(context, "en ", Toast.LENGTH_LONG).show();

        }
        usernamelayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (usernamelayout.getSuffixText().toString()) {
                    case "@gmail.com":
                        usernamelayout.setSuffixText("@hotmail.com");

                        break;
                    case "@hotmail.com":
                        usernamelayout.setSuffixText("@mail.com");

                        break;
                    case "@mail.com":
                        usernamelayout.setSuffixText("@gmail.com");

                        break;


                }

            }
        });
        tv_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(login.this, "ar");
                resources = context.getResources();
                Alerter("تم التحويل الى اللغة العربية", resources.getString(R.string.welcome));
                allarabic();
            }


        });
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(login.this, "en");
                resources = context.getResources();
                Alerter("changed to english language", resources.getString(R.string.welcome));
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
        progressDialog = new ProgressDialog(com.example.barcodescanner.login.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging In...");
    }

    public void LogIn() {
        String email, password;
        email = username_ed.getText().toString();
        password = password_ed.getText().toString();
        switch (usernamelayout.getSuffixText().toString()) {

            case "@gmail.com":
                email = username_ed.getText().toString() + "@gmail.com";

                break;
            case "@hotmail.com":
                email = username_ed.getText().toString() + "@hotmail.com";

                break;
            case "@mail.com":
                email = username_ed.getText().toString() + "@mail.com";

                break;
        }
        if (TextUtils.isEmpty(username_ed.getText().toString()) || TextUtils.isEmpty(password_ed.getText().toString())) {
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

            }
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.v(TAG, "Successfully login");
                        progressDialog.show();

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

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "please check Your Email and password ", Snackbar.LENGTH_LONG).show();

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

                if (fireworks.isAnimating()) {
                    fireworks.cancelAnimation();

                } else {
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


        current_app_language = "ar";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", current_app_language);
        // editor.remove("language");
        editor.commit();

        context = LocaleHelper.setLocale(login.this, "ar");
        resources = context.getResources();
        usernamelayout.setHint(resources.getString(R.string.email));
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

        current_app_language = "en";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", current_app_language);
        // editor.remove("language");


        editor.commit();
        context = LocaleHelper.setLocale(login.this, "en");
        resources = context.getResources();

        usernamelayout.setHint(resources.getString(R.string.email));
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

    private void Alerter(String title, String text) {
        Alerter.create(this)
                .setIcon(R.drawable.alerter_ic_face)
                .setBackgroundColorRes(R.color.Coloralert)
                .setTitle(title)
                .setText(text)
                .enableProgress(true)
                .setProgressColorRes(R.color.Coloralert2)
                .enableSwipeToDismiss()

                .show();

    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

}