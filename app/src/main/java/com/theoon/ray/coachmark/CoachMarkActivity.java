package com.theoon.ray.coachmark;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ray on 4/11/15.
 */
public class CoachMarkActivity extends AppCompatActivity {

    private static final String TAG = CoachMarkActivity.class.getSimpleName();
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_mark);
        container = (RelativeLayout) findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createCoachMarks();
    }

    private void createCoachMarks() {
        final ArrayList<CoachMarkModel> coachMarks = getIntent().getParcelableArrayListExtra("coachMarks");
        if (coachMarks != null && coachMarks.size() > 0) {
            for (CoachMarkModel coachMark : coachMarks) {
                final float x = coachMark.x;
                final float y = coachMark.y;
                final int w = coachMark.width;
                final int h = coachMark.height;
                final String text = coachMark.message;
                final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_coach_mark, (ViewGroup) container, false);
                final ImageView indicator = (ImageView) relativeLayout.findViewById(R.id.indicator);
                final ClipDrawable clipDrawable = (ClipDrawable) indicator.getDrawable();
                clipDrawable.setLevel(0);
                final TextView message = (TextView) relativeLayout.findViewById(R.id.message);
                message.setAlpha(0);
                message.setMaxWidth((int) (getScreenWidth(getApplicationContext()) * 0.7) + message.getPaddingRight() + message.getPaddingLeft());
                message.setText(text);
                if (y > getScreenHeight(this) / 2) {
                    ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    ((RelativeLayout.LayoutParams) indicator.getLayoutParams()).addRule(RelativeLayout.BELOW, message.getId());
                } else {
                    ((RelativeLayout.LayoutParams) indicator.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.BELOW, indicator.getId());
                }
                relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (relativeLayout.getHeight() > 0 && relativeLayout.getWidth() > 0) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            if (y > getScreenHeight(getApplicationContext()) / 2) {
                                relativeLayout.setY(y - h - relativeLayout.getMeasuredHeight());
                                indicator.setRotation(180);
                            } else {
                                relativeLayout.setY(y - h / 2);
                            }
                            if (x + relativeLayout.getWidth() > getScreenWidth(getApplicationContext())) {
                                relativeLayout.setX(getScreenWidth(getApplicationContext()) - relativeLayout.getWidth());
                            }
                        }
                    }
                });
                message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (message.getHeight() > 0 && message.getWidth() > 0) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                message.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            if (message.getWidth() != getScreenWidth(getApplicationContext())) {
                                if (x + w / 2 + message.getWidth() / 2 > getScreenWidth(getApplicationContext())) {
                                    ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                    message.setGravity(GravityCompat.END);
                                } else if (x + w / 2 - message.getWidth() / 2 < 0) {
                                    ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                    message.setGravity(GravityCompat.START);
                                } else if (x + w / 2 - message.getWidth() / 2 > 0 && x + w / 2 + message.getWidth() / 2 > 0) {
                                    ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                                    message.setGravity(Gravity.CENTER);
                                }
                            }
                            final ObjectAnimator mAnimatorAlpha = ObjectAnimator.ofFloat(message, "alpha", 0.0f, 1.0f);
                            final ValueAnimator mAnimatorAlphaClip = ValueAnimator.ofInt(0, 10000);
                            mAnimatorAlphaClip.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    clipDrawable.setLevel((Integer) animation.getAnimatedValue());
                                }
                            });
                            final AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.playSequentially(mAnimatorAlphaClip, mAnimatorAlpha);
                            animatorSet.setStartDelay(300);
                            animatorSet.setDuration(200);
                            animatorSet.start();
                        }
                    }
                });
                indicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (indicator.getHeight() > 0 && indicator.getWidth() > 0) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                indicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                indicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            if (x + (w / 2) + indicator.getWidth() / 2 > getScreenWidth(getApplicationContext())) {
                                ((RelativeLayout.LayoutParams) indicator.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                            } else if (x + (w / 2) - indicator.getWidth() / 2 < 0) {
                                ((RelativeLayout.LayoutParams) indicator.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                            } else {
                                indicator.setX(x + (w / 2) - indicator.getWidth() / 2);
                            }
                        }
                    }
                });
                container.addView(relativeLayout);
            }
        } else {
            finish();
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }
}