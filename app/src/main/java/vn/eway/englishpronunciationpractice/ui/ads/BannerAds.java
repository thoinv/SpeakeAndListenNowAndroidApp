package vn.eway.englishpronunciationpractice.ui.ads;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import vn.english.tools.speak.instance.R;

/**
 * Created by nguye_000 on 1/26/2016.
 */
public class BannerAds extends LinearLayout {
    private AdView adView;
    private View rootView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BannerAds(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BannerAds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BannerAds(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        rootView = LinearLayout.inflate(getContext(), R.layout.ad_banner_view, null);
        adView = (AdView) rootView.findViewById(R.id.adView);
        addView(rootView);
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }
}
