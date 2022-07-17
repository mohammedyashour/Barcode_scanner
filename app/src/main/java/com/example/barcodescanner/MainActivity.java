package com.example.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.barcodescanner.Admin.AdminDrawerFragment;
import com.example.barcodescanner.Fragments.Add;
import com.example.barcodescanner.Fragments.Dashboard;
import com.example.barcodescanner.Fragments.Inventory;
import com.example.barcodescanner.Fragments.Menu;
import com.example.barcodescanner.Fragments.Scanner;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.tapadoo.alerter.Alerter;

public class MainActivity extends AppCompatActivity {
    private FlowingDrawer mDrawer;

    private Button scan;
    private TextView textView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FragmentTransaction fragmentTransaction;
    BubbleNavigationLinearView bubbleNavigationLinearView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Alerter();

        initAdminViews();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser CurrentUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("medo.ash.2019@gmail.com")) {

            initAdminViews();
        }
        if (CurrentUser !=null){

            // Snackbar.make(findViewById(android.R.id.content),  firebaseAuth.getCurrentUser().getEmail().toString(), Snackbar.LENGTH_LONG).show();

        } else{
            Intent intent =new Intent(MainActivity.this,SplashScreen.class);
            startActivity(intent);
            finish();

        }


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

    private void Alerter() {
        Alerter.create(this)
                .setIcon(R.drawable.alerter_ic_face)
                .setBackgroundColorRes(R.color.Coloralert)
                .setTitle(R.string.textalert)
                .setText(R.string.welcome)
                .enableProgress(true)
                .setProgressColorRes(R.color.Coloralert2)
                .enableSwipeToDismiss()

                .show();

    }
    private void initAdminViews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.id_container_menu, new AdminDrawerFragment()).addToBackStack(null).commit();
        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.bringToFront();
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }
}