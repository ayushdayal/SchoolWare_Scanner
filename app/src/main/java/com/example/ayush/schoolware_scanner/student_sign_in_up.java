package com.example.ayush.schoolware_scanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class student_sign_in_up extends AppCompatActivity {
    FirebaseDatabase database_stndt;
    String selected_school_stndt;
    String selected_state_stndt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_sign_in);
        database_stndt = FirebaseDatabase.getInstance();
    }

    public void forgotpswrd_student(View view) {
        //do something
    }

    public void opensignup_stndt(View view) {
        setContentView(R.layout.student_sign_up);

    }

    public void check_signup_detail_stndnt(View view) {
        EditText et_userid = (EditText) findViewById(R.id.ed_userid_stndnt);
        EditText et_newpswrd = (EditText) findViewById(R.id.ed_newpswrd_stndnt);
        EditText et_cnfrmpswrd = (EditText) findViewById(R.id.ed_cnfrmpswrd_stndnt);
        String str_userid_stndnt = et_userid.getText().toString();
        String str_newpswrd_stndnt = et_newpswrd.getText().toString();
        String str_cnfrmpswrd_stndnt = et_cnfrmpswrd.getText().toString();

        if (str_newpswrd_stndnt.equals(str_cnfrmpswrd_stndnt)) {
            //continue
            setContentView(R.layout.student_personal_details);
        } else {
            et_cnfrmpswrd.setError("password don't match");

        }
        checking_userid_stndnt(str_userid_stndnt);


    }

    public void checking_userid_stndnt(String str_userid_stndnt) {
        //check user id hai ki nhi
    }

    public void back_to_sign_in(View view) {
        setContentView(R.layout.student_sign_in);
    }


    public void additemson_stndt_state_spinner(View view) {

        final Spinner sppiner_state_stndnt = (Spinner) findViewById(R.id.spinner_stndt_state);
        final Spinner sppiner_school_stndnt = (Spinner) findViewById(R.id.spinner_stndt_school);
        final List<String> state_list_stndnt = new ArrayList<String>();
        final List<String> schoollist_stndnt = new ArrayList<String>();
        final DatabaseReference state = database_stndt.getReference("state");

        state.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    state_list_stndnt.add(String.valueOf(dsp.getKey())); //add result into array list
                    Log.d("ayush", "onDataChange: " + dsp.getKey());
                }
                Log.d("sagar", "onDataChange: " + state_list_stndnt);
                sppiner_state_stndnt.setAdapter(new ArrayAdapter<String>(student_sign_in_up.this, R.layout.support_simple_spinner_dropdown_item, state_list_stndnt));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sppiner_state_stndnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_state_stndt = adapterView.getSelectedItem().toString();
                state.child(selected_state_stndt).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            schoollist_stndnt.add(snapshot.getKey());
                            Log.d("sagar dabas", "onDataChange: " + snapshot.getKey());
                        }
                        Log.d("sagar dabas", "onDataChange: school" + schoollist_stndnt);
                        sppiner_school_stndnt.setAdapter(new ArrayAdapter<String>(student_sign_in_up.this, R.layout.support_simple_spinner_dropdown_item, schoollist_stndnt));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sppiner_school_stndnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_school_stndt = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


}
