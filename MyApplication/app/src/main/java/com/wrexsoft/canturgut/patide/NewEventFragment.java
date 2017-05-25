package com.wrexsoft.canturgut.patide;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends Fragment {

    View view;
    EditText mEventName;
    EditText mEstimatedTime;
    EditText mComments;
    Button ChooseDateButton;
    Button AddEventButton;
    TextView tvChooseDate;
    SeekBar mPriority;

    DatabaseReference dref;

    String eventNameString;
    String estimatedTimeString;
    String commentsString;
    String dateString;
    String priorityString;

    SharedPreferences settings;
    String userID;


    public NewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_event, container, false);

        dref = FirebaseDatabase.getInstance().getReference();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        userID = settings.getString("FbUserId", "userID");

        mEventName = (EditText) view.findViewById(R.id.mEventName);
        mEstimatedTime = (EditText) view.findViewById(R.id.mEstimatedTime);
        mComments = (EditText) view.findViewById(R.id.mComments);

        ChooseDateButton = (Button) view.findViewById(R.id.chooseDateBtn);
        ChooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        mPriority = (SeekBar) view.findViewById(R.id.choosePrioritySB);
        AddEventButton = (Button) view.findViewById(R.id.addEventButton);

        AddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewEvent();
                GoToHome();
            }
        });
        return view;
    }

    private void showDatePicker() {
        final Dialog datepicker = new Dialog(getContext());

        datepicker.setTitle("Pick a Date");

        datepicker.setContentView(R.layout.dialog_date);

        final DatePicker dPicker = (DatePicker) datepicker.findViewById(R.id.datePicker);
        dPicker.setMinDate(System.currentTimeMillis() - 1000);
        Button okBtn = (Button) datepicker.findViewById(R.id.btnDate);

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String day = "";
                if (dPicker.getDayOfMonth() < 10) {
                    day = "0";
                }
                String month = "";
                if (dPicker.getMonth() < 10) {
                    month = "0";
                }
                int  monthInt = dPicker.getMonth() + 1;
                ChooseDateButton.setText(day + dPicker.getDayOfMonth() + "/" + month + Integer.toString(monthInt)+ "/" + dPicker.getYear());
                showTimePicker();
                datepicker.dismiss();

            }
        });

        datepicker.show();

    }

    private void showTimePicker() {

        final Dialog timepicker = new Dialog(getContext());

        timepicker.setTitle("Pick a Time");

        timepicker.setContentView(R.layout.dialog_time);

        final TimePicker tPicker = (TimePicker) timepicker.findViewById(R.id.timePicker);
        Button backBtn = (Button) timepicker.findViewById(R.id.back2date);
        Button okBtn = (Button) timepicker.findViewById(R.id.timeOK);

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String date = "" + ChooseDateButton.getText();

                String hr = "";
                if (tPicker.getHour() < 10) {
                    hr = "0";
                }
                String min = "";
                if (tPicker.getMinute() < 10) {
                    min = "0";
                }
                ChooseDateButton.setText(date + " " + hr + tPicker.getHour() + ":" + min + tPicker.getMinute());
                timepicker.dismiss();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                timepicker.dismiss();
                showDatePicker();

            }
        });

        timepicker.show();
    }

    private void GoToHome() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AllEventsFragment allEventsFragment = new AllEventsFragment();
        ft.replace(R.id.main_frame, allEventsFragment);
        ft.commit();
    }

    private void CreateNewEvent() {

        eventNameString = mEventName.getText().toString();
        estimatedTimeString = mEstimatedTime.getText().toString();
        commentsString = mComments.getText().toString();
        dateString = ChooseDateButton.getText().toString();
        priorityString = Integer.toString(mPriority.getProgress());

        HashMap<String, Object> eventDetails = new HashMap<>();
        eventDetails.put("eventname", eventNameString);
        eventDetails.put("estimatedtime", estimatedTimeString);
        eventDetails.put("comments", commentsString);
        eventDetails.put("date", dateString);
        eventDetails.put("priority", priorityString);

        dref.child("Users").child(userID).child("Events").push().setValue(eventDetails);

        Toast.makeText(getContext(), "Your New Event is Created!", Toast.LENGTH_SHORT).show();
    }

}
