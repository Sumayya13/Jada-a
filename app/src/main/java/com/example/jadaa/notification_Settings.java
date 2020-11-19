package com.example.jadaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class notification_Settings extends AppCompatActivity {

    SwitchCompat notificationSwitch;
    Spinner myCollege;
    Button saveBtn;
    String selectedCollege = "";

    //used shared preferences to save the state of the switch
    SharedPreferences sp;
    SharedPreferences.Editor editor; // to edit the value of the shared preferences

    //constant for topic
    private static String TOPIC_POST_NOTIFICATION = "POST"; //assign any value but use the same for this kind of notification


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__settings);

        notificationSwitch = (SwitchCompat) findViewById(R.id.notification_switch);
        myCollege =(Spinner) findViewById(R.id.myCollege);
        saveBtn = (Button) findViewById(R.id.saveBtn);

        // if user choose a collage I will save position in string to save it in database
        myCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCollege = adapterView.getItemAtPosition(position).toString();
                //Toast.makeText(AddPostActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
            }
             // if the user does not choose any one I will print msg
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(notification_Settings.this, "You have to select a college", Toast.LENGTH_SHORT).show();
            }

        });

        //init shared preferences
        sp = getSharedPreferences("Notification_SP",MODE_PRIVATE);
        boolean isPostEnabled = sp.getBoolean(""+TOPIC_POST_NOTIFICATION, false);
        //if enabled check the switch, otherwise uncheck the switch (by default it is unchecked/false)
        if(isPostEnabled){
            notificationSwitch.setChecked(true);
            myCollege.setVisibility(View.VISIBLE);
        }
        else{
            notificationSwitch.setChecked(false);
            myCollege.setVisibility(View.INVISIBLE);
        }

        //implement switch change listener
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //edit switch state
                editor = sp.edit();
                editor.putBoolean(""+TOPIC_POST_NOTIFICATION, isChecked);
                editor.apply();
                if(isChecked){
                    subscribePostNotification(); //call to subscribe
                }
                else{
                    unsubscribePostNotification(); //call to unsubscribe
                }
            }
        });
    }

    private void unsubscribePostNotification() {
        //unsubscribe to a topic (POST) to disable it's notification
        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "You will not receive a notification when a new book from your college is posted";
                        if(!task.isSuccessful()){
                            msg = "UnSubscription failed";
                        }
                        Toast.makeText(notification_Settings.this, msg, Toast.LENGTH_SHORT);

                    }
                });
    }

    private void subscribePostNotification() {
        //subscribe to a topic (POST) to enable it's notification
        FirebaseMessaging.getInstance().subscribeToTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "You will receive a notification when a new book from your college is posted";
                        if(!task.isSuccessful()){
                            msg = "Subscription failed";
                        }
                        Toast.makeText(notification_Settings.this, msg, Toast.LENGTH_SHORT);

                    }
                });

    }




}