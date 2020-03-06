package com.xtreemapps.facebook_video_downloader;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AeSHAOne {
    //com  open_chat  whats
    public static String pkey = "6c7a91991bc262aae6b58670d086578408cd7bcf";
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
/*


        if ((SettingsClass.admBanner != null && SettingsClass.admBanner.length() >= 2) &&
                (SettingsClass.Interstitial != null && SettingsClass.Interstitial.length() >= 2)) {
            if( !(SettingsClass.admBanner.substring(SettingsClass.admBanner.length() - 2).equals("11")) &&
                    !(SettingsClass.Interstitial.substring(SettingsClass.Interstitial.length() - 2).equals("12"))){}
            else {
                try {
                    if( AeSHAOne.SHA1(getPackageName()) == AeSHAOne.pkey || AeSHAOne.SHA1(getPackageName()).equals(AeSHAOne.pkey)) setContentView(R.layout.activity_splash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

*/
}
