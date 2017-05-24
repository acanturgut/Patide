package com.wrexsoft.canturgut.patide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    View view;
    String eventId;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_event_detail, container, false);

        final Bundle bundle = this.getArguments();

        if (bundle != null) {
            eventId = bundle.getString("eventId", "0");
        }

        return view;
    }

}
