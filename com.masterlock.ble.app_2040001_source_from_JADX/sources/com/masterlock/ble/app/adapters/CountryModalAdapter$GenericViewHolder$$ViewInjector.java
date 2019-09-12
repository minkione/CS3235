package com.masterlock.ble.app.adapters;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.CountryModalAdapter.GenericViewHolder;

public class CountryModalAdapter$GenericViewHolder$$ViewInjector {
    public static void inject(Finder finder, GenericViewHolder genericViewHolder, Object obj) {
        genericViewHolder.countryTxt = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_country, "field 'countryTxt'");
        genericViewHolder.countryCode = (TextView) finder.findRequiredView(obj, C1075R.C1077id.country_code, "field 'countryCode'");
    }

    public static void reset(GenericViewHolder genericViewHolder) {
        genericViewHolder.countryTxt = null;
        genericViewHolder.countryCode = null;
    }
}
