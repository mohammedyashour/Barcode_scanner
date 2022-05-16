package com.example.barcodescanner.Fragments;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bitvale.switcher.SwitcherX;
import com.example.barcodescanner.MainActivity;
import com.example.barcodescanner.R;
import com.example.barcodescanner.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Scanner extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
private Uri downUri;
    //here you can type instans varible
private TextView text,tvscannumber,scantv,product_name, product_description,product_Type,product_price;
public int scannumber;
private ImageView product_image;
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
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        thiscontext = container.getContext();
tvscannumber =(TextView) view.findViewById(R.id.scannumber);
        scantv =(TextView) view.findViewById(R.id.scanned);


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
                firebaseFirestore.collection("Products" )
                        .whereEqualTo("barcode",result.getContents().toString())
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for ( QueryDocumentSnapshot q:task.getResult()) {
                               // Toast.makeText(getContext(), "Scanned : " +q.getData().get("product_name").toString(),Toast.LENGTH_LONG);
                                scannumber++;
                                 tvscannumber.setText(Integer.toString(scannumber));

                                Product_Dialog(q.getData().get("product_name").toString(),q.getData().get("product_type").toString(),q.getData().get("Description").toString(),q.getData().get("price").toString()/*,q.getData().get("product_image_uri").toString()*/);
                            }
                        } else{

                        }
                    }
                });
firebaseFirestore.collection("Products").whereNotEqualTo("barcode",result.getContents().toString()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
       No_Result();
    }
});
           //     scannumber++;
               // tvscannumber.setText(Integer.toString(scannumber));
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
    public void Product_Dialog (String name, String type, String description, String price/*,String product_image_uri*/) {

        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.product_info_dialog);
        product_name=dialog.findViewById(R.id.product_name);
        product_Type=dialog.findViewById(R.id.product_Type);
        product_description=dialog.findViewById(R.id.product_description);
        product_price=dialog.findViewById(R.id.product_price);
        product_image=dialog.findViewById(R.id.product_image);
//Uri uristring=Uri.parse(product_image_uri);
//product_image.setImageURI(uristring);
        product_name.setText(name);
        product_Type.setText(type);
        product_description.setText(description);
        product_price.setText("Price: ₪"+price);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);


        ((Button) dialog.findViewById(R.id.btn_cont)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });
        dialog.show();


    }
    public void No_Result () {
        final Dialog dialog = new Dialog(thiscontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.successfuldialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        LottieAnimationView lottieAnimationView =dialog.findViewById(R.id.lottie
        );
        lottieAnimationView.setAnimation(R.raw.no_result_found);

        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }

}

