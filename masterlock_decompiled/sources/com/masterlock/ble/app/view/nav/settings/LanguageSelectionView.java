package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.LanguageSelectionPresenter;
import java.util.Locale;

public class LanguageSelectionView extends LinearLayout {
    private LanguageSelectionPresenter mLanguageSelectionPresenter;
    private OnCheckedChangeListener onCheckedChangeListener;
    @InjectView(2131296699)
    RadioGroup rGLanguages;

    public LanguageSelectionView(Context context) {
        this(context, null);
    }

    public LanguageSelectionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.onCheckedChangeListener = new OnCheckedChangeListener() {
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                LanguageSelectionView.lambda$new$0(LanguageSelectionView.this, radioGroup, i);
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mLanguageSelectionPresenter = new LanguageSelectionPresenter(this);
            this.mLanguageSelectionPresenter.start();
            this.rGLanguages.setOnCheckedChangeListener(this.onCheckedChangeListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mLanguageSelectionPresenter.finish();
        super.onDetachedFromWindow();
    }

    public void displaySuccess() {
        Toast.makeText(getContext(), C1075R.string.notifications_events_updated, 0).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void populateLanguages(Locale[] localeArr) {
        int i = 0;
        while (i < localeArr.length) {
            Locale locale = localeArr[i];
            RadioButton radioButton = new RadioButton(getContext());
            StringBuilder sb = new StringBuilder(i == 0 ? getResources().getString(C1075R.string.account_setting_default_language_text) : locale.getDisplayLanguage());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            radioButton.setText(sb);
            radioButton.setTag(Integer.valueOf(i));
            radioButton.setId(View.generateViewId());
            this.rGLanguages.addView(radioButton);
            if ((i == 0) && this.mLanguageSelectionPresenter.isDefaultLanguage()) {
                radioButton.setChecked(true);
            } else {
                if (this.mLanguageSelectionPresenter.getSelectedLanguage().equalsIgnoreCase(locale.getLanguage()) && (!this.mLanguageSelectionPresenter.isDefaultLanguage())) {
                    radioButton.setChecked(true);
                }
            }
            i++;
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296362})
    public void saveLocale() {
        this.mLanguageSelectionPresenter.applyLanguage();
    }

    public static /* synthetic */ void lambda$new$0(LanguageSelectionView languageSelectionView, RadioGroup radioGroup, int i) {
        RadioButton radioButton = (RadioButton) ButterKnife.findById((View) languageSelectionView, i);
        if (radioButton.getTag() != null) {
            languageSelectionView.mLanguageSelectionPresenter.setSelectedLanguage(((Integer) radioButton.getTag()).intValue());
        }
    }
}
