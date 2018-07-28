package com.example.ayush.schoolware_scanner;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    final HashMap<String, String> student_list = new HashMap<>();

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView peripheralTextView;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    ArrayList<String> ArrLst = new ArrayList<>();
    FirebaseDatabase database;

    String state;
    String Class;
    String school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            state = String.valueOf(bundle.getChar("state"));
            Class = String.valueOf(bundle.getChar("class"));
            school = String.valueOf(bundle.getChar("school"));
        }
        peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);


        database = FirebaseDatabase.getInstance();

        //todo: suspended database commands

//        database = FirebaseDatabase.getInstance();
//        DatabaseReference s=database.getReference("schoid");
//        DatabaseReference ref = s.child("teachers");
//        ref.addListenerForSingleValueEvent(
//                new ValueEventListener()
//                {
//                    public ArrayList<String> Userlist;
//
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        //Get map of users in datasnapshot
//                        Userlist = new ArrayList<String>();
//                        // Result will be holded Here
//                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                            Log.d("data from database ", "onDataChange: "+dsp.getValue());
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        DatabaseReference n=s.child("teachers").child("abc").child("classname");
//        s.child("teachers").child("abc").child("com").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                startScanning();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        })
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (btManager != null) {
            btAdapter = btManager.getAdapter();
        } else Log.d("s", "onCreate: adapter not found");
        btScanner = btAdapter.getBluetoothLeScanner();

        Bundle s;

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }


    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getDevice().getName() != null) {

                database.getReference("schoid").child("uniqid").child("prmsid").child("id").setValue(-1 * result.getRssi());

                if (Check(result.getDevice().getAddress())) {
                    peripheralTextView.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n" + result.getDevice().getAddress());
                    // auto scroll for text view
                    final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
                    // if there is no need to scroll, scrollAmount will be <=0
                    if (scrollAmount > 0)
                        peripheralTextView.scrollTo(0, scrollAmount);
                }
            }
        }

    };

    public boolean Check(String str) {
        if (ArrLst.contains(str)) {
            Log.d("g", "Check: returnig false" + str);
            return false;
        } else {
            ArrLst.add(str);
            Log.d("j", "Check: returnig true");

            return true;
        }

    }

    public void Student_List() {

        final DatabaseReference State = database.getReference("state");

        State.child(state).child(school).child("classes").child(Class).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot student : dataSnapshot.getChildren()) {
                    Log.d("bsagR", "onDataChange: "+student.getKey()+student.getValue());
                    student_list.put(student.getValue().toString(), student.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        attendence();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        peripheralTextView.setText("");
        startScanningButton.setVisibility(View.INVISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.startScan(leScanCallback);
//            }
//        });
        ArrLst.add("1235456");
        ArrLst.add("student1");
        ArrLst.add("35456");
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("adcd", "run: "); //Do something after 100ms
                stopScanning();
            }
        }, 5000);
        Student_List();
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        peripheralTextView.append("Stopped Scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.stopScan(leScanCallback);
//            }
//        });
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("after stopping", "run: "); //Do something after 100ms

            }
        }, 5000);
    }

    public void attendence() {
        Log.d("sagar", "attendence: cd");
        final DatabaseReference dataschool = database.getReference("schooldatas");
        Log.d("sd", "attendence: "+ArrLst+student_list);
        for (int i=0;i<student_list.size();i++)

        {                Log.d("sadgar", "attendence: lelio");

            String Student_name;
            String key = null;
            Student_name = student_list.get(key);

            if (ArrLst.contains(key)) {

                dataschool.child(school).child("students").child(Class).child(Student_name).child("attendance").child("today").setValue("pre");
                Log.d("sadgar", "attendence: lelio");
            }
            else {
                Log.d("sagar", "attendence: else ka");
                dataschool.child(school).child("students").child(Class).child(Student_name).child("attendance").child("today").setValue("absent");

            }


        }


    }
}
