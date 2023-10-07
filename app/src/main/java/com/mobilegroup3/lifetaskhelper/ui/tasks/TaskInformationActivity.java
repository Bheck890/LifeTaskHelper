package com.mobilegroup3.lifetaskhelper.ui.tasks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilegroup3.lifetaskhelper.R;
import com.mobilegroup3.lifetaskhelper.SQL.ActionDatabaseHelper;
import com.mobilegroup3.lifetaskhelper.task.ActionViewAdapter;
import com.mobilegroup3.lifetaskhelper.task.Actions;
import com.mobilegroup3.lifetaskhelper.task.Task;

import java.util.ArrayList;

public class TaskInformationActivity extends AppCompatActivity {

    // Required View Task Information
    int taskId;
    int actionId = 0;

    //Action Database Variables.
    public static ArrayList<Actions> action = new ArrayList<>();
    private static SQLiteOpenHelper actionDatabase;
    public static final String A_TB_NAME = ActionDatabaseHelper.TB_NAME; // the name of our Table

    //Database Systems that manipulate the Data.
    private SQLiteDatabase action_db;
    private Cursor DB_cursor;
    private ActionViewAdapter listAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);

        //Default Task stuff
        Intent intent = getIntent(); // Fetch data that is passed from MainActivity
        TextView infoTextViewTitle = findViewById(R.id.infoTxtTitle);
        ListView pastActivityList = findViewById(R.id.infoListViewTaskHistory);

        //Labels
        TextView infoTextDescription = findViewById(R.id.infoTxtViewDescription);
        TextView infoTextLocation = findViewById(R.id.infoTextViewLocation);

        //Text that changes
        TextView infoTextViewDescription = findViewById(R.id.infoTxtDescription);
        TextView infoTextViewLocation = findViewById(R.id.infoTxtLocation);

        // Accessing the data using key and value
        taskId = intent.getIntExtra("taskid",-1);
        Task taskInstance = TasksFragment.tasks.get(taskId-1); //SQLGatherObjects
        System.out.println("@@@@@@@ Begin Information Instance, For Task: \n" + taskInstance);

        if (taskId != -1) { //
            infoTextViewTitle.setText(taskInstance.getTitle());
        }

        //On load Description and Location is Hidden
        if(actionId == 0){
            //Description is not seen
            infoTextDescription.setVisibility(View.INVISIBLE);
            infoTextViewDescription.setVisibility(View.INVISIBLE);
            //Location is not seen
            infoTextLocation.setVisibility(View.INVISIBLE);
            infoTextViewLocation.setVisibility(View.INVISIBLE);
        }


        //Need to gather Latest Actions that were preformed to update the task system
        if(action.isEmpty()) {

            actionDatabase = new ActionDatabaseHelper(TaskInformationActivity.this); //creates instance of actions Database

            try {
                System.out.println("@@@@@@@@@@@@-Initiate Action Database");
                //Gathers Database
                action_db = actionDatabase.getReadableDatabase();
                //Selects all the data from the Database
                DB_cursor = action_db.query (A_TB_NAME,
                        new String[] {
                                "_id",
                                "TASK_ID",
                                "DESCRIPTION",
                                "DATE",
                                "TIME",
                                "LOCATION"},
                        null, null, null, null, null);

                //Move to the first record in the Cursor to Read the list of Actions
                if (DB_cursor.moveToFirst()) {
                    int check = 0;
                    while (!DB_cursor.isAfterLast()) {

                        //Only Add to this List View if it is the same as the Task ID
                        int task_ID = DB_cursor.getInt(1);

                        /*
                        //Debug note to see the Action check that are Imported.
                        System.out.println("@@@@@@@@@@@@- Checking " + check
                                + " (Action ID): " + task_ID + " = "
                                + " = (Selected Task ID): " + taskId);
                         */

                        if(task_ID == taskId) {
                            System.out.println("@@@@@@@@@@@@-Reading Action Database Line");
                            int _id = DB_cursor.getInt(0);
                            //int task_ID = action_cursor.getInt(1);
                            String description = DB_cursor.getString(2);
                            String date = DB_cursor.getString(3);
                            String time = DB_cursor.getString(4);
                            String location = DB_cursor.getString(5);

                            action.add(new Actions(
                                    _id,
                                    task_ID,
                                    description,
                                    date,
                                    location
                            ));
                        }
                        DB_cursor.moveToNext();
                        check++;
                    }
                }

                listAdapter = new ActionViewAdapter(TaskInformationActivity.this, action);
                pastActivityList.setAdapter(listAdapter);

                //Debug to see what Actions are being Identified
                /*
                for (Actions Act: action) {
                    System.out.println("@@@@@@@@@@@@- Action\n" + Act.ActionString());
                }
                */

                action_db.close();
                System.out.println("@@@@@@@@@@@@- Closed Actions Database Connection - @@@@@@@@@");

            } catch (SQLiteException e) {
                System.out.println("@@@@@@@@@@@@-Error Reading Action Database");
                Toast toast = Toast.makeText(TaskInformationActivity.this,
                        "Action Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        }

        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Description is now Visible
                        infoTextDescription.setVisibility(View.VISIBLE);
                        infoTextViewDescription.setVisibility(View.VISIBLE);

                        System.out.println("@@ Location:" + action.get(position).getLocation());
                        //Hide if there is no Location Set
                        if(action.get(position).getLocation().equals("N/A")){
                            System.out.println("@@@ - Location Visible");
                            //Location is now Visible
                            infoTextLocation.setVisibility(View.INVISIBLE);
                            infoTextViewLocation.setVisibility(View.INVISIBLE);
                        }
                        else{
                            infoTextLocation.setVisibility(View.VISIBLE);
                            infoTextViewLocation.setVisibility(View.VISIBLE);
                        }

                        infoTextViewDescription.setText(action.get(position).getActionNote());
                        infoTextViewLocation.setText(action.get(position).getLocation());
                    }
                };
        pastActivityList.setOnItemClickListener(itemClickListener);

        Button buttonSettings = findViewById(R.id.infoBtnSettings);
        buttonSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //NewTaskFragment NewTask = (NewTaskFragment) getSupportFragmentManager().findFragmentById(R.id.New
                        Intent intent = new Intent(TaskInformationActivity.this, EditTaskActivity.class);
                        intent.putExtra("taskid", taskInstance.getId());
                        TaskInformationActivity.this.startActivity(intent);
                        finish();
                    }
                }
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("@@@@@@@@@@@- Information Activity Destroyed");
        finish();
        // actions need to be destroyed to call Database again when it is called
        // for a different Task Instance
        action.clear();
        //listAdapter = ;

        //when the view is closed the viewer of the Database is closed, as it is still used in the
        // List view for some strange reason?
        DB_cursor.close();
        return;
    }

}