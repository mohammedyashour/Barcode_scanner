package com.example.barcodescanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.explosionfield.ExplosionField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;


public class UserProfile extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView username, email;
    private CircularImageView profileimage;
    private ImageView top_bedge, emptybadge, crown, ontime, verified, trophy, reward, goal;
    private LinearLayout linearLayout;
    Drawable favoriteBorder;
    private String  stringtrophy, stringontime, stringverified, stringgoal, stringcrown, stringrewardn;
    boolean badge = false;
    String msg = "MainActivity";
    private Context context;
    private String currentbadge="empty";
    private ExplosionField explosionField;
      SharedPreferences sharedPreferences;
    private   HashMap<String, String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context = getApplicationContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
       data = new HashMap<>();



        initViews();
        addbadge();

        String sp_currentbadge=getDefaults("currentbadge",context);
switch (sp_currentbadge){
    case "crown":
        top_bedge.setImageResource(R.drawable.badge_crown);
        break;
    case "goal":
        top_bedge.setImageResource(R.drawable.badge_goal);
        break;
    case "reward":
        top_bedge.setImageResource(R.drawable.badge_reward);
        break;
    case "trophy":
        top_bedge.setImageResource(R.drawable.badge_trophy);
        break;
    case "verified":
        top_bedge.setImageResource(R.drawable.badge_verified);
        break;
    case "ontime":
        top_bedge.setImageResource(R.drawable.badge_ontime);
        break;
    default:
        top_bedge.setImageResource(R.drawable.ic_add_circle);
}
        firebaseFirestore.collection("users")
                .whereEqualTo("Email", firebaseAuth.getCurrentUser().getEmail().toString())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                username.setText(q.getData().get("Username").toString());
                                profileimage.setImageURI(Uri.parse(q.getData().get("imageuri").toString()));
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "failed to get data from firebase ", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    private void initViews() {
        username = findViewById(R.id.profileusername);
        profileimage = findViewById(R.id.image);


        top_bedge = findViewById(R.id.top_bedge);
        sharedPreferences = getSharedPreferences("MySP", MODE_PRIVATE); //open

    }

    private void addbadge() {
        if (firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("medo.ash.2019@gmail.com")) {
            favoriteBorder = getResources().getDrawable(R.drawable.badge_crown);
            badge = true;
            top_bedge.setImageResource(R.drawable.ic_add_circle);
        } else {
            favoriteBorder = getResources().getDrawable(R.drawable.ic_add_circle);

            top_bedge.setImageResource(R.drawable.ic_add_circle);
        }
        top_bedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (top_bedge.getDrawable().getConstantState().equals(favoriteBorder.getConstantState())) {
                    Snackbar.make(findViewById(android.R.id.content), "you are admin you cannot change admin badge", Snackbar.LENGTH_LONG).show();
                    choose_badge();


                } else {
                    choose_badge();
                }

            }
        });
    }

    private void choose_badge() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.badgedialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        emptybadge = dialog.findViewById(R.id.badge_bg);
        linearLayout = dialog.findViewById(R.id.linearlayout);
        trophy = dialog.findViewById(R.id.trophy);
        crown = dialog.findViewById(R.id.crown);
        goal = dialog.findViewById(R.id.goal);
        reward = dialog.findViewById(R.id.reward);
        ontime = dialog.findViewById(R.id.ontime);
        verified = dialog.findViewById(R.id.verified);
        stringtrophy = (String) trophy.getContentDescription();
        stringontime = (String) ontime.getContentDescription();
        stringverified = (String) verified.getContentDescription();
        stringgoal = (String) goal.getContentDescription();
        stringcrown = (String) crown.getContentDescription();
        stringrewardn = (String) reward.getContentDescription();

        draganddropcheck(linearLayout, trophy, ontime, verified,goal, crown, reward);
explosionField=new ExplosionField(getApplicationContext());
explosionField = ExplosionField.attach2Window(this);
        ((Button) dialog.findViewById(R.id.savebtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentbadge!="empty"){
                    dialog.dismiss();

                    switch (currentbadge){
                        case "crown":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_crown);
                                    explosionField.reset(top_bedge,ExplosionField.FROM_BOTTOM);
                                    setDefaults("currentbadge", currentbadge,context);


                                }
                            },1000);


                            break;
                        case "goal":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_goal);
                                    explosionField.reset(top_bedge,ExplosionField.FROM_BOTTOM);
                                    setDefaults("currentbadge", currentbadge,context);

                                }
                            },1000);



                            break;
                        case "reward":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_reward);
                                    explosionField.reset(top_bedge,ExplosionField.SEQUENTIAL);
                                    setDefaults("currentbadge", currentbadge,context);

                                }
                            },1000);




                            break;
                        case "trophy":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_trophy);
                                    explosionField.reset(top_bedge,ExplosionField.BLINK);
                                    setDefaults("currentbadge", currentbadge,context);

                                }
                            },1000);



                            break;
                        case "verified":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_verified);
                                    explosionField.reset(top_bedge,ExplosionField.FROM_BOTTOM);
                                    setDefaults("currentbadge", currentbadge,context);

                                }
                            },1000);



                            break;
                        case "ontime":
                            explosionField.explode(top_bedge);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    top_bedge.setImageResource(R.drawable.badge_ontime);
                                    explosionField.reset(top_bedge,ExplosionField.FALL_DOWN);
                                    setDefaults("currentbadge", currentbadge,context);

                                }
                            },1000);



                            break;
                    }
                    data.put("Username",username.getText().toString());
                    data.put("Top_Badge",currentbadge);

                    Snackbar.make(findViewById(android.R.id.content), "The badge has been updated.", Snackbar.LENGTH_LONG).show();

                }

            }
        });

        dialog.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void draganddropcheck(LinearLayout layout, ImageView img, ImageView ontime, ImageView verified, ImageView goal, ImageView crown, ImageView reward) {
       img.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               layout.setOnDragListener(new View.OnDragListener() {
                   @Override
                   public boolean onDrag(View v, DragEvent event) {
                       switch (event.getAction()) {
                           case DragEvent.ACTION_DRAG_STARTED:
                               Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                               break;

                           case DragEvent.ACTION_DRAG_ENTERED:
                               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                               break;

                           case DragEvent.ACTION_DRAG_EXITED:
                               Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                               break;

                           case DragEvent.ACTION_DRAG_LOCATION:
                               Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                               break;

                           case DragEvent.ACTION_DRAG_ENDED:
                               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                               break;

                           case DragEvent.ACTION_DROP:
                               Log.d(msg, "ACTION_DROP event");
                               View tvState = (View) event.getLocalState();
                               Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                               Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                               ViewGroup tvParent = (ViewGroup) tvState.getParent();
                               LinearLayout container = (LinearLayout) v;
                               if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                   currentbadgecheck();

                                   emptybadge.setImageResource(R.drawable.badge_trophy);
                                   currentbadge="trophy";

                                   trophy.setVisibility(View.GONE);

                               }

                               break;
                           default:
                               break;
                       }
                       return true;
                   }
               });
               emptybadgeonclick(trophy);

               if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   ClipData data = ClipData.newPlainText("", "");
                   View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(img);
                   v.startDrag(data, shadowBuilder, v, 0);
                   v.setVisibility(View.VISIBLE);
                   return true;
               } else {


               return false;
           }

       }});
        ontime.setOnTouchListener((v, event) -> {
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            break;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            View tvState = (View) event.getLocalState();
                            Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                            Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                            ViewGroup tvParent = (ViewGroup) tvState.getParent();
                            LinearLayout container = (LinearLayout) v;
                            if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                currentbadgecheck();

                                emptybadge.setImageResource(R.drawable.badge_ontime);
                                currentbadge="ontime";

                                ontime.setVisibility(View.GONE);

                            }

                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            emptybadgeonclick(ontime);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(ontime);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.VISIBLE);
                return true;
            } else {


                return false;
            }

        });
        verified.setOnTouchListener((v, event) -> {
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            break;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            View tvState = (View) event.getLocalState();
                            Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                            Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                            ViewGroup tvParent = (ViewGroup) tvState.getParent();
                            LinearLayout container = (LinearLayout) v;
                            if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                currentbadgecheck();
                            emptybadge.setImageResource(R.drawable.badge_verified);
                                currentbadge="verified";

                                verified.setVisibility(View.GONE);

                            }


                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            emptybadgeonclick(verified);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(verified);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.VISIBLE);
                return true;
            } else {


                return false;
            }

        });
        goal.setOnTouchListener((v, event) -> {
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            break;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            View tvState = (View) event.getLocalState();
                            Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                            Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                            ViewGroup tvParent = (ViewGroup) tvState.getParent();
                            LinearLayout container = (LinearLayout) v;
                            if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                currentbadgecheck();

                                emptybadge.setImageResource(R.drawable.badge_goal);
                                currentbadge="goal";

                                goal.setVisibility(View.GONE);



                            }


                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            emptybadgeonclick(goal);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(goal);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.VISIBLE);
                return true;
            } else {


                return false;
            }
        });
        crown.setOnTouchListener((v, event) -> {
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            break;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            View tvState = (View) event.getLocalState();
                            Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                            Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                            ViewGroup tvParent = (ViewGroup) tvState.getParent();
                            LinearLayout container = (LinearLayout) v;
                            if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                currentbadgecheck();

                                emptybadge.setImageResource(R.drawable.badge_crown);
                                currentbadge="crown";

                                crown.setVisibility(View.GONE);



                            }


                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            emptybadgeonclick(crown);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(crown);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.VISIBLE);
                return true;
            } else {


                return false;
            }

        });
        reward.setOnTouchListener((v, event) -> {
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            break;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            View tvState = (View) event.getLocalState();
                            Log.d(msg, "onDrag:viewX" + event.getX() + "viewY" + event.getY());
                            Log.d(msg, "onDrag: Owner->" + tvState.getParent());
                            ViewGroup tvParent = (ViewGroup) tvState.getParent();
                            LinearLayout container = (LinearLayout) v;
                            if (event.getX() <= 480 && event.getX() >= 260 && event.getY() <= 900 && event.getY() >= 300) {
                                currentbadgecheck();

                                emptybadge.setImageResource(R.drawable.badge_reward);
                                currentbadge="reward";
                                reward.setVisibility(View.GONE);




                            }

                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            emptybadgeonclick(reward);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(reward);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.VISIBLE);
                return true;
            } else {


                return false;
            }

        });



    }
private void emptybadgeonclick(ImageView image){
        emptybadge.setOnClickListener(v -> {
            if (image.getVisibility()==View.VISIBLE){

            }else{
                currentbadge="empty";
                emptybadge.setImageResource(R.drawable.ic_add_circle);
                image.setVisibility(View.VISIBLE);
            }
        });
}
   private void currentbadgecheck(){
        switch (currentbadge){
            case "crown":
                crown.setVisibility(View.VISIBLE);
                break;
            case "goal":
                goal.setVisibility(View.VISIBLE);
                break;
            case "reward":
                reward.setVisibility(View.VISIBLE);
                break;
            case "trophy":
                trophy.setVisibility(View.VISIBLE);
                break;
            case "verified":
                verified.setVisibility(View.VISIBLE);
                break;
            case "ontime":
                ontime.setVisibility(View.VISIBLE);
                break;
        }
   }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}