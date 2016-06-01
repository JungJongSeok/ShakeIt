package com.mingle.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.google.android.gcm.GCMBaseIntentService;
import com.mingle.myapplication.activity.MainActivity;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = GCMIntentService.class.getSimpleName();
    public static final String APP_NAME = "com.mingle.myapplication";
    public static final int HAT_VALUE = 1;
    public static final int SHIRT_VALUE = 2;
    public static final int PANTS_VALUE = 3;
    public static final int SHOES_VALUE = 4;
    private static final String ITEM_VALUE = "value";
    private static final String MESSAGE = "msg";
    private static final int NOTIFICATION_ID = 1234;

    private BubblesManager bubblesManager = null;
    private boolean isShowBubbleView = false;

    public GCMIntentService() {
        super("233557157355");
        Log.e(TAG, "서비스 시작");
    }

    private static void generateNotification(Context context, String message) {

        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_launcher, message, when);
        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    @Override
    protected void onError(Context arg0, String arg1) {}

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.e(TAG, "메세지 수신");
        String msg = intent.getStringExtra(MESSAGE);
        String value = intent.getStringExtra(ITEM_VALUE);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.anim.animation, msg, System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "긴급 속보", msg, pendingIntent);
        nm.notify(NOTIFICATION_ID, notification);

        if(bubblesManager == null){
            initializeBubblesManager();
        }
        addNewBubble(Integer.parseInt(value));

    }

    @Override
    protected void onRegistered(Context context, final String reg_id) {
        Log.e(TAG, "키를 등록합니다.(GCM INTENTSERVICE)" + reg_id);
        MainActivity.REGISTRATION_ID = reg_id;
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.e(TAG, "키를 제거합니다.(GCM INTENTSERVICE) 제거되었습니다.");
    }

    private void addNewBubble(final int valueNum) {
        Log.e(TAG,"view 추가");
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);
        ImageView mImageView = (ImageView)bubbleView.findViewById(R.id.avatar);
        if(valueNum == HAT_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.hat_icon));
        }
        else if(valueNum == SHIRT_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.tee_icon));
        }
        else if(valueNum == PANTS_VALUE) {
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.pants_icon));
        }
        else if(valueNum == SHOES_VALUE) {
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
        bubblesManager.addBubble(bubbleView, (int) (Math.random() * 400), (int) (Math.random() * 100));
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
            intent.putExtra("value", valueNum);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    @Override
    public void onDestroy() {
        bubblesManager.recycle();
        super.onDestroy();
    }
}
