package com.codingcamels.coachibrahim;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by samsoom on 1/12/15.
 */
public class MySQLHelper extends SQLiteOpenHelper implements DatabaseErrorHandler {
    private static final String DATABASE_NAME = "habibi_phrases.db";
    private static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "create table ";
    public static final String COLUMN_ID = "_id ";
    public static final String COLUMN_ID_INSERT = "_id integer primary key autoincrement, ";
    public static final String COMMA = ", ";
    public static final String UNSIGNED = " unsigned ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String AUTO_INCREMENT = " AUTO_INCREMENT ";
    //    ADD BOB INT UNSIGNED NOT NULL AUTO_INCREMENT,
    public static final String COLUMN_DATE_FORMAT = "YYYY-MM-DD";
    public static final String COLUMN_DATE_FORMAT2 = "yyyy-MM-dd";

    // DB TYPES
    public static final String TEXT = " text";
    public static final String INTEGER = " integer"; // 4b (0, 4,294,967,295)
    public static final String TINY_INT = " tyint"; // 1b (0, 255)
    public static final String SMALL_INT = " smallint"; // 2b (0, 65,535)
    public static final String MED_INT = " mediumint"; // 4b (0, 167,77,215)
    public static final String DATE = " date";
    public static final String FLOAT = " float";
    public static final String DOUBLE = " double";



    // TABLE DAY LOG
    public static final String TABLE_DAY_LOG = "day_log";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATE_INSERT = COLUMN_DATE + TEXT + COMMA;
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_ACTIVITY_INSERT = COLUMN_ACTIVITY + UNSIGNED + TINY_INT;

    // ACCOMPLISHMENT
    public static final String TABLE_ACCOMPLISHMENT = "accomplishment";
    public static final String DAY_LOG_KEY = "day_log_key";
    public static final String DAY_LOG_KEY_INSERT = DAY_LOG_KEY + UNSIGNED + INTEGER + COMMA;
    public static final String DIST_NUMERIC = "distance";
    public static final String DIST_NUMERIC_INSERT = DIST_NUMERIC + INTEGER + COMMA;
    public static final String DIST_UNIT_KEY = "distance_unit_key";
    public static final String DIST_UNIT_KEY_INSERT = DIST_UNIT_KEY + UNSIGNED + INTEGER + COMMA;
    public static final String WEIGHT_NUMERIC = "weight";
    public static final String WEIGHT_NUMERIC_INSERT = WEIGHT_NUMERIC + UNSIGNED + INTEGER + COMMA;
    public static final String WEIGHT_UNIT_KEY = "weigh_unit_key";
    public static final String WEIGHT_UNIT_KEY_INSERT = WEIGHT_UNIT_KEY + UNSIGNED + INTEGER + COMMA;
    public static final String REPS = "reps";
    public static final String REPS_KEY_INSERT = REPS + UNSIGNED + INTEGER + COMMA;
    public static final String SETS = "sets";
    public static final String SETS_KEY_INSERT = SETS + UNSIGNED + INTEGER;

    private static final String CREATE_TABLE_DAY_LOG =
            CREATE_TABLE + TABLE_DAY_LOG
                    + "(" + COLUMN_ID_INSERT
                    +   COLUMN_DATE_INSERT
                    +   COLUMN_ACTIVITY_INSERT
                    + " ); ";

    private static final String CREATE_TABLE_ACCOMPLISHMENT =
            CREATE_TABLE + TABLE_ACCOMPLISHMENT
                    + "(" + COLUMN_ID_INSERT
                    +   DAY_LOG_KEY_INSERT
                    +   DIST_NUMERIC_INSERT
                    +   DIST_UNIT_KEY_INSERT
                    +   WEIGHT_NUMERIC_INSERT
                    +   WEIGHT_UNIT_KEY_INSERT
                    +   REPS_KEY_INSERT
                    +   SETS_KEY_INSERT
                    + " ); ";


    // TODO: Create Weight & Distance unit

    private SQLiteDatabase db;
    private Context context;

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        super(context, Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
//        String pathB = Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME;
//        this.db = SQLiteDatabase.openDatabase(pathB, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Do nothing
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        dropTables();
        onCreate(db);
    }

    @Override
    public void onCorruption(SQLiteDatabase dbObj) {
        Log.e("DB", "Cheers! You broke the db!");
    }

    public void setupDatabase() {
        try {
            this.db.execSQL(CREATE_TABLE_DAY_LOG);
            this.db.execSQL(CREATE_TABLE_ACCOMPLISHMENT);
        } catch  (Exception ex) {
            Log.e("SQL", "Couldn't write initial DB: " + ex.toString());
        }
    }

    public void dropTables() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOMPLISHMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY_LOG);
    }

    //TODO
//    public void updateTable()
}