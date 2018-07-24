package com.example.ayush.schoolware_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity {
    private Spinner sppiner_state;
    private Spinner sppiner_school;
    private Spinner sppiner_class;

    String selected_school="school1";
    String selected_class="1a";
    String selected_state="goa";

    String TAG = "s";
    private Button btnSubmit;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        addItemsOnSpinner2();
        addListenerOnButton();
       // makeStructure();
    }

    public void addItemsOnSpinner2() {

        sppiner_state = (Spinner) findViewById(R.id.spinner);
        sppiner_school = (Spinner) findViewById(R.id.spinner2);
        sppiner_class = (Spinner) findViewById(R.id.spinner3);

        final List<String> state_list = new ArrayList<String>();
        final List<String> schoollist = new ArrayList<String>();
        final List<String> classes_list = new ArrayList<String>();

        database = FirebaseDatabase.getInstance();
        final DatabaseReference state = database.getReference("state");

        state.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    state_list.add(String.valueOf(dsp.getKey())); //add result into array list
                    Log.d(TAG, "onDataChange: " + dsp.getKey());
                }
                Log.d(TAG, "onDataChange: " + state_list);
                sppiner_state.setAdapter(new ArrayAdapter<String>(signup.this, R.layout.support_simple_spinner_dropdown_item, state_list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        sppiner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_state = adapterView.getSelectedItem().toString();
                state.child(selected_state).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            schoollist.add(snapshot.getKey());
                            Log.d(TAG, "onDataChange: " + snapshot.getKey());
                        }
                        Log.d(TAG, "onDataChange: school" + schoollist);
                        sppiner_school.setAdapter(new ArrayAdapter<>(signup.this, R.layout.support_simple_spinner_dropdown_item, schoollist));
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

        sppiner_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: in sele sldf");
                selected_school = adapterView.getSelectedItem().toString();
                if (selected_school != null) {
                    state.child(selected_state).child(adapterView.getSelectedItem().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        classes_list.add(snapshot.getKey());
                                        Log.d(TAG, "onDataChange: class" + snapshot.getKey());
                                    }
                                    sppiner_class.setAdapter(new ArrayAdapter<>(signup.this, R.layout.support_simple_spinner_dropdown_item, classes_list));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sppiner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_class = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    public void addListenerOnButton() {

        sppiner_state = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.button2);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(signup.this,
                        "OnClickListener : " +
                                "\nSpinner 2 : " + String.valueOf(sppiner_state.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(signup.this, MainActivity.class);
                intent.putExtra("state", selected_state);
                intent.putExtra("school", selected_school);
                intent.putExtra("class", selected_class);
                startActivity(intent);
            }

        });
    }

    public void makeStructure() {
        DatabaseReference state = database.getReference("state");
        String stateList[] = {"rajasthan", "utterpradesh", "utrakhand", "keral", "goa"};
        for (String s : stateList) {
            state.child(s).child("school1").child("classes").child("1a").child("mac").setValue("345");
            state.child(s).child("school1").child("classes").child("1b").child("mac").setValue("456");
            state.child(s).child("school1").child("classes").child("2a").child("mac").setValue("567");
            state.child(s).child("school2").child("classes").child("1a").child("mac").setValue("345");
            state.child(s).child("school2").child("classes").child("1b").child("mac").setValue("456");
            state.child(s).child("school2").child("classes").child("2a").child("mac").setValue("567");
            state.child(s).child("school3").child("classes").child("1a").child("mac").setValue("345");
            state.child(s).child("school3").child("classes").child("1b").child("mac").setValue("456");
            state.child(s).child("school3").child("classes").child("2a").child("mac").setValue("567");
        }

        DatabaseReference schooldatas = database.getReference("schooldatas");
        schooldatas.child("school1").child("quote").setValue("nothing can stop me");
        schooldatas.child("school1").child("contact").child("calllist").child("calllist1").setValue("1234567890");
        schooldatas.child("school1").child("contact").child("calllist").child("calllist2").setValue("1234567890");
        schooldatas.child("school1").child("contact").child("calllist").child("calllist3").setValue("1234567890");

        schooldatas.child("school1").child("contact").child("emails").child("calllist1").setValue("1234567890");
        schooldatas.child("school1").child("contact").child("emails").child("calllist2").setValue("1234567890");
        schooldatas.child("school1").child("contact").child("emails").child("calllist3").setValue("1234567890");

        schooldatas.child("school1").child("teachers").child("teacher1").child("classname").setValue("1a");
        schooldatas.child("school1").child("teachers").child("teacher1").child("mac").setValue("123:456:789");
        schooldatas.child("school1").child("teachers").child("teacher2").child("classname").setValue("1a");
        schooldatas.child("school1").child("teachers").child("teacher2").child("mac").setValue("123:456:789");
        schooldatas.child("school1").child("teachers").child("teacher3").child("classname").setValue("1a");
        schooldatas.child("school1").child("teachers").child("teacher3").child("mac").setValue("123:456:789");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("announcement").child("announcement1").child("detail").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("announcement").child("announcement1").child("message").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("announcement").child("announcement1").child("subject").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("announcement").child("announcement1").child("type").setValue("1234567890");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("notification").child("notification1").child("detail").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("notification").child("notification1").child("message").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("notification").child("notification1").child("subject").setValue("1234567890");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("notification").child("notification1").child("type").setValue("1234567890");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("attendance").child("today").setValue("p");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("personaldetails").child("name").setValue("examplename1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("personaldetails").child("dob").setValue("010220");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("personaldetails").child("parentsname").setValue("examplename1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("personaldetails").child("parentsno").setValue("123456789");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("personaldetails").child("mac").setValue("132:465:789");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("buslocation").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("routeno").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("eta").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("model").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("vendor").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("busno").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("couductor").child("name").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("couductor").child("mobileno").setValue("123456789");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("couductor").child("feedback").setValue("fuudu");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("couductor").child("id").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("driver").child("name").setValue("examplelocation1");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("driver").child("mobileno").setValue("132456789");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("driver").child("feedback").setValue("awesome");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("droproute").child("driver").child("id").setValue("examplelocation1");

        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location1").setValue("0432");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location2").setValue("0522");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location3").setValue("2132");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location4").setValue("1639");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location5").setValue("0432");
        schooldatas.child("school1").child("students").child("1a").child("student1").child("location").child("location6").setValue("1432");


    }

    public void check_sign_up_details(View view) {
        if(selected_state.isEmpty()){
            ((TextView) sppiner_state.getSelectedView()).setError("select the state");
        }
        else{
            if(selected_school.isEmpty()){
                ((TextView) sppiner_school.getSelectedView()).setError("select the state");
            }
            else{
                if(selected_class.isEmpty()){
                    ((TextView) sppiner_class.getSelectedView()).setError("select the state");
                }
                else{
                    //add deatails to firebase
                }
            }
        }
    }
}
