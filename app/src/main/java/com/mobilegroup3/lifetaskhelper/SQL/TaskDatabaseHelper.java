package com.mobilegroup3.lifetaskhelper.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobilegroup3.lifetaskhelper.ui.tasks.TasksFragment;


public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME =
            "lifeTasks" +
            TasksFragment.DatabaseNumber +
            ".sqlite"; // the name of our database
    //public static final String DB_NAME = "lifeTasks8.sqlite"; // the name of our database
    public static final String TB_NAME = "TASK"; // the name of our Table
    //private static final String A_TB_NAME = ActionDatabaseHelper.TB_NAME;
    private static final int DB_VERSION = 1; // the version of the database

    public TaskDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("@@@@@@@@@@@@-Creating Task Database");
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
        System.out.println("@@@@@@@@@@@@-Upgraded TaskDB to version " + DB_VERSION);
    }

    //Database Testing input Data
    private static void insertTask(SQLiteDatabase db,
                                    String title) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", title);
        taskValues.put("LATITUDE", 0);
        taskValues.put("LONGITUDE", 0);
        taskValues.put("ADDRESS", "");
        taskValues.put("ENABLE_ADDRESS", false);
        taskValues.put("ADDRESS_VERIFIED", false);
        taskValues.put("DATE", "");
        taskValues.put("HOUR", 0);
        taskValues.put("MINUTE", 0);

        db.insert(TB_NAME, null, taskValues);
    }

    //Database Testing input Data for Date
    private static void insertDateDefault(SQLiteDatabase db,
                                          String title) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", title);
        taskValues.put("LATITUDE", 0);
        taskValues.put("LONGITUDE", 0);
        taskValues.put("ADDRESS", "");
        taskValues.put("ENABLE_ADDRESS", false);
        taskValues.put("ADDRESS_VERIFIED", false);
        taskValues.put("DATE", "2022-04-14");
        taskValues.put("HOUR", 14);
        taskValues.put("MINUTE", 5); //2:05PM

        db.insert(TB_NAME, null, taskValues);
    }

    //Database Testing input Data for Location
    private static void insertLocationDefault(SQLiteDatabase db,
                                              String title) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", title);
        taskValues.put("LATITUDE", 29.999762099999995);
        taskValues.put("LONGITUDE", -90.4127206);
        taskValues.put("ADDRESS", "123 Apple Street");
        taskValues.put("ENABLE_ADDRESS", true);
        taskValues.put("ADDRESS_VERIFIED", true);
        taskValues.put("DATE", "");
        taskValues.put("HOUR", 0);
        taskValues.put("MINUTE", 0); //2:05PM

        db.insert(TB_NAME, null, taskValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Task Database Update \nOLD: " + oldVersion + " New: " + newVersion);
        if (oldVersion < 1) { //Version 0
            db.execSQL("CREATE TABLE " + TB_NAME + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TITLE TEXT,"
                    + "LATITUDE DOUBLE(4,25) NOT NULL DEFAULT(0), "
                    + "LONGITUDE DOUBLE(4,25) NOT NULL DEFAULT(0),"
                    + "ADDRESS TEXT,"
                    + "ENABLE_ADDRESS BOOL,"
                    + "ADDRESS_VERIFIED BOOL,"
                    + "DATE TEXT,"
                    + "HOUR INT(4) NOT NULL DEFAULT(0),"
                    + "MINUTE INT(4) NOT NULL DEFAULT(0));");

            insertLocationDefault(db, "Example Location note"); // Green
            insertDateDefault(db, "Example Date note"); //Blue
            insertTask(db, "Example Basic note"); // Invisible
        }
        if (oldVersion < 2) { //Version 1

        }
        if (oldVersion < 3) { //Version 2
            //db.execSQL("ALTER TABLE Tasks ADD COLUMN \"LIKE\" INTEGER NOT NULL DEFAULT(0);");
        }
    }
}

