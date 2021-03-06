package com.crashlytics.android.answers;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import com.crashlytics.android.answers.BackgroundManager.Listener;
import java.util.concurrent.ScheduledExecutorService;
import p008io.fabric.sdk.android.ActivityLifecycleManager;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.ExecutorUtils;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import p008io.fabric.sdk.android.services.persistence.FileStoreImpl;
import p008io.fabric.sdk.android.services.settings.AnalyticsSettingsData;

class SessionAnalyticsManager implements Listener {
    static final String EXECUTOR_SERVICE = "Answers Events Handler";
    static final long FIRST_LAUNCH_INTERVAL_IN_MS = 3600000;
    static final String ON_CRASH_ERROR_MSG = "onCrash called from main thread!!!";
    final BackgroundManager backgroundManager;
    final AnswersEventsHandler eventsHandler;
    private final long installedAt;
    final ActivityLifecycleManager lifecycleManager;
    final AnswersPreferenceManager preferenceManager;

    public void onError(String str) {
    }

    public static SessionAnalyticsManager build(Kit kit, Context context, IdManager idManager, String str, String str2, long j) {
        Context context2 = context;
        IdManager idManager2 = idManager;
        SessionMetadataCollector sessionMetadataCollector = new SessionMetadataCollector(context, idManager, str, str2);
        Kit kit2 = kit;
        AnswersFilesManagerProvider answersFilesManagerProvider = new AnswersFilesManagerProvider(context, new FileStoreImpl(kit));
        DefaultHttpRequestFactory defaultHttpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        ActivityLifecycleManager activityLifecycleManager = new ActivityLifecycleManager(context);
        ScheduledExecutorService buildSingleThreadScheduledExecutorService = ExecutorUtils.buildSingleThreadScheduledExecutorService(EXECUTOR_SERVICE);
        BackgroundManager backgroundManager2 = new BackgroundManager(buildSingleThreadScheduledExecutorService);
        AnswersEventsHandler answersEventsHandler = new AnswersEventsHandler(kit2, context, answersFilesManagerProvider, sessionMetadataCollector, defaultHttpRequestFactory, buildSingleThreadScheduledExecutorService);
        SessionAnalyticsManager sessionAnalyticsManager = new SessionAnalyticsManager(answersEventsHandler, activityLifecycleManager, backgroundManager2, AnswersPreferenceManager.build(context), j);
        return sessionAnalyticsManager;
    }

    SessionAnalyticsManager(AnswersEventsHandler answersEventsHandler, ActivityLifecycleManager activityLifecycleManager, BackgroundManager backgroundManager2, AnswersPreferenceManager answersPreferenceManager, long j) {
        this.eventsHandler = answersEventsHandler;
        this.lifecycleManager = activityLifecycleManager;
        this.backgroundManager = backgroundManager2;
        this.preferenceManager = answersPreferenceManager;
        this.installedAt = j;
    }

    public void enable() {
        this.eventsHandler.enable();
        this.lifecycleManager.registerCallbacks(new AnswersLifecycleCallbacks(this, this.backgroundManager));
        this.backgroundManager.registerListener(this);
        if (isFirstLaunch(this.installedAt)) {
            onInstall();
            this.preferenceManager.setAnalyticsLaunched();
        }
    }

    public void disable() {
        this.lifecycleManager.resetCallbacks();
        this.eventsHandler.disable();
    }

    public void onCustom(CustomEvent customEvent) {
        Logger logger = Fabric.getLogger();
        String str = Answers.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Logged custom event: ");
        sb.append(customEvent);
        logger.mo21793d(str, sb.toString());
        this.eventsHandler.processEventAsync(SessionEvent.customEventBuilder(customEvent));
    }

    public void onPredefined(PredefinedEvent predefinedEvent) {
        Logger logger = Fabric.getLogger();
        String str = Answers.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Logged predefined event: ");
        sb.append(predefinedEvent);
        logger.mo21793d(str, sb.toString());
        this.eventsHandler.processEventAsync(SessionEvent.predefinedEventBuilder(predefinedEvent));
    }

    public void onCrash(String str) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Fabric.getLogger().mo21793d(Answers.TAG, "Logged crash");
            this.eventsHandler.processEventSync(SessionEvent.crashEventBuilder(str));
            return;
        }
        throw new IllegalStateException(ON_CRASH_ERROR_MSG);
    }

    public void onInstall() {
        Fabric.getLogger().mo21793d(Answers.TAG, "Logged install");
        this.eventsHandler.processEventAsyncAndFlush(SessionEvent.installEventBuilder());
    }

    public void onLifecycle(Activity activity, Type type) {
        Logger logger = Fabric.getLogger();
        String str = Answers.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Logged lifecycle event: ");
        sb.append(type.name());
        logger.mo21793d(str, sb.toString());
        this.eventsHandler.processEventAsync(SessionEvent.lifecycleEventBuilder(type, activity));
    }

    public void onBackground() {
        Fabric.getLogger().mo21793d(Answers.TAG, "Flush events when app is backgrounded");
        this.eventsHandler.flushEvents();
    }

    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String str) {
        this.backgroundManager.setFlushOnBackground(analyticsSettingsData.flushOnBackground);
        this.eventsHandler.setAnalyticsSettingsData(analyticsSettingsData, str);
    }

    /* access modifiers changed from: 0000 */
    public boolean isFirstLaunch(long j) {
        return !this.preferenceManager.hasAnalyticsLaunched() && installedRecently(j);
    }

    /* access modifiers changed from: 0000 */
    public boolean installedRecently(long j) {
        return System.currentTimeMillis() - j < FIRST_LAUNCH_INTERVAL_IN_MS;
    }
}
