package com.fbvideodownloader.facebookvideosaver;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import config.admob;

public class BrowseBtnFragment extends Fragment {

    public Button browseBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browse_btn, container, false);

            browseBtn = (Button) view.findViewById(R.id.button6);
            browseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.browse_btn_frame, new facebook());
                    fr.commit();

                    browseBtn.setVisibility(View.GONE);

                }
            });


        return view;
    }
}
