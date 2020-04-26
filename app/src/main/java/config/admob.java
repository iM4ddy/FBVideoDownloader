package config;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.fbvideodownloader.facebookvideosaver.ConsentSDK;

import android.app.Activity;
import android.widget.LinearLayout;

public class admob {

    public static int ad_count = 0;

    public static String publisherID = "pub-5260818953194377";

     /* ============= my real ads ============= */

    public static String admBanner = "ca-app-pub-5260818953194377/7856356130";

    public static String Interstitial = "ca-app-pub-5260818953194377/2607898889";

    public static String native_unit = "ca-app-pub-5260818953194377/7668653871";

    /* ============= my Test ads ============= */

    /*public static String admBanner = "ca-app-pub-3940256099942544/6300978111";

    public static String Interstitial = "ca-app-pub-3940256099942544/1033173712";

    public static String native_unit = "ca-app-pub-3940256099942544/2247696110";*/


    public static String privacy_policy_url ="https://sites.google.com/view/xtreemapps/home";
	
	public static void admobBannerCall(Activity acitivty , LinearLayout linerlayout){
		
        AdView adView = new AdView(acitivty);
        adView.setAdUnitId(admBanner);
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(ConsentSDK.getAdRequest(acitivty));
        linerlayout.addView(adView);

	}
	
}