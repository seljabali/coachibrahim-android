package com.seljabali.coachibrahim;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seljabali.coachibrahim.datasources.DayLogDatasource;
import com.seljabali.coachibrahim.models.DayLog;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import hirondelle.date4j.DateTime;


public class MainActivity extends ActionBarActivity {
    private ViewGroup dayViewContainer;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_TIME = "my_first_time";
    private static final int HAS_ACTIVITY = R.color.has_activity;
    private static final int HAS_NO_ACTIVITY = R.color.has_no_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dayViewContainer = (ViewGroup) findViewById(R.id.dayViewContainer);

        // setup calendar fragment
        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        if (isFirstTimeUser(this)) {
            setupDatabase();
            setFirstTimeUser(this, false);
        } else {
            for (final DayLog dayLog : DayLogDatasource.getAllDayLogs(this)) {
                setHasActivityFlag(caldroidFragment, dayLog);
            }
        }
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Calendar currentDate = Calendar.getInstance();
                if (currentDate.getTime().after(date)) {
                    selectDate(date, view, caldroidFragment);
                }
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.calendarContainer, caldroidFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDate(Date date, View view, CaldroidFragment caldroidFragment) {
        DayLog storedDayLog = getDayLog(date);
        if (storedDayLog == null) {
            Log.v("Selecting Date:", date.toString());
            caldroidFragment.setBackgroundResourceForDate(HAS_ACTIVITY, date);
            long result = addDayLog(new DayLog(date, true));
            if (result == -1) {
                Toast.makeText(this, "Sorry somthing went wrong.", Toast.LENGTH_SHORT).show();
            }
        } else {
            final DayLog newDayLog = new DayLog(date, !storedDayLog.hasActivity());
            Log.v("Selecting Date:", "New Day: " + date.toString());
            if (newDayLog.hasActivity()) {
                caldroidFragment.setBackgroundResourceForDate(HAS_ACTIVITY, date);
            } else {
                caldroidFragment.setBackgroundResourceForDate(HAS_NO_ACTIVITY, date);
            }
            long result = updateDayLog(newDayLog);
            if (result == -1) {
                Toast.makeText(this, "Sorry somthing went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
        caldroidFragment.refreshView();
    }

    private long addDayLog(DayLog dayLog) {
        DayLogDatasource dataSource = new DayLogDatasource(this);
        dataSource.open();
        long wordId = dataSource.addDayLog(dayLog);
        dataSource.close();
        return wordId;
    }

    private long updateDayLog(DayLog dayLog) {
        DayLogDatasource dataSource = new DayLogDatasource(this);
        dataSource.open();
        long result = dataSource.updateDayLog(dayLog);
        dataSource.close();
        return result;
    }

    private List<DayLog> getDayLogs() {
        DayLogDatasource dayLogDatasource = new DayLogDatasource(this);
        dayLogDatasource.open();
        List<DayLog> dayLogResults = dayLogDatasource.getAllDayLogs(this);
        dayLogDatasource.close();

        return dayLogResults;
    }

    private DayLog getDayLog(Date date) {
        DayLogDatasource dayLogDatasource = new DayLogDatasource(this);
        dayLogDatasource.open();
        DayLog dayLogResult = dayLogDatasource.getDay(date);
        dayLogDatasource.close();

        return dayLogResult;
    }

    private void toastAllDayLogs() {
        List<DayLog> dayLogs = getDayLogs();
        for (DayLog dayLog : dayLogs) {
            Toast.makeText(this, dayLog.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDatabase() {
        MySQLHelper mySQLHelper = new MySQLHelper(this);
        mySQLHelper.dropTables();
        mySQLHelper.setupDatabase();
    }
    public static boolean isFirstTimeUser(Context context) {
        SharedPreferences sharedSettings = context.getSharedPreferences(PREFS_NAME, 0);
        return sharedSettings.getBoolean(FIRST_TIME, true);
    }

    public static void setFirstTimeUser(Context context, boolean firstTime) {
        SharedPreferences sharedSettings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        sharedSettings.edit().putBoolean(MainActivity.FIRST_TIME, firstTime).commit();
    }

    public static Date getDateTimeToDate(DateTime dateTime) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format1.parse(dateTime.toString());
        } catch (Exception e) {
            Log.e("Error parsing date", e.toString());
        }
        return date;
    }
    public static Date getDateTimeToDate2(DateTime dateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
        return gregorianCalendar.getTime();
    }

    private void setHasActivityFlag(CaldroidFragment caldroidFragment, DayLog dayLog) {
        Date date = getDateTimeToDate(dayLog.getDay());
        if (dayLog.hasActivity()) {
            caldroidFragment.setBackgroundResourceForDate(HAS_ACTIVITY, date);
        } else {
            caldroidFragment.setBackgroundResourceForDate(HAS_NO_ACTIVITY, date);
        }
    }

}