package com.crashlytics.android.core;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Logger;

class SessionReport implements Report {
    private final Map<String, String> customHeaders;
    private final File file;

    public SessionReport(File file2) {
        this(file2, Collections.emptyMap());
    }

    public SessionReport(File file2, Map<String, String> map) {
        this.file = file2;
        this.customHeaders = new HashMap(map);
        if (this.file.length() == 0) {
            this.customHeaders.putAll(ReportUploader.HEADER_INVALID_CLS_FILE);
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return getFile().getName();
    }

    public String getIdentifier() {
        String fileName = getFileName();
        return fileName.substring(0, fileName.lastIndexOf(46));
    }

    public Map<String, String> getCustomHeaders() {
        return Collections.unmodifiableMap(this.customHeaders);
    }

    public boolean remove() {
        Logger logger = Fabric.getLogger();
        String str = CrashlyticsCore.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Removing report at ");
        sb.append(this.file.getPath());
        logger.mo21793d(str, sb.toString());
        return this.file.delete();
    }
}
