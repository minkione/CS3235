package com.crashlytics.android.core;

import java.io.File;
import java.io.IOException;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.persistence.FileStore;

class CrashlyticsFileMarker {
    private final FileStore fileStore;
    private final String markerName;

    public CrashlyticsFileMarker(String str, FileStore fileStore2) {
        this.markerName = str;
        this.fileStore = fileStore2;
    }

    public boolean create() {
        try {
            return getMarkerFile().createNewFile();
        } catch (IOException e) {
            Logger logger = Fabric.getLogger();
            String str = CrashlyticsCore.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Error creating marker: ");
            sb.append(this.markerName);
            logger.mo21796e(str, sb.toString(), e);
            return false;
        }
    }

    public boolean isPresent() {
        return getMarkerFile().exists();
    }

    public boolean remove() {
        return getMarkerFile().delete();
    }

    private File getMarkerFile() {
        return new File(this.fileStore.getFilesDir(), this.markerName);
    }
}
