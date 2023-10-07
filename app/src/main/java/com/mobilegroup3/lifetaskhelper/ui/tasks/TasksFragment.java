package com.mobilegroup3.lifetaskhelper.ui.tasks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mobilegroup3.lifetaskhelper.SQL.ActionDatabaseHelper;
import com.mobilegroup3.lifetaskhelper.SQL.TaskDatabaseHelper;
import com.mobilegroup3.lifetaskhelper.databinding.FragmentHomeBinding;
import com.mobilegroup3.lifetaskhelper.task.Task;
import com.mobilegroup3.lifetaskhelper.task.TaskListView;
import com.mobilegroup3.lifetaskhelper.task.TaskViewAdapter;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    //Database number Used for Testing and a new Database if the old one has bad Information
    // or if the database had a bug and want to rest the values

    //Recommend to reset to 1 after you uninstall the application on the device that is being used.
    public static int DatabaseNumber = 1;

    //Task Database Variables.
    public static ArrayList<Task> tasks = new ArrayList<>();
    private static SQLiteOpenHelper taskDatabase;
    private static final String T_TB_NAME = TaskDatabaseHelper.TB_NAME; // the name of Task Table
    private static final String A_TB_NAME = ActionDatabaseHelper.TB_NAME; // the name of Action Table
    private TaskListView listView;

    //Database Systems that manipulate the Data.
    private SQLiteDatabase db;
    private Cursor cursor;

    //Fragment Variables.
    private static TasksViewModel homeViewModel;
    private FragmentHomeBinding binding;
    static TaskViewAdapter taskAdapter; //Adapter for the buttons in the listView
    private boolean location = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = (TaskListView) binding.taskList;

        //SQLGatherObjects SQL = new SQLGatherObjects();
        //taskAdapter = SQL.gatherTaskObjects(getContext(),listView,taskAdapter);


        //After Actions are Gathered, the Task list can show helpful Information
        if(tasks.isEmpty()) {
            taskDatabase = new TaskDatabaseHelper(getContext()); //creates instance of task Database
            try {
                System.out.println("@@@@@@@@@@@@-Initiate Task Database");

                db = taskDatabase.getReadableDatabase();
                cursor = db.query (T_TB_NAME,
                        new String[] {
                                "_id",
                                "TITLE",
                                "LATITUDE",
                                "LONGITUDE",
                                "ADDRESS",
                                "ENABLE_ADDRESS",
                                "ADDRESS_VERIFIED",
                                "DATE",
                                "HOUR",
                                "MINUTE"
                        }, //
                        null, null, null, null, null);

                //Move to the first record in the Cursor
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        System.out.println("@@@@@@@@@@@@-Reading Task Database Line");
                        int _id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        double latitude = cursor.getDouble(2);
                        double longitude = cursor.getInt(3);
                        String address = cursor.getString(4);
                        boolean enable_address = (cursor.getInt(5) == 1);
                        boolean address_verified = (cursor.getInt(6) == 1);
                        String date = cursor.getString(7);
                        int hour = cursor.getInt(8);
                        int min = cursor.getInt(9);

                        tasks.add(new Task(
                                _id,
                                title,
                                latitude,
                                longitude,
                                address,
                                enable_address,
                                address_verified,
                                date,
                                hour,
                                min
                        ));

                        if(address_verified) {
                            System.out.println("@@@@@@ Location Tracking");
                            homeViewModel.addCoordinates(latitude,longitude);
                        }

                        cursor.moveToNext();
                    }
                }

                //Adds the Tasks day that was last updated on it
                tasks = updateTaskActionDate(tasks);

                taskAdapter = new TaskViewAdapter(getContext(), tasks);

                // Needs to gather from the action Table the first instance that matches the
                /*
                cursor = db.query (A_TB_NAME,
                        new String[] {
                                "_id",
                                "TASK_ID"
                        }, //
                        null, null, null, null, null);

                 */

                /*
                //Output the tasks for Debugging verification
                for (Task Tas: tasks) {
                    System.out.println("@@@@@@@@@@@@-" + Tas);
                }
                */
                listView.setAdapter(taskAdapter);

                //Close Database Variables
                cursor.close();
                db.close();
                //System.out.println("@@@@@@@@@@@@- Closed Task Database Connection - @@@@@@@@@");


            } catch (SQLiteException e) {
                System.out.println("@@@@@@@@@@@@-Error Reading Task Database");
                Toast toast = Toast.makeText(getContext(), "Task Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //Empty the data for the system. for a good refresh of the data.
        tasks.clear();
        //SQLGatherObjects.clearArrays();
    }

    public static TaskViewAdapter getAdapter() {
        return taskAdapter;
    }

    //Adds Tasks to the Database
    public static void addLastAddedTask() {
        Task newTaskInstance = tasks.get(tasks.size()-1);

        System.out.println("@@@@@@@-NewTask" + newTaskInstance);

        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", newTaskInstance.getTitle());
        taskValues.put("LATITUDE", newTaskInstance.getLatitude());
        taskValues.put("LONGITUDE", newTaskInstance.getLongitude());
        taskValues.put("ADDRESS", newTaskInstance.getAddress());
        taskValues.put("ENABLE_ADDRESS", newTaskInstance.getEnable_address());
        taskValues.put("ADDRESS_VERIFIED", newTaskInstance.getAddress_verified());
        taskValues.put("DATE", newTaskInstance.getDate());
        taskValues.put("HOUR", newTaskInstance.getHour());
        taskValues.put("MINUTE", newTaskInstance.getMinute());

        //Get a reference to the database and update the FAVORITE column
        try {
            //TaskDatabase = new TaskDatabaseHelper();
            SQLiteDatabase db = taskDatabase.getWritableDatabase();
            db.insert(T_TB_NAME, null, taskValues);
            db.close();

        } catch(SQLiteException e) {
            System.out.println("@@@@@@@@@@@@@ - Issue adding the New Task to Database");
            //Toast toast = Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }
    }

    //Updates Tasks to the Database
    public static void updateTaskDB(Task TaskInstance) {
        System.out.println("@@@@@@@-UpdateTask2" + TaskInstance);

        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", TaskInstance.getTitle());
        taskValues.put("LATITUDE", TaskInstance.getLatitude());
        taskValues.put("LONGITUDE", TaskInstance.getLongitude());
        taskValues.put("ADDRESS", TaskInstance.getAddress());
        taskValues.put("ENABLE_ADDRESS", TaskInstance.getEnable_address());
        taskValues.put("ADDRESS_VERIFIED", TaskInstance.getAddress_verified());
        taskValues.put("DATE", TaskInstance.getDate());
        taskValues.put("HOUR", TaskInstance.getHour());
        taskValues.put("MINUTE", TaskInstance.getMinute());

        //Get a reference to the database and update the FAVORITE column
        try {
            //TaskDatabase = new TaskDatabaseHelper();
            SQLiteDatabase db = taskDatabase.getWritableDatabase();
            db.update(T_TB_NAME, taskValues,"_id = ?",
                    new String[] {Integer.toString(TaskInstance.getId())});
            db.close();

        } catch(SQLiteException e) {
            System.out.println("@@@@@@@@@@@@@ - Issue updating the Task to the Database");
            //Toast toast = Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }
    }

    //Deletes the task.
    public static void deleteTaskDB(Task TaskInstance) {
        System.out.println("@@@@@@@-DeleteTask" + TaskInstance);

        //Get a reference to the database and update the FAVORITE column
        try {
            //TaskDatabase = new TaskDatabaseHelper();
            SQLiteDatabase db = taskDatabase.getWritableDatabase();
            //db.update(TB_NAME, taskValues,"_id = ?", new String[] {Integer.toString(TaskInstance.getId())});
            db.delete(T_TB_NAME, "_id = ?", new String[]{Integer.toString(TaskInstance.getId())});
            db.close();

        } catch(SQLiteException e) {
            System.out.println("@@@@@@@@@@@@@ - Issue updating the Task to the Database");
            //Toast toast = Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }
    }

    public static double[] getCoords(){
        double[] LocationValues = {10.5, -100};
        LocationValues = homeViewModel.LocationValues;
        return LocationValues;
    }

    //Updates Tasks to the Database
    public ArrayList<Task> updateTaskActionDate(ArrayList<Task> tasksUpdate) {
        System.out.println("@@@@@@@-Update Tasks with Dates");

        //Go through the Actions and update each one to the task is assigned with.
        try {
            SQLiteOpenHelper actionDatabase = new ActionDatabaseHelper(getContext());
            SQLiteDatabase db = actionDatabase.getWritableDatabase();
            cursor = db.query (A_TB_NAME,
                    new String[] {
                            "_id",
                            "TASK_ID",
                            "DATE"
                    }, //
                    null, null, null, null, null);

            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    System.out.println("@@@@@@@@@@@@-Reading Action Database Line");
                    int _id = cursor.getInt(0);
                    int t_id = cursor.getInt(1);
                    String date = cursor.getString(2);//.substring(0,cursor.getString(2).indexOf(" "));

                    System.out.println("@@@@@@@@ Date: " + date);
                    tasksUpdate.get(t_id-1).setDateSinceUpdate(date);

                    cursor.moveToNext();
                }
            }

            //Close Database Variables
            cursor.close();
            db.close();
            System.out.println("@@@@@@@@@@@@- Closed Action Database Connection Check - @@@@@@@@@");

        } catch(SQLiteException e) {
            System.out.println("@@@@@@@@@@@@@ - Issue updating the Action days from tasks Database");
        }
        return tasksUpdate;
    }



}