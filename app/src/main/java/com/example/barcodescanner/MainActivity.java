package com.example.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.barcodescanner.Fragments.Add;
import com.example.barcodescanner.Fragments.Dashboard;
import com.example.barcodescanner.Fragments.Inventory;
import com.example.barcodescanner.Fragments.Menu;
import com.example.barcodescanner.Fragments.Scanner;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
 private Button scan;
private TextView textView;
FragmentTransaction fragmentTransaction;
BubbleNavigationLinearView bubbleNavigationLinearView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubbleNavigationLinearView=findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setBadgeValue(0,"30");
        bubbleNavigationLinearView.setBadgeValue(1,"25");
        bubbleNavigationLinearView.setBadgeValue(2,"20");
        bubbleNavigationLinearView.setBadgeValue(3,"10");
        bubbleNavigationLinearView.setBadgeValue(4,"5");
        //***
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,new Dashboard());
       fragmentTransaction.commit();
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
            switch (position){
                case 0:
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new Dashboard());
                    fragmentTransaction.commit();
                    break;
                case 1:
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new Scanner());
                    fragmentTransaction.commit();
                    break;
                case 2:
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new Inventory());
                    fragmentTransaction.commit();
                    break;
                case 3:
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new Add());
                    fragmentTransaction.commit();
                    break;
                case 4:
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new Menu());
                    fragmentTransaction.commit();
                    break;
            }
            }
        });











       /* AnimationDrawable animationDrawable =(AnimationDrawable)  constraintLayout.getBackground();
animationDrawable.setEnterFadeDuration(2500);
animationDrawable.setExitFadeDuration(5000);
animationDrawable.start();*/
/*
         scan=findViewById(R.id.scanbtn);
        textView=findViewById(R.id.textview);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);*/
    }
/*
public void ScaneButton (View view) {
    IntentIntegrator  intentIntegrator =new IntentIntegrator(this);
          intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
       if (intentResult != null){
                 if (intentResult.getContents() == null){
                     textView.setText("cancelled");
                 }else{
                     textView.setText(intentResult.getContents());

                 }
       }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public  void  scaner(){

        IntentIntegrator  intentIntegrator =new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }*/
}