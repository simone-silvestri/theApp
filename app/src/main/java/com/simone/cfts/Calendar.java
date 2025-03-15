package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Date;

public class Calendar extends AppCompatActivity {

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

        java.util.Calendar currentDay= java.util.Calendar.getInstance();
        int currDate= currentDay.get(java.util.Calendar.DATE);
        int currMonth= currentDay.get(java.util.Calendar.MONTH)+1;
        int currYear= currentDay.get(java.util.Calendar.YEAR);
        String day = new String(currDate + "-" + currMonth + "-" + currYear);

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        History datewod = dbhandler.loadDate(day);
        if(datewod.getWod()==-1) {
            dailyWod.setText("No WoD the " + day);
        } else {
            Workout wod = dbhandler.loadWorkoutFromId(datewod.getWod());
            dailyWod.setText("WoD: " + wod.getTitle());
        }

        int count = 0;
        for (int i = 1; i <= currDate; i++) {
            day = i + "-" + currMonth + "-" + currYear;
            datewod = dbhandler.loadDate(day);
            if(datewod.getWod()!=-1) {
                count+=1;
            }
        }
        monthlyGoals.setText(count +" of 22 monthly workouts");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month=month+1;
                DatabaseHelper dbhandler = DatabaseHelper.getInstance(Calendar.this);
                String day2 = new String(dayOfMonth + "-" + month + "-" + year);
                History datewod = dbhandler.loadDate(day2);
                if(datewod.getWod()==-1) {
                    dailyWod.setText("No WoD the " + day2);
                } else {
                    Workout wod = dbhandler.loadWorkoutFromId(datewod.getWod());
                    dailyWod.setText("" + wod.getTitle());
                }
            }
        });
    }

    // magic number=
    // millisec * sec * min * hours
    // 1000 * 60 * 60 * 24 = 86400000
    public static final long MAGIC=86400000L;

    public int DateToDays (Date date){
        //  convert a date to an integer and back again
        long currentTime=date.getTime();
        currentTime=currentTime/MAGIC;
        return (int) currentTime;
    }

    public Date DaysToDate(int days) {
        //  convert integer back again to a date
        long currentTime=(long) days*MAGIC;
        return new Date(currentTime);
    }
}