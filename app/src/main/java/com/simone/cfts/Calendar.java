package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class Calendar extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView monthlyGoals, dailyWod;

    public String displayWods(History datewod, DatabaseHelper dbhandler) {
        String output;
        String day = datewod.getDate();
        if (datewod.getWod().isEmpty()) {
            output = "No WoD the " + day;
        } else {
            ArrayList<Integer> wodIndices = datewod.getWod();
            ArrayList<Workout> wodList = new ArrayList<>();
            for (int i = 0; i < wodIndices.size(); i++) {
                int wodIdx = wodIndices.get(i);
                Workout wod = dbhandler.loadWorkoutFromId(wodIdx);
                wodList.add(wod);
            }
            // TODO: display all workouts (change the Textview to a Listview)
            output = "There is a WoD";
        }

        return output;
    }

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
        int currMonth= currentDay.get(java.util.Calendar.MONTH) + 1;
        int currYear = currentDay.get(java.util.Calendar.YEAR);
        String day = new String(currDate + "-" + currMonth + "-" + currYear);

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        History datewod = dbhandler.loadDate(day);

        dailyWod.setText(displayWods(datewod, dbhandler));

        int count = 0;
        for (int i = 1; i <= currDate; i++) {
            day = i + "-" + currMonth + "-" + currYear;
            datewod = dbhandler.loadDate(day);
            count += datewod.getWod().size();
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
                dailyWod.setText(displayWods(datewod, dbhandler));
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