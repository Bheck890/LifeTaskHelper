package com.mobilegroup3.lifetaskhelper.task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TaskListView extends ListView {

    //Used in the XML file that Determines the list view system

    //add these three constructors
    public TaskListView(Context context){
        super(context);
    }

    public TaskListView(Context context , AttributeSet attrs){
        super(context , attrs);
    }

    public TaskListView(Context context , AttributeSet attrs, int defStyleAttr){
        super(context , attrs, defStyleAttr);
    }


    //handle the item click
    @Override
    public boolean performItemClick(View view , int position , long id){

        if(!view.isEnabled()){
            //don't handle the click
            return false;
        }else{
            //handle the click
            System.out.println("@@@@@@@@@@@@@ TASK-LIST-VIEW @@@@@@@@@@Position: " + position);
            return super.performItemClick(view, position, id);
        }

    }



}
