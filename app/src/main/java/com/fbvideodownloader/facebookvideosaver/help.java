package com.fbvideodownloader.facebookvideosaver;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import config.admob;

public class help extends AppCompatActivity {

    ActionBar actionBar;
    public static LinearLayout linearlayout;
    public static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        context = this;

        actionBar= this.getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setTitle("How To Download");

        /*AdLoader.Builder builder = new AdLoader.Builder(this, admob.native_unit);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                TemplateView templateView = findViewById(R.id.my_template_5);
                templateView.setVisibility(View.VISIBLE);
                templateView.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);*/

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
}
