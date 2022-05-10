package com.example.barcodescanner.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.barcodescanner.R;
import com.example.barcodescanner.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class Add extends Fragment implements AdapterView.OnItemClickListener{

    private FirebaseFirestore firebaseFirestore;

    Boolean currency = false;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    TextInputLayout BarcodeTextInputLayout, typeTextInputLayout, priceTextInputLayout, produdateTextInputLayout,
            expdateTextInputLayout, nameTextInputLayout, searchTextInputLayout, descriptionTextInputLayout;
    TextInputEditText BarcodetextInputEditText, priceTextInputEditText, proddateTextInputEditText, expdateTextInputEditText,
            nameTextInputEditText, descriptionTextEditText;
    AutoCompleteTextView autoCompleteTextView, searchautotextview;
    Button addbtn;
    RadioGroup radioGroup;
    RadioButton rbtn1, rbtn2, rbtn3, rbtn4, rbtn5, rbtn6, rbtn7, rbtn8, rbtn9, rbtn10, rbtn11, rbtn12, rbtn13, rbtn14, rbtn15, rbtn16, rbtn17, rbtn18, rbtn19, rbtn20, rbtn21, rbtn22, rbtn23, rbtn24;
    Context thiscontext;
    IntentIntegrator integrator;
    String[] some_array;
    ArrayAdapter arrayAdapter;

    public Add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        SignUp signUp =new SignUp();
firebaseFirestore =FirebaseFirestore.getInstance();
        thiscontext = container.getContext();
        initViews(view);

        priceTextInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currency) {
                    currency = false;
                    priceTextInputLayout.setStartIconDrawable(R.drawable.shekel);

                } else {
                    currency = true;
                    priceTextInputLayout.setStartIconDrawable(R.drawable.dollar_sign);

                }
            }
        });
        typeTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkboxdialog();
            }
        });
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkboxdialog();
            }
        });


        BarcodeTextInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrator = IntentIntegrator.forSupportFragment(Add.this);


                integrator.initiateScan();

            }
        });
        addbtn.setOnClickListener(View -> {
            if (TextUtils.isEmpty(nameTextInputEditText.getText().toString()) || TextUtils.isEmpty(descriptionTextEditText.getText().toString()) || TextUtils.isEmpty(autoCompleteTextView.getText().toString()) || TextUtils.isEmpty(priceTextInputEditText.getText().toString()) || TextUtils.isEmpty(proddateTextInputEditText.getText().toString()) || TextUtils.isEmpty(expdateTextInputEditText.getText().toString()) || TextUtils.isEmpty(BarcodetextInputEditText.getText().toString())) {
                addtextboxcheck();
            } else {
                Addproduct();

            }
        });
        return view;
    }

    private void Addproduct() {
String product_name,Description,product_type,price,pord_date,exp_date,barcode;
        product_name=nameTextInputEditText.getText().toString();
        Description=descriptionTextEditText.getText().toString();
        product_type=autoCompleteTextView.getText().toString();
        price=priceTextInputEditText.getText().toString();
        pord_date=proddateTextInputEditText.getText().toString();
        exp_date=expdateTextInputEditText.getText().toString();
        barcode=BarcodetextInputEditText.getText().toString();

        HashMap<String,String> data= new HashMap<>();
        data.put("product_name",product_name);
        data.put("Description",Description);
        data.put("product_type",product_type);
        data.put("price",price);
        data.put("pord_date",pord_date);
        data.put("exp_date",exp_date);
        data.put("barcode",barcode);

        firebaseFirestore.collection("Products").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    successfuldialog();
                    Toast
                            .makeText(thiscontext, " product add successful" ,
                                    Toast.LENGTH_SHORT)
                            .show();
                }else{

                }
            }
        });

    }
    public void successfuldialog () {
        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.successfuldialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        LottieAnimationView lottieAnimationView =dialog.findViewById(R.id.fireworks);
lottieAnimationView.setAnimation(R.raw.add_product);

        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }

    private void initViews(View view) {
        addbtn = view.findViewById(R.id.addbtn);
        expdateTextInputEditText = view.findViewById(R.id.ed_Expiration_date);
        expdateTextInputLayout = view.findViewById(R.id.lay_Expiration_date);
        descriptionTextInputLayout = view.findViewById(R.id.descriptionlay);
        descriptionTextEditText = view.findViewById(R.id.ed_description);
        nameTextInputLayout = view.findViewById(R.id.layname);
        nameTextInputEditText = view.findViewById(R.id.edname);
        produdateTextInputLayout = view.findViewById(R.id.lay_Production_date);
        proddateTextInputEditText = view.findViewById(R.id.ed_Production_date);
        priceTextInputLayout = view.findViewById(R.id.Pricelay);
        priceTextInputEditText = view.findViewById(R.id.ed_Price);
        autoCompleteTextView = view.findViewById(R.id.filled_exposed_dropdown);
        typeTextInputLayout = view.findViewById(R.id.type_lay);
        BarcodeTextInputLayout = (TextInputLayout) view.findViewById(R.id.lay_Barcode);
        BarcodetextInputEditText = (TextInputEditText) view.findViewById(R.id.ed_barcode);
    }


    private void checkboxdialog() {
        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.checkboxdialog);

        radioGroup = dialog.findViewById(R.id.radioGroup);

        rbtn1 = dialog.findViewById(R.id.radio_button_1);
        rbtn2 = dialog.findViewById(R.id.radio_button_2);
        rbtn3 = dialog.findViewById(R.id.radio_button_3);
        rbtn4 = dialog.findViewById(R.id.radio_button_4);
        rbtn5 = dialog.findViewById(R.id.radio_button_5);
        rbtn6 = dialog.findViewById(R.id.radio_button_6);
        rbtn7 = dialog.findViewById(R.id.radio_button_7);
        rbtn8 = dialog.findViewById(R.id.radio_button_8);
        rbtn9 = dialog.findViewById(R.id.radio_button_9);
        rbtn10 = dialog.findViewById(R.id.radio_button_10);
        rbtn11 = dialog.findViewById(R.id.radio_button_11);
        rbtn12 = dialog.findViewById(R.id.radio_button_12);
        rbtn13 = dialog.findViewById(R.id.radio_button_13);
        rbtn14 = dialog.findViewById(R.id.radio_button_14);
        rbtn15 = dialog.findViewById(R.id.radio_button_15);
        rbtn16 = dialog.findViewById(R.id.radio_button_16);
        rbtn17 = dialog.findViewById(R.id.radio_button_17);
        rbtn18 = dialog.findViewById(R.id.radio_button_18);
        rbtn19 = dialog.findViewById(R.id.radio_button_19);
        rbtn20 = dialog.findViewById(R.id.radio_button_20);
        rbtn21 = dialog.findViewById(R.id.radio_button_21);
        rbtn22 = dialog.findViewById(R.id.radio_button_22);
        rbtn23 = dialog.findViewById(R.id.radio_button_23);
        rbtn24 = dialog.findViewById(R.id.radio_button_24);
        searchautotextview = dialog.findViewById(R.id.searchautotextview);
        some_array = getResources().getStringArray(R.array.pt);
        arrayAdapter = new ArrayAdapter(thiscontext, android.R.layout.simple_list_item_1, some_array);
        searchautotextview.setAdapter(arrayAdapter);
        searchautotextview.setOnItemClickListener(this);
        searchTextInputLayout = dialog.findViewById(R.id.lay_search);
        searchTextInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault() );//.getDefault() to get and make speak language as your device language
                //you  can use "Locale.US" OR "Locale.forLanguageTag("ar")"
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(thiscontext, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
                startActivityForResult(intent, 1);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                switch (i) {
                    case R.id.radio_button_1:
                        autoCompleteTextView.setText(R.string.Pt1);
                        break;
                    case R.id.radio_button_2:
                        autoCompleteTextView.setText(R.string.Pt2);

                        break;
                    case R.id.radio_button_3:
                        autoCompleteTextView.setText(R.string.Pt3);
                        break;
                    case R.id.radio_button_4:
                        autoCompleteTextView.setText(R.string.Pt4);
                        break;
                    case R.id.radio_button_5:
                        autoCompleteTextView.setText(R.string.Pt5);
                        break;
                    case R.id.radio_button_6:
                        autoCompleteTextView.setText(R.string.Pt6);
                        break;
                    case R.id.radio_button_7:
                        autoCompleteTextView.setText(R.string.Pt7);
                        break;
                    case R.id.radio_button_8:
                        autoCompleteTextView.setText(R.string.Pt8);
                        break;
                    case R.id.radio_button_9:
                        autoCompleteTextView.setText(R.string.Pt9);
                        break;
                    case R.id.radio_button_10:
                        autoCompleteTextView.setText(R.string.Pt10);
                        break;
                    case R.id.radio_button_11:
                        autoCompleteTextView.setText(R.string.Pt11);
                        break;
                    case R.id.radio_button_12:
                        autoCompleteTextView.setText(R.string.Pt12);
                        break;
                    case R.id.radio_button_13:
                        autoCompleteTextView.setText(R.string.Pt13);
                        break;
                    case R.id.radio_button_14:
                        autoCompleteTextView.setText(R.string.Pt14);
                        break;
                    case R.id.radio_button_15:
                        autoCompleteTextView.setText(R.string.Pt15);
                        break;
                    case R.id.radio_button_16:
                        autoCompleteTextView.setText(R.string.Pt16);
                        break;
                    case R.id.radio_button_17:
                        autoCompleteTextView.setText(R.string.Pt17);
                        break;
                    case R.id.radio_button_18:
                        autoCompleteTextView.setText(R.string.Pt18);
                        break;
                    case R.id.radio_button_19:
                        autoCompleteTextView.setText(R.string.Pt19);
                        break;
                    case R.id.radio_button_20:
                        autoCompleteTextView.setText(R.string.Pt20);
                        break;
                    case R.id.radio_button_21:
                        autoCompleteTextView.setText(R.string.Pt21);
                        break;
                    case R.id.radio_button_22:
                        autoCompleteTextView.setText(R.string.Pt22);
                        break;
                    case R.id.radio_button_23:
                        autoCompleteTextView.setText(R.string.Pt23);
                        break;
                    case R.id.radio_button_24:
                        autoCompleteTextView.setText(R.string.Pt24);
                        break;

                }
            }
        });
        ((Button) dialog.findViewById(R.id.Cancelbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear text view when click cancel
                if (radioGroup.getCheckedRadioButtonId() == -1) {

                    autoCompleteTextView.setText("");
                    // no radio buttons are checked
                    dialog.dismiss();

                } else {
                    // one of the radio buttons is checked

                    autoCompleteTextView.setText("");
                    dialog.dismiss();

                }

            }
        });
        ((Button) dialog.findViewById(R.id.savebtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(thiscontext,
//                        "This is a message displayed in a Toast",
//                        Toast.LENGTH_SHORT);
//
//                toast.show();
                if (radioGroup.getCheckedRadioButtonId() == -1) {

                    Toast toast = Toast.makeText(thiscontext,
                            "Please Choose The type of product",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    // no radio buttons are checked
                } else {
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == RESULT_OK && intent != null) {
                ArrayList<String> result = intent.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                searchautotextview.setText(Objects.requireNonNull(result).get(0));
              //  texttospeechautoselect();
                //استبدلنا هاي الميثود بميثود تانية بتمررلها قيمة نص وبتشغتل لحالها
              autoselectmethod( searchautotextview.getText().toString());
            }
        } else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {

                    BarcodetextInputEditText.setText(intentResult.getContents().toString());

                }
            }
        }

    }


    public void addtextboxcheck() {
        if (TextUtils.isEmpty(nameTextInputEditText.getText().toString())) {
            nameTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nameTextInputLayout.setError("");
                    nameTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            nameTextInputLayout.setHelperText("");
        }


        if (TextUtils.isEmpty(descriptionTextEditText.getText().toString())) {
            descriptionTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    descriptionTextInputLayout.setError("");
                    descriptionTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            descriptionTextInputLayout.setHelperText("");
        }


        if (TextUtils.isEmpty(autoCompleteTextView.getText().toString())) {
            typeTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    typeTextInputLayout.setError("");
                    typeTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            typeTextInputLayout.setHelperText("");
        }

        if (TextUtils.isEmpty(priceTextInputEditText.getText().toString())) {
            priceTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    priceTextInputLayout.setError("");
                    priceTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            priceTextInputLayout.setHelperText("");
        }

        if (TextUtils.isEmpty(proddateTextInputEditText.getText().toString())) {
            produdateTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    produdateTextInputLayout.setError("");
                    produdateTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            produdateTextInputLayout.setHelperText("");
        }
        if (TextUtils.isEmpty(expdateTextInputEditText.getText().toString())) {
            expdateTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    expdateTextInputLayout.setError("");
                    expdateTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);
        } else {
            expdateTextInputLayout.setHelperText("");
        }
        if (TextUtils.isEmpty(BarcodetextInputEditText.getText().toString())) {
            BarcodeTextInputLayout.setError(getString(R.string.thisfieldcantbeempty));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BarcodeTextInputLayout.setError("");
                    BarcodeTextInputLayout.setHelperText(getString(R.string.Required));

                }
            }, 4000);

        } else {
            BarcodeTextInputLayout.setHelperText("");
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();

        // create Toast with user selected value
        Toast.makeText(thiscontext, "Selected Item is: \t" + item, Toast.LENGTH_LONG).show();
        searchautotextview.setText(item);
        //auto select when click on the item
        autoselectmethod(item);
        // set user selected value to the TextView
    }
    public String autoselectmethod(String s){
        //this improved function just pass a string to the method and it will run auto and select auto
        //العناصر كلها بفنكشن وحدة بتمررلها متغير من نوع نص عند الاستدعاء
        if(s.toString().equalsIgnoreCase(getString(R.string.Pt1))){
            rbtn1.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt2))){
            rbtn2.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt3))){
            rbtn3.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt4))){
            rbtn4.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt5))){
            rbtn5.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt6))){
            rbtn6.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt7))){
            rbtn7.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt8))){
            rbtn8.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt9))){
            rbtn9.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt10))){
            rbtn10.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt11))){
            rbtn11.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt12))){
            rbtn12.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt13))){
            rbtn13.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt14))){
            rbtn14.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt15))){
            rbtn15.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt16))){
            rbtn16.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt17))){
            rbtn17.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt18))){
            rbtn18.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt19))){
            rbtn19.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt20))){
            rbtn20.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt21))){
            rbtn21.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt22))){
            rbtn22.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt23))){
            rbtn23.setChecked(true);
        }else if(s.toString().equalsIgnoreCase(getString(R.string.Pt24))){
            rbtn24.setChecked(true);
        }
        return s;
    }
}
