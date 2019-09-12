package com.masterlock.ble.app.view.modal;

import com.masterlock.core.Country;
import java.util.Comparator;

/* renamed from: com.masterlock.ble.app.view.modal.-$$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0 reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0 implements Comparator {
    public static final /* synthetic */ $$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0 INSTANCE = new $$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0();

    private /* synthetic */ $$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0() {
    }

    public final int compare(Object obj, Object obj2) {
        return ((Country) obj).name.commonName.compareTo(((Country) obj2).name.commonName);
    }
}
