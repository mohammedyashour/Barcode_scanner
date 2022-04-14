package com.example.barcodescanner.Fragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcodescanner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;


public class Add extends Fragment {
    TextInputLayout BarcodeTextInputLayout,typeTextInputLayout;
    TextInputEditText BarcodetextInputEditText;
    AutoCompleteTextView autoCompleteTextView;
    int radioButtonID;
RadioGroup radioGroup;
RadioButton rbtn1,rbtn2,rbtn3,rbtn4,rbtn5,rbtn6,rbtn7,rbtn8,rbtn9,rbtn10,rbtn11,rbtn12,rbtn13,rbtn14,rbtn15,rbtn16,rbtn17,rbtn18,rbtn19,rbtn20,rbtn21,rbtn22,rbtn23,rbtn24;
    Context thiscontext;
    IntentIntegrator integrator;

    public Add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);
        rbtn1=view.findViewById(R.id.radio_button_1);
        rbtn2=view.findViewById(R.id.radio_button_2);
        rbtn3=view.findViewById(R.id.radio_button_3);
        rbtn4=view.findViewById(R.id.radio_button_4);
        rbtn5=view.findViewById(R.id.radio_button_5);
        rbtn6=view.findViewById(R.id.radio_button_6);
        rbtn7=view.findViewById(R.id.radio_button_7);
        rbtn8=view.findViewById(R.id.radio_button_8);
        rbtn9=view.findViewById(R.id.radio_button_9);
        rbtn10=view.findViewById(R.id.radio_button_10);
        rbtn11=view.findViewById(R.id.radio_button_11);
        rbtn12=view.findViewById(R.id.radio_button_12);
        rbtn13=view.findViewById(R.id.radio_button_13);
        rbtn14=view.findViewById(R.id.radio_button_14);
        rbtn15=view.findViewById(R.id.radio_button_15);
        rbtn16=view.findViewById(R.id.radio_button_16);
        rbtn17=view.findViewById(R.id.radio_button_17);
        rbtn18=view.findViewById(R.id.radio_button_18);
        rbtn19=view.findViewById(R.id.radio_button_19);
        rbtn20=view.findViewById(R.id.radio_button_20);
        rbtn21=view.findViewById(R.id.radio_button_21);
        rbtn22=view.findViewById(R.id.radio_button_22);
        rbtn23=view.findViewById(R.id.radio_button_23);
        rbtn24=view.findViewById(R.id.radio_button_24);
        thiscontext = container.getContext();
        radioGroup=view.findViewById(R.id.radioGroup);

        autoCompleteTextView =view.findViewById(R.id.filled_exposed_dropdown);
        typeTextInputLayout = view.findViewById(R.id.type_lay);
        BarcodeTextInputLayout=(TextInputLayout) view.findViewById(R.id.lay_Barcode);
        BarcodetextInputEditText=(TextInputEditText) view.findViewById(R.id.ed_barcode) ;
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                BarcodetextInputEditText.setText( result.getContents().toString());

            }
        }
    }
    private void checkboxdialog () {
        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.checkboxdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        ((Button) dialog.findViewById(R.id.Cancelbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        ((Button) dialog.findViewById(R.id.savebtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // radioButtonID=radioGroup.getCheckedRadioButtonId();

                dialog.dismiss();

            }
        });

        dialog.show();

    }

}