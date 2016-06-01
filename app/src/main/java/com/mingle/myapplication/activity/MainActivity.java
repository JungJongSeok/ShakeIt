package com.mingle.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.mingle.myapplication.R;
import com.mingle.myapplication.chart.ScatterChartActivity;
import com.mingle.myapplication.mapviewer.NMapViewer;
import com.mingle.myapplication.research.view.NaverSearchActivity;
import com.mingle.myapplication.shake.ShakeActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.google.android.gcm.GCMRegistrar;

import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.phoenix.PullToRefreshView;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long RIPPLE_DURATION = 250;
    public static String REGISTRATION_ID = "233557157355";//registration Id
    private static final String APIKEY = "AIzaSyASOQ1rz4O02r6afc700lWGyCLt8fLnJWE"; //GCM web key
    public static final int HAT_VALUE = 1;
    public static final int SHIRT_VALUE = 2;
    public static final int PANTS_VALUE = 3;
    public static final int SHOES_VALUE = 4;

    public static final String ITEM_VALUE = "value";

    private GuillotineAnimation mGuillotineAnimation;
    private Toolbar toolbar;
    private ImageView contentHamburger;
    private RelativeLayout root;
    private PullToRefreshView mPullToRefreshView;

    private BlurLayout mHatBlurLayout;
    private BlurLayout mTeeBlurLayout;
    private BlurLayout mPantsBlurLayout;
    private BlurLayout mShoesBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashActivity.class));

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        root = (RelativeLayout) findViewById(R.id.root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentHamburger = (ImageView) findViewById(R.id.content_hamburger);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        initImageLoading();
        setupGuillotine();
        setupBlurView();

        registerGcm();
//        setupViewpager();
//        setupRecyclerView();
//        setupCustomView();
    }

    private void setupGuillotine(){
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

        mGuillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        
        guillotineMenu.findViewById(R.id.fake_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ScatterChartActivity.class);
//                startActivity(intent);
                mGuillotineAnimation.close();
            }
        });
        guillotineMenu.findViewById(R.id.feed_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShakeActivity.class);
                startActivity(intent);
                mGuillotineAnimation.close();
            }
        });

        root.addView(guillotineMenu);

    }

    private void setupBlurView() {
        mHatBlurLayout = (BlurLayout)findViewById(R.id.hat_blurLayout);
        mTeeBlurLayout = (BlurLayout)findViewById(R.id.tee_blurLayout);
        mPantsBlurLayout = (BlurLayout)findViewById(R.id.pants_blurLayout);
        mShoesBlurLayout = (BlurLayout)findViewById(R.id.shoes_blurLayout);
        setBlureLayout(mHatBlurLayout, 1);
        setBlureLayout(mTeeBlurLayout, 2);
        setBlureLayout(mPantsBlurLayout, 3);
        setBlureLayout(mShoesBlurLayout, 4);
    }
    private void setBlureLayout(BlurLayout mBlurLayout, final int value){
        View hover = LayoutInflater.from(MainActivity.this).inflate(R.layout.hover_zoom, null);
        hover.findViewById(R.id.buyImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
                Toast.makeText(MainActivity.this, "상품과 비슷한 제품 사러 가기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NaverSearchActivity.class);
                intent.putExtra(ITEM_VALUE,value);
                startActivity(intent);
            }
        });
        hover.findViewById(R.id.mapImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
                Toast.makeText(MainActivity.this, "제품을 살수 있는 곳", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NMapViewer.class);
                intent.putExtra(ITEM_VALUE,value);
                startActivity(intent);
            }
        });
        hover.findViewById(R.id.eyeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
                Toast.makeText(MainActivity.this, "요일별 상품을 들어본 사람수", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ScatterChartActivity.class);
                intent.putExtra(ITEM_VALUE,value);
                startActivity(intent);
            }
        });
        mBlurLayout.setHoverView(hover);
        mBlurLayout.addChildAppearAnimator(hover, R.id.buyImage, Techniques.Landing);
        mBlurLayout.addChildDisappearAnimator(hover, R.id.buyImage, Techniques.TakingOff);
        mBlurLayout.addChildAppearAnimator(hover, R.id.mapImage, Techniques.Landing);
        mBlurLayout.addChildDisappearAnimator(hover, R.id.mapImage, Techniques.TakingOff);
        mBlurLayout.addChildAppearAnimator(hover, R.id.eyeImage, Techniques.Landing);
        mBlurLayout.addChildDisappearAnimator(hover, R.id.eyeImage, Techniques.TakingOff);
        mBlurLayout.enableZoomBackground(true);
        mBlurLayout.setBlurDuration(1200);

    }

    public void registerGcm() {
        String gcmRegId = GCMRegistrar.getRegistrationId(this);

        if(TextUtils.isEmpty(gcmRegId)){
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            GCMRegistrar.register(this, "233557157355");
        }
        else{
            Log.e(TAG,gcmRegId);
            REGISTRATION_ID = gcmRegId;
        }

    }

    public void initImageLoading(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
