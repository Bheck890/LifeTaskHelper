package com.mobilegroup3.lifetaskhelper.task;

public class Actions {

    //Action ID
    private int actionID = 0;

    //Task ID
    private int taskID = 0;

    //Note From the task Preformed
    private String actionNote = "";

    //Date when user Preformed Action
    private final String date;

    //Data where the user was when Preformed
    private String Location;

    //Input Data from Database
    public Actions(int actionID,
                   int taskID,
                   String description,
                   String date,
                   String location
                   ) {
        this.actionID = actionID;
        this.taskID = taskID;
        this.actionNote = description;
        this.date = date;
        this.Location = location;
    }

    //Set Description and Date (Default New Task)
    public Actions( int taskID,
                   String description,
                   String date,
                   int hour,
                   int minute)
    {
        setActionNote(description);
        this.taskID = taskID;
        this.actionNote = getActionNote();
        this.date = dateAndTimeFormatting(date,hour,minute);
        this.Location = "N/A"; //Empty Location Have it not appear later.
    }

    //Set Action with Defaults and the Location Data (New task Created with location)
    public Actions(int taskID,
                   String description,
                   String date,
                   int hour,
                   int minute,
                   double latitude,
                   double longitude,
                   String address)
    {
        setActionNote(description);
        this.taskID = taskID;
        this.actionNote = getActionNote();
        this.date = dateAndTimeFormatting(date,hour,minute);
        this.Location = address + "\nLatitude: " + latitude + "\nLongitude: " + longitude;
    }

    @Override
    public String toString() { //List data that shows the day the task was done
        return "date: " + getDate();
    }

    public String ActionString() { //Debug to output the data of the object
        return "\nAction_ID: " + getActionID() +
                "\nTask_ID: " + getTaskID() +
                "\nactionNote: " + getActionNote() +
                "\ndate: " + getDate() +
                "\nLocation: " + getLocation();
    }

    //Returns the time with AM PM formatting for New Objects
    public String dateAndTimeFormatting(String date, int hour, int minute){
        String date1 = date;
        if(hour > 12) {
            int hrS = hour;
            if (hour > 12)
                hrS -= 12;
            if(minute < 10){
                String minS = String.format("%02d", minute);
                date1 += " - " + hrS + ":" + minS + " PM";
            }
            else{
                date1 += " - " + hrS + ":" + minute  + " PM";
            }
        }
        else if(minute < 10){
            String minS = String.format("%02d", minute); //adds a zero in front of the min number
            date1 += " - " + hour + ":" + minS  + " AM";
        }
        else {
            date1 += " - " + hour + ":" + minute + " AM";
        }
        return date1;
    }


    public int getTaskID() {
        return taskID;
    }

    public int getActionID() {
        return actionID;
    }

    public String getActionNote() {
        return actionNote;
    }

    public void setActionNote(String actionNote) {
        if (actionNote.isEmpty())
            this.actionNote = "No Description Set";
        else
            this.actionNote = actionNote;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return Location;
    }



}
