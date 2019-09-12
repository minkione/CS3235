package com.masterlock.ble.app.adapters;

import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.CountryModalAdapter.GenericViewHolder;
import com.masterlock.core.Country;
import java.util.List;

public class CountryModalAdapter extends Adapter<GenericViewHolder> {
    private List<Country> mCountries;
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClickListener;

    public class GenericViewHolder extends ViewHolder {
        @InjectView(2131296395)
        TextView countryCode;
        @InjectView(2131296855)
        TextView countryTxt;

        public GenericViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
            view.setOnTouchListener(C1096x48b6eb64.INSTANCE);
            view.setOnClickListener(new OnClickListener(view) {
                private final /* synthetic */ View f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    CountryModalAdapter.this.mOnItemClickListener.onItemClick(this.f$1, GenericViewHolder.this.getAdapterPosition());
                }
            });
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0037, code lost:
            if (r1.equals("es") != false) goto L_0x0045;
         */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0049  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0056  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0063  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void bind(com.masterlock.core.Country r6) {
            /*
                r5 = this;
                android.widget.TextView r0 = r5.countryCode
                java.lang.String[] r1 = r6.callingCode
                r2 = 0
                r1 = r1[r2]
                r0.setText(r1)
                com.masterlock.core.Country$CountryName r0 = r6.name
                java.lang.String r0 = r0.commonName
                java.util.Locale r1 = java.util.Locale.getDefault()
                java.lang.String r1 = r1.getLanguage()
                int r3 = r1.hashCode()
                r4 = 3201(0xc81, float:4.486E-42)
                if (r3 == r4) goto L_0x003a
                r4 = 3246(0xcae, float:4.549E-42)
                if (r3 == r4) goto L_0x0031
                r2 = 3276(0xccc, float:4.59E-42)
                if (r3 == r2) goto L_0x0027
                goto L_0x0044
            L_0x0027:
                java.lang.String r2 = "fr"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L_0x0044
                r2 = 1
                goto L_0x0045
            L_0x0031:
                java.lang.String r3 = "es"
                boolean r1 = r1.equals(r3)
                if (r1 == 0) goto L_0x0044
                goto L_0x0045
            L_0x003a:
                java.lang.String r2 = "de"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L_0x0044
                r2 = 2
                goto L_0x0045
            L_0x0044:
                r2 = -1
            L_0x0045:
                switch(r2) {
                    case 0: goto L_0x0063;
                    case 1: goto L_0x0056;
                    case 2: goto L_0x0049;
                    default: goto L_0x0048;
                }
            L_0x0048:
                goto L_0x006f
            L_0x0049:
                com.masterlock.core.Translation r1 = r6.translations
                com.masterlock.core.CountryTranslation r1 = r1.deu
                if (r1 == 0) goto L_0x006f
                com.masterlock.core.Translation r6 = r6.translations
                com.masterlock.core.CountryTranslation r6 = r6.deu
                java.lang.String r0 = r6.common
                goto L_0x006f
            L_0x0056:
                com.masterlock.core.Translation r1 = r6.translations
                com.masterlock.core.CountryTranslation r1 = r1.fra
                if (r1 == 0) goto L_0x006f
                com.masterlock.core.Translation r6 = r6.translations
                com.masterlock.core.CountryTranslation r6 = r6.fra
                java.lang.String r0 = r6.common
                goto L_0x006f
            L_0x0063:
                com.masterlock.core.Translation r1 = r6.translations
                com.masterlock.core.CountryTranslation r1 = r1.spa
                if (r1 == 0) goto L_0x006f
                com.masterlock.core.Translation r6 = r6.translations
                com.masterlock.core.CountryTranslation r6 = r6.spa
                java.lang.String r0 = r6.common
            L_0x006f:
                android.widget.TextView r6 = r5.countryTxt
                r6.setText(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.adapters.CountryModalAdapter.GenericViewHolder.bind(com.masterlock.core.Country):void");
        }
    }

    public CountryModalAdapter(List<Country> list) {
        this.mCountries = list;
    }

    public GenericViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GenericViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1075R.layout.country_list_item, viewGroup, false));
    }

    public void onBindViewHolder(GenericViewHolder genericViewHolder, int i) {
        genericViewHolder.bind((Country) this.mCountries.get(i));
    }

    public int getItemCount() {
        return this.mCountries.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
