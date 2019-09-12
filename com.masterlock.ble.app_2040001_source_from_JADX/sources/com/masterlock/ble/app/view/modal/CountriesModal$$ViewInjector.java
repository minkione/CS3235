package com.masterlock.ble.app.view.modal;

import android.support.p003v7.widget.RecyclerView;
import android.widget.EditText;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class CountriesModal$$ViewInjector {
    public static void inject(Finder finder, CountriesModal countriesModal, Object obj) {
        countriesModal.findCountry = (EditText) finder.findRequiredView(obj, C1075R.C1077id.find_country, "field 'findCountry'");
        countriesModal.countriesList = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.recycler_code_list, "field 'countriesList'");
    }

    public static void reset(CountriesModal countriesModal) {
        countriesModal.findCountry = null;
        countriesModal.countriesList = null;
    }
}
