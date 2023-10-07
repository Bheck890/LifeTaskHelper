package com.mobilegroup3.lifetaskhelper.task;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Task {

    /*
    Need to work on:
    Improvements on the Clients Data and Indicators
    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    Needed for the Group project needed to complete: (Complete Application)
    2. Notification to remind the User of the specified reminder (if there is extra time and make it cool)
     - have the date and time set to remind the user, needs to make an action task as their reminder is activated,
         it just needs a system to notify the user.
         if the date matches the date that they specified
         **(mainly) if their location matches the address that they want to be notified at**
            way to have app running in background checking for dates and location

    ---------------------------------------------------------------
    Project Future things:

    Future Features
    - add a area where you can add info for inputting an action before the default current date.
        in case you did it yesterday but you forgot to track the action
    - if the user changed the task and then press back button, then there should be a notice
        saying the changes are not saved.
    - After Performing a task, if the user wants to add a Date or location to the task,
        have it open the settings after the action is entered, to input the Data to set the reminder.
         - would need to disable the Date and Location options if they do select it,
            since they will change it after the action is preformed.
    - When the task is made have it create an optional first action saying it got done now
        or to mark will be preformed later

    - When the location is in the info area they can click on the location and it will open the address
        in their browser so they can know exactly where they were when they preformed the activity.
            - if the address is invalid it uses the coordinates.
    - When they make an action add a choice to add their current location in the action for later use.


      ### Locations Features Update:
        - pre-set a determined set of Locations and past locations so they can mark a location with a
            known name so they know where the location cords would be. and if they get there again
        - Way to input the local grocery store or something.
        - Autofill address in the text box when entering address.
        - for the location implement Google maps box so they can just use that instead of address,
            (Requires API Access)

    - other features that would make this application helpful and cool to use?

    Visible cosmetics
    - Message informing the user to add a new Task when there are no tasks
        (Have it first have the user start by adding a Task, only on start up of install.)
    - when there are no Actions preformed have it just show a screen with title and inform the user
        to preform a task so there can be some data.
    - if there is no (Description or Location) for the Action have the text on the screen shift
        up, or not be shown if its not needed.
    - Better Color choices?
    - Verify it looks good on a Device in dark mode.


    Possible Future Looks
    - Info page to have a graph of the date specified in month format
        to show how many times a month or week the action may occur
    - Update Reminder button to be Red if the date surpasses the current date,
        as it would be overdue


    Backend Bugs: (need Inspection)
     - When you delete a task have it also delete the Actions that were also for that Task
     - during Development there was an issue of index out of bounds for adding 3 and removing 2
        then the index gets out of wack and needs to be inspected.

     - longitude Database variable does not have the trailing numbers (Inspection)
     - if the user changes the address the valid address is changed to disable
        until it is verified again (bug)


    Solved Bugs?
    - with 3 Tasks (Defaults (location,Date,task)) - after editing task 2,
        the ordering of the tasks change (need further Testing and inspection)
            - (bug update n) [if notice something else with the ordering place note here]
            - (Bug Update 2) it glitches the position id indicator
                to a number and can override other tasks
            - (bug update 3) after editing the task settings from initially the info page
                The issue is also occurring.
            - (Possible solved) by changing size to the instance id instead.
            - Waiting for it to occur again in a while else it is solved.
    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    Background and Future Issues:
    when saved have it in a format for the notification handler to push a notification.
    Date format for Notification Reminder
    Location coordinates way that can identify Location



     */
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private int id = 0;

    // Title of task
    private String Title = "";

    //////////////////////////////
    //Since the task was performed
    private String dateSinceUpdate = "Never Started";

    //Day and time to remind the User (Running in background and remind when time matches)
    private String dateToRemindUser = " ";

    //Coordinates to check if user is close to area.
    private String locationReminder = " ";
    //////////////////////////////

    private double latitude;
    private double longitude;
    private String address;
    private Boolean enable_address;
    private Boolean address_verified;
    private String date;
    private int hour;
    private int minute;


    //Basic
    public Task(int id, String name){
        this.id = id;
        this.Title = name;
    }

    //Set title and Date
    public Task(int id, String name, String update){
        this.id = id;
        this.Title = name;
        //this.dateSinceUpdate = update;
    }

    //All SQL stuff
    public Task(int id,
                String title,
                double latitude,
                double longitude,
                String address,
                Boolean enable_address,
                Boolean address_verified,
                String date,
                int hour,
                int minute){
        this.id = id;
        this.Title = title;
        //this.dateSinceUpdate = update;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.enable_address = enable_address;
        this.address_verified = address_verified;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    //All SQL stuff
    public void updateTask(int id,
                String title,
                double latitude,
                double longitude,
                String address,
                Boolean enable_address,
                Boolean address_verified,
                String date,
                int hour,
                int minute){
        this.id = id;
        this.Title = title;
        //this.dateSinceUpdate = update;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.enable_address = enable_address;
        this.address_verified = address_verified;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    //All SQL stuff with New Action
    public void updateTask(int id,
                           String title,
                           double latitude,
                           double longitude,
                           String address,
                           Boolean enable_address,
                           Boolean address_verified,
                           String date,
                           int hour,
                           int minute,
                           String datePreformed){
        this.id = id;
        this.Title = title;
        //this.dateSinceUpdate = update;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.enable_address = enable_address;
        this.address_verified = address_verified;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.dateSinceUpdate = datePreformed;
    }

    @Override
    public String toString() {

        return "\nID: " + getId() +
                "\nTask: " + getTitle() +
               "\nlatitude: " + getLatitude() +
                "\nlong: " + getLongitude() +
                "\naddress: " + getAddress() +
                "\nenableAddress: " + getEnable_address() +
                "\naddressVerified: " + getAddress_verified() +
                "\ndate: " + getDate() +
                "\nhour: " + getHour() +
                "\nmin: " +getMinute();
    }

    //-------------------------------------------------------------

    public String getTitle() {
        return Title;
    }

    public void setTitle(String name) {
        Title = name;
    }

    public String getDateSinceUpdate() {
        //when have sql data have it calculate days since last update.
        if(dateSinceUpdate.equals("Never Started"))
            return dateSinceUpdate;
        return "Preformed on : " + dateSinceUpdate;
    }

    public void setDateSinceUpdate(String dateSinceUpdate) {
        this.dateSinceUpdate = dateSinceUpdate;
    }


    public int getId() {
        return id;
    }

    public String getDateToRemindUser() {
        return dateToRemindUser;
    }

    public void setDateToRemindUser(String dateToRemindUser) {
        //Get Date;
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        final Calendar newDate = Calendar.getInstance();
        final String date = dateFormatter.format(newDate.getTime()); // = is the date

        this.dateToRemindUser = dateToRemindUser;
    }

    //The Reminder Notification is going to be in the View Model
    // might need a Location Task Identifier to identify which notification has been triggered
    public String getLocationReminder() {
        return locationReminder;
    }

    public void setLocationReminder(String locationReminder) {
        this.locationReminder = locationReminder;
    }

    //-------------------------------------------------------------

    //Location Values that determine the values that the user should be notified.
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getEnable_address() {
        return enable_address;
    }

    public void setEnable_address(Boolean enable_address) {
        this.enable_address = enable_address;
    }

    public Boolean getAddress_verified() {
        return address_verified;
    }

    public void setAddress_verified(Boolean address_verified) {
        this.address_verified = address_verified;
    }

    //Date to Set to Remind the user from.
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public int getReminderColor(){
        if(getEnable_address()) //Location
            return (Color.GREEN);
        else if(!getDate().isEmpty()) //Date
            return (Color.BLUE);
        else // No Reminder
            return (Color.WHITE); //(View.INVISIBLE);
    }
}
