package com.wrexsoft.canturgut.patide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainMenuActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    AllEventsFragment allEventsFragment = new AllEventsFragment();
                    ft.replace(R.id.main_frame, allEventsFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    NewEventFragment newEventFragment = new NewEventFragment();
                    ft.replace(R.id.main_frame, newEventFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_notifications:
                    UserFragment userFragment = new UserFragment();
                    ft.replace(R.id.main_frame, userFragment);
                    ft.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
