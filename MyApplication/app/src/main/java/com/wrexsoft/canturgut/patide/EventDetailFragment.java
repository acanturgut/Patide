package com.wrexsoft.canturgut.patide;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    View view;
    String eventId;
    String fbuserId;
    DatabaseReference dref;
    SharedPreferences settings;

    EditText mEventName;
    EditText mEstimatedTime;
    EditText mComments;
    Button ChooseDateButton;
    Button AddEventButton;
    TextView tvChooseDate;
    SeekBar mPriority;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_event_detail, container, false);

        mEventName = (EditText) view.findViewById(R.id.mEventName);
        mEstimatedTime = (EditText) view.findViewById(R.id.mEstimatedTime);
        mComments = (EditText) view.findViewById(R.id.mComments);

        ChooseDateButton = (Button) view.findViewById(R.id.chooseDateBtn);
/*        ChooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });*/
        mPriority = (SeekBar) view.findViewById(R.id.choosePrioritySB);
        AddEventButton = (Button) view.findViewById(R.id.editEventButton);

        final Bundle bundle = this.getArguments();

        if (bundle != null) {
            eventId = bundle.getString("eventId", "0");
        }

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fbuserId = settings.getString("FbUserId", "userId");

        dref = FirebaseDatabase.getInstance().getReference();
        dref.child("Users").child(fbuserId).child("Events").child(eventId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mEventName.setText(dataSnapshot.child("eventname").getValue().toString());
                mEstimatedTime.setText(dataSnapshot.child("estimatedtime").getValue().toString());
                mComments.setText(dataSnapshot.child("comments").getValue().toString());
                ChooseDateButton.setText(dataSnapshot.child("date").getValue().toString());
                mPriority.setProgress(Integer.parseInt(dataSnapshot.child("eventname").getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
