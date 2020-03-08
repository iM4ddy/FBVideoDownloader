package com.fbvideodownloader.facebookvideosaver;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class help extends AppCompatActivity {

    ActionBar actionBar;
    public static LinearLayout linearlayout;
    public static Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        context = this;
        linearlayout = (LinearLayout) findViewById(R.id.unitads);
        config.admob.admobBannerCall(this, linearlayout);

        actionBar= this.getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setTitle("How To Download");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(this, "This is my Toast message!",
                        //Toast.LENGTH_LONG).show();
                Intent intent = new Intent(help.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                Toast.makeText(this, "Sorry some Error block app",
                        Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
