package com.crashlytics.android.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.ScrollView;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.internal.CrashEventDataProvider;
import com.crashlytics.android.core.internal.models.SessionEventData;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HttpsURLConnection;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.ApiKey;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.Crash.FatalException;
import p008io.fabric.sdk.android.services.common.Crash.LoggedException;
import p008io.fabric.sdk.android.services.common.ExecutorUtils;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.concurrency.DependsOn;
import p008io.fabric.sdk.android.services.concurrency.Priority;
import p008io.fabric.sdk.android.services.concurrency.PriorityCallable;
import p008io.fabric.sdk.android.services.concurrency.Task;
import p008io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import p008io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;
import p008io.fabric.sdk.android.services.persistence.FileStore;
import p008io.fabric.sdk.android.services.persistence.FileStoreImpl;
import p008io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import p008io.fabric.sdk.android.services.settings.PromptSettingsData;
import p008io.fabric.sdk.android.services.settings.SessionSettingsData;
import p008io.fabric.sdk.android.services.settings.Settings;
import p008io.fabric.sdk.android.services.settings.Settings.SettingsAccess;
import p008io.fabric.sdk.android.services.settings.SettingsData;

@DependsOn({CrashEventDataProvider.class})
public class CrashlyticsCore extends Kit<Void> {
    static final float CLS_DEFAULT_PROCESS_DELAY = 1.0f;
    static final String COLLECT_CUSTOM_KEYS = "com.crashlytics.CollectCustomKeys";
    static final String COLLECT_CUSTOM_LOGS = "com.crashlytics.CollectCustomLogs";
    static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    static final String CRASHLYTICS_REQUIRE_BUILD_ID = "com.crashlytics.RequireBuildId";
    static final boolean CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT = true;
    static final String CRASH_MARKER_FILE_NAME = "crash_marker";
    static final int DEFAULT_MAIN_HANDLER_TIMEOUT_SEC = 4;
    private static final String INITIALIZATION_MARKER_FILE_NAME = "initialization_marker";
    static final int MAX_ATTRIBUTES = 64;
    static final int MAX_ATTRIBUTE_SIZE = 1024;
    private static final String PREF_ALWAYS_SEND_REPORTS_KEY = "always_send_reports_opt_in";
    private static final boolean SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT = false;
    public static final String TAG = "CrashlyticsCore";
    private String apiKey;
    private final ConcurrentHashMap<String, String> attributes;
    private String buildId;
    private CrashlyticsFileMarker crashMarker;
    private float delay;
    private boolean disabled;
    private CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private CrashEventDataProvider externalCrashEventDataProvider;
    private FileStore fileStore;
    private CrashlyticsUncaughtExceptionHandler handler;
    private HttpRequestFactory httpRequestFactory;
    /* access modifiers changed from: private */
    public CrashlyticsFileMarker initializationMarker;
    private String installerPackageName;
    private CrashlyticsListener listener;
    private String packageName;
    private final PinningInfoProvider pinningInfo;
    private File sdkDir;
    private final long startTime;
    private String userEmail;
    private String userId;
    private String userName;
    private String versionCode;
    private String versionName;

    public static class Builder {
        private float delay = -1.0f;
        private boolean disabled = false;
        private CrashlyticsListener listener;
        private PinningInfoProvider pinningInfoProvider;

        public Builder delay(float f) {
            if (f <= 0.0f) {
                throw new IllegalArgumentException("delay must be greater than 0");
            } else if (this.delay <= 0.0f) {
                this.delay = f;
                return this;
            } else {
                throw new IllegalStateException("delay already set.");
            }
        }

        public Builder listener(CrashlyticsListener crashlyticsListener) {
            if (crashlyticsListener == null) {
                throw new IllegalArgumentException("listener must not be null.");
            } else if (this.listener == null) {
                this.listener = crashlyticsListener;
                return this;
            } else {
                throw new IllegalStateException("listener already set.");
            }
        }

        @Deprecated
        public Builder pinningInfo(PinningInfoProvider pinningInfoProvider2) {
            if (pinningInfoProvider2 == null) {
                throw new IllegalArgumentException("pinningInfoProvider must not be null.");
            } else if (this.pinningInfoProvider == null) {
                this.pinningInfoProvider = pinningInfoProvider2;
                return this;
            } else {
                throw new IllegalStateException("pinningInfoProvider already set.");
            }
        }

        public Builder disabled(boolean z) {
            this.disabled = z;
            return this;
        }

        public CrashlyticsCore build() {
            if (this.delay < 0.0f) {
                this.delay = CrashlyticsCore.CLS_DEFAULT_PROCESS_DELAY;
            }
            return new CrashlyticsCore(this.delay, this.listener, this.pinningInfoProvider, this.disabled);
        }
    }

    private static final class CrashMarkerCheck implements Callable<Boolean> {
        private final CrashlyticsFileMarker crashMarker;

        public CrashMarkerCheck(CrashlyticsFileMarker crashlyticsFileMarker) {
            this.crashMarker = crashlyticsFileMarker;
        }

        public Boolean call() throws Exception {
            if (!this.crashMarker.isPresent()) {
                return Boolean.FALSE;
            }
            Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Found previous crash marker.");
            this.crashMarker.remove();
            return Boolean.TRUE;
        }
    }

    private static final class NoOpListener implements CrashlyticsListener {
        public void crashlyticsDidDetectCrashDuringPreviousExecution() {
        }

        private NoOpListener() {
        }
    }

    private static class OptInLatch {
        private final CountDownLatch latch;
        private boolean send;

        private OptInLatch() {
            this.send = false;
            this.latch = new CountDownLatch(1);
        }

        /* access modifiers changed from: 0000 */
        public void setOptIn(boolean z) {
            this.send = z;
            this.latch.countDown();
        }

        /* access modifiers changed from: 0000 */
        public boolean getOptIn() {
            return this.send;
        }

        /* access modifiers changed from: 0000 */
        public void await() {
            try {
                this.latch.await();
            } catch (InterruptedException unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public static int dipsToPixels(float f, int i) {
        return (int) (f * ((float) i));
    }

    public String getIdentifier() {
        return "com.crashlytics.sdk.android.crashlytics-core";
    }

    public String getVersion() {
        return "2.3.8.97";
    }

    public CrashlyticsCore() {
        this(CLS_DEFAULT_PROCESS_DELAY, null, null, false);
    }

    CrashlyticsCore(float f, CrashlyticsListener crashlyticsListener, PinningInfoProvider pinningInfoProvider, boolean z) {
        this(f, crashlyticsListener, pinningInfoProvider, z, ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
    }

    CrashlyticsCore(float f, CrashlyticsListener crashlyticsListener, PinningInfoProvider pinningInfoProvider, boolean z, ExecutorService executorService) {
        this.userId = null;
        this.userEmail = null;
        this.userName = null;
        this.delay = f;
        if (crashlyticsListener == null) {
            crashlyticsListener = new NoOpListener();
        }
        this.listener = crashlyticsListener;
        this.pinningInfo = pinningInfoProvider;
        this.disabled = z;
        this.executorServiceWrapper = new CrashlyticsExecutorServiceWrapper(executorService);
        this.attributes = new ConcurrentHashMap<>();
        this.startTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public boolean onPreExecute() {
        return onPreExecute(super.getContext());
    }

    /* access modifiers changed from: 0000 */
    public boolean onPreExecute(Context context) {
        if (this.disabled) {
            return false;
        }
        this.apiKey = new ApiKey().getValue(context);
        if (this.apiKey == null) {
            return false;
        }
        Logger logger = Fabric.getLogger();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Initializing Crashlytics ");
        sb.append(getVersion());
        logger.mo21798i(str, sb.toString());
        this.fileStore = new FileStoreImpl(this);
        this.crashMarker = new CrashlyticsFileMarker(CRASH_MARKER_FILE_NAME, this.fileStore);
        this.initializationMarker = new CrashlyticsFileMarker(INITIALIZATION_MARKER_FILE_NAME, this.fileStore);
        try {
            setAndValidateKitProperties(context, this.apiKey);
            ManifestUnityVersionProvider manifestUnityVersionProvider = new ManifestUnityVersionProvider(context, getPackageName());
            boolean didPreviousInitializationFail = didPreviousInitializationFail();
            checkForPreviousCrash();
            installExceptionHandler(manifestUnityVersionProvider);
            if (!didPreviousInitializationFail || !CommonUtils.canTryConnection(context)) {
                return true;
            }
            finishInitSynchronously();
            return false;
        } catch (CrashlyticsMissingDependencyException e) {
            throw new UnmetDependencyException((Throwable) e);
        } catch (Exception e2) {
            Fabric.getLogger().mo21796e(TAG, "Crashlytics was not started due to an exception during initialization", e2);
            return false;
        }
    }

    private void setAndValidateKitProperties(Context context, String str) throws NameNotFoundException {
        PinningInfoProvider pinningInfoProvider = this.pinningInfo;
        CrashlyticsPinningInfoProvider crashlyticsPinningInfoProvider = pinningInfoProvider != null ? new CrashlyticsPinningInfoProvider(pinningInfoProvider) : null;
        this.httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        this.httpRequestFactory.setPinningInfoProvider(crashlyticsPinningInfoProvider);
        this.packageName = context.getPackageName();
        this.installerPackageName = getIdManager().getInstallerPackageName();
        Logger logger = Fabric.getLogger();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Installer package name is: ");
        sb.append(this.installerPackageName);
        logger.mo21793d(str2, sb.toString());
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(this.packageName, 0);
        this.versionCode = Integer.toString(packageInfo.versionCode);
        this.versionName = packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName;
        this.buildId = CommonUtils.resolveBuildId(context);
        getBuildIdValidator(this.buildId, isRequiringBuildId(context)).validate(str, this.packageName);
    }

    private void installExceptionHandler(UnityVersionProvider unityVersionProvider) {
        try {
            Fabric.getLogger().mo21793d(TAG, "Installing exception handler...");
            CrashlyticsUncaughtExceptionHandler crashlyticsUncaughtExceptionHandler = new CrashlyticsUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this.executorServiceWrapper, getIdManager(), unityVersionProvider, this.fileStore, this);
            this.handler = crashlyticsUncaughtExceptionHandler;
            this.handler.openSession();
            Thread.setDefaultUncaughtExceptionHandler(this.handler);
            Fabric.getLogger().mo21793d(TAG, "Successfully installed exception handler.");
        } catch (Exception e) {
            Fabric.getLogger().mo21796e(TAG, "There was a problem installing the exception handler.", e);
        }
    }

    /* access modifiers changed from: protected */
    public Void doInBackground() {
        markInitializationStarted();
        this.handler.cleanInvalidTempFiles();
        try {
            SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
            if (awaitSettingsData == null) {
                Fabric.getLogger().mo21806w(TAG, "Received null settings, skipping initialization!");
                markInitializationComplete();
                return null;
            } else if (!awaitSettingsData.featuresData.collectReports) {
                Fabric.getLogger().mo21793d(TAG, "Collection of crash reports disabled in Crashlytics settings.");
                markInitializationComplete();
                return null;
            } else {
                this.handler.finalizeSessions();
                CreateReportSpiCall createReportSpiCall = getCreateReportSpiCall(awaitSettingsData);
                if (createReportSpiCall == null) {
                    Fabric.getLogger().mo21806w(TAG, "Unable to create a call to upload reports.");
                    markInitializationComplete();
                    return null;
                }
                new ReportUploader(createReportSpiCall).uploadReports(this.delay);
                markInitializationComplete();
                return null;
            }
        } catch (Exception e) {
            Fabric.getLogger().mo21796e(TAG, "Crashlytics encountered a problem during asynchronous initialization.", e);
        } catch (Throwable th) {
            markInitializationComplete();
            throw th;
        }
    }

    public static CrashlyticsCore getInstance() {
        return (CrashlyticsCore) Fabric.getKit(CrashlyticsCore.class);
    }

    public PinningInfoProvider getPinningInfoProvider() {
        if (!this.disabled) {
            return this.pinningInfo;
        }
        return null;
    }

    public void logException(Throwable th) {
        if (this.disabled || !ensureFabricWithCalled("prior to logging exceptions.")) {
            return;
        }
        if (th == null) {
            Fabric.getLogger().log(5, TAG, "Crashlytics is ignoring a request to log a null exception.");
        } else {
            this.handler.writeNonFatalException(Thread.currentThread(), th);
        }
    }

    public void log(String str) {
        doLog(3, TAG, str);
    }

    private void doLog(int i, String str, String str2) {
        if (!this.disabled && ensureFabricWithCalled("prior to logging messages.")) {
            this.handler.writeToLog(System.currentTimeMillis() - this.startTime, formatLogMessage(i, str, str2));
        }
    }

    public void log(int i, String str, String str2) {
        doLog(i, str, str2);
        Logger logger = Fabric.getLogger();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(str2);
        logger.log(i, sb2, sb3.toString(), true);
    }

    public void setUserIdentifier(String str) {
        if (!this.disabled) {
            this.userId = sanitizeAttribute(str);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserName(String str) {
        if (!this.disabled) {
            this.userName = sanitizeAttribute(str);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserEmail(String str) {
        if (!this.disabled) {
            this.userEmail = sanitizeAttribute(str);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setString(String str, String str2) {
        String str3;
        if (!this.disabled) {
            if (str == null) {
                Context context = getContext();
                if (context == null || !CommonUtils.isAppDebuggable(context)) {
                    Fabric.getLogger().mo21796e(TAG, "Attempting to set custom attribute with null key, ignoring.", null);
                    return;
                }
                throw new IllegalArgumentException("Custom attribute key must not be null.");
            }
            String sanitizeAttribute = sanitizeAttribute(str);
            if (this.attributes.size() < 64 || this.attributes.containsKey(sanitizeAttribute)) {
                if (str2 == null) {
                    str3 = "";
                } else {
                    str3 = sanitizeAttribute(str2);
                }
                this.attributes.put(sanitizeAttribute, str3);
                this.handler.cacheKeyData(this.attributes);
                return;
            }
            Fabric.getLogger().mo21793d(TAG, "Exceeded maximum number of custom attributes (64)");
        }
    }

    public void setBool(String str, boolean z) {
        setString(str, Boolean.toString(z));
    }

    public void setDouble(String str, double d) {
        setString(str, Double.toString(d));
    }

    public void setFloat(String str, float f) {
        setString(str, Float.toString(f));
    }

    public void setInt(String str, int i) {
        setString(str, Integer.toString(i));
    }

    public void setLong(String str, long j) {
        setString(str, Long.toString(j));
    }

    public void crash() {
        new CrashTest().indexOutOfBounds();
    }

    public boolean verifyPinning(URL url) {
        try {
            return internalVerifyPinning(url);
        } catch (Exception e) {
            Fabric.getLogger().mo21796e(TAG, "Could not verify SSL pinning", e);
            return false;
        }
    }

    @Deprecated
    public synchronized void setListener(CrashlyticsListener crashlyticsListener) {
        Fabric.getLogger().mo21806w(TAG, "Use of setListener is deprecated.");
        if (crashlyticsListener != null) {
            this.listener = crashlyticsListener;
        } else {
            throw new IllegalArgumentException("listener must not be null.");
        }
    }

    static void recordLoggedExceptionEvent(String str) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new LoggedException(str));
        }
    }

    static void recordFatalExceptionEvent(String str) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new FatalException(str));
        }
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    /* access modifiers changed from: 0000 */
    public BuildIdValidator getBuildIdValidator(String str, boolean z) {
        return new BuildIdValidator(str, z);
    }

    /* access modifiers changed from: 0000 */
    public String getPackageName() {
        return this.packageName;
    }

    /* access modifiers changed from: 0000 */
    public String getApiKey() {
        return this.apiKey;
    }

    /* access modifiers changed from: 0000 */
    public String getInstallerPackageName() {
        return this.installerPackageName;
    }

    /* access modifiers changed from: 0000 */
    public String getVersionName() {
        return this.versionName;
    }

    /* access modifiers changed from: 0000 */
    public String getVersionCode() {
        return this.versionCode;
    }

    /* access modifiers changed from: 0000 */
    public String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }

    /* access modifiers changed from: 0000 */
    public String getBuildId() {
        return this.buildId;
    }

    /* access modifiers changed from: 0000 */
    public CrashlyticsUncaughtExceptionHandler getHandler() {
        return this.handler;
    }

    /* access modifiers changed from: 0000 */
    public String getUserIdentifier() {
        if (getIdManager().canCollectUserIds()) {
            return this.userId;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public String getUserEmail() {
        if (getIdManager().canCollectUserIds()) {
            return this.userEmail;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public String getUserName() {
        if (getIdManager().canCollectUserIds()) {
            return this.userName;
        }
        return null;
    }

    private void finishInitSynchronously() {
        C04201 r0 = new PriorityCallable<Void>() {
            public Void call() throws Exception {
                return CrashlyticsCore.this.doInBackground();
            }

            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        for (Task addDependency : getDependencies()) {
            r0.addDependency(addDependency);
        }
        Future submit = getFabric().getExecutorService().submit(r0);
        Fabric.getLogger().mo21793d(TAG, "Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            submit.get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Fabric.getLogger().mo21796e(TAG, "Crashlytics was interrupted during initialization.", e);
        } catch (ExecutionException e2) {
            Fabric.getLogger().mo21796e(TAG, "Problem encountered during Crashlytics initialization.", e2);
        } catch (TimeoutException e3) {
            Fabric.getLogger().mo21796e(TAG, "Crashlytics timed out during initialization.", e3);
        }
    }

    /* access modifiers changed from: 0000 */
    public void markInitializationStarted() {
        this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() {
            public Void call() throws Exception {
                CrashlyticsCore.this.initializationMarker.create();
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Initialization marker file created.");
                return null;
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void markInitializationComplete() {
        this.executorServiceWrapper.executeAsync((Callable<T>) new Callable<Boolean>() {
            public Boolean call() throws Exception {
                try {
                    boolean remove = CrashlyticsCore.this.initializationMarker.remove();
                    Logger logger = Fabric.getLogger();
                    String str = CrashlyticsCore.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Initialization marker file removed: ");
                    sb.append(remove);
                    logger.mo21793d(str, sb.toString());
                    return Boolean.valueOf(remove);
                } catch (Exception e) {
                    Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Problem encountered deleting Crashlytics initialization marker.", e);
                    return Boolean.valueOf(false);
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public boolean didPreviousInitializationFail() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(CrashlyticsCore.this.initializationMarker.isPresent());
            }
        })).booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public void setExternalCrashEventDataProvider(CrashEventDataProvider crashEventDataProvider) {
        this.externalCrashEventDataProvider = crashEventDataProvider;
    }

    /* access modifiers changed from: 0000 */
    public SessionEventData getExternalCrashEventData() {
        CrashEventDataProvider crashEventDataProvider = this.externalCrashEventDataProvider;
        if (crashEventDataProvider != null) {
            return crashEventDataProvider.getCrashEventData();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public boolean internalVerifyPinning(URL url) {
        if (getPinningInfoProvider() == null) {
            return false;
        }
        HttpRequest buildHttpRequest = this.httpRequestFactory.buildHttpRequest(HttpMethod.GET, url.toString());
        ((HttpsURLConnection) buildHttpRequest.getConnection()).setInstanceFollowRedirects(false);
        buildHttpRequest.code();
        return true;
    }

    /* access modifiers changed from: 0000 */
    public File getSdkDirectory() {
        if (this.sdkDir == null) {
            this.sdkDir = new FileStoreImpl(this).getFilesDir();
        }
        return this.sdkDir;
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldPromptUserBeforeSendingCrashReports() {
        return ((Boolean) Settings.getInstance().withSettings(new SettingsAccess<Boolean>() {
            public Boolean usingSettings(SettingsData settingsData) {
                if (settingsData.featuresData.promptEnabled) {
                    return Boolean.valueOf(!CrashlyticsCore.this.shouldSendReportsWithoutPrompting());
                }
                return Boolean.valueOf(false);
            }
        }, Boolean.valueOf(false))).booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldSendReportsWithoutPrompting() {
        return new PreferenceStoreImpl(this).get().getBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, false);
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"CommitPrefEdits"})
    public void setShouldSendUserReportsWithoutPrompting(boolean z) {
        PreferenceStoreImpl preferenceStoreImpl = new PreferenceStoreImpl(this);
        preferenceStoreImpl.save(preferenceStoreImpl.edit().putBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, z));
    }

    /* access modifiers changed from: 0000 */
    public boolean canSendWithUserApproval() {
        return ((Boolean) Settings.getInstance().withSettings(new SettingsAccess<Boolean>() {
            public Boolean usingSettings(SettingsData settingsData) {
                Activity currentActivity = CrashlyticsCore.this.getFabric().getCurrentActivity();
                return Boolean.valueOf((currentActivity == null || currentActivity.isFinishing() || !CrashlyticsCore.this.shouldPromptUserBeforeSendingCrashReports()) ? true : CrashlyticsCore.this.getSendDecisionFromUser(currentActivity, settingsData.promptData));
            }
        }, Boolean.valueOf(true))).booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public CreateReportSpiCall getCreateReportSpiCall(SettingsData settingsData) {
        if (settingsData != null) {
            return new DefaultCreateReportSpiCall(this, getOverridenSpiEndpoint(), settingsData.appData.reportsUrl, this.httpRequestFactory);
        }
        return null;
    }

    private void checkForPreviousCrash() {
        if (Boolean.TRUE.equals((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new CrashMarkerCheck(this.crashMarker)))) {
            try {
                this.listener.crashlyticsDidDetectCrashDuringPreviousExecution();
            } catch (Exception e) {
                Fabric.getLogger().mo21796e(TAG, "Exception thrown by CrashlyticsListener while notifying of previous crash.", e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void createCrashMarker() {
        this.crashMarker.create();
    }

    /* access modifiers changed from: private */
    public boolean getSendDecisionFromUser(Activity activity, PromptSettingsData promptSettingsData) {
        final DialogStringResolver dialogStringResolver = new DialogStringResolver(activity, promptSettingsData);
        OptInLatch optInLatch = new OptInLatch();
        final Activity activity2 = activity;
        final OptInLatch optInLatch2 = optInLatch;
        final PromptSettingsData promptSettingsData2 = promptSettingsData;
        C04267 r0 = new Runnable() {
            public void run() {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity2);
                C04271 r1 = new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        optInLatch2.setOptIn(true);
                        dialogInterface.dismiss();
                    }
                };
                float f = activity2.getResources().getDisplayMetrics().density;
                int access$400 = CrashlyticsCore.dipsToPixels(f, 5);
                TextView textView = new TextView(activity2);
                textView.setAutoLinkMask(15);
                textView.setText(dialogStringResolver.getMessage());
                textView.setTextAppearance(activity2, 16973892);
                textView.setPadding(access$400, access$400, access$400, access$400);
                textView.setFocusable(false);
                ScrollView scrollView = new ScrollView(activity2);
                scrollView.setPadding(CrashlyticsCore.dipsToPixels(f, 14), CrashlyticsCore.dipsToPixels(f, 2), CrashlyticsCore.dipsToPixels(f, 10), CrashlyticsCore.dipsToPixels(f, 12));
                scrollView.addView(textView);
                builder.setView(scrollView).setTitle(dialogStringResolver.getTitle()).setCancelable(false).setNeutralButton(dialogStringResolver.getSendButtonTitle(), r1);
                if (promptSettingsData2.showCancelButton) {
                    builder.setNegativeButton(dialogStringResolver.getCancelButtonTitle(), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            optInLatch2.setOptIn(false);
                            dialogInterface.dismiss();
                        }
                    });
                }
                if (promptSettingsData2.showAlwaysSendButton) {
                    builder.setPositiveButton(dialogStringResolver.getAlwaysSendButtonTitle(), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CrashlyticsCore.this.setShouldSendUserReportsWithoutPrompting(true);
                            optInLatch2.setOptIn(true);
                            dialogInterface.dismiss();
                        }
                    });
                }
                builder.show();
            }
        };
        activity.runOnUiThread(r0);
        Fabric.getLogger().mo21793d(TAG, "Waiting for user opt-in.");
        optInLatch.await();
        return optInLatch.getOptIn();
    }

    static SessionSettingsData getSessionSettingsData() {
        SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
        if (awaitSettingsData == null) {
            return null;
        }
        return awaitSettingsData.sessionData;
    }

    private static boolean isRequiringBuildId(Context context) {
        return CommonUtils.getBooleanResourceValue(context, CRASHLYTICS_REQUIRE_BUILD_ID, true);
    }

    private static String formatLogMessage(int i, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(CommonUtils.logPriorityToString(i));
        sb.append("/");
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        return sb.toString();
    }

    private static boolean ensureFabricWithCalled(String str) {
        CrashlyticsCore instance = getInstance();
        if (instance != null && instance.handler != null) {
            return true;
        }
        Logger logger = Fabric.getLogger();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Crashlytics must be initialized by calling Fabric.with(Context) ");
        sb.append(str);
        logger.mo21796e(str2, sb.toString(), null);
        return false;
    }

    private static String sanitizeAttribute(String str) {
        if (str == null) {
            return str;
        }
        String trim = str.trim();
        return trim.length() > 1024 ? trim.substring(0, 1024) : trim;
    }
}
