package com.fbvideodownloader.facebookvideosaver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import adapters.MyAdapter;
import adapters.arrayAdapter;
import config.admob;
import func.reg;

/**
 * Created by mac on 26/01/16.
 */
public class recyclerview extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private File[] listFile;
    public String folderName = "";
    File file;
    ArrayList<arrayAdapter> jData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){

        final View rootView = inflater.inflate(R.layout.recyclerview, container, false);

        /*linearlayout = (LinearLayout)rootView.findViewById(R.id.unitads);
        config.admob.admobBannerCall(getActivity(), linearlayout);*/

        folderName = getResources().getString(R.string.foldername);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Native Ad
        MobileAds.initialize(getContext());
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), admob.native_unit);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                TemplateView templateView = rootView.findViewById(R.id.my_template_2);
                templateView.setVisibility(View.VISIBLE);
                templateView.setNativeAd(unifiedNativeAd);

            }
        });

        AdLoader adLoader = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);

        return rootView;
    }

    public void loadMedia(){

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), Context.MODE_PRIVATE);

        if(!preferences.getString("path", "DEFAULT").equals("DEFAULT")){
            file = new File(preferences.getString("path", "DEFAULT"));
        }else{
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + folderName + File.separator);
        }

        // use a linear layout manager
        jData.clear();

        if (file.isDirectory()) {

            if(Build.VERSION.SDK_INT >= 23){

                boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                if (hasPermission) {

                    displayfiles(file, jData, mRecyclerView, getActivity());
                }


            }else{

                displayfiles(file, jData, mRecyclerView, getActivity());
            }
        }
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }

        File[] sortedByDate = folder.listFiles();

        if (sortedByDate != null && sortedByDate.length > 1) {
            Arrays.sort(sortedByDate, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return (int) ((object1.lastModified() > object2.lastModified()) ? object1.lastModified(): object2.lastModified());
                }
            });
        }

        return sortedByDate;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void displayfiles(File file, ArrayList<arrayAdapter> jData, RecyclerView mRecyclerView, Activity a){

        File[] listfilemedia = dirListByAscendingDate(file);
        if(listfilemedia.length != 0){try{a.findViewById(R.id.isEmptyDownloadList).setVisibility(View.GONE);}catch(Exception j){}}
        else{try{a.findViewById(R.id.isEmptyDownloadList).setVisibility(View.VISIBLE);}catch(Exception j){}}

        for (int i = 0; i < listfilemedia.length; i++) {

            if(!listfilemedia[i].isDirectory() && !reg.getBack(listfilemedia[i].getAbsolutePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v|\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf|\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()){

                arrayAdapter ja = new arrayAdapter(listfilemedia[i].getAbsolutePath(),listfilemedia[i].getName());

                jData.add(ja);

            }

        }



        RecyclerView.Adapter mAdapter = new MyAdapter(getActivity(), jData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}