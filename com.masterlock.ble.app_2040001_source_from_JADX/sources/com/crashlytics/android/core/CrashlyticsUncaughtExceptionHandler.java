package com.crashlytics.android.core;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Environment;
import com.crashlytics.android.core.internal.models.SessionEventData;
import com.masterlock.api.entity.MasterBackupResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.DeliveryMechanism;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.persistence.FileStore;
import p008io.fabric.sdk.android.services.settings.Settings;

class CrashlyticsUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final int ANALYZER_VERSION = 1;
    static final FilenameFilter ANY_SESSION_FILENAME_FILTER = new FilenameFilter() {
        public boolean accept(File file, String str) {
            return CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(str).matches();
        }
    };
    private static final String EVENT_TYPE_CRASH = "crash";
    private static final String EVENT_TYPE_LOGGED = "error";
    private static final String GENERATOR_FORMAT = "Crashlytics Android SDK/%s";
    private static final String[] INITIAL_SESSION_PART_TAGS = {SESSION_USER_TAG, SESSION_APP_TAG, SESSION_OS_TAG, SESSION_DEVICE_TAG};
    static final String INVALID_CLS_CACHE_DIR = "invalidClsFiles";
    static final Comparator<File> LARGEST_FILE_NAME_FIRST = new Comparator<File>() {
        public int compare(File file, File file2) {
            return file2.getName().compareTo(file.getName());
        }
    };
    private static final int MAX_COMPLETE_SESSIONS_COUNT = 4;
    private static final int MAX_LOCAL_LOGGED_EXCEPTIONS = 64;
    static final int MAX_OPEN_SESSIONS = 8;
    /* access modifiers changed from: private */
    public static final Map<String, String> SEND_AT_CRASHTIME_HEADER = Collections.singletonMap("X-CRASHLYTICS-SEND-FLAGS", MasterBackupResponse.SUCCESS);
    static final String SESSION_APP_TAG = "SessionApp";
    static final String SESSION_BEGIN_TAG = "BeginSession";
    static final String SESSION_DEVICE_TAG = "SessionDevice";
    static final String SESSION_FATAL_TAG = "SessionCrash";
    static final FilenameFilter SESSION_FILE_FILTER = new FilenameFilter() {
        public boolean accept(File file, String str) {
            return str.length() == 39 && str.endsWith(ClsFileOutputStream.SESSION_FILE_EXTENSION);
        }
    };
    /* access modifiers changed from: private */
    public static final Pattern SESSION_FILE_PATTERN = Pattern.compile("([\\d|A-Z|a-z]{12}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{12}).+");
    private static final int SESSION_ID_LENGTH = 35;
    static final String SESSION_NON_FATAL_TAG = "SessionEvent";
    static final String SESSION_OS_TAG = "SessionOS";
    static final String SESSION_USER_TAG = "SessionUser";
    static final Comparator<File> SMALLEST_FILE_NAME_FIRST = new Comparator<File>() {
        public int compare(File file, File file2) {
            return file.getName().compareTo(file2.getName());
        }
    };
    /* access modifiers changed from: private */
    public final CrashlyticsCore crashlyticsCore;
    private final UncaughtExceptionHandler defaultHandler;
    private final DevicePowerStateListener devicePowerStateListener;
    private final AtomicInteger eventCounter = new AtomicInteger(0);
    private final CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private final FileStore fileStore;
    private final IdManager idManager;
    /* access modifiers changed from: private */
    public final AtomicBoolean isHandlingException;
    /* access modifiers changed from: private */
    public final LogFileManager logFileManager;
    private final String unityVersion;

    private static class AnySessionPartFileFilter implements FilenameFilter {
        private AnySessionPartFileFilter() {
        }

        public boolean accept(File file, String str) {
            return !CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER.accept(file, str) && CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(str).matches();
        }
    }

    static class FileNameContainsFilter implements FilenameFilter {
        private final String string;

        public FileNameContainsFilter(String str) {
            this.string = str;
        }

        public boolean accept(File file, String str) {
            return str.contains(this.string) && !str.endsWith(ClsFileOutputStream.IN_PROGRESS_SESSION_FILE_EXTENSION);
        }
    }

    private static final class SendSessionRunnable implements Runnable {
        private final CrashlyticsCore crashlyticsCore;
        private final File fileToSend;

        public SendSessionRunnable(CrashlyticsCore crashlyticsCore2, File file) {
            this.crashlyticsCore = crashlyticsCore2;
            this.fileToSend = file;
        }

        public void run() {
            if (CommonUtils.canTryConnection(this.crashlyticsCore.getContext())) {
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Attempting to send crash report at time of crash...");
                CreateReportSpiCall createReportSpiCall = this.crashlyticsCore.getCreateReportSpiCall(Settings.getInstance().awaitSettingsData());
                if (createReportSpiCall != null) {
                    new ReportUploader(createReportSpiCall).forceUpload(new SessionReport(this.fileToSend, CrashlyticsUncaughtExceptionHandler.SEND_AT_CRASHTIME_HEADER));
                }
            }
        }
    }

    static class SessionPartFileFilter implements FilenameFilter {
        private final String sessionId;

        public SessionPartFileFilter(String str) {
            this.sessionId = str;
        }

        public boolean accept(File file, String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.sessionId);
            sb.append(ClsFileOutputStream.SESSION_FILE_EXTENSION);
            boolean z = false;
            if (str.equals(sb.toString())) {
                return false;
            }
            if (str.contains(this.sessionId) && !str.endsWith(ClsFileOutputStream.IN_PROGRESS_SESSION_FILE_EXTENSION)) {
                z = true;
            }
            return z;
        }
    }

    CrashlyticsUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler, CrashlyticsExecutorServiceWrapper crashlyticsExecutorServiceWrapper, IdManager idManager2, UnityVersionProvider unityVersionProvider, FileStore fileStore2, CrashlyticsCore crashlyticsCore2) {
        this.defaultHandler = uncaughtExceptionHandler;
        this.executorServiceWrapper = crashlyticsExecutorServiceWrapper;
        this.idManager = idManager2;
        this.crashlyticsCore = crashlyticsCore2;
        this.unityVersion = unityVersionProvider.getUnityVersion();
        this.fileStore = fileStore2;
        this.isHandlingException = new AtomicBoolean(false);
        Context context = crashlyticsCore2.getContext();
        this.logFileManager = new LogFileManager(context, fileStore2);
        this.devicePowerStateListener = new DevicePowerStateListener(context);
    }

    public synchronized void uncaughtException(final Thread thread, final Throwable th) {
        AtomicBoolean atomicBoolean;
        this.isHandlingException.set(true);
        try {
            Logger logger = Fabric.getLogger();
            String str = CrashlyticsCore.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Crashlytics is handling uncaught exception \"");
            sb.append(th);
            sb.append("\" from thread ");
            sb.append(thread.getName());
            logger.mo21793d(str, sb.toString());
            this.devicePowerStateListener.dispose();
            final Date date = new Date();
            this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() {
                public Void call() throws Exception {
                    CrashlyticsUncaughtExceptionHandler.this.handleUncaughtException(date, thread, th);
                    return null;
                }
            });
            Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
            this.defaultHandler.uncaughtException(thread, th);
            atomicBoolean = this.isHandlingException;
        } catch (Exception e) {
            try {
                Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "An error occurred in the uncaught exception handler", e);
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
                this.defaultHandler.uncaughtException(thread, th);
                atomicBoolean = this.isHandlingException;
            } catch (Throwable th2) {
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
                this.defaultHandler.uncaughtException(thread, th);
                this.isHandlingException.set(false);
                throw th2;
            }
        }
        atomicBoolean.set(false);
        return;
    }

    /* access modifiers changed from: private */
    public void handleUncaughtException(Date date, Thread thread, Throwable th) throws Exception {
        this.crashlyticsCore.createCrashMarker();
        writeFatal(date, thread, th);
        doCloseSessions();
        doOpenSession();
        trimSessionFiles();
        if (!this.crashlyticsCore.shouldPromptUserBeforeSendingCrashReports()) {
            sendSessionReports();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isHandlingException() {
        return this.isHandlingException.get();
    }

    /* access modifiers changed from: 0000 */
    public void writeToLog(final long j, final String str) {
        this.executorServiceWrapper.executeAsync((Callable<T>) new Callable<Void>() {
            public Void call() throws Exception {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    CrashlyticsUncaughtExceptionHandler.this.logFileManager.writeToLog(j, str);
                }
                return null;
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void writeNonFatalException(final Thread thread, final Throwable th) {
        final Date date = new Date();
        this.executorServiceWrapper.executeAsync((Runnable) new Runnable() {
            public void run() {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    CrashlyticsUncaughtExceptionHandler.this.doWriteNonFatal(date, thread, th);
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void cacheUserData(final String str, final String str2, final String str3) {
        this.executorServiceWrapper.executeAsync((Callable<T>) new Callable<Void>() {
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsUncaughtExceptionHandler.this.getFilesDir()).writeUserData(CrashlyticsUncaughtExceptionHandler.this.getCurrentSessionId(), new UserMetaData(str, str2, str3));
                return null;
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void cacheKeyData(final Map<String, String> map) {
        this.executorServiceWrapper.executeAsync((Callable<T>) new Callable<Void>() {
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsUncaughtExceptionHandler.this.getFilesDir()).writeKeyData(CrashlyticsUncaughtExceptionHandler.this.getCurrentSessionId(), map);
                return null;
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void openSession() {
        this.executorServiceWrapper.executeAsync((Callable<T>) new Callable<Void>() {
            public Void call() throws Exception {
                CrashlyticsUncaughtExceptionHandler.this.doOpenSession();
                return null;
            }
        });
    }

    /* access modifiers changed from: private */
    public String getCurrentSessionId() {
        File[] listSortedSessionBeginFiles = listSortedSessionBeginFiles();
        if (listSortedSessionBeginFiles.length > 0) {
            return getSessionIdFromSessionFile(listSortedSessionBeginFiles[0]);
        }
        return null;
    }

    private String getPreviousSessionId() {
        File[] listSortedSessionBeginFiles = listSortedSessionBeginFiles();
        if (listSortedSessionBeginFiles.length > 1) {
            return getSessionIdFromSessionFile(listSortedSessionBeginFiles[1]);
        }
        return null;
    }

    private String getSessionIdFromSessionFile(File file) {
        return file.getName().substring(0, 35);
    }

    /* access modifiers changed from: 0000 */
    public boolean hasOpenSession() {
        return listSessionBeginFiles().length > 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean finalizeSessions() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                if (CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Skipping session finalization because a crash has already occurred.");
                    return Boolean.FALSE;
                }
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Finalizing previously open sessions.");
                SessionEventData externalCrashEventData = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getExternalCrashEventData();
                if (externalCrashEventData != null) {
                    CrashlyticsUncaughtExceptionHandler.this.writeExternalCrashEvent(externalCrashEventData);
                }
                CrashlyticsUncaughtExceptionHandler.this.doCloseSessions(true);
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Closed all previously open sessions");
                return Boolean.TRUE;
            }
        })).booleanValue();
    }

    /* access modifiers changed from: private */
    public void doOpenSession() throws Exception {
        Date date = new Date();
        String clsuuid = new CLSUUID(this.idManager).toString();
        Logger logger = Fabric.getLogger();
        String str = CrashlyticsCore.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Opening an new session with ID ");
        sb.append(clsuuid);
        logger.mo21793d(str, sb.toString());
        writeBeginSession(clsuuid, date);
        writeSessionApp(clsuuid);
        writeSessionOS(clsuuid);
        writeSessionDevice(clsuuid);
        this.logFileManager.setCurrentSession(clsuuid);
    }

    /* access modifiers changed from: 0000 */
    public void doCloseSessions() throws Exception {
        doCloseSessions(false);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=int, for r3v0, types: [boolean, int] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doCloseSessions(int r3) throws java.lang.Exception {
        /*
            r2 = this;
            int r0 = r3 + 8
            r2.trimOpenSessions(r0)
            java.io.File[] r0 = r2.listSortedSessionBeginFiles()
            int r1 = r0.length
            if (r1 > r3) goto L_0x0018
            io.fabric.sdk.android.Logger r3 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r0 = "CrashlyticsCore"
            java.lang.String r1 = "No open sessions to be closed."
            r3.mo21793d(r0, r1)
            return
        L_0x0018:
            r1 = r0[r3]
            java.lang.String r1 = r2.getSessionIdFromSessionFile(r1)
            r2.writeSessionUser(r1)
            com.crashlytics.android.core.CrashlyticsCore r1 = r2.crashlyticsCore
            io.fabric.sdk.android.services.settings.SessionSettingsData r1 = com.crashlytics.android.core.CrashlyticsCore.getSessionSettingsData()
            if (r1 != 0) goto L_0x0035
            io.fabric.sdk.android.Logger r3 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r0 = "CrashlyticsCore"
            java.lang.String r1 = "Unable to close session. Settings are not loaded."
            r3.mo21793d(r0, r1)
            return
        L_0x0035:
            int r1 = r1.maxCustomExceptionEvents
            r2.closeOpenSessions(r0, r3, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.doCloseSessions(boolean):void");
    }

    private void closeOpenSessions(File[] fileArr, int i, int i2) {
        Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, "Closing open sessions.");
        while (i < fileArr.length) {
            File file = fileArr[i];
            String sessionIdFromSessionFile = getSessionIdFromSessionFile(file);
            Logger logger = Fabric.getLogger();
            String str = CrashlyticsCore.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Closing session: ");
            sb.append(sessionIdFromSessionFile);
            logger.mo21793d(str, sb.toString());
            writeSessionPartsToSessionFile(file, sessionIdFromSessionFile, i2);
            i++;
        }
    }

    private void closeWithoutRenamingOrLog(ClsFileOutputStream clsFileOutputStream) {
        if (clsFileOutputStream != null) {
            try {
                clsFileOutputStream.closeInProgressStream();
            } catch (IOException e) {
                Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Error closing session file stream in the presence of an exception", e);
            }
        }
    }

    private void deleteSessionPartFilesFor(String str) {
        for (File delete : listSessionPartFilesFor(str)) {
            delete.delete();
        }
    }

    private File[] listSessionPartFilesFor(String str) {
        return listFilesMatching(new SessionPartFileFilter(str));
    }

    private File[] listCompleteSessionFiles() {
        return listFilesMatching(SESSION_FILE_FILTER);
    }

    /* access modifiers changed from: 0000 */
    public File[] listSessionBeginFiles() {
        return listFilesMatching(new FileNameContainsFilter(SESSION_BEGIN_TAG));
    }

    private File[] listSortedSessionBeginFiles() {
        File[] listSessionBeginFiles = listSessionBeginFiles();
        Arrays.sort(listSessionBeginFiles, LARGEST_FILE_NAME_FIRST);
        return listSessionBeginFiles;
    }

    /* access modifiers changed from: private */
    public File[] listFilesMatching(FilenameFilter filenameFilter) {
        return ensureFileArrayNotNull(getFilesDir().listFiles(filenameFilter));
    }

    private File[] ensureFileArrayNotNull(File[] fileArr) {
        return fileArr == null ? new File[0] : fileArr;
    }

    private void trimSessionEventFiles(String str, int i) {
        File filesDir = getFilesDir();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(SESSION_NON_FATAL_TAG);
        Utils.capFileCount(filesDir, new FileNameContainsFilter(sb.toString()), i, SMALLEST_FILE_NAME_FIRST);
    }

    /* access modifiers changed from: 0000 */
    public void trimSessionFiles() {
        Utils.capFileCount(getFilesDir(), SESSION_FILE_FILTER, 4, SMALLEST_FILE_NAME_FIRST);
    }

    private void trimOpenSessions(int i) {
        File[] listFilesMatching;
        HashSet hashSet = new HashSet();
        File[] listSortedSessionBeginFiles = listSortedSessionBeginFiles();
        int min = Math.min(i, listSortedSessionBeginFiles.length);
        for (int i2 = 0; i2 < min; i2++) {
            hashSet.add(getSessionIdFromSessionFile(listSortedSessionBeginFiles[i2]));
        }
        this.logFileManager.discardOldLogFiles(hashSet);
        for (File file : listFilesMatching(new AnySessionPartFileFilter())) {
            String name = file.getName();
            Matcher matcher = SESSION_FILE_PATTERN.matcher(name);
            matcher.matches();
            if (!hashSet.contains(matcher.group(1))) {
                Logger logger = Fabric.getLogger();
                String str = CrashlyticsCore.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Trimming open session file: ");
                sb.append(name);
                logger.mo21793d(str, sb.toString());
                file.delete();
            }
        }
    }

    private File[] getTrimmedNonFatalFiles(String str, File[] fileArr, int i) {
        if (fileArr.length <= i) {
            return fileArr;
        }
        Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, String.format(Locale.US, "Trimming down to %d logged exceptions.", new Object[]{Integer.valueOf(i)}));
        trimSessionEventFiles(str, i);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(SESSION_NON_FATAL_TAG);
        return listFilesMatching(new FileNameContainsFilter(sb.toString()));
    }

    /* access modifiers changed from: 0000 */
    public void cleanInvalidTempFiles() {
        this.executorServiceWrapper.executeAsync((Runnable) new Runnable() {
            public void run() {
                CrashlyticsUncaughtExceptionHandler crashlyticsUncaughtExceptionHandler = CrashlyticsUncaughtExceptionHandler.this;
                crashlyticsUncaughtExceptionHandler.doCleanInvalidTempFiles(crashlyticsUncaughtExceptionHandler.listFilesMatching(ClsFileOutputStream.TEMP_FILENAME_FILTER));
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void doCleanInvalidTempFiles(File[] fileArr) {
        File[] listFilesMatching;
        deleteLegacyInvalidCacheDir();
        for (File file : fileArr) {
            Logger logger = Fabric.getLogger();
            String str = CrashlyticsCore.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Found invalid session part file: ");
            sb.append(file);
            logger.mo21793d(str, sb.toString());
            final String sessionIdFromSessionFile = getSessionIdFromSessionFile(file);
            C043613 r4 = new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return str.startsWith(sessionIdFromSessionFile);
                }
            };
            Logger logger2 = Fabric.getLogger();
            String str2 = CrashlyticsCore.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Deleting all part files for invalid session: ");
            sb2.append(sessionIdFromSessionFile);
            logger2.mo21793d(str2, sb2.toString());
            for (File file2 : listFilesMatching(r4)) {
                Logger logger3 = Fabric.getLogger();
                String str3 = CrashlyticsCore.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Deleting session file: ");
                sb3.append(file2);
                logger3.mo21793d(str3, sb3.toString());
                file2.delete();
            }
        }
    }

    private void deleteLegacyInvalidCacheDir() {
        File file = new File(this.crashlyticsCore.getSdkDirectory(), INVALID_CLS_CACHE_DIR);
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File delete : file.listFiles()) {
                    delete.delete();
                }
            }
            file.delete();
        }
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r2v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r13v3, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v2, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v1 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r5v0, types: [com.crashlytics.android.core.CodedOutputStream] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v6
      assigns: []
      uses: []
      mth insns count: 66
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeFatal(java.util.Date r12, java.lang.Thread r13, java.lang.Throwable r14) {
        /*
            r11 = this;
            r0 = 0
            java.lang.String r1 = r11.getCurrentSessionId()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            if (r1 != 0) goto L_0x001d
            io.fabric.sdk.android.Logger r12 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r13 = "CrashlyticsCore"
            java.lang.String r14 = "Tried to write a fatal exception while no session was open."
            r12.mo21796e(r13, r14, r0)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r12 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r12)
            java.lang.String r12 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r0, r12)
            return
        L_0x001d:
            com.crashlytics.android.core.CrashlyticsCore.recordFatalExceptionEvent(r1)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            com.crashlytics.android.core.ClsFileOutputStream r2 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.io.File r3 = r11.getFilesDir()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r4.<init>()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r4.append(r1)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r1 = "SessionCrash"
            r4.append(r1)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r2.<init>(r3, r1)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            com.crashlytics.android.core.CodedOutputStream r0 = com.crashlytics.android.core.CodedOutputStream.newInstance(r2)     // Catch:{ Exception -> 0x0056, all -> 0x0054 }
            java.lang.String r9 = "crash"
            r10 = 1
            r4 = r11
            r5 = r0
            r6 = r12
            r7 = r13
            r8 = r14
            r4.writeSessionEvent(r5, r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0056, all -> 0x0054 }
            java.lang.String r12 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r12)
            java.lang.String r12 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r2, r12)
            goto L_0x0077
        L_0x0054:
            r12 = move-exception
            goto L_0x007b
        L_0x0056:
            r12 = move-exception
            r13 = r0
            r0 = r2
            goto L_0x005f
        L_0x005a:
            r12 = move-exception
            r2 = r0
            goto L_0x007b
        L_0x005d:
            r12 = move-exception
            r13 = r0
        L_0x005f:
            io.fabric.sdk.android.Logger r14 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x0078 }
            java.lang.String r1 = "CrashlyticsCore"
            java.lang.String r2 = "An error occurred in the fatal exception logger"
            r14.mo21796e(r1, r2, r12)     // Catch:{ all -> 0x0078 }
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r12, r0)     // Catch:{ all -> 0x0078 }
            java.lang.String r12 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r13, r12)
            java.lang.String r12 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r0, r12)
        L_0x0077:
            return
        L_0x0078:
            r12 = move-exception
            r2 = r0
            r0 = r13
        L_0x007b:
            java.lang.String r13 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r13)
            java.lang.String r13 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r2, r13)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeFatal(java.util.Date, java.lang.Thread, java.lang.Throwable):void");
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r2v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v1 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r0v8, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v7
      assigns: []
      uses: []
      mth insns count: 67
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeExternalCrashEvent(com.crashlytics.android.core.internal.models.SessionEventData r8) throws java.io.IOException {
        /*
            r7 = this;
            r0 = 0
            java.lang.String r1 = r7.getPreviousSessionId()     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            if (r1 != 0) goto L_0x001d
            io.fabric.sdk.android.Logger r8 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.lang.String r1 = "CrashlyticsCore"
            java.lang.String r2 = "Tried to write a native crash while no session was open."
            r8.mo21796e(r1, r2, r0)     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.lang.String r8 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r8)
            java.lang.String r8 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r0, r8)
            return
        L_0x001d:
            com.crashlytics.android.core.CrashlyticsCore.recordFatalExceptionEvent(r1)     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            com.crashlytics.android.core.ClsFileOutputStream r2 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.io.File r3 = r7.getFilesDir()     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            r4.<init>()     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            r4.append(r1)     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.lang.String r5 = "SessionCrash"
            r4.append(r5)     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            r2.<init>(r3, r4)     // Catch:{ Exception -> 0x006f, all -> 0x006c }
            com.crashlytics.android.core.CodedOutputStream r0 = com.crashlytics.android.core.CodedOutputStream.newInstance(r2)     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            com.crashlytics.android.core.MetaDataStore r3 = new com.crashlytics.android.core.MetaDataStore     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            java.io.File r4 = r7.getFilesDir()     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            java.util.Map r3 = r3.readKeyData(r1)     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            com.crashlytics.android.core.LogFileManager r4 = new com.crashlytics.android.core.LogFileManager     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            com.crashlytics.android.core.CrashlyticsCore r5 = r7.crashlyticsCore     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            android.content.Context r5 = r5.getContext()     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            io.fabric.sdk.android.services.persistence.FileStore r6 = r7.fileStore     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            r4.<init>(r5, r6, r1)     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            com.crashlytics.android.core.NativeCrashWriter.writeNativeCrash(r8, r4, r3, r0)     // Catch:{ Exception -> 0x0068, all -> 0x0066 }
            java.lang.String r8 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r8)
            java.lang.String r8 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r2, r8)
            goto L_0x0089
        L_0x0066:
            r8 = move-exception
            goto L_0x008d
        L_0x0068:
            r8 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x0071
        L_0x006c:
            r8 = move-exception
            r2 = r0
            goto L_0x008d
        L_0x006f:
            r8 = move-exception
            r1 = r0
        L_0x0071:
            io.fabric.sdk.android.Logger r2 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x008a }
            java.lang.String r3 = "CrashlyticsCore"
            java.lang.String r4 = "An error occurred in the native crash logger"
            r2.mo21796e(r3, r4, r8)     // Catch:{ all -> 0x008a }
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r8, r0)     // Catch:{ all -> 0x008a }
            java.lang.String r8 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r1, r8)
            java.lang.String r8 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r0, r8)
        L_0x0089:
            return
        L_0x008a:
            r8 = move-exception
            r2 = r0
            r0 = r1
        L_0x008d:
            java.lang.String r1 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r1)
            java.lang.String r0 = "Failed to close fatal exception file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r2, r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeExternalCrashEvent(com.crashlytics.android.core.internal.models.SessionEventData):void");
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r14v4, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r1v2, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r14v5 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r6v0, types: [com.crashlytics.android.core.CodedOutputStream] */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v6
      assigns: []
      uses: []
      mth insns count: 84
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doWriteNonFatal(java.util.Date r13, java.lang.Thread r14, java.lang.Throwable r15) {
        /*
            r12 = this;
            java.lang.String r0 = r12.getCurrentSessionId()
            r1 = 0
            if (r0 != 0) goto L_0x0013
            io.fabric.sdk.android.Logger r13 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r14 = "CrashlyticsCore"
            java.lang.String r15 = "Tried to write a non-fatal exception while no session was open."
            r13.mo21796e(r14, r15, r1)
            return
        L_0x0013:
            com.crashlytics.android.core.CrashlyticsCore.recordLoggedExceptionEvent(r0)
            io.fabric.sdk.android.Logger r2 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r3 = "CrashlyticsCore"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r4.<init>()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r5 = "Crashlytics is logging non-fatal exception \""
            r4.append(r5)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r4.append(r15)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r5 = "\" from thread "
            r4.append(r5)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r5 = r14.getName()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r4.append(r5)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r2.mo21793d(r3, r4)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.util.concurrent.atomic.AtomicInteger r2 = r12.eventCounter     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            int r2 = r2.getAndIncrement()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r2 = p008io.fabric.sdk.android.services.common.CommonUtils.padWithZerosToMaxIntWidth(r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r3.<init>()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r3.append(r0)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r4 = "SessionEvent"
            r3.append(r4)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r3.append(r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            com.crashlytics.android.core.ClsFileOutputStream r3 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            java.io.File r4 = r12.getFilesDir()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r3.<init>(r4, r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            com.crashlytics.android.core.CodedOutputStream r1 = com.crashlytics.android.core.CodedOutputStream.newInstance(r3)     // Catch:{ Exception -> 0x007f, all -> 0x007d }
            java.lang.String r10 = "error"
            r11 = 0
            r5 = r12
            r6 = r1
            r7 = r13
            r8 = r14
            r9 = r15
            r5.writeSessionEvent(r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x007f, all -> 0x007d }
            java.lang.String r13 = "Failed to flush to non-fatal file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r1, r13)
            java.lang.String r13 = "Failed to close non-fatal file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r3, r13)
            goto L_0x00a0
        L_0x007d:
            r13 = move-exception
            goto L_0x00b6
        L_0x007f:
            r13 = move-exception
            r14 = r1
            r1 = r3
            goto L_0x0088
        L_0x0083:
            r13 = move-exception
            r3 = r1
            goto L_0x00b6
        L_0x0086:
            r13 = move-exception
            r14 = r1
        L_0x0088:
            io.fabric.sdk.android.Logger r15 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x00b3 }
            java.lang.String r2 = "CrashlyticsCore"
            java.lang.String r3 = "An error occurred in the non-fatal exception logger"
            r15.mo21796e(r2, r3, r13)     // Catch:{ all -> 0x00b3 }
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r13, r1)     // Catch:{ all -> 0x00b3 }
            java.lang.String r13 = "Failed to flush to non-fatal file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r14, r13)
            java.lang.String r13 = "Failed to close non-fatal file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r13)
        L_0x00a0:
            r13 = 64
            r12.trimSessionEventFiles(r0, r13)     // Catch:{ Exception -> 0x00a6 }
            goto L_0x00b2
        L_0x00a6:
            r13 = move-exception
            io.fabric.sdk.android.Logger r14 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r15 = "CrashlyticsCore"
            java.lang.String r0 = "An error occurred when trimming non-fatal files."
            r14.mo21796e(r15, r0, r13)
        L_0x00b2:
            return
        L_0x00b3:
            r13 = move-exception
            r3 = r1
            r1 = r14
        L_0x00b6:
            java.lang.String r14 = "Failed to flush to non-fatal file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r1, r14)
            java.lang.String r14 = "Failed to close non-fatal file output stream."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r3, r14)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.doWriteNonFatal(java.util.Date, java.lang.Thread, java.lang.Throwable):void");
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r9v3 */
    /* JADX WARNING: type inference failed for: r0v2, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r9v4 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v3, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v6
      assigns: []
      uses: []
      mth insns count: 50
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeBeginSession(java.lang.String r8, java.util.Date r9) throws java.lang.Exception {
        /*
            r7 = this;
            r0 = 0
            com.crashlytics.android.core.ClsFileOutputStream r1 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            java.io.File r2 = r7.getFilesDir()     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            r3.<init>()     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            r3.append(r8)     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            java.lang.String r4 = "BeginSession"
            r3.append(r4)     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            r1.<init>(r2, r3)     // Catch:{ Exception -> 0x0051, all -> 0x004e }
            com.crashlytics.android.core.CodedOutputStream r0 = com.crashlytics.android.core.CodedOutputStream.newInstance(r1)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.util.Locale r2 = java.util.Locale.US     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r3 = "Crashlytics Android SDK/%s"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            r5 = 0
            com.crashlytics.android.core.CrashlyticsCore r6 = r7.crashlyticsCore     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r6 = r6.getVersion()     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            r4[r5] = r6     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r2 = java.lang.String.format(r2, r3, r4)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            long r3 = r9.getTime()     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            r5 = 1000(0x3e8, double:4.94E-321)
            long r3 = r3 / r5
            com.crashlytics.android.core.SessionProtobufHelper.writeBeginSession(r0, r8, r2, r3)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r8 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r8)
            java.lang.String r8 = "Failed to close begin session file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r8)
            return
        L_0x0048:
            r8 = move-exception
            goto L_0x005a
        L_0x004a:
            r8 = move-exception
            r9 = r0
            r0 = r1
            goto L_0x0053
        L_0x004e:
            r8 = move-exception
            r1 = r0
            goto L_0x005a
        L_0x0051:
            r8 = move-exception
            r9 = r0
        L_0x0053:
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r8, r0)     // Catch:{ all -> 0x0057 }
            throw r8     // Catch:{ all -> 0x0057 }
        L_0x0057:
            r8 = move-exception
            r1 = r0
            r0 = r9
        L_0x005a:
            java.lang.String r9 = "Failed to flush to session begin file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r9)
            java.lang.String r9 = "Failed to close begin session file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeBeginSession(java.lang.String, java.util.Date):void");
    }

    private void writeSessionApp(String str) throws Exception {
        CodedOutputStream codedOutputStream;
        ClsFileOutputStream clsFileOutputStream;
        Exception e;
        try {
            File filesDir = getFilesDir();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SESSION_APP_TAG);
            clsFileOutputStream = new ClsFileOutputStream(filesDir, sb.toString());
            try {
                codedOutputStream = CodedOutputStream.newInstance((OutputStream) clsFileOutputStream);
                try {
                    SessionProtobufHelper.writeSessionApp(codedOutputStream, this.idManager.getAppIdentifier(), this.crashlyticsCore.getApiKey(), this.crashlyticsCore.getVersionCode(), this.crashlyticsCore.getVersionName(), this.idManager.getAppInstallIdentifier(), DeliveryMechanism.determineFrom(this.crashlyticsCore.getInstallerPackageName()).getId(), this.unityVersion);
                    CommonUtils.flushOrLog(codedOutputStream, "Failed to flush to session app file.");
                    CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session app file.");
                } catch (Exception e2) {
                    e = e2;
                    try {
                        ExceptionUtils.writeStackTraceIfNotNull(e, clsFileOutputStream);
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                        CommonUtils.flushOrLog(codedOutputStream, "Failed to flush to session app file.");
                        CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session app file.");
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                codedOutputStream = null;
                ExceptionUtils.writeStackTraceIfNotNull(e, clsFileOutputStream);
                throw e;
            } catch (Throwable th2) {
                th = th2;
                codedOutputStream = null;
                CommonUtils.flushOrLog(codedOutputStream, "Failed to flush to session app file.");
                CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session app file.");
                throw th;
            }
        } catch (Exception e4) {
            clsFileOutputStream = null;
            e = e4;
            codedOutputStream = null;
            ExceptionUtils.writeStackTraceIfNotNull(e, clsFileOutputStream);
            throw e;
        } catch (Throwable th3) {
            clsFileOutputStream = null;
            th = th3;
            codedOutputStream = null;
            CommonUtils.flushOrLog(codedOutputStream, "Failed to flush to session app file.");
            CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session app file.");
            throw th;
        }
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r4v0 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r0v8, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v7
      assigns: []
      uses: []
      mth insns count: 46
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 11 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeSessionOS(java.lang.String r6) throws java.lang.Exception {
        /*
            r5 = this;
            r0 = 0
            com.crashlytics.android.core.ClsFileOutputStream r1 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            java.io.File r2 = r5.getFilesDir()     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            r3.<init>()     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            r3.append(r6)     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            java.lang.String r6 = "SessionOS"
            r3.append(r6)     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            java.lang.String r6 = r3.toString()     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            r1.<init>(r2, r6)     // Catch:{ Exception -> 0x0041, all -> 0x003e }
            com.crashlytics.android.core.CodedOutputStream r0 = com.crashlytics.android.core.CodedOutputStream.newInstance(r1)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            com.crashlytics.android.core.CrashlyticsCore r6 = r5.crashlyticsCore     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            android.content.Context r6 = r6.getContext()     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            boolean r6 = p008io.fabric.sdk.android.services.common.CommonUtils.isRooted(r6)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            com.crashlytics.android.core.SessionProtobufHelper.writeSessionOS(r0, r6)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            java.lang.String r6 = "Failed to flush to session OS file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r6)
            java.lang.String r6 = "Failed to close session OS file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r6)
            return
        L_0x0037:
            r6 = move-exception
            goto L_0x004b
        L_0x0039:
            r6 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0043
        L_0x003e:
            r6 = move-exception
            r1 = r0
            goto L_0x004b
        L_0x0041:
            r6 = move-exception
            r1 = r0
        L_0x0043:
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r6, r0)     // Catch:{ all -> 0x0047 }
            throw r6     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r6 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x004b:
            java.lang.String r2 = "Failed to flush to session OS file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r2)
            java.lang.String r0 = "Failed to close session OS file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeSessionOS(java.lang.String):void");
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r19v0 */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r19v1 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r2v8, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r5v7, types: [com.crashlytics.android.core.CodedOutputStream] */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r2v14 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v7
      assigns: []
      uses: []
      mth insns count: 69
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 12 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeSessionDevice(java.lang.String r21) throws java.lang.Exception {
        /*
            r20 = this;
            r1 = r20
            r2 = 0
            com.crashlytics.android.core.ClsFileOutputStream r3 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.io.File r0 = r20.getFilesDir()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r4.<init>()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r5 = r21
            r4.append(r5)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r5 = "SessionDevice"
            r4.append(r5)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r3.<init>(r0, r4)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            com.crashlytics.android.core.CodedOutputStream r2 = com.crashlytics.android.core.CodedOutputStream.newInstance(r3)     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            com.crashlytics.android.core.CrashlyticsCore r0 = r1.crashlyticsCore     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            android.content.Context r0 = r0.getContext()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            android.os.StatFs r4 = new android.os.StatFs     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.io.File r5 = android.os.Environment.getDataDirectory()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r5 = r5.getPath()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            r4.<init>(r5)     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            io.fabric.sdk.android.services.common.IdManager r5 = r1.idManager     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r6 = r5.getDeviceUUID()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            int r7 = p008io.fabric.sdk.android.services.common.CommonUtils.getCpuArchitectureInt()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.Runtime r5 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            int r9 = r5.availableProcessors()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            long r10 = p008io.fabric.sdk.android.services.common.CommonUtils.getTotalRamInBytes()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            int r5 = r4.getBlockCount()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            long r12 = (long) r5     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            int r4 = r4.getBlockSize()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            long r4 = (long) r4     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            long r12 = r12 * r4
            boolean r14 = p008io.fabric.sdk.android.services.common.CommonUtils.isEmulator(r0)     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            io.fabric.sdk.android.services.common.IdManager r4 = r1.idManager     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.util.Map r15 = r4.getDeviceIdentifiers()     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            int r16 = p008io.fabric.sdk.android.services.common.CommonUtils.getDeviceState(r0)     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r8 = android.os.Build.MODEL     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r17 = android.os.Build.MANUFACTURER     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r18 = android.os.Build.PRODUCT     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            r5 = r2
            com.crashlytics.android.core.SessionProtobufHelper.writeSessionDevice(r5, r6, r7, r8, r9, r10, r12, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x007d, all -> 0x007b }
            java.lang.String r0 = "Failed to flush session device info."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r2, r0)
            java.lang.String r0 = "Failed to close session device file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r3, r0)
            return
        L_0x007b:
            r0 = move-exception
            goto L_0x0093
        L_0x007d:
            r0 = move-exception
            r19 = r3
            r3 = r2
            r2 = r19
            goto L_0x0089
        L_0x0084:
            r0 = move-exception
            r3 = r2
            goto L_0x0093
        L_0x0087:
            r0 = move-exception
            r3 = r2
        L_0x0089:
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r0, r2)     // Catch:{ all -> 0x008d }
            throw r0     // Catch:{ all -> 0x008d }
        L_0x008d:
            r0 = move-exception
            r19 = r3
            r3 = r2
            r2 = r19
        L_0x0093:
            java.lang.String r4 = "Failed to flush session device info."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r2, r4)
            java.lang.String r2 = "Failed to close session device file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r3, r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeSessionDevice(java.lang.String):void");
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5, types: [java.io.OutputStream, com.crashlytics.android.core.ClsFileOutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r0v8, types: [com.crashlytics.android.core.CodedOutputStream, java.io.Flushable] */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: type inference failed for: r0v16 */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v7
      assigns: []
      uses: []
      mth insns count: 57
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 11 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeSessionUser(java.lang.String r7) throws java.lang.Exception {
        /*
            r6 = this;
            r0 = 0
            com.crashlytics.android.core.ClsFileOutputStream r1 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            java.io.File r2 = r6.getFilesDir()     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            r3.<init>()     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            r3.append(r7)     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            java.lang.String r4 = "SessionUser"
            r3.append(r4)     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            r1.<init>(r2, r3)     // Catch:{ Exception -> 0x0052, all -> 0x004f }
            com.crashlytics.android.core.CodedOutputStream r0 = com.crashlytics.android.core.CodedOutputStream.newInstance(r1)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            com.crashlytics.android.core.UserMetaData r7 = r6.getUserMetaData(r7)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            boolean r2 = r7.isEmpty()     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            if (r2 == 0) goto L_0x0034
            java.lang.String r7 = "Failed to flush session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r7)
            java.lang.String r7 = "Failed to close session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r7)
            return
        L_0x0034:
            java.lang.String r2 = r7.f32id     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r3 = r7.name     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r7 = r7.email     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            com.crashlytics.android.core.SessionProtobufHelper.writeSessionUser(r0, r2, r3, r7)     // Catch:{ Exception -> 0x004a, all -> 0x0048 }
            java.lang.String r7 = "Failed to flush session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r7)
            java.lang.String r7 = "Failed to close session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r7)
            return
        L_0x0048:
            r7 = move-exception
            goto L_0x005c
        L_0x004a:
            r7 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0054
        L_0x004f:
            r7 = move-exception
            r1 = r0
            goto L_0x005c
        L_0x0052:
            r7 = move-exception
            r1 = r0
        L_0x0054:
            com.crashlytics.android.core.ExceptionUtils.writeStackTraceIfNotNull(r7, r0)     // Catch:{ all -> 0x0058 }
            throw r7     // Catch:{ all -> 0x0058 }
        L_0x0058:
            r7 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
        L_0x005c:
            java.lang.String r2 = "Failed to flush session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r0, r2)
            java.lang.String r0 = "Failed to close session user file."
            p008io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r1, r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.writeSessionUser(java.lang.String):void");
    }

    private void writeSessionEvent(CodedOutputStream codedOutputStream, Date date, Thread thread, Throwable th, String str, boolean z) throws Exception {
        Thread[] threadArr;
        Map map;
        Context context = this.crashlyticsCore.getContext();
        long time = date.getTime() / 1000;
        float batteryLevel = CommonUtils.getBatteryLevel(context);
        int batteryVelocity = CommonUtils.getBatteryVelocity(context, this.devicePowerStateListener.isPowerConnected());
        boolean proximitySensorEnabled = CommonUtils.getProximitySensorEnabled(context);
        int i = context.getResources().getConfiguration().orientation;
        long totalRamInBytes = CommonUtils.getTotalRamInBytes() - CommonUtils.calculateFreeRamInBytes(context);
        long calculateUsedDiskSpaceInBytes = CommonUtils.calculateUsedDiskSpaceInBytes(Environment.getDataDirectory().getPath());
        RunningAppProcessInfo appProcessInfo = CommonUtils.getAppProcessInfo(context.getPackageName(), context);
        LinkedList linkedList = new LinkedList();
        StackTraceElement[] stackTrace = th.getStackTrace();
        String buildId = this.crashlyticsCore.getBuildId();
        String appIdentifier = this.idManager.getAppIdentifier();
        int i2 = 0;
        if (z) {
            Map allStackTraces = Thread.getAllStackTraces();
            Thread[] threadArr2 = new Thread[allStackTraces.size()];
            for (Entry entry : allStackTraces.entrySet()) {
                threadArr2[i2] = (Thread) entry.getKey();
                linkedList.add(entry.getValue());
                i2++;
            }
            threadArr = threadArr2;
        } else {
            threadArr = new Thread[0];
        }
        if (!CommonUtils.getBooleanResourceValue(context, "com.crashlytics.CollectCustomKeys", true)) {
            map = new TreeMap();
        } else {
            Map attributes = this.crashlyticsCore.getAttributes();
            map = (attributes == null || attributes.size() <= 1) ? attributes : new TreeMap(attributes);
        }
        SessionProtobufHelper.writeSessionEvent(codedOutputStream, time, str, th, thread, stackTrace, threadArr, linkedList, map, this.logFileManager, appProcessInfo, i, appIdentifier, buildId, batteryLevel, batteryVelocity, proximitySensorEnabled, totalRamInBytes, calculateUsedDiskSpaceInBytes);
    }

    private void writeSessionPartsToSessionFile(File file, String str, int i) {
        Logger logger = Fabric.getLogger();
        String str2 = CrashlyticsCore.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Collecting session parts for ID ");
        sb.append(str);
        logger.mo21793d(str2, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(SESSION_FATAL_TAG);
        File[] listFilesMatching = listFilesMatching(new FileNameContainsFilter(sb2.toString()));
        boolean z = listFilesMatching != null && listFilesMatching.length > 0;
        Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, String.format(Locale.US, "Session %s has fatal exception: %s", new Object[]{str, Boolean.valueOf(z)}));
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(SESSION_NON_FATAL_TAG);
        File[] listFilesMatching2 = listFilesMatching(new FileNameContainsFilter(sb3.toString()));
        boolean z2 = listFilesMatching2 != null && listFilesMatching2.length > 0;
        Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, String.format(Locale.US, "Session %s has non-fatal exceptions: %s", new Object[]{str, Boolean.valueOf(z2)}));
        if (z || z2) {
            synthesizeSessionFile(file, str, getTrimmedNonFatalFiles(str, listFilesMatching2, i), z ? listFilesMatching[0] : null);
        } else {
            Logger logger2 = Fabric.getLogger();
            String str3 = CrashlyticsCore.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("No events present for session ID ");
            sb4.append(str);
            logger2.mo21793d(str3, sb4.toString());
        }
        Logger logger3 = Fabric.getLogger();
        String str4 = CrashlyticsCore.TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Removing session part files for ID ");
        sb5.append(str);
        logger3.mo21793d(str4, sb5.toString());
        deleteSessionPartFilesFor(str);
    }

    private void synthesizeSessionFile(File file, String str, File[] fileArr, File file2) {
        ClsFileOutputStream clsFileOutputStream;
        ClsFileOutputStream clsFileOutputStream2;
        boolean z = file2 != null;
        ClsFileOutputStream clsFileOutputStream3 = null;
        try {
            clsFileOutputStream = new ClsFileOutputStream(getFilesDir(), str);
            try {
                CodedOutputStream newInstance = CodedOutputStream.newInstance((OutputStream) clsFileOutputStream);
                Logger logger = Fabric.getLogger();
                String str2 = CrashlyticsCore.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Collecting SessionStart data for session ID ");
                sb.append(str);
                logger.mo21793d(str2, sb.toString());
                writeToCosFromFile(newInstance, file);
                newInstance.writeUInt64(4, new Date().getTime() / 1000);
                newInstance.writeBool(5, z);
                writeInitialPartsTo(newInstance, str);
                writeNonFatalEventsTo(newInstance, fileArr, str);
                if (z) {
                    writeToCosFromFile(newInstance, file2);
                }
                newInstance.writeUInt32(11, 1);
                newInstance.writeEnum(12, 3);
                CommonUtils.flushOrLog(newInstance, "Error flushing session file stream");
                CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close CLS file");
            } catch (Exception e) {
                e = e;
                clsFileOutputStream2 = null;
                clsFileOutputStream3 = clsFileOutputStream;
                try {
                    Logger logger2 = Fabric.getLogger();
                    String str3 = CrashlyticsCore.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to write session file for session ID: ");
                    sb2.append(str);
                    logger2.mo21796e(str3, sb2.toString(), e);
                    ExceptionUtils.writeStackTraceIfNotNull(e, clsFileOutputStream3);
                    CommonUtils.flushOrLog(clsFileOutputStream2, "Error flushing session file stream");
                    closeWithoutRenamingOrLog(clsFileOutputStream3);
                } catch (Throwable th) {
                    th = th;
                    clsFileOutputStream = clsFileOutputStream3;
                    clsFileOutputStream3 = clsFileOutputStream2;
                    CommonUtils.flushOrLog(clsFileOutputStream3, "Error flushing session file stream");
                    CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close CLS file");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                CommonUtils.flushOrLog(clsFileOutputStream3, "Error flushing session file stream");
                CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close CLS file");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            clsFileOutputStream2 = null;
            Logger logger22 = Fabric.getLogger();
            String str32 = CrashlyticsCore.TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("Failed to write session file for session ID: ");
            sb22.append(str);
            logger22.mo21796e(str32, sb22.toString(), e);
            ExceptionUtils.writeStackTraceIfNotNull(e, clsFileOutputStream3);
            CommonUtils.flushOrLog(clsFileOutputStream2, "Error flushing session file stream");
            closeWithoutRenamingOrLog(clsFileOutputStream3);
        } catch (Throwable th3) {
            th = th3;
            clsFileOutputStream = null;
            CommonUtils.flushOrLog(clsFileOutputStream3, "Error flushing session file stream");
            CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close CLS file");
            throw th;
        }
    }

    private static void writeNonFatalEventsTo(CodedOutputStream codedOutputStream, File[] fileArr, String str) {
        Arrays.sort(fileArr, CommonUtils.FILE_MODIFIED_COMPARATOR);
        for (File file : fileArr) {
            try {
                Fabric.getLogger().mo21793d(CrashlyticsCore.TAG, String.format(Locale.US, "Found Non Fatal for session ID %s in %s ", new Object[]{str, file.getName()}));
                writeToCosFromFile(codedOutputStream, file);
            } catch (Exception e) {
                Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Error writting non-fatal to session.", e);
            }
        }
    }

    private void writeInitialPartsTo(CodedOutputStream codedOutputStream, String str) throws IOException {
        String[] strArr;
        for (String str2 : INITIAL_SESSION_PART_TAGS) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str2);
            File[] listFilesMatching = listFilesMatching(new FileNameContainsFilter(sb.toString()));
            if (listFilesMatching.length == 0) {
                Logger logger = Fabric.getLogger();
                String str3 = CrashlyticsCore.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Can't find ");
                sb2.append(str2);
                sb2.append(" data for session ID ");
                sb2.append(str);
                logger.mo21796e(str3, sb2.toString(), null);
            } else {
                Logger logger2 = Fabric.getLogger();
                String str4 = CrashlyticsCore.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Collecting ");
                sb3.append(str2);
                sb3.append(" data for session ID ");
                sb3.append(str);
                logger2.mo21793d(str4, sb3.toString());
                writeToCosFromFile(codedOutputStream, listFilesMatching[0]);
            }
        }
    }

    private static void writeToCosFromFile(CodedOutputStream codedOutputStream, File file) throws IOException {
        FileInputStream fileInputStream;
        if (!file.exists()) {
            Logger logger = Fabric.getLogger();
            String str = CrashlyticsCore.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to include a file that doesn't exist: ");
            sb.append(file.getName());
            logger.mo21796e(str, sb.toString(), null);
            return;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                copyToCodedOutputStream(fileInputStream, codedOutputStream, (int) file.length());
                CommonUtils.closeOrLog(fileInputStream, "Failed to close file input stream.");
            } catch (Throwable th) {
                th = th;
                CommonUtils.closeOrLog(fileInputStream, "Failed to close file input stream.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            CommonUtils.closeOrLog(fileInputStream, "Failed to close file input stream.");
            throw th;
        }
    }

    private static void copyToCodedOutputStream(InputStream inputStream, CodedOutputStream codedOutputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < bArr.length) {
            int read = inputStream.read(bArr, i2, bArr.length - i2);
            if (read < 0) {
                break;
            }
            i2 += read;
        }
        codedOutputStream.writeRawBytes(bArr);
    }

    private UserMetaData getUserMetaData(String str) {
        return isHandlingException() ? new UserMetaData(this.crashlyticsCore.getUserIdentifier(), this.crashlyticsCore.getUserName(), this.crashlyticsCore.getUserEmail()) : new MetaDataStore(getFilesDir()).readUserData(str);
    }

    private void sendSessionReports() {
        for (File sendSessionRunnable : listCompleteSessionFiles()) {
            this.executorServiceWrapper.executeAsync((Runnable) new SendSessionRunnable(this.crashlyticsCore, sendSessionRunnable));
        }
    }

    /* access modifiers changed from: private */
    public File getFilesDir() {
        return this.fileStore.getFilesDir();
    }
}
