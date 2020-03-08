package config;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.fbvideodownloader.facebookvideosaver.ConsentSDK;

import android.app.Activity;
import android.widget.LinearLayout;

public class admob {

    public static String publisherID = "pub-6497675273236775";
    public static String admBanner = "ca-app-pub-6497675273236775/6687771395";
	public static String Interstitial = "ca-app-pub-6497675273236775/2748526380";
    public static String privacy_policy_url ="https://sites.google.com/view/xtreemapps/home";
	
	public static void admobBannerCall(Activity acitivty , LinearLayout linerlayout){
		
        AdView adView = new AdView(acitivty);
        adView.setAdUnitId(admBanner);
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(ConsentSDK.getAdRequest(acitivty));
        linerlayout.addView(adView);

	}
	
}