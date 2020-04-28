package dialog;


import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fbvideodownloader.facebookvideosaver.MainActivity;
import com.fbvideodownloader.facebookvideosaver.R;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.squareup.picasso.Picasso;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;

import config.admob;
import func.reg;

/**
 * Created by mac on 28/01/16.
 */
public class dialoginfo extends DialogFragment{

    private Button stream, saveVideo ;
    private ImageButton cancel;
    private Uri downloadUri , destinationUri;
    private RetryPolicy retryPolicy;
    private EditText saveas;
    private TextView tsave;
    private String video = "", saveasvideo = "" ;
    private DownloadRequest downloadRequest;
    private String mMediaPath = "" , mBaseFolderPath;
    private static final int REQUEST_WRITE_STORAGE = 112;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.dialoginfo, container,
                false);

        saveas = (EditText)rootView.findViewById(R.id.saveas2);
        saveVideo = (Button)rootView.findViewById(R.id.button);
        cancel = (ImageButton)rootView.findViewById(R.id.cancel);
        tsave = (TextView)rootView.findViewById(R.id.textView3);
        stream = (Button)rootView.findViewById(R.id.stream);


        preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), Context.MODE_PRIVATE);

        video = getArguments().getString("video");


        if(!video.isEmpty()){

            saveasvideo = reg.getBack(video,"([^/]+$)");
            if(saveasvideo.contains("?")){

                saveasvideo = reg.getBack(saveasvideo, "(^[^?]+)");
            }

            saveasvideo = reg.getBack(saveasvideo, "(((?!(.mp4)).)*)");

            saveas.setText(saveasvideo);

        }else{

            saveVideo.setVisibility(View.GONE);
            stream.setVisibility(View.GONE);

        }

        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                func.player.mPlayerStream(video , getActivity());
            }
        });

        saveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                admob.ad_count++;
                if(admob.ad_count >= 4){
                    MainActivity.displayInterstitial();
                    admob.ad_count = 0;
                }

                if(Build.VERSION.SDK_INT >= 23){

                    boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                    }else{

                        downloadManager(video , "video" , saveVideo);

                    }


                }else{

                    downloadManager(video, "video", saveVideo);

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // Native ads
        MobileAds.initialize(getContext());
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), admob.native_unit);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                TemplateView templateView = rootView.findViewById(R.id.my_template_4);
                templateView.setNativeAd(unifiedNativeAd);

            }
        });
        AdLoader adLoader = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void downloadManager(String vid_url , final String type , final Button btn){

        String folderName = getResources().getString(R.string.foldername);

        try {

            if(!preferences.getString("path", "DEFAULT").equals("DEFAULT")){

                mBaseFolderPath = preferences
                        .getString("path", "DEFAULT");

            }else{

                mBaseFolderPath = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + folderName + File.separator;
            }

            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }

            String name = reg.getBack(vid_url, "/([^/]+)$");

            if(type.equals("video") && !saveas.getText().toString().isEmpty()){

                name = saveas.getText().toString() + ".mp4";

            }

            mMediaPath = mBaseFolderPath + File.separator + name;

            saveas.setVisibility(View.GONE);
            tsave.setVisibility(View.GONE);

            retryPolicy = new DefaultRetryPolicy();


            downloadUri = Uri.parse(vid_url);
            destinationUri = Uri.parse("file://" + mMediaPath);


            if(preferences.getBoolean(getResources().getString(R.string.pref_hidenotification) , false)){

                DownloadManager.Request req = new DownloadManager.Request(downloadUri);
                req.setDestinationUri(destinationUri)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .allowScanningByMediaScanner();

                DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().getApplicationContext().DOWNLOAD_SERVICE);
                dm.enqueue(req);

                Toast toast = Toast.makeText(getActivity(),
                        "Download Started",
                        Toast.LENGTH_SHORT);
                toast.show();
                getDialog().dismiss();


            }else{

                downloadRequest = new DownloadRequest(downloadUri)
                        .addCustomHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setRetryPolicy(retryPolicy)
                        .setDownloadContext("Download1")
                        .setStatusListener(new MyDownloadDownloadStatusListenerV1());

                ThinDownloadManager downloadManager = new ThinDownloadManager();


                int downloadId = downloadManager.add(downloadRequest);

                btn.setVisibility(View.GONE);

                /**
                 * Change Text download SaveVideo to Cancel button by setText(""); for user
                 * to be able to cancel download
                 *
                 int status = downloadManager.cancel(downloadId);
                 saveVideo.setText("Cancel");

                 **/

            }


        } catch (Exception e) {

            if(preferences.getBoolean(getResources().getString(R.string.pref_hidenotification) , false)){

                try {

                    downloadRequest = new DownloadRequest(downloadUri)
                            .addCustomHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setRetryPolicy(retryPolicy)
                            .setDownloadContext("Download1")
                            .setStatusListener(new MyDownloadDownloadStatusListenerV1());

                    ThinDownloadManager downloadManager = new ThinDownloadManager();


                    int downloadId = downloadManager.add(downloadRequest);
                    btn.setVisibility(View.GONE);

                }catch(Exception j){

                    Log.e("trace",j.getMessage());

                    Toast toast = Toast.makeText(getActivity(),
                            "Download Failed",
                            Toast.LENGTH_SHORT);
                    toast.show();

                }

            }else{

                Log.e("trace",e.getMessage());

                Toast toast = Toast.makeText(getActivity(),
                        "Download Failed",
                        Toast.LENGTH_SHORT);
                toast.show();

            }

        }

        //Video_Managers.IsRUN = false;


    }

    class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {


        @Override
        public void onDownloadComplete(DownloadRequest downloadRequest) {


            try{
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(getActivity(), new String[] {mMediaPath }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
//                                Log.i("ExternalStorage", "Scanned " + path + ":");
//                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });

            }catch(Exception e){
            }

        }

        @Override
        public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
            final int id = request.getDownloadId();

        }

        @Override
        public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
            int id = request.getDownloadId();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(getActivity(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}



