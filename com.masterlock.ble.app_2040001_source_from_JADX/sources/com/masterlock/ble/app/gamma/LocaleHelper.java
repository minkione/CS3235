package com.masterlock.ble.app.gamma;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import java.util.Locale;

public class LocaleHelper {
    public static Context onAttach() {
        String storedOrDefaultLanguage = getStoredOrDefaultLanguage();
        int selectedLanguageArrayIndex = MasterLockSharedPreferences.getInstance().getSelectedLanguageArrayIndex();
        if (selectedLanguageArrayIndex == -1) {
            selectedLanguageArrayIndex = 0;
        }
        return setLocale(storedOrDefaultLanguage, selectedLanguageArrayIndex);
    }

    public static String getLanguage() {
        return getStoredOrDefaultLanguage();
    }

    public static Context setLocale(String str, int i) {
        storeSelectedLanguage(str, i);
        if (VERSION.SDK_INT >= 23) {
            return updateResources(str);
        }
        return updateResourcesLegacy(str);
    }

    private static String getStoredOrDefaultLanguage() {
        return MasterLockSharedPreferences.getInstance().getSelectedLanguageArrayIndex() > 0 ? MasterLockSharedPreferences.getInstance().getSelectedLanguage() : MasterLockApp.get().getDefSystemLanguage();
    }

    private static void storeSelectedLanguage(String str, int i) {
        MasterLockSharedPreferences.getInstance().putSelectedLanguage(str);
        MasterLockSharedPreferences.getInstance().putSelectedLanguageArrayIndex(i);
    }

    private static Context updateResources(String str) {
        Locale locale = new Locale(str, Locale.getDefault().getCountry(), Locale.getDefault().getVariant());
        Locale.setDefault(locale);
        Configuration configuration = MasterLockApp.get().getResources().getConfiguration();
        configuration.setLocale(locale);
        return MasterLockApp.get().createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(String str) {
        Locale locale = new Locale(str, Locale.getDefault().getCountry(), Locale.getDefault().getVariant());
        Locale.setDefault(locale);
        Resources resources = MasterLockApp.get().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return MasterLockApp.get();
    }
}
