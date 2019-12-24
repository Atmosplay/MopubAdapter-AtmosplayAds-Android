package com.atmosplayads.mediationmopub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;

/**
 * Description:
 * <p>
 * Created by yfb on 2019/12/18.
 */

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    static final String MOPUB_UNIT_ID_REWARDED_VIDEO = "c2689a580d634d858cb480750f4af3f9";
    static final String MOPUB_UNIT_ID_INTERSTITIAL = "f3c726c915184270be57c0dc9811064b";
    static final String MOPUB_UNIT_ID_BANNER = "0fc6633338d6461db811b79eaf36f983";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(MOPUB_UNIT_ID_REWARDED_VIDEO)
                .build();

        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d(TAG, "onInitializationFinished: ");
            }
        };
    }

    public void showInterstitial(View view) {
        Intent i = new Intent(this, InterstitialActivity.class);
        startActivity(i);
    }

    public void showRewardedVideo(View view) {
        Intent i = new Intent(this, RewardedVideoActivity.class);
        startActivity(i);
    }

    public void showBanner(View view) {
        Intent i = new Intent(this, BannerActivity.class);
        startActivity(i);
    }
}
