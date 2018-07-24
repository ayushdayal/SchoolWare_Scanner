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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class teacher_sign_up_in extends AppCompatActivity {
    FirebaseDatabase database_tchr;
    String selected_school_tchr;
    String selected_state_tchr;
    private Spinner sppiner_state_tchr;
    private Spinner sppiner_school_tchr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_sign_in);
        database_tchr = FirebaseDatabase.getInstance();
        //additemson_tcher_state_spinner();
    }

    public void forgotpass(View view) {
        //do something
    }

    public void opensignup(View view) {
        setContentView(R.layout.teacher_sign_up);
    }

//    public void additemson_tcher_state_spinner() {
//        sppiner_state_tchr = (Spinner) findViewById(R.id.spinner_tchr_state);
//        sppiner_school_tchr = (Spinner) findViewById(R.id.spinner_tchr_school);
//        final List<String> state_list_tchr = new ArrayList<>();
//        final List<String> schoollist_tchr = new ArrayList<>();
//        final DatabaseReference state = database_tchr.getReference("state");
//
//        state.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                    state_list_tchr.add(String.valueOf(dsp.getKey())); //add result into array list
//                    Log.d("ayush", "onDataChange: " + dsp.getKey());
//                }
//                Log.d("sagar", "onDataChange: " + state_list_tchr);
//                sppiner_state_tchr.setAdapter(new ArrayAdapter<String>(teacher_sign_up_in.this, R.layout.support_simple_spinner_dropdown_item, state_list_tchr));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        sppiner_state_tchr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_state_tchr = adapterView.getSelectedItem().toString();
//                state.child(selected_state_tchr).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            schoollist_tchr.add(snapshot.getKey());
//                            Log.d("sagar dabas", "onDataChange: " + snapshot.getKey());
//                        }
//                        Log.d("sagar dabas", "onDataChange: school" + schoollist_tchr);
//                        sppiner_school_tchr.setAdapter(new ArrayAdapter<>(teacher_sign_up_in.this, R.layout.support_simple_spinner_dropdown_item, schoollist_tchr));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        sppiner_school_tchr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_school_tchr = adapterView.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
//
//
//    }


    public void check_signup_detail(View view) {
        EditText et_emailid = (EditText) findViewById(R.id.ed_userid);
        EditText et_newpswrd = (EditText) findViewById(R.id.ed_newpswrd);
        EditText et_cnfrmpswrd = (EditText) findViewById(R.id.ed_cnfrmpswrd);
        String str_userid = et_emailid.getText().toString();
        String str_newpswrd = et_newpswrd.getText().toString();
        String str_cnfrmpswrd = et_cnfrmpswrd.getText().toString();

        if (str_newpswrd.equals(str_cnfrmpswrd)) {
            //continue
            if (str_userid.isEmpty()) {
                et_emailid.setError("Enter the email id");
            } else {

                if (selected_state_tchr.isEmpty()) {
                    ((TextView) sppiner_state_tchr.getSelectedView()).setError("select the state");

                } else {
                    if (selected_school_tchr.isEmpty()) {
                        ((TextView) sppiner_school_tchr.getSelectedView()).setError("select the school");
                    } else {
                        // set contentview on next page.......
                    }
                }

            }
        } else {
            et_cnfrmpswrd.setError("password don't match");

        }


    }


    public void back_to_sign_in(View view) {
        setContentView(R.layout.teacher_sign_in);
    }


}
