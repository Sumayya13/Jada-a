package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static String PACKAGE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        PACKAGE_NAME=getApplicationContext().getPackageName();

    }

    public void shareMe(View view) {


        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String value ="https://trello.com/b/v0XZ75Ph/jadaa-app"+PACKAGE_NAME;
        intent.putExtra(Intent.EXTRA_TEXT,value);
        startActivity(intent.createChooser(intent,"share via"));
    }
}