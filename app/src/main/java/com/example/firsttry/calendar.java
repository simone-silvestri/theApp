package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class calendar extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView monthlyGoals, dailyWod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView) findViewById(R.id.calendarView); // get the reference of CalendarView
        dailyWod = (TextView) findViewById(R.id.dailyWod);
        monthlyGoals = (TextView) findViewById(R.id.monthlyGoals);

        long date = System.currentTimeMillis();
        calendarView.setDate(date); // set selected date 22 May 2016 in milliseconds

        Calendar currentDay= Calendar.getInstance();
        int currDate= currentDay.get(Calendar.DATE);
        int currMonth= currentDay.get(Calendar.MONTH)+1;
        int currYear= currentDay.get(Calendar.YEAR);
        String day = new String(currDate + "-" + currMonth + "-" + currYear);

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        ArrayList<History> datewod = dbhandler.loadCalendar();
        if(datewod.isEmpty()) {
            dailyWod.setText(day);
        } else {
            Workout wod = dbhandler.loadWorkoutFromId(datewod.get(0).getWod());
            dailyWod.setText(wod.getTitle());
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month=month+1;
                DatabaseHelper dbhandler = DatabaseHelper.getInstance(calendar.this);
                String day2 = new String(dayOfMonth + "-" + month + "-" + year);
                ArrayList<History> datewod = dbhandler.loadDate(day2);
                if(datewod.isEmpty()) {
                    dailyWod.setText(day2);
                } else {
                    Workout wod = dbhandler.loadWorkoutFromId(datewod.get(1).getWod());
                    dailyWod.setText(wod.getTitle());
                }

            }
        });



    }
}