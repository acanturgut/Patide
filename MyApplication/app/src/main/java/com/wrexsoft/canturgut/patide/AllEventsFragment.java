package com.wrexsoft.canturgut.patide;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEventsFragment extends Fragment {

    View view;

    private ListView listViewFriends;
    protected ArrayList<String> listEvents = new ArrayList<>();
    protected ArrayList<String> listEventIds = new ArrayList<>();
    AVLoadingIndicatorView avi;
    ArrayAdapter<String> adapter;
    String fbuserId;
    DatabaseReference dref;
    SharedPreferences settings;

    public AllEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_events, container, false);

        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fbuserId = settings.getString("FbUserId", "userId");

        listViewFriends = (ListView) view.findViewById(R.id.listViewAllEvents);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listEvents);
        listViewFriends.setAdapter(adapter);

        dref = FirebaseDatabase.getInstance().getReference();
        EventsLoader friendsLoader = new EventsLoader(this);
        friendsLoader.execute();
        return view;
    }

    public class EventsLoader extends AsyncTask<Void, Void, Void> {

        AllEventsFragment allEventsFragment;

        public EventsLoader(AllEventsFragment allEventsFragment) {
            this.allEventsFragment = allEventsFragment;
        }

        @Override
        protected void onPreExecute() {
            startAnim();
            allEventsFragment.listViewFriends.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                dref.child("Users").child(fbuserId).child("Events").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        listEventIds.add(dataSnapshot.getKey());
                        listEvents.add(dataSnapshot.child("eventname").getValue().toString());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {

                Log.e("Errorr::", "e.toString()");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            runTimer();

        }
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnim();
                listViewFriends.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void startAnim() {
        avi.show();
    }


    private void stopAnim() {
        avi.smoothToHide();
    }
}
