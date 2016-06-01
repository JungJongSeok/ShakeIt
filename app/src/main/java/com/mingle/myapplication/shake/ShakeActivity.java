package com.mingle.myapplication.shake;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.mingle.myapplication.activity.MainActivity;
import com.mingle.myapplication.R;
import com.mingle.myapplication.textSurface.CookieThumperSample;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import su.levenetc.android.textsurface.TextSurface;

public class ShakeActivity extends Activity implements SensorEventListener {
    private static final String TAG = ShakeActivity.class.getSimpleName();
    public static final String APP_NAME = "com.mingle.myapplication";

    public static final int SHOW_SURFACE_TEXT_TIMER = 10000;
    private static final String ITEM_VALUE = "value";

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

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    private BubblesManager bubblesManager;

    private TextSurface textSurface;

    private boolean isShowBubbleView = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textSurface = (TextSurface) findViewById(R.id.text_surface);
        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
                mHandler.sendEmptyMessageDelayed(0, SHOW_SURFACE_TEXT_TIMER);
            }
        }, 100);


        initializeBubblesManager();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    show();
                    if(mHandler.hasMessages(0) == false) {
                        mHandler.sendEmptyMessageDelayed(0, SHOW_SURFACE_TEXT_TIMER);
                    }
                    break;
            }
        }
    };
    private void show() {
        textSurface.reset();
        CookieThumperSample.play(textSurface, getAssets());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // 이벤트발생!!
                    Log.e(TAG, "이벤트 발생");

                    int valueNum = 3;
                    if(isShowBubbleView == false) {
                        // 플로팅 뷰 생성성
                        addNewBubble(valueNum);
                        // 홈화면으로 이동
                        moveHomeActivity();

                        isShowBubbleView = true;
                    }
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }

    private void addNewBubble(final int valueNum) {
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);
        ImageView mImageView = (ImageView)bubbleView.findViewById(R.id.avatar);
        if(valueNum == MainActivity.HAT_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.hat_icon));
        }
        else if(valueNum == MainActivity.SHIRT_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.tee_icon));
        }
        else if(valueNum == MainActivity.PANTS_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.pants_icon));
        }
        else if(valueNum == MainActivity.SHOES_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.shoes_icon));
        }
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
            }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
//                Toast.makeText(getApplicationContext(), "Clicked !",
//                        Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isShowBubbleView = false;
                        startSelectApp(APP_NAME, valueNum);
                        bubblesManager.removeBubble(bubbleView);
                    }
                }, 0);

            }
        });
        //벽에 붙게 하기
        bubbleView.setShouldStickToWall(false);
        bubblesManager.addBubble(bubbleView, (int)(Math.random()*400), (int)(Math.random()*100));
    }


    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash_layout)
//                .setInitializationCallback(new OnInitializedCallback() {
//                    @Override
//                    public void onInitialized() {
//                        addNewBubble();
//                    }
//                })
                .build();
        bubblesManager.initialize();
    }

    // 선택한 App 시작
    public void startSelectApp(String packageName, int valueNum){
        ComponentName componentName = new ComponentName(packageName, packageName + ".chart.ScatterChartActivity");
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent!=null){
            intent.setComponent(componentName);
            intent.putExtra(ITEM_VALUE, valueNum);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //애플리케이션에서 홈화면으로 이동
    private void moveHomeActivity(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();

        if (mHandler != null) {
            if(mHandler.hasMessages(0)) {
                mHandler.removeMessages(0);
            }
        }
    }
}
