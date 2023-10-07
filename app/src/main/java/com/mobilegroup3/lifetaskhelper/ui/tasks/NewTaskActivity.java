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

public class NewTaskActivity extends AppCompatActivity {

    int taskId;
    private SimpleDateFormat dateFormatter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        EditText editTextTitle = findViewById(R.id.edtxtTitle);

        // Fetch data that is passed from MainActivity
        Intent intent = getIntent();

        // Accessing the data using key and value
        taskId = intent.getIntExtra("taskid",-1);
        if (taskId != -1) {
            editTextTitle.setText(TasksFragment.tasks.get(taskId).getTitle());
        } else {
            editTextTitle.setText("");
        }

        //Set Elements For Time
        //https://www.journaldev.com/9976/android-date-time-picker-dialog
        EditText editTextDate = findViewById(R.id.edtxtDate);
        ImageButton buttonDate = this.findViewById(R.id.imageButtonDate);
        EditText editTextTime = findViewById(R.id.edtxtTime);
        ImageButton buttonTime = this.findViewById(R.id.imageButtonTime);
        final int[] hour = {0};
        final int[] min = {0};

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                editTextTime.setText(TimeFormatting(hourOfDay,minute));
                                hour[0] = hourOfDay;
                                min[0] = minute;

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        //Set Elements For Location
        EditText editTextLocation = findViewById(R.id.edtxtLocation);
        Button buttonVerify = this.findViewById(R.id.buttonVerifyAddress);
        CheckBox CheckboxAddressVerify = this.findViewById(R.id.checkBoxAddressCheck);
        CheckBox CheckboxEnableAddress = this.findViewById(R.id.checkBoxEnableLocation);

        //checkBoxEnableLocation
        CheckboxAddressVerify.setClickable(false);
        final boolean[] location = {false};
        final Address[] Location = {null};

        //editTextLocation.setClickable(false);
        editTextLocation.setEnabled(false);
        buttonVerify.setEnabled(false);

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
                    editTextDate.setHint(R.string.dateBox);
                    editTextTime.setHint(R.string.timeBox);
                }
                else{//(!(location[0])){
                    //Enable Location Reminder Options
                    location[0] = true;
                    editTextLocation.setEnabled(true);
                    buttonVerify.setEnabled(true);
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

                    Toast toast = Toast.makeText(NewTaskActivity.this,
                            "Not enough address information, please try again.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    //e.printStackTrace();
                }
            }
        });

        //Save Button
        Button buttonSave = this.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(editTextTitle.getText().toString().isEmpty()){ //Title is Required
                            Toast toast = Toast.makeText(NewTaskActivity.this,
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
                                editTextTitle.getText().toString(); //String Title

                                TasksFragment.tasks.add(new Task(
                                        TasksFragment.tasks.size()+1,
                                        editTextTitle.getText().toString(),
                                        Location[0].getLatitude(),
                                        Location[0].getLongitude(),
                                        editTextLocation.getText().toString(),
                                        CheckboxEnableAddress.isChecked(),
                                        CheckboxAddressVerify.isChecked(),
                                        "",
                                        0,
                                        0)
                                );

                                finish();
                                TasksFragment.getAdapter().notifyDataSetChanged();
                                //Update the Task function is Below
                                TasksFragment.addLastAddedTask();
                                //----------------------------------------------------------------
                            }
                            else{
                                Toast toast = Toast.makeText(NewTaskActivity.this,
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
                                        editTextTitle.getText().toString(); //String Title

                                        editTextDate.getText().toString(); //Get Date to set
                                        String Time = hour[0] + ":" + min[0]; //hour and min to put into database

                                        System.out.println("Title: " + editTextTitle.getText().toString() +
                                                "\nDate: " + editTextDate.getText().toString() +
                                                "\nTime: " + Time
                                        );
                                        //Boolean for Date True

                                        TasksFragment.tasks.add(new Task(
                                                TasksFragment.tasks.size()+1,
                                                editTextTitle.getText().toString(),
                                                0,
                                                0,
                                                "",
                                                false,
                                                false,
                                                editTextDate.getText().toString(),
                                                hour[0],
                                                min[0])
                                        );

                                        //Update the Task function is Below
                                        //TasksFragment.tasks.get(taskId).setTitle(editText.getText().toString());
                                        finish();
                                        TasksFragment.getAdapter().notifyDataSetChanged();
                                        TasksFragment.addLastAddedTask();
                                        //----------------------------------------------------------------
                                    }
                                    else{
                                        Toast toast = Toast.makeText(NewTaskActivity.this,
                                                "Time Not Inputted or valid",
                                                Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                                else{
                                    Toast toast = Toast.makeText(NewTaskActivity.this,
                                            "Date Not Inputted or valid",
                                            Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                            else{ //only Title for Task (Manual Task System)

                                //----------------------------------------------------------------
                                //Variables to put into Database
                                editTextTitle.getText().toString(); //String Title

                                System.out.println("Title: " + editTextTitle.getText().toString());
                                //Boolean for Date False

                                TasksFragment.tasks.add(new Task(
                                        TasksFragment.tasks.size()+1,
                                        editTextTitle.getText().toString(),
                                        0,
                                        0,
                                        "",
                                        false,
                                        false,
                                        "",
                                        0,
                                        0)
                                );

                                //Update the Task function is Below
                                //TasksFragment.tasks.get(taskId).setTitle(editText.getText().toString());
                                finish();
                                TasksFragment.getAdapter().notifyDataSetChanged();
                                TasksFragment.addLastAddedTask();
                                //----------------------------------------------------------------


                            } //Title
                        } // if (Location) else (Date Reminder)/else (Title)
                    } // Save Click
                });
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
}