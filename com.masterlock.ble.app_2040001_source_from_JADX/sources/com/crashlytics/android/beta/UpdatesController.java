package com.crashlytics.android.beta;

import android.content.Context;
import p008io.fabric.sdk.android.services.common.CurrentTimeProvider;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;
import p008io.fabric.sdk.android.services.persistence.PreferenceStore;
import p008io.fabric.sdk.android.services.settings.BetaSettingsData;

interface UpdatesController {
    void initialize(Context context, Beta beta, IdManager idManager, BetaSettingsData betaSettingsData, BuildProperties buildProperties, PreferenceStore preferenceStore, CurrentTimeProvider currentTimeProvider, HttpRequestFactory httpRequestFactory);

    boolean isActivityLifecycleTriggered();
}
