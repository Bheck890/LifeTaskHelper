package com.mobilegroup3.lifetaskhelper.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobilegroup3.lifetaskhelper.ui.tasks.TasksFragment;


public class ActionDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME =
            "lifeActions" +
                    TasksFragment.DatabaseNumber +
                    ".sqlite"; // the name of our database
    public static final String TB_NAME = "ACTIONS"; // the name of our Table
    private static final int DB_VERSION = 1; // the version of the database

    public ActionDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("@@@@@@@@@@@@-Creating Action Database");
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
        System.out.println("@@@@@@@@@@@@-Upgraded ActionDB to version " + DB_VERSION);
    }

    //Database Testing input Data
    private static void insertAction(SQLiteDatabase db,
                                              String description) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TASK_ID", 1);
        taskValues.put("DESCRIPTION", description);
        taskValues.put("DATE", "2022-04-13 - 5:30 PM");
        taskValues.put("LOCATION", "N/A");
        db.insert(TB_NAME, null, taskValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Action Database Update \nOLD: " + oldVersion + " New: " + newVersion);
        if (oldVersion < 1) { //Version 0
            db.execSQL("CREATE TABLE " + TB_NAME + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TASK_ID INTEGER,"
                    + "DESCRIPTION TEXT,"
                    + "DATE TEXT,"
                    + "TIME TEXT,"
                    + "LOCATION TEXT);");

            //insertLocationDefault(db, "Example Location note"); // Green
            //insertDateDefault(db, "Example Date note"); //Blue
            insertAction(db,"Initial Description Test"); // Invisible
            //insertAllDataDefault(db, "Example Everything note");
        }
        if (oldVersion < 2) { //Version 1
            //db.execSQL("ALTER TABLE Tasks ADD COLUMN FAVORITE NUMERIC;");
        }
        if (oldVersion < 3) { //Version 2
            //db.execSQL("ALTER TABLE Tasks ADD COLUMN \"LIKE\" INTEGER NOT NULL DEFAULT(0);");
        }
    }
}

