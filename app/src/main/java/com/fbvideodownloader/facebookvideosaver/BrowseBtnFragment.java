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

    private Button browseBtn;
    TemplateView templateView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.browse_btn, container, false);

            templateView = rootView.findViewById(R.id.my_template_3);
            browseBtn = (Button) rootView.findViewById(R.id.button6);
            browseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.linearLayout2, new facebook());
                    fr.commit();

                    browseBtn.setVisibility(View.GONE);
                    templateView.setVisibility(View.GONE);
                }
            });

            MobileAds.initialize(getContext());
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), admob.native_unit);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                templateView.setVisibility(View.VISIBLE);
                templateView.setNativeAd(unifiedNativeAd);

            }
        });

        AdLoader adLoader = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);

        return rootView;
    }
}
