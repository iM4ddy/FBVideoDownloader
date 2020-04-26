package com.fbvideodownloader.facebookvideosaver;

/**
 * Created by mac on 25/01/16.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import config.admob;
import dialog.dialoginfo;
import func.reg;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;



public class home extends Fragment {

    private String html = "", desc = "", imagina = "", url = "" , video = "" , videoArray="";
    private TextInputEditText textField;
    private Button past , btnshow ;
    private TextView textView;
    private ProgressDialog prgDialog;
    // variable to track event time
    private long mLastClickTime = 0;
    private ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){

        final View rootView = inflater.inflate(R.layout.home, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        /*linearlayout = (LinearLayout)rootView.findViewById(R.id.unitads);
        config.admob.admobBannerCall(getActivity(), linearlayout);*/

        textField = (TextInputEditText) rootView.findViewById(R.id.my_text_field2);
        past = (Button)rootView.findViewById(R.id.btnshow);
        btnshow = (Button)rootView.findViewById(R.id.btndl);
        textView = (TextView)rootView.findViewById(R.id.textView);



        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setCancelable(true);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String packageName = getActivity().getPackageName();

        Intent intent = getActivity().getIntent();

        if(intent.hasExtra("url")){

            textField.setText(func.reg.getBack(intent.getStringExtra("url").toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));

            showContent( textField);

            getActivity().getIntent().removeExtra("url");
        }

        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(!textView.getText().toString().isEmpty()){
                    showContent(textField);
                }
                return false;
            }
        });

        past.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // Gets the ID of the "paste" menu item

                    final android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                    ClipData clip = clipboard.getPrimaryClip();

                    if(clip != null){

                        ClipData.Item item = clip.getItemAt(0);
                        textField.setText(func.reg.getBack(item.getText().toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));



                    }else{
                        Toast.makeText(getActivity(), "Empty clipboard!", Toast.LENGTH_LONG).show();
                    }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

                }else{

                    showContent( textField);

                }
                mLastClickTime = SystemClock.elapsedRealtime();

            }
        });

        btnshow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

                }else{

                    showContent( textField);

                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        });


        // Native Ads

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), admob.native_unit);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                TemplateView templateView = rootView.findViewById(R.id.my_template);
                templateView.setVisibility(View.VISIBLE);
                templateView.setNativeAd(unifiedNativeAd);


            }
        });

        AdLoader adLoader = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);

        return rootView;

    }

    private void showContent(EditText textField){

        url = textField.getText().toString().replaceAll(" ","");

        if(reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)").isEmpty()){

            Toast.makeText(getActivity() , "Please copy a video link first" , Toast.LENGTH_LONG).show();

        }else{

                video = "";

                    // correct url structure
                        if(url.contains("facebook")){

                            url = "https://m.facebook." + reg.getBack(url,"(((?!.com).)+$)");

                        }


                    url = func.reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)");

                    AsyncHttpClient client = new AsyncHttpClient();
                        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
                        client.get(url, new TextHttpResponseHandler() {
                            @Override
                            public void onStart() {

                                // Initiated the request
                                    prgDialog.setMessage("Loading...");
                                    prgDialog.show();
                            }


                            @Override
                            public void onFinish() {
                                // Completed the request (either success or failure)
                                prgDialog.hide();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                                // TODO Auto-generated method stub
                                prgDialog.hide();
                                Toast.makeText(getActivity(), "Conexion Faild!", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                                // TODO Auto-generated method stub

                                html = responseBody;

                                int i = 0;
                                for(Header header : headers){

                                    Log.e("header : ", header.getValue());
                                }


                                if(url.contains("facebook.com") || url.contains("fb.com")){

                                    facebook();

                                }

                            }
                        });

            }
    }


    private void asynHttp(final String web){

        AsyncHttpClient client = new AsyncHttpClient();
        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        client.get(web, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                // Initiated the request

                prgDialog.setMessage("getting video...");
                prgDialog.show();

            }

            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
                prgDialog.hide();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                Toast.makeText(getActivity(), "Conexion Faild!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String responseBody) {
                // TODO Auto-generated method stub

                html = responseBody;


                if(web.contains("savedeo.com")){

                    video = reg.getBack(html , "href=\"(.+?\\.mp4)\"");

                    mDialog();

                } else {

                    String powerJS = reg.getBack(html , "data-config=\"(.+?)\"");

                    if (!func.json.jsonObject(powerJS , "video_url").isEmpty()){

                        asynHttp("https://savedeo.com/download?url="+url);

                    }else if(!func.json.jsonObject(powerJS , "vmap_url").isEmpty()){

                        try{

                            video = reg.getBack(func.httpRequest.get(func.json.jsonObject(powerJS , "vmap_url")), "<MediaFile>[^<]+<\\!\\[CDATA\\[([^\\]]+)");
                            mDialog();

                        }catch(Exception e){

                        }

                    }

                }

            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();

        Intent a = getActivity().getIntent();

        if (a.hasExtra("url")) {

            textField.setText(func.reg.getBack(a.getStringExtra("url").toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));
            a.removeExtra("url");
            showContent(textField);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void facebook(){

        String jsonVideo = escapeXml(reg.getBack(html, "\"([^\"]+)\" data-sigil=\"inlineVideo\""));

        String jsonImage = escapeXml(reg.getBack(html, "data-store=\"([^\"]+imgsrc[^\"]+)\""));

        String jGif = reg.getBack(html, "class=\"_4o54\".+?&amp;url=(.+?)&");

        try {

            if(!jsonVideo.isEmpty()){
                JSONObject obj = new JSONObject(jsonVideo);
                video = obj.getString("src");

            }else if(!jGif.isEmpty()){

                imagina = URLDecoder.decode(jGif, "UTF-8");

            }else if(!jsonImage.isEmpty()){

                JSONObject obj = new JSONObject(jsonImage);
                imagina = obj.getString("imgsrc");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mDialog();

    }


    public String escapeXml(String s) {
        return s.replaceAll("&#123;","{").replaceAll("&#125;","}").replaceAll("&amp;" ,"&").replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&quot;","\"").replaceAll("&apos;","'");
    }

    public void mDialog(){

        if(!video.isEmpty() ){

            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialoginfo info = new dialoginfo();
            Bundle args = new Bundle();
            video.replace("\\","");
            args.putString("video", video.replace("\\",""));
            info.setArguments(args);
            info.show(fm, "fragment_info");

        }else{
            Toast.makeText(getContext(), "There is No results try again with new Link!", Toast.LENGTH_LONG).show();
        }
    }

}