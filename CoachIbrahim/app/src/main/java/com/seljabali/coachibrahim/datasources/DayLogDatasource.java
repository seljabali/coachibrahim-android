package com.seljabali.coachibrahim.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.seljabali.coachibrahim.MySQLHelper;
import com.seljabali.coachibrahim.models.DayLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by samsoom on 1/12/15.
 */
public class DayLogDatasource {
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_DATE = 1;
    public static final int COLUMN_ACTIVITY = 2;

    // Database fields
    private SQLiteDatabase database;
    private MySQLHelper dbHelper;
    private static final String[] allColumns = { MySQLHelper.COLUMN_ID, MySQLHelper.COLUMN_DATE, MySQLHelper.COLUMN_ACTIVITY};

    public DayLogDatasource(Context context) {
        dbHelper = new MySQLHelper(context);
    }
    public DayLogDatasource(MySQLHelper mySQLHelper) {
        dbHelper = mySQLHelper;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addDayLog(DayLog dayLog) {
        return addDayLog(dayLog.getDay(), dayLog.hasActivity());
    }

    public long addDayLog(DateTime day, boolean activity) {
        ContentValues values = new ContentValues();
        values.put(MySQLHelper.COLUMN_DATE, day.format(MySQLHelper.COLUMN_DATE_FORMAT));
        values.put(MySQLHelper.COLUMN_ACTIVITY, activity);
        long insertId;
        try {
            insertId = database.insert(MySQLHelper.TABLE_DAY_LOG, null, values);
            Log.v("DB:DayLog ADD:", day.toString() + " ID: " + Integer.toString((int) insertId));
        } catch (SQLException e) {
            Log.e("DB:DayLog ADD:", "Error inserting " + values, e);
            insertId = -1;
        }
        return insertId;
    }

//    UPDATE [LOW_PRIORITY] [IGNORE] table_reference
//    SET col_name1={expr1|DEFAULT} [, col_name2={expr2|DEFAULT}] ...
//            [WHERE where_condition]
//            [ORDER BY ...]
//            [LIMIT row_count]
    public long updateDayLog(DayLog dayLog) {
        return updateDayLog(dayLog.getDay(), dayLog.hasActivity());
    }

    public long updateDayLog(DateTime day, boolean activity) {
        final String dayTime = day.toString();
        final int hasActivity = activity ? 1 : 0;
        int result = 0;
        String rawQuery = "UPDATE " + MySQLHelper.TABLE_DAY_LOG
                + " SET " + MySQLHelper.COLUMN_ACTIVITY + " = " + hasActivity
                + " WHERE "+ MySQLHelper.COLUMN_DATE + " = '" + dayTime + "'";
        rawQuery += ";";

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(rawQuery, null);
            result = cursor.getCount();
            Log.v("DB:DayLog UPDATEDAY:", dayTime + " : " + hasActivity);
        } catch (Exception e) {
            Log.e("DB:DayLog UPDATEDAY:", e.toString());
            result = -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<DayLog> getDays() {
        List<DayLog> dayLogs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(false, MySQLHelper.TABLE_DAY_LOG, allColumns, null, null,
                    null, null, null, null);
            dayLogs = cursorToDayLogs(cursor);
            cursor.close();
            Log.v("DB:DayLog GETDAYS:", "");
        } catch (SQLException e) {
            Log.e("DB:DayLog GETDAYS:", "DayLog: Error getting day logs " + e.toString());
        }  finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dayLogs;
    }

    public DayLog getDay(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat(MySQLHelper.COLUMN_DATE_FORMAT2);
        String formatted = format1.format(date.getTime());
        final DateTime dateTime = new DateTime(formatted);
        return getDay(dateTime);
    }

    public DayLog getDay(DateTime time) {
        String rawQuery = " SELECT * "
                + " FROM " + MySQLHelper.TABLE_DAY_LOG
                + " WHERE " + MySQLHelper.COLUMN_DATE + " = '" + time.toString() + "'";
        rawQuery += ";";

        Cursor cursor = null;
        DayLog dayLog = null;
        try {
            cursor = database.rawQuery(rawQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                dayLog = cursorToDayLog(cursor);
                cursor.moveToNext();
            }
            cursor.close();
            Log.v("DB:DayLog GETDAY:", "");
        } catch (Exception e) {
            Log.e("DB:DayLog GETDAY:", e.toString());
        }  finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dayLog;
    }

    public List<DayLog> getDaysInRange(DateTime fromTime, DateTime toTime) {
        String rawQuery = " SELECT * "
                + " FROM " + MySQLHelper.TABLE_DAY_LOG
                + " WHERE " + MySQLHelper.COLUMN_DATE + " >= " + fromTime.format(MySQLHelper.COLUMN_DATE_FORMAT)
                + " AND "
                + MySQLHelper.COLUMN_DATE + " <= " + toTime.format(MySQLHelper.COLUMN_DATE_FORMAT);
        rawQuery += ";";

        Cursor cursor = null;
        List<DayLog> dayLogs = null;
        try {
            cursor = database.rawQuery(rawQuery, null);
            cursor.moveToFirst();
            dayLogs = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                DayLog dayLog = cursorToDayLog(cursor);
                dayLogs.add(dayLog);
                cursor.moveToNext();
            }
            cursor.close();
            Log.v("DB:getDaysInRange", "");
        } catch (Exception e) {
            Log.e("DB:getDaysInRange", e.toString());
        }  finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return dayLogs;
    }

    private List<DayLog> cursorToDayLogs(Cursor cursor) {
        List<DayLog> dayLogs = new ArrayList<>();
        while (cursor.moveToNext()) {
            dayLogs.add(cursorToDayLog(cursor));
        }
        return dayLogs;
    }

    private DayLog cursorToDayLog(Cursor cursor) {
        DayLog dayLog = new DayLog();
        dayLog.setDay(new DateTime(cursor.getString(COLUMN_DATE)));
        dayLog.setHasActivity(cursor.getInt(COLUMN_ACTIVITY) == 1);
        return dayLog;
    }

    public static List<DayLog> getAllDayLogs(Context context) {
        List<DayLog> dayLogs = new ArrayList<>();
        DayLogDatasource dayLogDatasource = new DayLogDatasource(context);
        dayLogDatasource.open();
        dayLogs.addAll(dayLogDatasource.getDays());
        dayLogDatasource.close();
        return dayLogs;
    }
}
