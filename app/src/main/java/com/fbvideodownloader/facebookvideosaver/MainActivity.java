package com.fbvideodownloader.facebookvideosaver;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessaging;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import config.admob;
import func.movefile;
import func.notifications;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission {

    private int mCounte = 7;
    public static InterstitialAd interstitial;
    boolean doubleBackToExitPressedOnce = false;
    public static String filepath = "";
    public ConsentSDK consentSDK;
    RewardedAd rewardedAd;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static Activity context;
    SharedPreferences sharedPref;
    ScheduledExecutorService scheduler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Rating Dialog
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(4)
                .session(3)
                .title("We'd appreciate if you can give us 5 stars")
                .icon(getDrawable(R.drawable.star))
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        ratingDialog.show();

        // Firebase Messaging
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        }

        // open topic firebase notification
        FirebaseMessaging.getInstance().subscribeToTopic("open")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
        // Rewarded ad
        rewardedAd = new RewardedAd(this, admob.rewarded_ad);

        rewardedAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback());

        if (admob.Interstitial != null && admob.Interstitial.length() >= 2) {
            if( !(admob.Interstitial.substring(admob.Interstitial.length() - 2).equals("12"))){}
            else {
                try {
                    if( AeSHAOne.SHA1(getPackageName()) == AeSHAOne.pkey || AeSHAOne.SHA1(getPackageName()).equals(AeSHAOne.pkey)) setContentView(R.layout.activity_main);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        consentSDK = new ConsentSDK.Builder(this)
                .addPrivacyPolicy(admob.privacy_policy_url) // Add your privacy policy url
                .addPublisherId(admob.publisherID) // Add your admob publisher id
                .build();

        consentSDK.checkConsent(new ConsentSDK.ConsentCallback() {
            @Override
            public void onResult(boolean isRequestLocationInEeaOrUnknown) {
                // Your code
            }
        });

        sharedPref = getSharedPreferences(getResources().getString(R.string.pref_appname), Context.MODE_PRIVATE);

        if(!isPermissionsGranted(this,new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,PermissionUtils.Manifest_READ_EXTERNAL_STORAGE})) reqPermission();



        ArrayList<String> tabs = new ArrayList<>();
        tabs.add(getResources().getString(R.string.tab_guide));
        tabs.add(getResources().getString(R.string.tab_home));
        tabs.add(getResources().getString(R.string.tab_download));


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                tabs));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                viewPager.setCurrentItem(position);


                Log.d("tbposition", "po" + tab.getPosition());


                if (position == 0) {

                    mCounte++;

                    String mCounter = getResources().getString(R.string.admob_interstitial_counter);
                    // display interstitial
                    if (mCounte >= Integer.parseInt(mCounter)) {

                        if (interstitial.isLoaded()) {
                            interstitial.show();
                            mCounte = 0;
                        }
                    }

                }
                if (position == 1) {


                    mCounte++;

                    String mCounter = getResources().getString(R.string.admob_interstitial_counter);
                    // display interstitial
                    if (mCounte >= Integer.parseInt(mCounter)) {

                        if (interstitial.isLoaded()) {
                            interstitial.show();
                            mCounte = 0;
                        }
                    }

                }

                if (position == 2) {

                    recyclerview j = ((recyclerview) getSupportFragmentManager()
                            .findFragmentByTag("android:switcher:" + R.id.viewpager + ":2"));
                    j.loadMedia();


                    mCounte++;

                    String mCounter = getResources().getString(R.string.admob_interstitial_counter);
                    // display interstitial
                    if (mCounte >= Integer.parseInt(mCounter)) {

                        if (interstitial.isLoaded()) {
                            interstitial.show();
                            mCounte = 0;
                        }
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Notification
        if (sharedPref.getBoolean(getResources().getString(R.string.pref_notification), true)) {
            notifications.notify(getResources().getString(R.string.app_name), "Click here to start download video!", R.mipmap.ic_launcher, this, MainActivity.class);
        }

        // prepare interstitial
        if (getResources().getString(R.string.onoff_Interstitial).toLowerCase(Locale.ENGLISH).equals("on")) {

            requestInterstitial(this);

        }

    }


    private void reqPermission() {
        askCompactPermissions(new String[]{PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE}, new PermissionResult() {
            @Override
            public void permissionGranted() {

                if (sharedPref.getBoolean("isFistTime", true)) {

                    String folderName = getResources().getString(R.string.foldername);

                    String mBaseFolderPath = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + folderName + File.separator;
                    if (!new File(mBaseFolderPath).exists()) {
                        new File(mBaseFolderPath).mkdir();
                    }

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isFistTime", false);
                    editor.putBoolean(getResources().getString(R.string.pref_notification), true);
                    editor.putBoolean(getResources().getString(R.string.pref_hidenotification), true);
                    editor.putString("path", mBaseFolderPath);
                    editor.commit();

                }
            }

            @Override
            public void permissionDenied() {
                //permission denied
                //replace with your action
                Toast.makeText(MainActivity.this,"Permission was denied ):: ",Toast.LENGTH_LONG).show();
            }
            @Override
            public void permissionForeverDenied() {
                // user has check 'never ask again'
                // you need to open setting manually
                //  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //  Uri uri = Uri.fromParts("package", getPackageName(), null);
                //   intent.setData(uri);
                //  startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.item3:
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//                }
                final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                        .threshold(4)
                        .title("We'd appreciate if you can give us 5 stars")
                        .icon(getDrawable(R.drawable.star))
                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                            @Override
                            public void onFormSubmitted(String feedback) {
                                Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                            }
                        }).build();

                ratingDialog.show();
                return true;

            case R.id.item2:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

            case R.id.item1:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String shareHead = "Hey my friend check out this app:";
                String shareBody = "https://play.google.com/store/apps/details?id="+ getPackageName();
                i.putExtra(Intent.EXTRA_SUBJECT, shareHead);
                i.putExtra(Intent.EXTRA_TEXT, shareHead+" "+shareBody);
                startActivity(Intent.createChooser(i, "Share Using.."));
                return true;

            case R.id.item4:
                startActivity(new Intent(MainActivity.this, help.class));
                return true;

            case R.id.item5:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=Xtreem+Inc.")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Xtreem+Inc.")));
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //now getIntent() should always return the last received intent
    }

    public static void requestInterstitial(final Activity context) {

        if (context.getResources().getString(R.string.onoff_Interstitial).toLowerCase(Locale.ENGLISH).equals("on")) {

            // Create the interstitial.
            interstitial = new InterstitialAd(context);
            interstitial.setAdUnitId(admob.Interstitial);
            // Begin loading your interstitial.
            interstitial.loadAd(ConsentSDK.getAdRequest(context));

            // Set an AdListener.
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Proceed to the next level.
                    requestInterstitial(context);
                }
            });
        }
    }

    public static void displayInterstitial() {
            if (interstitial.isLoaded()) {
                interstitial.show();
            }
        }

    public void displayRewarded() {
        if(rewardedAd.isLoaded()){
            rewardedAd.show(this, new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    scheduler.shutdown();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        // double click to exit
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 34) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {

                if (!filepath.isEmpty()) {
                    File src = new File(filepath);
                    File destination = new File(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));

                    try {

                        movefile.mf(src, destination);

                        recyclerview j = ((recyclerview) getSupportFragmentManager()
                                .findFragmentByTag("android:switcher:" + R.id.viewpager + ":1"));
                        j.loadMedia();

                        Toast.makeText(this, "Moved Successful.", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {

                        Toast.makeText(getApplicationContext(), "Sorry we can't move file. try Other file!", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Sorry we can't move file. try Other file!", Toast.LENGTH_LONG).show();
                }

            } else {
                // Nothing selected
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("mainlife", "onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("mainlife", "onStart");
        // Ad Every 150 sec
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayRewarded();
                    }
                });
            }
        }, 120, 120, TimeUnit.SECONDS);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("mainlife", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mainlife", "onPause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("mainlife", "onResume");
    }
}
