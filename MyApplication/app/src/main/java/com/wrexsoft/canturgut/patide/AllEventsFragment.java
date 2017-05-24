package com.wrexsoft.canturgut.patide;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllEventsFragment extends Fragment {

    View view;

    private ListView listViewEvents;
    protected ArrayList<String> listEventIds = new ArrayList<>();

    ArrayList<HashMap<String, String>> listOfEvents = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapterListEvents;

    AVLoadingIndicatorView avi;
    ArrayAdapter<String> adapter;
    String fbuserId;
    DatabaseReference dref;
    SharedPreferences settings;

    HashMap<String, String> holder;


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

        listViewEvents = (ListView) view.findViewById(R.id.listViewAllEvents);


        dref = FirebaseDatabase.getInstance().getReference();
        EventsLoader friendsLoader = new EventsLoader(this);
        friendsLoader.execute();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapterListEvents = new SimpleAdapter(getActivity(),
                listOfEvents,
                R.layout.list_view,
                new String[]{"Content", "Time"},
                new int[]{R.id.content, R.id.time});


        listViewEvents.setAdapter(adapterListEvents);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //lvAllUsers.setEnabled(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //lvAllUsers.setEnabled(false);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public class EventsLoader extends AsyncTask<Void, Void, Void> {

        AllEventsFragment allEventsFragment;

        public EventsLoader(AllEventsFragment allEventsFragment) {
            this.allEventsFragment = allEventsFragment;
        }

        @Override
        protected void onPreExecute() {
            startAnim();
            allEventsFragment.listViewEvents.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (checkConnection(getActivity().getApplicationContext())) {
                    dref.child("Users").child(fbuserId).child("Events").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            listEventIds.add(dataSnapshot.getKey());
                            MainMenuActivity.mydb.insertData(dataSnapshot.getKey().toString(), dataSnapshot.child("comments").getValue().toString(), dataSnapshot.child("date").getValue().toString(), dataSnapshot.child("estimatedtime").getValue().toString(), dataSnapshot.child("eventname").getValue().toString(), dataSnapshot.child("priority").getValue().toString());
                            adapterListEvents.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            adapterListEvents.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            MainMenuActivity.mydb.removeEvent(dataSnapshot.getKey().toString());
                            adapterListEvents.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            adapterListEvents.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            adapterListEvents.notifyDataSetChanged();
                        }
                    });
                }

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

    public void ListUpdate() {
        Cursor listCursor = MainMenuActivity.mydb.getSQLiteData();
        int numOfEvent = listCursor.getCount();
        if (numOfEvent == 0) {
            Log.d("databaseInsert", "Cursor Null");
        } else {
            StringBuffer buffer = new StringBuffer();
            while (listCursor.moveToNext()) {
                listEventIds.add(listCursor.getString(1));
                holder = new HashMap<String, String>();
                holder.put("Content", listCursor.getString(5));
                holder.put("Time", listCursor.getString(3));
                listOfEvents.add(holder);
                adapterListEvents.notifyDataSetChanged();
            }
        }
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListUpdate();
                stopAnim();
                listViewEvents.setVisibility(View.VISIBLE);
            }
        }, 2500);
    }

    private void startAnim() {
        avi.show();
    }


    private void stopAnim() {
        avi.smoothToHide();
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                return true;
            }
        }
        return false;
    }


}
