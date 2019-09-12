package com.masterlock.ble.app.presenter.nav.settings;

import android.util.Log;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.ApplyLanguageEvent;
import com.masterlock.ble.app.gamma.LocaleHelper;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.nav.settings.LanguageSelectionView;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.Locale;
import javax.inject.Inject;

public class LanguageSelectionPresenter extends Presenter<Void, LanguageSelectionView> {
    private Locale[] appLanguages = {new Locale(MasterLockApp.get().getDefSystemLanguage()), Locale.ENGLISH, new Locale("es"), Locale.FRENCH, Locale.GERMAN};
    private String lastSavedLanguage = this.selectedLanguage;
    private int lastSelectedPosition = MasterLockSharedPreferences.getInstance().getSelectedLanguageArrayIndex();
    @Inject
    Bus mEventBus;
    @Inject
    IScheduler mScheduler;
    private boolean mustUpdateLanguage = false;
    private String selectedLanguage = LocaleHelper.getLanguage();

    public LanguageSelectionPresenter(LanguageSelectionView languageSelectionView) {
        super(languageSelectionView);
        MasterLockApp.get().inject(this);
        this.mEventBus.register(this);
    }

    public void start() {
        ((LanguageSelectionView) this.view).populateLanguages(this.appLanguages);
    }

    public void finish() {
        super.finish();
        this.mEventBus.unregister(this);
        boolean z = !this.lastSavedLanguage.equals(this.selectedLanguage);
        if (this.mustUpdateLanguage) {
            this.mEventBus.post(new ApplyLanguageEvent(this.selectedLanguage, this.lastSelectedPosition, z));
        }
    }

    public String getSelectedLanguage() {
        return this.selectedLanguage;
    }

    public void setSelectedLanguage(int i) {
        this.selectedLanguage = this.appLanguages[i].getLanguage();
        this.lastSelectedPosition = i;
    }

    public void applyLanguage() {
        if (MasterLockSharedPreferences.getInstance().getSelectedLanguageArrayIndex() != this.lastSelectedPosition) {
            ((LanguageSelectionView) this.view).displaySuccess();
            AppFlow.get(((LanguageSelectionView) this.view).getContext()).goBack();
            this.mustUpdateLanguage = true;
        }
    }

    public boolean isDefaultLanguage() {
        return MasterLockSharedPreferences.getInstance().getSelectedLanguageArrayIndex() == 0;
    }

    private void printLocales() {
        Locale[] localeArr;
        String str = "";
        for (Locale locale : this.appLanguages) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(locale.getLanguage());
            sb.append(", ");
            str = sb.toString();
        }
        String simpleName = getClass().getSimpleName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("printLocales: ");
        sb2.append(str);
        Log.d(simpleName, sb2.toString());
    }
}
