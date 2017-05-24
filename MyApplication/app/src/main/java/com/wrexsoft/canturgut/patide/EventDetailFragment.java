package com.wrexsoft.canturgut.patide;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    TextView eventName;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_event_detail, container, false);

        eventName = (TextView) view.findViewById(R.id.eventName);

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
                eventName.setText(dataSnapshot.child("eventname").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
