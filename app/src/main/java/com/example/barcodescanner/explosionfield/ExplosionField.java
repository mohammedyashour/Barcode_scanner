/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.barcodescanner.explosionfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.barcodescanner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ExplosionField extends View {
    public interface Listener {
        void onExplosionFinish();
    }
    private List<ExplosionAnimator> mExplosions = new ArrayList<>();
    private int[] mExpandInset = new int[2];
   public static String BLINK= "BLINK";
    public  static String BOUNCE= "BOUNCE";
    public static String FADE_IN= "FADE_IN";
    public static String FADE_OUT= "FADE_OUT";
    public static String FALL_DOWN= "FALL_DOWN";
    public static String FROM_BOTTOM= "FROM_BOTTOM";
    public static String FROM_RIGHT = "FROM_RIGHT";
    public static String ROTATE = "ROTATE";

    public static String SEQUENTIAL = "SEQUENTIAL";
    public static String SLIDE_UP = "SLIDE_UP";
    public  static String SLIDE_DOWN = "SLIDE_DOWN";
    public  static String ZOOM_IN = "ZOOM_IN";
    public  static String ZOOM_OUT = "ZOOM_OUT";
    private Listener listener;

    public ExplosionField listener(Listener listener) {
        this.listener = listener;
        return this;
    }
    public ExplosionField(Context context) {
        super(context);
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Arrays.fill(mExpandInset, Utils.dp2Px(32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator explosion : mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void expandExplosionBound(int dx, int dy) {
        mExpandInset[0] = dx;
        mExpandInset[1] = dy;
    }

    public void explode(Bitmap bitmap, Rect bound, long startDelay, long duration) {
        final ExplosionAnimator explosion = new ExplosionAnimator(this, bitmap, bound);
        explosion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExplosions.remove(animation);
            }
        });
        explosion.setStartDelay(startDelay);
        explosion.setDuration(duration);
        mExplosions.add(explosion);
        explosion.start();
    }

    public void explode(final View view) {

        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);
        int startDelay = 100;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);

            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onExplosionFinish();
                }
            }
        });        animator.start();
        view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0f).scaleY(0f).alpha(0f).start();
        explode(Utils.createBitmapFromView(view), r, startDelay, ExplosionAnimator.DEFAULT_DURATION);
    }

    public void clear() {
        mExplosions.clear();
        invalidate();
    }

    public static ExplosionField attach2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        ExplosionField explosionField = new ExplosionField(activity);
        rootView.addView(explosionField, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return explosionField;
    }
    public void reset(View root,String Animation) {

        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i),Animation);
            }
        } else {
            switch (Animation){
                case "BLINK":
                    android.view.animation.Animation BLINK = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

                    root.startAnimation(BLINK);
                    break;
                case "ROTATE":
                    android.view.animation.Animation ROTATE = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                    root.startAnimation(ROTATE);


                    break;
                case "BOUNCE":
                    Animation BOUNCE = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                    root.startAnimation(BOUNCE);
                    break;
                case "FADE_IN":
                    Animation FADE_IN = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    root.startAnimation(FADE_IN);

                    break;
                case "FADE_OUT":
                    Animation FADE_OUT = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                    root.startAnimation(FADE_OUT);

                    break;
                case "FALL_DOWN":
                    Animation FALL_DOWN = AnimationUtils.loadAnimation(getContext(), R.anim.fall_down);
                    root.startAnimation(FALL_DOWN);

                    break;
                case "FROM_BOTTOM":
                    Animation FROM_BOTTOM = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom);
                    root.startAnimation(FROM_BOTTOM);

                    break;
                case "FROM_RIGHT":
                    Animation FROM_RIGHT = AnimationUtils.loadAnimation(getContext(), R.anim.from_right);
                    root.startAnimation(FROM_RIGHT);

                    break;
                case "MOVE":
                    Animation MOVE = AnimationUtils.loadAnimation(getContext(), R.anim.move);
                    root.startAnimation(MOVE);

                    break;    case "SCALE":
                    Animation SCALE = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
                    root.startAnimation(SCALE);

                    break;
                case "SEQUENTIAL":
                    Animation SEQUENTIAL = AnimationUtils.loadAnimation(getContext(), R.anim.sequential);
                    root.startAnimation(SEQUENTIAL);

                    break; case "SLIDE_UP":
                    Animation SLIDE_UP = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                    root.startAnimation(SLIDE_UP);

                    break;
                case "SLIDE_DOWN":
                    Animation SLIDE_DOWN = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                    root.startAnimation(SLIDE_DOWN);

                    break;
                case "ZOOM_IN":
                    Animation ZOOM_IN = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
                    root.startAnimation(ZOOM_IN);

                    break;
                case "ZOOM_OUT":
                    Animation ZOOM_OUT = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
                    root.startAnimation(ZOOM_OUT);

                    break;


            }

        }
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);

        }
    }


