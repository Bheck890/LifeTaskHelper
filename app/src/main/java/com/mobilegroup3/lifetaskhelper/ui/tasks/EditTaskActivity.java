package com.mobilegroup3.lifetaskhelper.ui.tasks;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilegroup3.lifetaskhelper.R;
import com.mobilegroup3.lifetaskhelper.task.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    int taskId;
    private SimpleDateFormat dateFormatter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //Default Task stuff
        Intent intent = getIntent(); // Fetch data that is passed from MainActivity
        EditText editTitle = findViewById(R.id.editTxtTitle);

        // Accessing the data using key and value
        taskId = intent.getIntExtra("taskid",-1);
        Task taskInstance = TasksFragment.tasks.get(taskId-1); //

        System.out.println("@@@@@@@Began Edit on task Instance: " + taskInstance);

        //Set Elements For Time
        //https://www.journaldev.com/9976/android-date-time-picker-dialog
        EditText editTextDate = findViewById(R.id.editTxtDate);
        EditText editTextTime = findViewById(R.id.editTxtTime);
        ImageButton buttonDate = this.findViewById(R.id.editImageButtonDate);
        ImageButton buttonTime = this.findViewById(R.id.editImageButtonTime);
        final int[] hour = {0};
        final int[] min = {0};

        //Set Elements For Location
        EditText editTextLocation = findViewById(R.id.editTxtLocation);
        Button buttonVerify = this.findViewById(R.id.editButtonVerifyAddress);
        CheckBox CheckboxAddressVerify = this.findViewById(R.id.editCheckBoxAddressCheck);
        CheckBox CheckboxEnableAddress = this.findViewById(R.id.editCheckBoxLocation);
        //checkBoxEnableLocation
        CheckboxAddressVerify.setClickable(false);
        final boolean[] location = {false};
        final Address[] Location = {null};
        //editTextLocation.setClickable(false);
        editTextLocation.setEnabled(false);
        buttonVerify.setEnabled(false);

        if (taskId != -1) { //
            editTitle.setText(taskInstance.getTitle());
            editTextDate.setText(taskInstance.getDate());
            editTextLocation.setText(taskInstance.getAddress());
            CheckboxAddressVerify.setChecked(taskInstance.getAddress_verified());
            CheckboxEnableAddress.setChecked(taskInstance.getEnable_address());
            location[0] = taskInstance.getAddress_verified();

            //Set the time if there is time in it.
            if(!((taskInstance.getHour() == 0) && (taskInstance.getMinute() == 0))){
                hour[0] = taskInstance.getHour();
                min[0] = taskInstance.getMinute();
            }
            /*
            if(!((taskInstance.getLatitude() == 0) && (taskInstance.getLongitude() == 0))){
                Location[0].setLatitude(taskInstance.getLatitude());
                Location[0].setLongitude(taskInstance.getLongitude());
            }
             */

            if(taskInstance.getAddress_verified()){
                Location[0] = getLocationFromAddress(taskInstance.getAddress());
            }


        } else {
            finish();
        }

        if(location[0]){
            //Enable Location Reminder Options
            editTextLocation.setEnabled(true);
            buttonVerify.setEnabled(true);
            editTextLocation.setText(taskInstance.getAddress());
            editTextLocation.setHint(R.string.locationBox);

            //Disable Date Reminder Options
            editTextDate.setEnabled(false);
            editTextTime.setEnabled(false);
            buttonDate.setEnabled(false);
            buttonTime.setEnabled(false);
            editTextDate.setText("");
            editTextTime.setText("");
            editTextDate.setHint(R.string.disableMessage);
            editTextTime.setHint(R.string.disableMessage);

        }
        else{
            //Disable Location Reminder Options
            editTextLocation.setEnabled(false);
            buttonVerify.setEnabled(false);
            CheckboxAddressVerify.setChecked(false);
            editTextLocation.setText("");
            editTextLocation.setHint("Disabled");

            //Enable Date Reminder Options
            editTextDate.setEnabled(true);
            editTextTime.setEnabled(true);
            buttonDate.setEnabled(true);
            buttonTime.setEnabled(true);
            editTextDate.setText(taskInstance.getDate());
            if((taskInstance.getHour() == 0) && (taskInstance.getMinute() == 0)){
                editTextTime.setText("");
                System.out.println("@@@@@@@@@@@@@-Empty Time2");
            }
            else{
                //editTextTime.setText(taskInstance.getHour() + ":" + taskInstance.getMinute());
                editTextTime.setText(
                        TimeFormatting(
                            taskInstance.getHour(),
                            taskInstance.getMinute()
                        )
                );
            }
            editTextDate.setHint(R.string.dateBox);
            editTextTime.setHint(R.string.timeBox);
        }


        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //todo
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                editTextDate.setText(dateFormatter.format(newDate.getTime()));
                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                editTextTime.setText(TimeFormatting(hourOfDay,minute));

                                /*

                                if(minute < 10){
                                    String minS = String.format("%02d", minute);
                                    editTextTime.setText(hourOfDay + ":" + minS);
                                    System.out.println("@@@@@@@@@@@@@-Time Under 10");
                                }
                                else{
                                    editTextTime.setText(hourOfDay + ":" + minute);
                                    System.out.println("@@@@@@@@@@@@@-Time Default");
                                }
                                 */

                                hour[0] = hourOfDay;
                                min[0] = minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        CheckboxEnableAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(location[0]){
                    //Disable Location Reminder Options
                    location[0] = false;
                    editTextLocation.setEnabled(false);
                    buttonVerify.setEnabled(false);
                    CheckboxAddressVerify.setChecked(false);
                    editTextLocation.setText("");
                    editTextLocation.setHint("Disabled");

                    //Enable Date Reminder Options
                    editTextDate.setEnabled(true);
                    editTextTime.setEnabled(true);
                    buttonDate.setEnabled(true);
                    buttonTime.setEnabled(true);
                    editTextDate.setText(taskInstance.getDate());
                    editTextTime.setText(
                            TimeFormatting(
                                    taskInstance.getHour(),
                                    taskInstance.getMinute()
                            )
                    );
                    editTextDate.setHint(R.string.dateBox);
                    editTextTime.setHint(R.string.timeBox);

                }
                else{//(!(location[0])){
                    //Enable Location Reminder Options
                    location[0] = true;
                    editTextLocation.setEnabled(true);
                    buttonVerify.setEnabled(true);
                    editTextLocation.setText(taskInstance.getAddress());
                    editTextLocation.setHint(R.string.locationBox);

                    //Disable Date Reminder Options
                    editTextDate.setEnabled(false);
                    editTextTime.setEnabled(false);
                    buttonDate.setEnabled(false);
                    buttonTime.setEnabled(false);
                    editTextDate.setText("");
                    editTextTime.setText("");
                    editTextDate.setHint(R.string.disableMessage);
                    editTextTime.setHint(R.string.disableMessage);
                }
                //editTextLocation.setEnabled(true);
                //buttonVerify.setEnabled(true);
            }
        });


        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckboxAddressVerify.setChecked(false);
                String Address = editTextLocation.getText().toString();

                try {

                    Location[0] = getLocationFromAddress(Address);
                    System.out.println("Latitude: " + Location[0].getLatitude());
                    System.out.println("Longitude: " + Location[0].getLongitude());

                    CheckboxAddressVerify.setChecked(true);

                }catch (Exception e) {
                    System.out.println("Not enough Information please Try again.");

                    Toast toast = Toast.makeText(EditTaskActivity.this,
                            "Not enough address information, please try again.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    //e.printStackTrace();
                }
            }
        });


        Button editButtonSave = this.findViewById(R.id.editbuttonSave);
        editButtonSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(editTitle.getText().toString().isEmpty()){ //Title is Required
                            Toast toast = Toast.makeText(EditTaskActivity.this,
                                    "Title is not set",
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else if (location[0]){ //Exit with Location Set
                            //System.out.println("Location based Notification Set");
                            if(CheckboxAddressVerify.isChecked()){
                                System.out.println("Valid Address Entered");

                                //----------------------------------------------------------------
                                //Variables to put into Database
                                editTitle.getText().toString(); //String Title

                                TasksFragment.tasks.get(taskInstance.getId()-1).updateTask(
                                //SQLGatherObjects.tasks.get(taskInstance.getId()-1).updateTask(
                                        taskInstance.getId(), //TasksFragment.tasks.size(),
                                        editTitle.getText().toString(),
                                        Location[0].getLatitude(),
                                        Location[0].getLongitude(),
                                        editTextLocation.getText().toString(),
                                        CheckboxEnableAddress.isChecked(),
                                        CheckboxAddressVerify.isChecked(),
                                        "",
                                        0,
                                        0
                                );

                                finish();
                                TasksFragment.getAdapter().notifyDataSetChanged(); //changes size not contents.
                                //Update the Task function is Below
                                TasksFragment.updateTaskDB(taskInstance);
                                //TasksFragment.

                                //----------------------------------------------------------------
                            }
                            else{
                                Toast toast = Toast.makeText(EditTaskActivity.this,
                                        "Valid Address not inputted",
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }
                        else{ //Exit with just setting the Title and optional Date

                            if(editTextDate.getText().toString().length() > 1 ||
                                    editTextTime.getText().toString().length() > 1){

                                if(editTextDate.getText().toString().length() >= 10) {
                                    //System.out.println("Valid Date: " + editTextDate.getText().toString());
                                    if(editTextTime.getText().toString().length() >= 4) {
                                        System.out.println("Valid Time: " + editTextTime.getText().toString());

                                        //----------------------------------------------------------------
                                        //Variables to put into Database
                                        editTitle.getText().toString(); //String Title

                                        editTextDate.getText().toString(); //Get Date to set
                                        String Time = hour[0] + ":" + min[0]; //hour and min to put into database

                                        System.out.println("Title: " + editTitle.getText().toString() +
                                                "\nDate: " + editTextDate.getText().toString() +
                                                "\nTime: " + Time
                                        );

                                        TasksFragment.tasks.get(taskInstance.getId()-1).updateTask(
                                        //SQLGatherObjects.tasks.get(taskInstance.getId()-1).updateTask(
                                                taskInstance.getId(),
                                                editTitle.getText().toString(),
                                                0,
                                                0,
                                                "",
                                                false,
                                                false,
                                                editTextDate.getText().toString(),
                                                hour[0],
                                                min[0]
                                        );


                                        //Update the Task function is Below
                                        //TasksFragment.tasks.get(taskId).setTitle(editText.getText().toString());
                                        finish();
                                        TasksFragment.getAdapter().notifyDataSetChanged(); //changes size not contents.
                                        TasksFragment.updateTaskDB(taskInstance);


                                        //----------------------------------------------------------------
                                    }
                                    else{
                                        Toast toast = Toast.makeText(EditTaskActivity.this,
                                                "Time Not Inputted or valid",
                                                Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                                else{
                                    Toast toast = Toast.makeText(EditTaskActivity.this,
                                            "Date Not Inputted or valid",
                                            Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                            else{ //only Title for Task (Manual Task System)

                                //----------------------------------------------------------------
                                //Variables to put into Database
                                editTitle.getText().toString(); //String Title

                                System.out.println("Title: " + editTitle.getText().toString());
                                //Boolean for Date False

                                TasksFragment.tasks.get(taskInstance.getId()-1).updateTask(
                                //SQLGatherObjects.tasks.get(taskInstance.getId()-1).updateTask(
                                        taskInstance.getId(),
                                        editTitle.getText().toString(),
                                        0,
                                        0,
                                        "",
                                        false,
                                        false,
                                        "",
                                        0,
                                        0
                                );


                                //Update the Task function is Below
                                //TasksFragment.tasks.get(taskId).setTitle(editText.getText().toString());
                                finish();
                                TasksFragment.getAdapter().notifyDataSetChanged(); //changes size not contents.
                                TasksFragment.updateTaskDB(taskInstance);


                                //----------------------------------------------------------------


                            } //Title
                        } // if (Location) else (Date Reminder)/else (Title)
                    } // Save Click
                });


        Button editButtonDelete = this.findViewById(R.id.editDeleteBtn);
        editButtonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //taskInstance
                        TasksFragment.tasks.remove(taskInstance.getId()-1);

                        //Update the Task function is Below
                        //TasksFragment.tasks.get(taskId).setTitle(editText.getText().toString());
                        finish();
                        TasksFragment.getAdapter().notifyDataSetChanged(); //changes size not contents.
                        TasksFragment.deleteTaskDB(taskInstance);
                    }
                });
    }

    public Address getLocationFromAddress(String strAddress) {
        //https://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address/3574792#3574792
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            return location;
        } catch (IOException e) {
            System.out.println("Not enough Information please Try again.");
            e.printStackTrace();
        }
        return null;
    }

    //Returns the time with AM PM formatting
    public String TimeFormatting(int hour, int minute){
        String date1 = "";
        if(hour > 12) {
            int hrS = hour;
            if (hour > 12)
                hrS -= 12;
            if(minute < 10){
                String minS = String.format("%02d", minute);
                date1 += hrS + ":" + minS + " PM";
            }
            else{
                date1 += hrS + ":" + minute  + " PM";
            }
        }
        else if ((hour == 0) && (minute == 0)){
            date1 = "";
            System.out.println("@@@@@@@@@@@@@-Empty Time1");
        }
        else if(minute < 10){
            String minS = String.format("%02d", minute); //adds a zero in front of the min number
            date1 += hour + ":" + minS  + " AM";
        }
        else {
            date1 += hour + ":" + minute + " AM";
        }
        return date1;
    }

}
