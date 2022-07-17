package com.example.barcodescanner;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bitvale.switcher.SwitcherX;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    static Random rand;
    static char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    public Uri imageuri;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    final Calendar myCalendar = Calendar.getInstance();
    Boolean not_a_robot;
    private SwitcherX switcher;
    TextView switchtv;
    CircleImageView addprofileimg;
    private int check;
    private AutoCompleteTextView date;
    public TextInputEditText username_et, password_et, confirmpassword_et, email_et;
    private TextInputLayout username_lay, password_lay, dateofbirth_lay, confirmpass_lay, email_lay;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private String username, email, dateofbirth, password, confirmpassword;
    boolean is_internet_connected = false;
    Context context;
    private Boolean imageisselected = false;

    Resources resources;
    private Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        initViews();
        android.app.DatePickerDialog.OnDateSetListener datedialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };


        password_lay.setStartIconOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        password_lay.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random_Password_Generator(10);

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        addprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        shake();
        check = 0;
        switcher.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {


                if (switcher.isChecked()) {

                    showDialogCongrat();
                    not_a_robot = true;

                    check += 1;
                    switchtv.setTextColor(Color.parseColor("#48ea8b"));
                    //color start icon mode
                    if (password_et.getText().toString().equals(confirmpassword_et.getText().toString()) && !TextUtils.isEmpty(password_et.getText().toString())) {
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));
                    } else {
                        password_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                    }
                } else {
                    switchtv.setTextColor(Color.parseColor("#ff4651"));
                    //color start icon mode

                    if (password_et.getText().toString().equals(confirmpassword_et.getText().toString()) && !TextUtils.isEmpty(password_et.getText().toString())) {
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));
                    } else {
                        confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4651")));
                        not_a_robot = false;
                    }
                }

                return null;
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, R.style.DialogTheme, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }


        });
        dateofbirth_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, R.style.DialogTheme, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        email_lay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (email_lay.getSuffixText().toString()) {
                    case "@gmail.com":
                        email_lay.setSuffixText("@hotmail.com");

                        break;
                    case "@hotmail.com":
                        email_lay.setSuffixText("@mail.com");

                        break;
                    case "@mail.com":
                        email_lay.setSuffixText("@gmail.com");

                        break;


                }


            }

        });

    }

    private void Random_Password_Generator(int passlength) {

        for (int i = 0; i < 100; i++) {
            String password = getPassword(passlength).toString();
            password_et.setText(password);
            confirmpassword_et.setText(password);
            confirmpass_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));
            password_lay.setStartIconTintList(ColorStateList.valueOf(Color.parseColor("#48ea8b")));

        }
    }

    public static String getPassword(int length) {
        assert length >= 4;
        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        return new String(password);
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
        }, 7000);

    }

    private void showDialogverify() {

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 10000);
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
                if (check == 1) {
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

    public void shake() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    public static boolean IsValidEmail(String email) {
        //    String email_pattern ="[a-zA-0z-9._-]+@gmail.com";
        String email_pattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
        return email.matches(email_pattern);
    }

    private void initViews() {
        rand = new SecureRandom();
        not_a_robot = false;
        username_et = findViewById(R.id.ed_user);
        email_et = findViewById(R.id.ed_email);
        date = findViewById(R.id.ed_date);
        password_et = findViewById(R.id.ed_password);
        confirmpassword_et = findViewById(R.id.ed_conpassword);
//**************************************
        username_lay = findViewById(R.id.lay_user);
        password_lay = findViewById(R.id.lay_password);
        email_lay = findViewById(R.id.lay_email);
        confirmpass_lay = findViewById(R.id.lay_conpassword);
        dateofbirth_lay = findViewById(R.id.lay_date);
        //****************************************
        resources = getApplicationContext().getResources();

        switcher = findViewById(R.id.switcher);
        signupbtn = findViewById(R.id.signup_btn);
        switchtv = findViewById(R.id.robotchecktextview);
        addprofileimg = findViewById(R.id.addprofileimage);


    }

    private void signup() {
        switch (email_lay.getSuffixText().toString()) {

            case "@gmail.com":
                email = email_et.getText().toString() + "@gmail.com";

                break;
            case "@hotmail.com":
                email = email_et.getText().toString() + "@hotmail.com";

                break;
            case "@mail.com":
                email = email_et.getText().toString() + "@mail.com";

                break;
        }


        username = username_et.getText().toString();

        dateofbirth = date.getText().toString();
        password = password_et.getText().toString();
        confirmpassword = confirmpassword_et.getText().toString();

        if (username.isEmpty()) {
            username_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    username_lay.setError("");
                    username_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (email.isEmpty()) {
            email_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    email_lay.setError("");
                    email_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (dateofbirth.isEmpty()) {
            dateofbirth_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dateofbirth_lay.setError("");
                    dateofbirth_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (password.length() < 6) {
            password_lay.setError(resources.getString(R.string.lessthen6));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    password_lay.setError("");
                    password_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (confirmpassword.isEmpty()) {
            confirmpass_lay.setError(resources.getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirmpass_lay.setError("");
                    confirmpass_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (!password.equals(confirmpassword)) {
            confirmpass_lay.setError(resources.getString(R.string.passerror));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirmpass_lay.setError("");
                    confirmpass_lay.setHelperText(resources.getString(R.string.Required));

                }
            }, 4000);
        }
        if (not_a_robot != true) {
            Snackbar.make(findViewById(android.R.id.content), "please check that your are not a robot", Snackbar.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(username_et.getText().toString()) || TextUtils.isEmpty(email_et.getText().toString()) ||
                TextUtils.isEmpty(password_et.getText().toString()) || TextUtils.isEmpty(date.getText().toString()) ||
                TextUtils.isEmpty(confirmpassword_et.getText().toString())||imageisselected==false){
            if (imageisselected==false){
                Snackbar.make(findViewById(android.R.id.content), " Please Select Profile Image", Snackbar.LENGTH_LONG).show();

                addprofileimg.setBorderWidth(5);
                addprofileimg.setBorderColor(Color.RED);
            }
        } else {
//                if (is_internet_connected) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            HashMap<String, String> data = new HashMap<>();
                            data.put("Uid", uid);
                            data.put("Password", password);
                            data.put("Username", username);
                            data.put("Email", email);
                            data.put("dateofbirth", dateofbirth);
                            data.put("imageuri", imageuri + "");
                            data.put("TopBadge", "empty");
                            firebaseFirestore.collection("users").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                                        progressDialog.setTitle("Uploading Image....");
                                        progressDialog.show();
                                        final String profile_photo_id = user.getEmail().toString();
                                        StorageReference SR = storageReference.child("images/" + profile_photo_id);
                                        SR.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.dismiss();
                                                successfuldialog();
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();

                                                    }
                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double Progress = ((100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                                                progressDialog.setMessage("Percentage:" + (int) Progress + "%");
                                            }
                                        });
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {


                                            }
                                        }, 3000);


                                    } else {
                                        Snackbar.make(findViewById(android.R.id.content), "SignUp Field", Snackbar.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "SignUp Field", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
//                }else {
//                    Toast.makeText(context, "Please connect to internet!", Toast.LENGTH_LONG).show();
//                }
        }
    }

    public void successfuldialog() {

        final Dialog dialog = new Dialog(SignUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.successfuldialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);

            }
        });
        dialog.show();


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            addprofileimg.setImageURI(imageuri);
            imageisselected = true;

        }
    }

    private void UploadPhoto() {


    }
}