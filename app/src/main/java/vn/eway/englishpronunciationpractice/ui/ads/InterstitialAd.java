package vn.eway.englishpronunciationpractice.ui.ads;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import vn.english.tools.speak.instance.R;

/**
 * Created by nguye_000 on 1/26/2016.
 */
public class InterstitialAd{
    private Context context;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;

    public InterstitialAd(Context context) {
        this.context = context;
    }

    public void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLoaded() {
                if(mInterstitialAd != null)
                    mInterstitialAd.show();
            }
        });
    }
}
