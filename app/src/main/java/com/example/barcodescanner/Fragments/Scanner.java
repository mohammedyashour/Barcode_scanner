package com.example.barcodescanner.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitvale.switcher.SwitcherX;
import com.example.barcodescanner.MainActivity;
import com.example.barcodescanner.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Scanner extends Fragment {
//here you can type instans varible
static TextView text,tvscannumber ;
public int scannumber;
    SwitcherX switcherorientation,switcherbeepsound;
    Context thiscontext;
    TextView orientation,beepsound;
    IntentIntegrator integrator;
    Boolean beepsoundischaked =true;
    public Scanner() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_scanner, container, false);
        thiscontext = container.getContext();

tvscannumber =(TextView) view.findViewById(R.id.scannumber);


        Button scan=(Button) view.findViewById(R.id.scanbtn);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrator = IntentIntegrator.forSupportFragment(Scanner.this);

                //integrator.setOrientationLocked(false);
                //integrator.setPrompt("Scan QR code");
                //integrator.setBeepEnabled(false);
              //  integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);


                integrator.initiateScan();
            }
        });

scan.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        showDialogverify ();
        return false;
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

                Toast.makeText(getContext(), "Scanned : " + result.getContents(), Toast.LENGTH_LONG).show();

                scannumber++;
                tvscannumber.setText(Integer.toString(scannumber));
            }
        }
    }
    private void showDialogverify () {

        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.scansettingsdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        orientation= (TextView)dialog.findViewById(R.id.Orientation);
        beepsound= (TextView)dialog.findViewById(R.id.beepsound);
        switcherbeepsound =(SwitcherX) dialog.findViewById(R.id.switcherbeepsound) ;
        switcherorientation =(SwitcherX) dialog.findViewById(R.id.switcherorientation) ;

        switcherbeepsound.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                integrator = IntentIntegrator.forSupportFragment(Scanner.this);
                swichercheck();
                return null;
            }
        });
        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
       /*
switcherbeepsound.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
    @Override
    public Unit invoke(Boolean aBoolean) {
        if (switcherbeepsound.isChecked()){
            beepsound.setTextColor(Color.parseColor("#48ea8b"));
            integrator.setBeepEnabled(true);
        }else{
            beepsound.setTextColor(Color.parseColor("#ff4651"));
            integrator.setBeepEnabled(false);
        }


            return null;
    }
})  ;*/

         }
         public  void  swichercheck(){

             if (switcherbeepsound.isChecked()){

                 beepsound.setTextColor(getResources().getColor(R.color.switcher_on_color));
                 beepsoundischaked =true;
             }else {
                 beepsound.setTextColor(getResources().getColor(R.color.switcher_off_color));
                 beepsoundischaked =false;
             }
         }
    }

