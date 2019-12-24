package com.atmosplayads.mopubadapter;

import android.content.Context;
import android.util.Log;

import com.atmosplayads.AtmosplayAdsSettings;
import com.atmosplayads.AtmosplayInterstitial;
import com.atmosplayads.listener.AtmosplayAdLoadListener;
import com.atmosplayads.listener.SimpleAtmosplayAdListener;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getAppId;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getGDPRState;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getUnitId;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.isAutoLoad;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.shouldRequest_READ_PHONE_STATE;

/**
 * Description:
 * <p>
 * Created by yfb on 2019/12/18.
 */

public class AtmosplayAdsInterstitial extends CustomEventInterstitial {
    private static final String TAG = "AtmosplayInterstitial";

    private AtmosplayInterstitial mInterstitial;
    private String adUnitId;

    private CustomEventInterstitialListener mInterstitialListener;

    @Override
    protected void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        AtmosplayAdsSettings.setGDPRConsent(getGDPRState(serverExtras));
        AtmosplayAdsSettings.enableAutoRequestPermissions(shouldRequest_READ_PHONE_STATE(serverExtras));

        mInterstitialListener = customEventInterstitialListener;
        adUnitId = getUnitId(serverExtras);

        final String appId = getAppId(serverExtras);

        mInterstitial = AtmosplayInterstitial.init(context, appId);
        mInterstitial.setAutoLoadAd(isAutoLoad(serverExtras));
        mInterstitial.loadAd(adUnitId, new AtmosplayAdLoadListener() {
            @Override
            public void onLoadFinished() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialLoaded();
                }
            }

            @Override
            public void onLoadFailed(int i, String s) {
                Log.d(TAG, "onLoadFailed: " + s);
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialFailed(MoPubErrorCode.MRAID_LOAD_ERROR);
                }
            }
        });
    }

    @Override
    protected void showInterstitial() {
        mInterstitial.show(adUnitId, new SimpleAtmosplayAdListener() {

            @Override
            public void onLandingPageInstallBtnClicked() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialClicked();
                }
            }

            @Override
            public void onVideoStart() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialShown();
                }
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialDismissed();
                }
            }

            @Override
            public void onAdsError(int i, String s) {
                Log.d(TAG, "onAdsError: " + s);
            }
        });
    }

    @Override
    protected void onInvalidate() {
    }
}
