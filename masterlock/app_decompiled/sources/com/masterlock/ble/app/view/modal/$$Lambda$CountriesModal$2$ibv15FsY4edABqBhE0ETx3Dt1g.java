package com.masterlock.ble.app.view.modal;

import com.masterlock.core.Country;
import java.util.Comparator;

/* renamed from: com.masterlock.ble.app.view.modal.-$$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt-1g reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt1g implements Comparator {
    public static final /* synthetic */ $$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt1g INSTANCE = new $$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt1g();

    private /* synthetic */ $$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt1g() {
    }

    public final int compare(Object obj, Object obj2) {
        return ((Country) obj).name.commonName.compareTo(((Country) obj2).name.commonName);
    }
}
