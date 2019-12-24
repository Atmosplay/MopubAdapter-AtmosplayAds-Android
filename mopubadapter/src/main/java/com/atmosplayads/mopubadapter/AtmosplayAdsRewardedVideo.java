package com.atmosplayads.mopubadapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.atmosplayads.AtmosplayAdsSettings;
import com.atmosplayads.AtmosplayRewardVideo;
import com.atmosplayads.listener.AtmosplayAdListener;
import com.atmosplayads.listener.AtmosplayAdLoadListener;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.CustomEventRewardedVideo;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoManager;

import java.util.Map;

import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getAppId;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getChanelId;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getGDPRState;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.getUnitId;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.isAutoLoad;
import static com.atmosplayads.mopubadapter.AtmosplayAdsUtils.shouldRequest_READ_PHONE_STATE;

/**
 * Description: mopub mediation AtmosplayAds rewarded video
 * <p>
 * Created by yfb on 2019/12/18.
 */

public class AtmosplayAdsRewardedVideo extends CustomEventRewardedVideo {
    private static final String TAG = "AtmosplayRewardedVideo";
    private AtmosplayRewardVideo mRewardVider;
    private String adUnitId;

    @Override
    protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        AtmosplayAdsSettings.setGDPRConsent(getGDPRState(serverExtras));
        AtmosplayAdsSettings.enableAutoRequestPermissions(shouldRequest_READ_PHONE_STATE(serverExtras));

        final String appId = getAppId(serverExtras);

        mRewardVider = AtmosplayRewardVideo.init(launcherActivity, appId);
        mRewardVider.setChannelId(getChanelId(serverExtras));
        mRewardVider.setAutoLoadAd(isAutoLoad(serverExtras));
        return true;
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull Activity activity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        adUnitId = getUnitId(serverExtras);
        mRewardVider.loadAd(adUnitId, new AtmosplayAdLoadListener() {
            @Override
            public void onLoadFinished() {
                MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(AtmosplayAdsRewardedVideo.class, adUnitId);
            }

            @Override
            public void onLoadFailed(int i, String s) {
                Log.i(TAG, "onLoadFailed: " + s);
                MoPubRewardedVideoManager.onRewardedVideoLoadFailure(AtmosplayAdsRewardedVideo.class, adUnitId, MoPubErrorCode.MRAID_LOAD_ERROR);
            }
        });
    }

    @Override
    protected boolean hasVideoAvailable() {
        return mRewardVider != null && mRewardVider.isReady(adUnitId);
    }

    @Override
    protected void showVideo() {

        mRewardVider.show(adUnitId, new AtmosplayAdListener() {
            @Override
            public void onVideoStart() {
                MoPubRewardedVideoManager.onRewardedVideoStarted(AtmosplayAdsRewardedVideo.class, adUnitId);
            }

            @Override
            public void onVideoFinished() {
            }

            @Override
            public void onUserEarnedReward() {
                MoPubRewardedVideoManager.onRewardedVideoCompleted(AtmosplayAdsRewardedVideo.class, adUnitId, MoPubReward.success("ZPLAYAds", 1));
            }

            @Override
            public void onLandingPageInstallBtnClicked() {
                MoPubRewardedVideoManager.onRewardedVideoClicked(AtmosplayAdsRewardedVideo.class, adUnitId);
            }

            @Override
            public void onAdClosed() {
                MoPubRewardedVideoManager.onRewardedVideoClosed(AtmosplayAdsRewardedVideo.class, adUnitId);
            }

            @Override
            public void onAdsError(int i, String s) {
                Log.i(TAG, "onAdsError: " + s);
                MoPubRewardedVideoManager.onRewardedVideoPlaybackError(AtmosplayAdsRewardedVideo.class, adUnitId, MoPubErrorCode.VIDEO_PLAYBACK_ERROR);
            }
        });
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return adUnitId;
    }

    @Override
    protected void onInvalidate() {
    }
}
