package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.adapters.CountryModalAdapter;
import com.masterlock.ble.app.adapters.OnItemClickListener;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.core.Country;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;

public class CountriesModal extends CardView {
    /* access modifiers changed from: private */
    public static final String TAG = "CountriesModal";
    @InjectView(2131296682)
    RecyclerView countriesList;
    @InjectView(2131296470)
    EditText findCountry;
    /* access modifiers changed from: private */
    public CountryModalAdapter mAdapter;
    /* access modifiers changed from: private */
    public CountrySelectedListener mListener;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription;

    public interface CountrySelectedListener {
        void onCloseClicked();

        void onCountrySelected(Country country);
    }

    public CountriesModal(Context context) {
        super(context, null);
    }

    public CountriesModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        MasterLockApp.get().inject(this);
        inflate(getContext(), C1075R.layout.select_country_code_modal, this);
        ButterKnife.inject((View) this);
        this.countriesList.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.countriesList.setHasFixedSize(true);
        this.findCountry.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                CountriesModal.this.getCountriesListBasedOnSearch(charSequence.toString());
            }
        });
        this.findCountry.setOnEditorActionListener(new OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return CountriesModal.lambda$init$0(CountriesModal.this, textView, i, keyEvent);
            }
        });
        this.findCountry.setOnFocusChangeListener(new OnFocusChangeListener() {
            public final void onFocusChange(View view, boolean z) {
                CountriesModal.lambda$init$1(CountriesModal.this, view, z);
            }
        });
        getCountriesList();
    }

    public static /* synthetic */ boolean lambda$init$0(CountriesModal countriesModal, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        ViewUtil.hideKeyboard(countriesModal.getContext(), countriesModal.findCountry.getWindowToken());
        return true;
    }

    public static /* synthetic */ void lambda$init$1(CountriesModal countriesModal, View view, boolean z) {
        countriesModal.findCountry.setHint(z ? C1075R.string.empty_string : C1075R.string.country_code_textfield);
    }

    /* access modifiers changed from: 0000 */
    public void getCountriesList() {
        this.mSubscription = getCountries().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Country>>() {
            public void onCompleted() {
            }

            public void onStart() {
            }

            public void onError(Throwable th) {
                Log.e(CountriesModal.TAG, "onError: ", th);
            }

            public void onNext(List<Country> list) {
                Collections.sort(list, $$Lambda$CountriesModal$2$ibv15FsY4edABqBhE0ETx3Dt1g.INSTANCE);
                CountriesModal.this.mAdapter = new CountryModalAdapter(list);
                CountriesModal.this.mAdapter.setOnItemClickListener(new OnItemClickListener(list) {
                    private final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void onItemClick(View view, int i) {
                        CountriesModal.this.mListener.onCountrySelected((Country) this.f$1.get(i));
                    }
                });
                CountriesModal.this.countriesList.setAdapter(CountriesModal.this.mAdapter);
            }
        });
    }

    private Observable<List<Country>> getCountries() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe() {
            public final void call(Object obj) {
                CountriesModal.lambda$getCountries$2(CountriesModal.this, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getCountries$2(CountriesModal countriesModal, Subscriber subscriber) {
        String str = "country.json";
        new ArrayList();
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(countriesModal.getContext().getAssets().open(str)));
            List<Country> list = (List) new Gson().fromJson((Reader) bufferedReader, new TypeToken<List<Country>>() {
            }.getType());
            bufferedReader.close();
            for (Country country : list) {
                if (country.callingCode.length > 0) {
                    String[] strArr = country.callingCode;
                    StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(country.callingCode[0]);
                    strArr[0] = sb.toString();
                    arrayList.add(country);
                }
            }
            subscriber.onNext(arrayList);
        } catch (IOException e) {
            subscriber.onError(e);
        } catch (Throwable th) {
            subscriber.onCompleted();
            throw th;
        }
        subscriber.onCompleted();
    }

    /* access modifiers changed from: 0000 */
    public void getCountriesListBasedOnSearch(String str) {
        this.mSubscription = getCountriesBasedOnSearch(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Country>>() {
            public void onCompleted() {
            }

            public void onStart() {
            }

            public void onError(Throwable th) {
                Log.e(CountriesModal.TAG, "onError: ", th);
            }

            public void onNext(List<Country> list) {
                Collections.sort(list, $$Lambda$CountriesModal$4$InSkT7zjp7xOxF87MNjMBVdi0b0.INSTANCE);
                CountriesModal.this.mAdapter = new CountryModalAdapter(list);
                CountriesModal.this.mAdapter.setOnItemClickListener(new OnItemClickListener(list) {
                    private final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void onItemClick(View view, int i) {
                        CountriesModal.this.mListener.onCountrySelected((Country) this.f$1.get(i));
                    }
                });
                CountriesModal.this.countriesList.setAdapter(CountriesModal.this.mAdapter);
            }
        });
    }

    private Observable<List<Country>> getCountriesBasedOnSearch(String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                CountriesModal.lambda$getCountriesBasedOnSearch$3(CountriesModal.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getCountriesBasedOnSearch$3(CountriesModal countriesModal, String str, Subscriber subscriber) {
        String str2 = "country.json";
        new ArrayList();
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(countriesModal.getContext().getAssets().open(str2)));
            List<Country> list = (List) new Gson().fromJson((Reader) bufferedReader, new TypeToken<List<Country>>() {
            }.getType());
            bufferedReader.close();
            String language = Locale.getDefault().getLanguage();
            for (Country country : list) {
                String normalize = Normalizer.normalize(country.name.commonName.toLowerCase(), Form.NFD);
                char c = 65535;
                int hashCode = language.hashCode();
                if (hashCode != 3201) {
                    if (hashCode != 3246) {
                        if (hashCode == 3276) {
                            if (language.equals("fr")) {
                                c = 1;
                            }
                        }
                    } else if (language.equals("es")) {
                        c = 0;
                    }
                } else if (language.equals("de")) {
                    c = 2;
                }
                switch (c) {
                    case 0:
                        if (country.hasCommonTranslationForLanguage(language)) {
                            normalize = Normalizer.normalize(country.translations.spa.common.toLowerCase(), Form.NFD);
                            break;
                        }
                        break;
                    case 1:
                        if (country.hasCommonTranslationForLanguage(language)) {
                            normalize = Normalizer.normalize(country.translations.fra.common.toLowerCase(), Form.NFD);
                            break;
                        }
                        break;
                    case 2:
                        if (country.hasCommonTranslationForLanguage(language)) {
                            normalize = Normalizer.normalize(country.translations.deu.common.toLowerCase(), Form.NFD);
                            break;
                        }
                        break;
                }
                if (normalize.replaceAll("[^\\p{ASCII}]", "").startsWith(str.toLowerCase()) && country.callingCode.length > 0) {
                    String[] strArr = country.callingCode;
                    StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(country.callingCode[0]);
                    strArr[0] = sb.toString();
                    arrayList.add(country);
                }
            }
            subscriber.onNext(arrayList);
        } catch (Exception e) {
            subscriber.onError(e);
        } catch (Throwable th) {
            subscriber.onCompleted();
            throw th;
        }
        subscriber.onCompleted();
    }

    public void setCountrySelectedListener(CountrySelectedListener countrySelectedListener) {
        this.mListener = countrySelectedListener;
    }
}
