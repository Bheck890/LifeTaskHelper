package com.mobilegroup3.lifetaskhelper.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobilegroup3.lifetaskhelper.R;
import com.mobilegroup3.lifetaskhelper.ui.tasks.ActionRecordTaskActivity;
import com.mobilegroup3.lifetaskhelper.ui.tasks.EditTaskActivity;
import com.mobilegroup3.lifetaskhelper.ui.tasks.TaskInformationActivity;

import java.util.ArrayList;

public class TaskViewAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> tasks;

    // invoke the suitable constructor of the ArrayAdapter class
    public TaskViewAdapter(@NonNull Context context, ArrayList<Task> arrayList) {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        this.tasks = arrayList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Task taskInstance = getItem(position);

        TextView taskTitle = currentItemView.findViewById(R.id.titleTxt);
        taskTitle.setText(taskInstance.getTitle());

        TextView taskLastDate = currentItemView.findViewById(R.id.sincePreformedTxt);
        taskLastDate.setText(taskInstance.getDateSinceUpdate());

        Button buttonPreformed = currentItemView.findViewById(R.id.taskDoneBtn);
        buttonPreformed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //NewTaskFragment NewTask = (NewTaskFragment) getSupportFragmentManager().findFragmentById(R.id.New
                        Intent intent = new Intent(getContext(), ActionRecordTaskActivity.class);
                        intent.putExtra("taskid", taskInstance.getId());
                        getContext().startActivity(intent);
                    }
                }
        );

        Button buttonInfo = currentItemView.findViewById(R.id.taskInfoBtn);
        buttonInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //NewTaskFragment NewTask = (NewTaskFragment) getSupportFragmentManager().findFragmentById(R.id.New
                        Intent intent = new Intent(getContext(), TaskInformationActivity.class);
                        intent.putExtra("taskid", taskInstance.getId());
                        getContext().startActivity(intent);
                    }
                }
        );

        Button buttonReminder = currentItemView.findViewById(R.id.reminderIcon);
        buttonReminder.setBackgroundColor(taskInstance.getReminderColor());

        Button buttonSettings = currentItemView.findViewById(R.id.taskSettingsBtn);
        buttonSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //NewTaskFragment NewTask = (NewTaskFragment) getSupportFragmentManager().findFragmentById(R.id.New
                        Intent intent = new Intent(getContext(), EditTaskActivity.class);
                        intent.putExtra("taskid", taskInstance.getId());
                        getContext().startActivity(intent);
                    }
                }
        );

        // then return the recyclable view
        return currentItemView;
    }


}
