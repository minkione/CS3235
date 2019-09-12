package com.masterlock.api.util;

import java.util.Locale;

public interface IResourceWrapper {
    Locale getLocale();

    void logOut();

    String stringFromId(int i);

    String stringFromName(String str);

    String stringFromName(String str, Object... objArr);

    String stringFromPlural(String str, int i, Object... objArr);
}
