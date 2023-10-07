package com.mobilegroup3.lifetaskhelper.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobilegroup3.lifetaskhelper.R;

import java.util.ArrayList;

public class ActionViewAdapter extends ArrayAdapter<Actions> {

    private ArrayList<Actions> action;

    // invoke the suitable constructor of the ArrayAdapter class
    public ActionViewAdapter(@NonNull Context context, ArrayList<Actions> arrayList) {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        this.action = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.action_date_view, parent, false);
        }
        // get the position of the view from the ArrayAdapter
        Actions actionInstance = getItem(position);

        TextView actionDate = currentItemView.findViewById(R.id.textBasic);
        actionDate.setText(actionInstance.getDate());

        // then return the recyclable view
        return currentItemView;
    }


}
