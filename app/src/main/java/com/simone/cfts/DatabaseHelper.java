package com.simone.cfts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    private String msg;

    // Database Info
    private static final String DATABASE_NAME = "workexeDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_WORK = "Workouts";
    private static final String TABLE_EXE  = "Exercises";
    private static final String TABLE_REL  = "WorkoutExercises";
    private static final String TABLE_CAL  = "Calendar";

    // Calendar Table
    private static final String KEY_CAL_ID      = "id";
    private static final String KEY_CAL_DAY     = "day";
    private static final String KEY_CAL_WORK_ID = "workId";

    // Exercise Table
    private static final String KEY_EXE_ID   = "id";
    private static final String KEY_EXE_NAME = "exerciseName";
    private static final String KEY_EXE_DIFF = "exerciseDifficulty";
    private static final String KEY_EXE_DESC = "exerciseDescription";
    private static final String KEY_EXE_MUSC = "exerciseMuscle";

    // Relation Table
    private static final String KEY_REL_ID = "id";
    private static final String KEY_REL_WORK_ID = "relWorkoutId";
    private static final String KEY_REL_EXE_NAME = "relExerciseName";
    private static final String KEY_REL_TIME = "relExerciseTime";
    private static final String KEY_REL_PAUSE = "relExercisePause";
    private static final String KEY_REL_REPS = "relExerciseReps";

    // Workout table columns
    private static final String KEY_WORK_ID = "id";
    private static final String KEY_WORK_NAME = "workoutName";
    private static final String KEY_WORK_TYPE = "workoutType";
    private static final String KEY_WORK_WOD = "workoutWod";
    private static final String KEY_WORK_DIFF = "workoutDiff";
    private static final String KEY_WORK_TIME = "workoutTime";
    private static final String KEY_WORK_SET = "workoutSet";
    private static final String KEY_WORK_PAUSE = "workoutPause";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
//    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXE_TABLE = " CREATE TABLE " + TABLE_EXE + "("
                + KEY_EXE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_EXE_NAME + " TEXT, "
                + KEY_EXE_DIFF   + " INTEGER, "
                + KEY_EXE_DESC + " TEXT, "
                + KEY_EXE_MUSC + " TEXT)";
        String CREATE_REL_TABLE = " CREATE TABLE " + TABLE_REL + "("
                + KEY_REL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_REL_WORK_ID + " INTEGER REFERENCES " + TABLE_WORK + ", "
                + KEY_REL_EXE_NAME + " TEXT REFERENCES " + TABLE_EXE + ", "
                + KEY_REL_TIME + " INTEGER, "
                + KEY_REL_PAUSE + " INTEGER, "
                + KEY_REL_REPS + " INTEGER)";
        String CREATE_WORK_TABLE = " CREATE TABLE " + TABLE_WORK + "("
                + KEY_WORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_WORK_NAME + " TEXT, "
                + KEY_WORK_WOD + " TEXT, "
                + KEY_WORK_TYPE + " TEXT, "
                + KEY_WORK_DIFF + " INTEGER, "
                + KEY_WORK_TIME + " INTEGER, "
                + KEY_WORK_SET + " INTEGER, "
                + KEY_WORK_PAUSE + " INTEGER)";
        String CREATE_CAL_TABLE = " CREATE TABLE " + TABLE_CAL + "("
                + KEY_CAL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CAL_DAY   + " TEXT, "
                + KEY_CAL_WORK_ID + " INTEGER)";
        db.execSQL(CREATE_EXE_TABLE);
        db.execSQL(CREATE_REL_TABLE);
        db.execSQL(CREATE_WORK_TABLE);
        db.execSQL(CREATE_CAL_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAL);
            onCreate(db);
        }
    }

    // Insert or update a workout in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.


    public History loadDate(String date) {
        History dateWod = new History();

        String CAL_SELECT_QUERY = "SELECT * FROM " + TABLE_CAL+ " WHERE " + KEY_CAL_DAY + " = " + "'" + date + "'" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CAL_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    dateWod.setDate(cursor.getString(cursor.getColumnIndex(KEY_CAL_DAY)));
                    dateWod.setWod(cursor.getInt(cursor.getColumnIndex(KEY_CAL_WORK_ID)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return dateWod;
    }

    public long addDateToCalendar(String workname) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        int workoutId = loadWorkoutId(workname);
        long calendarId = -1;
        db.beginTransaction();
        try {
            Calendar currentDay= Calendar.getInstance();
            int currDate= currentDay.get(Calendar.DATE);
            int currMonth= currentDay.get(Calendar.MONTH)+1;
            int currYear= currentDay.get(Calendar.YEAR);
            String date = new String(currDate + "-" + currMonth + "-" + currYear);
            ContentValues values = new ContentValues();
            values.put(KEY_CAL_DAY, date);
            values.put(KEY_CAL_WORK_ID, workoutId);
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique
            int rows = db.update(TABLE_CAL, values, KEY_CAL_DAY + "= ?", new String[]{date});
            // Check if update succeeded
            if (rows == 1) {
                String calendarSelectQuery = "SELECT " + KEY_CAL_ID + " FROM " + TABLE_CAL
                        + " WHERE " + KEY_CAL_DAY + " = ?";
                Cursor cursor = db.rawQuery(calendarSelectQuery, new String[]{date});
                try {
                    if (cursor.moveToFirst()) {
                        calendarId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                calendarId = db.insertOrThrow(TABLE_CAL, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return calendarId;
    }

    public ArrayList<Workout> loadDatabaseDiff(int difficulty) {
        ArrayList<Workout> wdList = new ArrayList<>();
        // First load all the names, then populate them with the exercises

        String WORK_SELECT_QUERY = "SELECT * FROM " + TABLE_WORK + " WHERE " + KEY_WORK_DIFF + " = " + "'" + difficulty + "'" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WORK_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Workout work = new Workout();
                    work.setID(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ID)));
                    work.setTitle(cursor.getString(cursor.getColumnIndex(KEY_WORK_NAME)));
                    work.setWod(cursor.getString(cursor.getColumnIndex(KEY_WORK_WOD)));
                    work.setType(cursor.getString(cursor.getColumnIndex(KEY_WORK_TYPE)));
                    work.setTotalTime(cursor.getInt(cursor.getColumnIndex(KEY_WORK_TIME)));
                    work.setDifficulty(cursor.getInt(cursor.getColumnIndex(KEY_WORK_DIFF)));
                    work.setNumberOfSets(cursor.getInt(cursor.getColumnIndex(KEY_WORK_SET)));
                    work.setSetPause(cursor.getInt(cursor.getColumnIndex(KEY_WORK_PAUSE)));

                    ArrayList<Exercise> exeList = new ArrayList<>();
                    String REL_SELECT_QUERY = "SELECT * FROM " + TABLE_REL + " WHERE " + KEY_REL_WORK_ID + " = " + "'" + work.getID() + "'";
                    Cursor cursor2 = db.rawQuery(REL_SELECT_QUERY, null);
                    try {
                        if (cursor2.moveToFirst()) {
                            do {
                                Exercise exe = new Exercise();
                                exe.setWorkoutId(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_WORK_ID)));
                                exe.setName(cursor2.getString(cursor2.getColumnIndex(KEY_REL_EXE_NAME)));
                                exe.setPauseInSeconds(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_PAUSE)));
                                exe.setTimeInSeconds(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_TIME)));
                                exe.setReps(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_REPS)));
                                exeList.add(exe);
                            } while (cursor2.moveToNext());
                        }

                    } catch (Exception e) {
                        Log.d(msg, "Error while trying to get posts from database");
                    } finally {
                        if (cursor2 != null && !cursor2.isClosed()) {
                            cursor2.close();
                        }
                    }
                    work.setExercises(exeList);
                    wdList.add(work);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return wdList;
    }

    public ArrayList<Workout> loadDatabase() {

        ArrayList<Workout> wdList = new ArrayList<>();
        // First load all the names, then populate them with the exercises

        String WORK_SELECT_QUERY = "SELECT * FROM " + TABLE_WORK;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WORK_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Workout work = new Workout();
                    work.setID(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ID)));
                    work.setTitle(cursor.getString(cursor.getColumnIndex(KEY_WORK_NAME)));
                    work.setWod(cursor.getString(cursor.getColumnIndex(KEY_WORK_WOD)));
                    work.setType(cursor.getString(cursor.getColumnIndex(KEY_WORK_TYPE)));
                    work.setTotalTime(cursor.getInt(cursor.getColumnIndex(KEY_WORK_TIME)));
                    work.setDifficulty(cursor.getInt(cursor.getColumnIndex(KEY_WORK_DIFF)));
                    work.setNumberOfSets(cursor.getInt(cursor.getColumnIndex(KEY_WORK_SET)));
                    work.setSetPause(cursor.getInt(cursor.getColumnIndex(KEY_WORK_PAUSE)));

                    ArrayList<Exercise> exeList = new ArrayList<>();
                    String REL_SELECT_QUERY = "SELECT * FROM " + TABLE_REL + " WHERE " + KEY_REL_WORK_ID + " = " + "'" + work.getID() + "'";
                    Cursor cursor2 = db.rawQuery(REL_SELECT_QUERY, null);
                    try {
                        if (cursor2.moveToFirst()) {
                            do {
                                Exercise exe = new Exercise();
                                exe.setWorkoutId(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_WORK_ID)));
                                exe.setName(cursor2.getString(cursor2.getColumnIndex(KEY_REL_EXE_NAME)));
                                exe.setPauseInSeconds(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_PAUSE)));
                                exe.setTimeInSeconds(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_TIME)));
                                exe.setReps(cursor2.getInt(cursor2.getColumnIndex(KEY_REL_REPS)));
                                exeList.add(exe);
                            } while (cursor2.moveToNext());
                        }

                    } catch (Exception e) {
                        Log.d(msg, "Error while trying to get posts from database");
                    } finally {
                        if (cursor2 != null && !cursor2.isClosed()) {
                            cursor2.close();
                        }
                    }
                    work.setExercises(exeList);
                    wdList.add(work);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return wdList;
    }

    public void removeExercises(Workout work) {
        ArrayList<Exercise> exeList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            long workoutId = addOrUpdateWorkout(work);
            String query = "Select * FROM " + TABLE_REL + " WHERE " + KEY_REL_WORK_ID + "=" + "'" + workoutId + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(cursor.getColumnIndex(KEY_REL_ID));
                    db.delete(TABLE_REL, KEY_REL_ID + " = ?", new String[]{String.valueOf(id)});
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to get posts from database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Exercise> loadExercises() {
        ArrayList<Exercise> exeList = new ArrayList<>();
        String REL_SELECT_QUERY = "SELECT * FROM " + TABLE_REL;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery(REL_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Exercise exe = new Exercise();
                        exe.setWorkoutId(cursor.getInt(cursor.getColumnIndex(KEY_REL_WORK_ID)));
                        exe.setName(cursor.getString(cursor.getColumnIndex(KEY_REL_EXE_NAME)));
                        exe.setPauseInSeconds(cursor.getInt(cursor.getColumnIndex(KEY_REL_PAUSE)));
                        exe.setTimeInSeconds(cursor.getInt(cursor.getColumnIndex(KEY_REL_TIME)));
                        exe.setReps(cursor.getInt(cursor.getColumnIndex(KEY_REL_REPS)));
                        exeList.add(exe);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(msg, "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error when oadin one exercise list");
        } finally {
            db.endTransaction();
        }
        return exeList;

    }

    public int loadExerciseId(String name) {
        int exeId = -1;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT " + KEY_EXE_ID + " FROM " + TABLE_EXE
                    + " WHERE " + KEY_EXE_NAME + " = ?";
            Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(name)});
            if (cursor.moveToFirst()) {
                exeId = cursor.getInt(0);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exeId;
    }

    public ExerciseDetail loadOneExercise(String name) {
        ExerciseDetail exe = new ExerciseDetail();
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT * FROM " + TABLE_EXE
                    + " WHERE " + KEY_EXE_NAME + " = ?";
            Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{name});
            if (cursor.moveToFirst()) {
                exe.setName(cursor.getString(cursor.getColumnIndex(KEY_EXE_NAME)));
                exe.setDifficulty(cursor.getInt(cursor.getColumnIndex(KEY_EXE_DIFF)));
                exe.setDescription(cursor.getString(cursor.getColumnIndex(KEY_EXE_DESC)));
                exe.setMuscle(cursor.getString(cursor.getColumnIndex(KEY_EXE_MUSC)));
                db.setTransactionSuccessful();
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exe;
    }

    public ArrayList<ExerciseDetail> loadAllExercises() {
        ArrayList<ExerciseDetail> exeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT * FROM " + TABLE_EXE;
            Cursor cursor = db.rawQuery(workoutSelectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ExerciseDetail exe = new ExerciseDetail();
                    exe.setName(cursor.getString(cursor.getColumnIndex(KEY_EXE_NAME)));
                    exe.setDifficulty(cursor.getInt(cursor.getColumnIndex(KEY_EXE_DIFF)));
                    exe.setID(cursor.getInt(cursor.getColumnIndex(KEY_EXE_ID)));
                    exe.setDescription(cursor.getString(cursor.getColumnIndex(KEY_EXE_DESC)));
                    exe.setMuscle(cursor.getString(cursor.getColumnIndex(KEY_EXE_MUSC)));
                    exeList.add(exe);
                } while(cursor.moveToNext());
                cursor.close();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exeList;
    }

    public int loadWorkoutId(String name) {
        int workoutId = -1;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                    + " WHERE " + KEY_WORK_NAME + " = ?";
            Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(name)});
            if (cursor.moveToFirst()) {
                workoutId = cursor.getInt(0);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return workoutId;
    }

    public Workout loadWorkoutFromId(int wrkId) {
        Workout work = new Workout();
        String WRK_SELECT_QUERY = "SELECT * FROM " + TABLE_WORK+ " WHERE " + KEY_WORK_ID + " = " + "'" + wrkId + "'" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WRK_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                work.setID(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ID)));
                work.setTitle(cursor.getString(cursor.getColumnIndex(KEY_WORK_NAME)));
                work.setWod(cursor.getString(cursor.getColumnIndex(KEY_WORK_WOD)));
                work.setType(cursor.getString(cursor.getColumnIndex(KEY_WORK_TYPE)));
                work.setTotalTime(cursor.getInt(cursor.getColumnIndex(KEY_WORK_TIME)));
                work.setDifficulty(cursor.getInt(cursor.getColumnIndex(KEY_WORK_DIFF)));
                work.setNumberOfSets(cursor.getInt(cursor.getColumnIndex(KEY_WORK_SET)));
                work.setSetPause(cursor.getInt(cursor.getColumnIndex(KEY_WORK_PAUSE)));
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return work;
    }

    public void addExerciseInWorkout(Exercise exercise, Workout work) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            long workoutId = addOrUpdateWorkout(work);
            ContentValues values = new ContentValues();
            values.put(KEY_REL_WORK_ID, workoutId);
            values.put(KEY_REL_EXE_NAME, exercise.getName());
            values.put(KEY_REL_TIME, exercise.getTimeInSeconds());
            values.put(KEY_REL_PAUSE, exercise.getPauseInSeconds());
            values.put(KEY_REL_REPS, exercise.getReps());
            db.insertOrThrow(TABLE_REL, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public long addOneExercise(ExerciseDetail exe) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long exerciseId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXE_NAME, exe.getName());
            values.put(KEY_EXE_DIFF, exe.getDifficulty());
            values.put(KEY_EXE_DESC, exe.getDescription());
            values.put(KEY_EXE_MUSC, exe.getMuscle());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique

            exerciseId = db.insert(TABLE_EXE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exerciseId;
    }

    public long addOrUpdateExercise(ExerciseDetail exe) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long exerciseId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXE_NAME, exe.getName());
            values.put(KEY_EXE_DIFF, exe.getDifficulty());
            values.put(KEY_EXE_DESC, exe.getDescription());
            values.put(KEY_EXE_MUSC, exe.getMuscle());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique
            int rows = db.update(TABLE_EXE, values, KEY_EXE_NAME + "= ?", new String[]{exe.getName()});
            // Check if update succeeded
            if (rows == 1) {
                String workoutSelectQuery = "SELECT " + KEY_EXE_ID + " FROM " + TABLE_EXE
                        + " WHERE " + KEY_EXE_NAME + " = ?";
                Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(exe.getName())});
                try {
                    if (cursor.moveToFirst()) {
                        exerciseId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                exerciseId = db.insertOrThrow(TABLE_EXE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exerciseId;
    }

    public long addOrUpdateWorkout(Workout work) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long workoutId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_WORK_NAME, work.getTitle());
            values.put(KEY_WORK_WOD, work.getWod());
            values.put(KEY_WORK_TYPE, work.getType());
            values.put(KEY_WORK_DIFF, work.getDifficulty());
            values.put(KEY_WORK_TIME, work.getTotalTime());
            values.put(KEY_WORK_SET, work.getNumberOfSets());
            values.put(KEY_WORK_PAUSE, work.getSetPause());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique
            int rows = db.update(TABLE_WORK, values, KEY_WORK_NAME + "= ?", new String[]{work.getTitle()});
            // Check if update succeeded
            if (rows == 1) {
                String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                        + " WHERE " + KEY_WORK_NAME + " = ?";
                Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(work.getTitle())});
                try {
                    if (cursor.moveToFirst()) {
                        workoutId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                workoutId = db.insertOrThrow(TABLE_WORK, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(msg, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return workoutId;
    }

    // Delete everything
    public void deleteDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_REL,  null, null);
            db.delete(TABLE_EXE,  null, null);
            db.delete(TABLE_WORK, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllExercises() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_REL, null, null);
            db.delete(TABLE_EXE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteExercisesTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_EXE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteCalendarTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_CAL, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public int deleteWorkout(Workout work) {
        int workoutId = -1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                    + " WHERE " + KEY_WORK_NAME + " = ?";
            Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(work.getTitle())});
            if (cursor.moveToFirst()) {
                workoutId = cursor.getInt(0);
                db.delete(TABLE_REL, KEY_REL_WORK_ID + "= ?", new String[]{String.valueOf(workoutId)});
            }
            cursor.close();
            db.delete(TABLE_WORK, KEY_WORK_ID + "= ?", new String[]{String.valueOf(workoutId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(msg, "Error when trying to delete one workout");
        } finally {
            db.endTransaction();
        }
        return workoutId;
    }

    public static final long MAGIC=86400000L;

    public int DateToDays (Date date){
        //  convert a date to an integer and back again
        long currentTime=date.getTime();
        currentTime=currentTime/MAGIC;
        return (int) currentTime;
    }

}
