package com.wrexsoft.canturgut.patide;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    View view;

    TextView mUserName;
    Button mEdit;
    EditText mEmail;
    EditText mPassword;
    EditText mLeisure;
    EditText mWork;
    EditText mStudy;
    Button mSignout;

    Boolean editable;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    String fbuserId;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    DatabaseReference dref;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);

        settings = PreferenceManager.getDefaultSharedPreferences(getContext());

        mUserName = (TextView) view.findViewById(R.id.username);
        mUserName.setText(settings.getString("name", "name") + " " + (settings.getString("lastname", "lastname")));
        mEmail = (EditText) view.findViewById(R.id.etEmail);
        //mEmail.setText("" + settings.getString("email", "email"));
        mEmail.setTag(mEmail.getKeyListener());
        mEmail.setKeyListener(null);

        mPassword = (EditText) view.findViewById(R.id.etPassword);
        mPassword.setTag(mPassword.getKeyListener());
        mPassword.setKeyListener(null);

        mPassword.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);

        mLeisure = (EditText) view.findViewById(R.id.etLeisure);
        mLeisure.setText("" + settings.getString("leisure", "leisure"));
        mLeisure.setTag(mLeisure.getKeyListener());
        mLeisure.setKeyListener(null);

        mWork = (EditText) view.findViewById(R.id.etWork);
        mWork.setText("" + settings.getString("work", "work"));
        mWork.setTag(mWork.getKeyListener());
        mWork.setKeyListener(null);

        mStudy = (EditText) view.findViewById(R.id.etStudy);
        mStudy.setText("" + settings.getString("study", "study"));
        mStudy.setTag(mStudy.getKeyListener());
        mStudy.setKeyListener(null);

        dref = FirebaseDatabase.getInstance().getReference();


        //fbuserId = settings.getString("FbUserId", "userId");

        editable = false;
        mEdit = (Button) view.findViewById(R.id.edit_button);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editable) {
                    editable = true;
                    mPassword.setVisibility(View.VISIBLE);
                    mEmail.setVisibility(View.VISIBLE);
                    mEmail.setKeyListener((KeyListener) mEmail.getTag());
                    mPassword.setKeyListener((KeyListener) mPassword.getTag());
                    mLeisure.setKeyListener((KeyListener) mLeisure.getTag());
                    mWork.setKeyListener((KeyListener) mWork.getTag());
                    mStudy.setKeyListener((KeyListener) mStudy.getTag());
                    mEdit.setText("Done");

                } else {
                    editable = false;
                    mPassword.setVisibility(View.INVISIBLE);
                    mEmail.setVisibility(View.INVISIBLE);
                    mEmail.setKeyListener(null);
                    mPassword.setKeyListener(null);
                    mLeisure.setKeyListener(null);
                    mWork.setKeyListener(null);
                    mStudy.setKeyListener(null);
                    mEdit.setText("Edit");

                    String leisure = mLeisure.getText().toString();
                    String work = mWork.getText().toString();
                    String study = mStudy.getText().toString();

                    dref.child("Users").child(fbuserId).child("Daily").child("leisure").setValue(leisure);
                    dref.child("Users").child(fbuserId).child("Daily").child("work").setValue(work);
                    dref.child("Users").child(fbuserId).child("Daily").child("study").setValue(study);

                    editor = settings.edit();
                    editor.putString("leisure", leisure);
                    editor.putString("work", work);
                    editor.putString("study", study);
                    editor.apply();

                }
            }
        });

        mSignout = (Button) view.findViewById(R.id.signout_button);
        mSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                editor = settings.edit();
                editor.clear();
                editor.apply();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            public static final String TAG = "Firebase Auth";

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    fbuserId = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + fbuserId);
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
