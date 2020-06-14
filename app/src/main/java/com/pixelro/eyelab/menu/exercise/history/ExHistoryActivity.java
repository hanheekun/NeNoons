
// https://medium.com/@Patel_Prashant_/android-custom-calendar-with-events-fa48dfca8257

package com.pixelro.eyelab.menu.exercise.history;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pixelro.eyelab.BaseActivity;
import com.pixelro.eyelab.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.riontech.calendar.CustomCalendar;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;
import com.riontech.calendar.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ExHistoryActivity extends BaseActivity implements View.OnClickListener, CustomCalendar.OnScrollChangeListener {

    private final static String TAG = ExHistoryActivity.class.getSimpleName();

    private CustomCalendar customCalendar;

    private MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history);

//        customCalendar = (CustomCalendar) findViewById(R.id.customCalendar);
//        String[] arr = {"2020-06-10", "2020-06-11", "2020-06-15", "2020-06-16", "2020-06-25"};
//        for (int i = 0; i < 5; i++) {
//                int eventCount = 2;
//            customCalendar.addAnEvent(arr[i], eventCount, getEventDataList(eventCount));
//        }
//
//        customCalendar.setOnScrollChangeListener(this);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 5, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(ExHistoryActivity.this,date.getDate().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }


//    public ArrayList<EventData> getEventDataList(int count) {
//        ArrayList<EventData> eventDataList = new ArrayList();
//
//        for (int i = 0; i < count; i++) {
//            EventData dateData = new EventData();
//            ArrayList<dataAboutDate> dataAboutDates = new ArrayList();
//
//            //dateData.setSection(CalendarUtils.getNAMES()[new Random().nextInt(CalendarUtils.getNAMES().length)]);
//            dateData.setSection("운동1");
//            dataAboutDate dataAboutDate = new dataAboutDate();
//
//            int index = new Random().nextInt(CalendarUtils.getEVENTS().length);
//
//            //dataAboutDate.setTitle(CalendarUtils.getEVENTS()[index]);
//            //dataAboutDate.setSubject(CalendarUtils.getEventsDescription()[index]);
//            //dataAboutDates.add(dataAboutDate);
//
//            dataAboutDate.setTitle("setTitle");
//            dataAboutDate.setSubject("setSubject");
//            dataAboutDates.add(dataAboutDate);
//
//            dateData.setData(dataAboutDates);
//            eventDataList.add(dateData);
//        }
//
//        return eventDataList;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Toast.makeText(this,"가나다",Toast.LENGTH_SHORT).show();
    }
}
