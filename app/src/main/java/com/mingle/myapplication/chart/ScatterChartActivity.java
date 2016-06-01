
package com.mingle.myapplication.chart;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mingle.myapplication.R;
import com.mingle.myapplication.shake.ShakeActivity;

import java.util.ArrayList;
import java.util.List;

public class ScatterChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {
    private static final String ITEM_VALUE = "value";

    private ScatterChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    private Typeface tf;

    private int valueNum = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scatterchart);

        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);

        mChart = (ScatterChart) findViewById(R.id.chart1);
        mChart.setDescription("");

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);

        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        mSeekBarX.setProgress(30);
        mSeekBarY.setProgress(50);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        
        mChart.getAxisRight().setEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);
        xl.setDrawGridLines(false);

        mSeekBarX.setVisibility(View.GONE);
        mSeekBarY.setVisibility(View.GONE);
        tvX.setVisibility(View.GONE);
        tvY.setVisibility(View.GONE);

        mChart.animateXY(3000, 3000);
        List<IScatterDataSet> sets = mChart.getData()
                .getDataSets();
        for (IScatterDataSet iSet : sets) {
            ScatterDataSet set = (ScatterDataSet) iSet;
            set.setDrawValues(false);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.scatter, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.actionToggleValues: {
//                List<IScatterDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (IScatterDataSet iSet : sets) {
//
//                    ScatterDataSet set = (ScatterDataSet) iSet;
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHighlight: {
//                if(mChart.getData() != null) {
//                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
//                    mChart.invalidate();
//                }
//                break;
//            }
//            case R.id.actionTogglePinch: {
//                if (mChart.isPinchZoomEnabled())
//                    mChart.setPinchZoom(false);
//                else
//                    mChart.setPinchZoom(true);
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleAutoScaleMinMax: {
//                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
//                mChart.notifyDataSetChanged();
//                break;
//            }
//            case R.id.actionSave: {
//                // mChart.saveToGallery("title"+System.currentTimeMillis());
//                mChart.saveToPath("title" + System.currentTimeMillis(), "");
//                break;
//            }
//            case R.id.animateX: {
//                mChart.animateX(3000);
//                break;
//            }
//            case R.id.animateY: {
//                mChart.animateY(3000);
//                break;
//            }
//            case R.id.animateXY: {
//
//                mChart.animateXY(3000, 3000);
//                break;
//            }
//        }
//        return true;
//    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Intent intent = getIntent();
        valueNum = intent.getIntExtra(ITEM_VALUE, 0);

        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
            xVals.add((i) + "");
        }

        ArrayList<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();
        if(valueNum == 0 || valueNum == 1) {
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
            for (int i = 0; i < mSeekBarX.getProgress(); i++) {
                int val = (int) (Math.random() * mSeekBarY.getProgress()) + 3;
                yVals1.add(new Entry(val, i));
            }
            // create a dataset and give it a type
            ScatterDataSet set1 = new ScatterDataSet(yVals1, "모자");
            set1.setScatterShapeSize(8f);
            set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);
            dataSets.add(set1); // add the datasets
        }
        if(valueNum == 0 || valueNum == 2) {
            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            for (int i = 0; i < mSeekBarX.getProgress(); i++) {
                int val = (int) (Math.random() * mSeekBarY.getProgress()) + 3;
                yVals2.add(new Entry(val, i));
            }
            ScatterDataSet set2 = new ScatterDataSet(yVals2, "옷");
            set2.setColor(ColorTemplate.COLORFUL_COLORS[1]);
            set2.setScatterShapeSize(8f);
            dataSets.add(set2);
        }
        if(valueNum == 0 || valueNum == 3) {
            ArrayList<Entry> yVals3 = new ArrayList<Entry>();
            for (int i = 0; i < mSeekBarX.getProgress(); i++) {
                int val = (int) (Math.random() * mSeekBarY.getProgress()) + 3;
                yVals3.add(new Entry(val, i));
            }
            ScatterDataSet set3 = new ScatterDataSet(yVals3, "바지");
            set3.setColor(ColorTemplate.COLORFUL_COLORS[2]);
            set3.setScatterShapeSize(8f);
            dataSets.add(set3);
        }
        if(valueNum == 0 || valueNum == 4) {
            ArrayList<Entry> yVals4 = new ArrayList<Entry>();
            for (int i = 0; i < mSeekBarX.getProgress(); i++) {
                int val = (int) (Math.random() * mSeekBarY.getProgress()) + 3;
                yVals4.add(new Entry(val, i));
            }
            ScatterDataSet set4 = new ScatterDataSet(yVals4, "신발");
            set4.setColor(ColorTemplate.COLORFUL_COLORS[3]);
            set4.setScatterShapeSize(8f);
            dataSets.add(set4);
        }
        // create a data object with the datasets
        ScatterData data = new ScatterData(xVals, dataSets);
        data.setValueTypeface(tf);

        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        if(valueNum == 0 || valueNum == 1 || valueNum == 2 || valueNum == 3){
            finish();
        }
    }
}
