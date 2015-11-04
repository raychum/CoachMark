package com.theoon.ray.coachmark;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ray on 4/11/15.
 */
public class CoachMarkActivity extends AppCompatActivity {

    private static final String TAG = CoachMarkActivity.class.getSimpleName();
    private View container;
    private ArrayList<String> coachMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_mark);
        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        coachMarks = getIntent().getStringArrayListExtra("coachMarks");
        for (String coachMark : coachMarks) {
            String[] values = coachMark.split(";");
            final float x = Float.parseFloat(values[0]);
            final float y = Float.parseFloat(values[1]);
            final int w = Integer.parseInt(values[2]);
            final int h = Integer.parseInt(values[3]);
            final String text = values[4];
            final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_coach_mark, (ViewGroup) container, false);
            final View indicator = relativeLayout.findViewById(R.id.indicator);
            indicator.setX(x + (w / 2) - indicator.getLayoutParams().width / 2);
            final TextView message = (TextView) relativeLayout.findViewById(R.id.message);
            message.setMaxWidth(getScreenWidth(getApplicationContext()));
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
                            } else if (x + w / 2 - message.getWidth() / 2 < 0) {
                                ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                            } else if (x + w / 2 - message.getWidth() / 2 > 0 && x + w / 2 + message.getWidth() / 2 > 0) {
                                ((RelativeLayout.LayoutParams) message.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                            }
                        }
                    }
                }
            });
            ((ViewGroup) container).addView(relativeLayout);
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