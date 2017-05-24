package com.wrexsoft.canturgut.patide;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
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

    private void GoToHome() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All Events");
        //ab.setTitle("All Events");
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
