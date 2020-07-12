package com.pixelro.nenoons.menu.exercise.history;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pixelro.nenoons.BaseActivity;
import com.pixelro.nenoons.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + ""; // e.g. append a dollar-sign
    }
}

public class ExHistoryActivity extends BaseActivity implements View.OnClickListener, OnChartValueSelectedListener {

    private final static String TAG = ExHistoryActivity_calendar.class.getSimpleName();

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history_graph);

        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        chart = (BarChart)findViewById(R.id.barchart_ex_history);


        Description desc ;
        Legend L;

        L = chart.getLegend();
        desc = chart.getDescription();
        desc.setText(""); // this is the weirdest way to clear something!!
        L.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        //leftAxis.setAxisMaximum(8f); // the axis maximum is 100
        //leftAxis.setLabelCount(9, true);
        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
//        final String xVal[]={"Val1","Val2","Val3","Val4","Val5","Val6","Val7"};
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xVal[(int) value]; // xVal is a string array
//            }
//
//        });
        //xAxis.setLabelCount(7, true); // force 6 labels

        //xAxis.setLabelCount(20, true);


        leftAxis.setTextSize(10f);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        BarData data = new BarData(  setData());

        data.setBarWidth(0.7f); // set custom bar width
        chart.setData(data);

        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        //chart.setBackgroundColor(Color.parseColor("#ffd613"));
        chart.setBackgroundColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.setDrawBorders(false);
        chart.setDescription(desc);
        chart.setDrawValueAboveBar(true);

        chart.setOnChartValueSelectedListener(this);

        // usage on whole data object
        data.setValueFormatter(new MyValueFormatter());

    }

    private BarDataSet setData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        //entries.add(new BarEntry(0, 2));
        entries.add(new BarEntry(1, 8));
        entries.add(new BarEntry(2, 6));
        entries.add(new BarEntry(3, 10));
        entries.add(new BarEntry(4, 0));
        entries.add(new BarEntry(5, 6));
        entries.add(new BarEntry(6, 3));
        entries.add(new BarEntry(7, 80));
        entries.add(new BarEntry(8, 60));
        entries.add(new BarEntry(9, 50));
        entries.add(new BarEntry(10, 70));
        entries.add(new BarEntry(11, 60));
        entries.add(new BarEntry(12, 30));
        entries.add(new BarEntry(13, 80));
        entries.add(new BarEntry(14, 60));
        entries.add(new BarEntry(15, 50));
        entries.add(new BarEntry(16, 70));
        entries.add(new BarEntry(17, 60));
        entries.add(new BarEntry(18, 30));
        entries.add(new BarEntry(19, 80));
        entries.add(new BarEntry(20, 60));
        entries.add(new BarEntry(21, 50));
        entries.add(new BarEntry(22, 70));
        entries.add(new BarEntry(23, 60));
        entries.add(new BarEntry(24, 30));
        entries.add(new BarEntry(25, 80));
        entries.add(new BarEntry(26, 60));
        entries.add(new BarEntry(27, 50));
        entries.add(new BarEntry(28, 70));
        entries.add(new BarEntry(29, 60));
        entries.add(new BarEntry(30, 30));
        entries.add(new BarEntry(31, 80));

        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(Color.parseColor("#ffd613"));
        set.setValueTextColor(Color.rgb(155,155,155));
        //set.setDrawValues(false);

        return set;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        final String x = chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis());

        Toast.makeText(this,""+e.getX(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {

    }
}













