package com.defineway.BookNStart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DashboardActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);


        TextView tv = (TextView) findViewById(R.id.txtheading1);
        tv.setText("Welcome " + Sharing.fname + " " + Sharing.lname);

    }
}//close DashboardActivity
