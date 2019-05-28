package com.example.wangzipeng.viewentryexiteffect;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Button btnDown;
    private PopupWindow popupWindow;
    private Handler mHandler = new Handler();
    private ArrayList<View> viewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDown = findViewById(R.id.btn_down);
        btnDown.setText("PopupWindow");
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });
    }

    private void showWindow() {
        View layout = getLayoutInflater().inflate(R.layout.window_popup, null);
        final RelativeLayout relativeLayout = layout.findViewById(R.id.relative_popup);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加退场动画
                closeAnimation(relativeLayout);
            }
        });
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加进场动画
        showAnimation(relativeLayout);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        viewArrayList = new ArrayList<>();
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 800, 0);
                    fadeAnim.setDuration(800);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(800);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }
    }

    private void closeAnimation(ViewGroup layout) {
        viewArrayList.clear();
        for (int i = 0; i < layout.getChildCount(); i++) {
            viewArrayList.add(layout.getChildAt(i));
        }
        Collections.reverse(viewArrayList);
        for (int i = 0; i < viewArrayList.size(); i++) {
            final View child = viewArrayList.get(i);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, -900);
                    fadeAnim.setDuration(800);
                    fadeAnim.start();
                    fadeAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                            //判断如果是最后一个退场的View,退出PopupWindow窗口
                            if (child.getId() == R.id.source_linear) {
                                popupWindow.dismiss();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
            }, i * 50);
        }
    }
}
