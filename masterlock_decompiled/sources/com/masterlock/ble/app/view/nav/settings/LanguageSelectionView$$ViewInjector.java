package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LanguageSelectionView$$ViewInjector {
    public static void inject(Finder finder, final LanguageSelectionView languageSelectionView, Object obj) {
        languageSelectionView.rGLanguages = (RadioGroup) finder.findRequiredView(obj, C1075R.C1077id.rg_languages, "field 'rGLanguages'");
        finder.findRequiredView(obj, C1075R.C1077id.button_save, "method 'saveLocale'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                languageSelectionView.saveLocale();
            }
        });
    }

    public static void reset(LanguageSelectionView languageSelectionView) {
        languageSelectionView.rGLanguages = null;
    }
}
