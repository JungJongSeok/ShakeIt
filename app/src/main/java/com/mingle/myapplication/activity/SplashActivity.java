package com.mingle.myapplication.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mingle.myapplication.R;
import com.mingle.myapplication.shake.ShakeActivity;
import com.mingle.myapplication.textSurface.CookieThumperSample;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import su.levenetc.android.textsurface.TextSurface;

public class SplashActivity extends Activity {
    /** Called when the activity is first created. */
    public static final String APP_NAME = "com.mingle.myapplication";
    public static final int HAT_VALUE = 1;
    public static final int SHIRT_VALUE = 2;
    public static final int PANTS_VALUE = 3;

    public static final int SHOW_SURFACE_TEXT_TIMER = 10000;

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;


    private TextSurface textSurface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                finish();
            }
        };
        textSurface = (TextSurface) findViewById(R.id.text_surface);
        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
                handler.sendEmptyMessageDelayed(0, 9500);
            }
        }, 100);
        textSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void show() {
        textSurface.reset();
        CookieThumperSample.play(textSurface, getAssets());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
 

