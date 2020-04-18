package com.fbvideodownloader.facebookvideosaver;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import dialog.dialoginfo;


public class facebook extends Fragment {

    private String url = "" , video = "" , videoArray="" ,imagina = "" ,desc = "";
    private ProgressBar mprogress;
    WebView webo;
    // variable to track event time
//    public static LinearLayout linearlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        printLog("onCreateView Called");

        View rootView = inflater.inflate(R.layout.facebook, container, false);
//        linearlayout = (LinearLayout)rootView.findViewById(R.id.unitads);
//        config.admob.admobBannerCall(getActivity(), linearlayout);
        webo = (WebView) rootView.findViewById(R.id.webView);
        mprogress = (ProgressBar)rootView.findViewById(R.id.mprogress);

        mprogress.setProgress(0);
        mprogress.setMax(100);

        loadWebView();

        return rootView;

    }

    public void loadWebView(){

        url =  "https://m.facebook.com/";

        webo.getSettings().setJavaScriptEnabled(true);
        webo.addJavascriptInterface(this, "mJava");
        webo.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webo.canGoBack()) {
                    webo.goBack();
                    return true;
                }
                return false;
            }

        });

        webo.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mprogress.setProgress(newProgress);
            }
        });

        webo.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                mprogress.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mprogress.setVisibility(View.GONE);

                webo.loadUrl("javascript:(function() { "
                        + "var el = document.querySelectorAll('div[data-sigil]');"
                        + "var s =  document.getElementsByClassName('_4fmw grouped aclb')[0];"
                        + "if(s){s.innerHTML=\"\";}"
                        + "for(var i=0;i<el.length; i++)"
                        + "{"
                        + "var sigil = el[i].dataset.sigil;"
                        + "if(sigil.indexOf('inlineVideo') > -1){"
                        + "delete el[i].dataset.sigil;"
                        + "var jsonData = JSON.parse(el[i].dataset.store);"
                        + "el[i].setAttribute('onClick', 'mJava.getData(\"'+jsonData['src']+'\");');"
                        + "}" + "}"

                        +"\n" +
                        "function getparent(parent){\n" +
                        "\tif(typeof(parent.parentNode) !== \"undefined\"){\n" +
                        "\t\tif(parent.parentNode.tagName == \"SECTION\"){\n" +
                        "    \t\treturn parent.parentNode;\n" +
                        "\t\t}else{\n" +
                        "\t\t\treturn getparent(parent.parentNode);\n" +
                        "\t\t}\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function removeHref(parent){\n" +
                        "\n" +
                        "\tfor(i=0;i< parent.childNodes.length;i++){\n" +
                        "\t\tif(parent.childNodes[i].tagName == \"A\"){\n" +
                        "\t\t\tparent.removeChild(parent.childNodes[i]);\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var imgsrc = document.querySelectorAll('._5s61 > i');\n" +
                        "\n" +
                        "for (j=0;j<imgsrc.length;j++){\n" +
                        "\tvar child = imgsrc[j];\n" +
                        "\tif (typeof(child.parentNode) !== \"undefined\" \n" +
                        "\t&& typeof(child.parentNode.parentNode) !== \"undefined\") {\n" +
                        "\t\tvar parent = child.parentNode;\n" +
                        "        style = child.currentStyle || window.getComputedStyle(child, false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "        if(typeof(bi.split(\"url=\")[1]) !== \"undefined\"){\n" +
                        "        \tvar parentGetFromFunction = getparent(parent);\n" +
                        "        \tparentGetFromFunction.setAttribute('onClick', 'mJava.getData(\"'+decodeURIComponent(bi.split(\"url=\")[1].split(\"&\")[0])+'\");');\n" +
                        "        \tremoveHref(parentGetFromFunction);\n" +
                        "        }\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var gif = document.querySelectorAll('._5s61 > ._4o54 > i');\n" +
                        "for (j=0;j<gif.length;j++){\n" +
                        "\tvar child = gif[j];\n" +
                        "\tif (typeof(child.parentNode) !== \"undefined\" \n" +
                        "\t&& typeof(child.parentNode.parentNode.parentNode.parentNode) !== \"undefined\") {\n" +
                        "\t\tvar parent = child.parentNode;\n" +
                        "        style = child.currentStyle || window.getComputedStyle(child, false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "\n" +
                        "        if(typeof(bi.split(\"url=\")[1]) !== \"undefined\"){\n" +
                        "        \tvar parentGetFromFunction = getparent(parent);\n" +
                        "        \tparentGetFromFunction.setAttribute('onClick', 'mJava.getData(\"'+decodeURIComponent(bi.split(\"url=\")[1].split(\"&\")[0])+'\");');\n" +
                        "        \tremoveHref(parentGetFromFunction);\n" +
                        "        }\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var images = document.querySelectorAll('._5sgg > i');\n" +
                        "for(i=0;i<images.length;i++){\n" +
                        "\tstyle = images[i].currentStyle || window.getComputedStyle(images[i], false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "        var parent = images[i].parentNode.parentNode;\n" +
                        "\tif(parent.tagName == \"A\"){\n" +
                        "\t\tparent.setAttribute('onClick', 'mJava.getData(\"'+bi+'\");');\n" +
                        "\t\tparent.removeAttribute('href');\n" +
                        "\t\tparent.removeAttribute('data-autoid')\n" +
                        "\t}else if(parent.parentNode.tagName == \"A\"){\n" +
                        "\t\tparent.parentNode.setAttribute('onClick', 'mJava.getData(\"'+bi+'\");');\n" +
                        "\t\tparent.parentNode.removeAttribute('href');\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n"

                        + "})()");
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                webo.loadUrl("javascript:(function prepareVideo() { "
                        + "var el = document.querySelectorAll('div[data-sigil]');"
                        + "var s =  document.getElementsByClassName('_4fmw grouped aclb')[0];"
                        + "if(s){s.innerHTML=\"\";}"
                        + "for(var i=0;i<el.length; i++)"
                        + "{"
                        + "var sigil = el[i].dataset.sigil;"
                        + "if(sigil.indexOf('inlineVideo') > -1){"
                        + "delete el[i].dataset.sigil;"
                        + "var jsonData = JSON.parse(el[i].dataset.store);"
                        + "el[i].setAttribute('onClick', 'mJava.getData(\"'+jsonData['src']+'\");');"
                        + "}" + "}"


                        +"\n" +
                        "function getparent(parent){\n" +
                        "\tif(typeof(parent.parentNode) !== \"undefined\"){\n" +
                        "\t\tif(parent.parentNode.tagName == \"SECTION\"){\n" +
                        "    \t\treturn parent.parentNode;\n" +
                        "\t\t}else{\n" +
                        "\t\t\treturn getparent(parent.parentNode);\n" +
                        "\t\t}\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function removeHref(parent){\n" +
                        "\n" +
                        "\tfor(i=0;i< parent.childNodes.length;i++){\n" +
                        "\t\tif(parent.childNodes[i].tagName == \"A\"){\n" +
                        "\t\t\tparent.removeChild(parent.childNodes[i]);\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var imgsrc = document.querySelectorAll('._5s61 > i');\n" +
                        "\n" +
                        "for (j=0;j<imgsrc.length;j++){\n" +
                        "\tvar child = imgsrc[j];\n" +
                        "\tif (typeof(child.parentNode) !== \"undefined\" \n" +
                        "\t&& typeof(child.parentNode.parentNode) !== \"undefined\") {\n" +
                        "\t\tvar parent = child.parentNode;\n" +
                        "        style = child.currentStyle || window.getComputedStyle(child, false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "        if(typeof(bi.split(\"url=\")[1]) !== \"undefined\"){\n" +
                        "        \tvar parentGetFromFunction = getparent(parent);\n" +
                        "        \tparentGetFromFunction.setAttribute('onClick', 'mJava.getData(\"'+decodeURIComponent(bi.split(\"url=\")[1].split(\"&\")[0])+'\");');\n" +
                        "        \tremoveHref(parentGetFromFunction);\n" +
                        "        }\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var gif = document.querySelectorAll('._5s61 > ._4o54 > i');\n" +
                        "for (j=0;j<gif.length;j++){\n" +
                        "\tvar child = gif[j];\n" +
                        "\tif (typeof(child.parentNode) !== \"undefined\" \n" +
                        "\t&& typeof(child.parentNode.parentNode.parentNode.parentNode) !== \"undefined\") {\n" +
                        "\t\tvar parent = child.parentNode;\n" +
                        "        style = child.currentStyle || window.getComputedStyle(child, false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "\n" +
                        "        if(typeof(bi.split(\"url=\")[1]) !== \"undefined\"){\n" +
                        "        \tvar parentGetFromFunction = getparent(parent);\n" +
                        "        \tparentGetFromFunction.setAttribute('onClick', 'mJava.getData(\"'+decodeURIComponent(bi.split(\"url=\")[1].split(\"&\")[0])+'\");');\n" +
                        "        \tremoveHref(parentGetFromFunction);\n" +
                        "        }\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n" +
                        "var images = document.querySelectorAll('._5sgg > i');\n" +
                        "for(i=0;i<images.length;i++){\n" +
                        "\tstyle = images[i].currentStyle || window.getComputedStyle(images[i], false);\n" +
                        "        bi = style.backgroundImage.slice(4, -1).replace(/\"/g, \"\");\n" +
                        "        var parent = images[i].parentNode.parentNode;\n" +
                        "\tif(parent.tagName == \"A\"){\n" +
                        "\t\tparent.setAttribute('onClick', 'mJava.getData(\"'+bi+'\");');\n" +
                        "\t\tparent.removeAttribute('href');\n" +
                        "\t\tparent.removeAttribute('data-autoid')\n" +
                        "\t}else if(parent.parentNode.tagName == \"A\"){\n" +
                        "\t\tparent.parentNode.setAttribute('onClick', 'mJava.getData(\"'+bi+'\");');\n" +
                        "\t\tparent.parentNode.removeAttribute('href');\n" +
                        "\t}\n" +
                        "}\n" +
                        "\n"


                        + "})()");
                webo.loadUrl("javascript:( window.onload=prepareVideo;"
                        + ")()");

                //webo.loadUrl("javascript:window.mJava.processHTML(document.getElementsByTagName('HTML')[0].innerHTML);");

            }
        });

        webo.loadUrl(url);

    }


    @JavascriptInterface
    public void getData(String pathvideo){


        if(pathvideo.contains(".mp4")){

            imagina = "";
            video = pathvideo;

        }else{

            video = "";
            imagina = pathvideo;
        }

        mDialog();

    }


    @Override
    public void onResume() {
        super.onResume();
        printLog("onResume Called");

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragg, new BrowseBtnFragment());
                    fr.commit();
                    webo.setVisibility(View.GONE);
                    mprogress.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        printLog("onPause Called");
    }

    public void mDialog(){

        if(!video.isEmpty() || !imagina.isEmpty() || !videoArray.isEmpty()){

            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialoginfo info = new dialoginfo();
            Bundle args = new Bundle();
            args.putString("videoArray", videoArray);
            args.putString("video", video);
            args.putString("image", imagina);
            if(desc.length() > 300){desc=desc.substring(0,300);}
            args.putString("desc", desc);
            info.setArguments(args);
            info.show(fm, "fragment_info");

        }else{

            Toast.makeText(getContext(), "There is No results try again with new Link!", Toast.LENGTH_LONG).show();
        }
    }

    // Frag Life Cycle
    private void printLog(String s) {
// display a message in Log File
        Log.d("LifeCycle:", s);
    }

}