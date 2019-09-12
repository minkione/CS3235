package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ShareTemporaryCodesView$$ViewInjector {
    public static void inject(Finder finder, final ShareTemporaryCodesView shareTemporaryCodesView, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.rb_share_code, "method 'onClickShareCodeRadioButton'");
        shareTemporaryCodesView.rBShareCode = (RadioButton) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                shareTemporaryCodesView.onClickShareCodeRadioButton();
            }
        });
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.rb_share_future_code, "field 'rBShareFutureCode' and method 'onClickShareFutureCodeRadioButton'");
        shareTemporaryCodesView.rBShareFutureCode = (RadioButton) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                shareTemporaryCodesView.onClickShareFutureCodeRadioButton();
            }
        });
        shareTemporaryCodesView.dateContainer = finder.findRequiredView(obj, C1075R.C1077id.rl_date_container, "field 'dateContainer'");
        View findRequiredView3 = finder.findRequiredView(obj, C1075R.C1077id.iv_calendar, "field 'iVCalendar' and method 'onClick'");
        shareTemporaryCodesView.iVCalendar = (ImageView) findRequiredView3;
        findRequiredView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                shareTemporaryCodesView.onClick();
            }
        });
        shareTemporaryCodesView.tVTemporaryCodeDate = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_code_date, "field 'tVTemporaryCodeDate'");
        shareTemporaryCodesView.tVTimeZone = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_time_zone, "field 'tVTimeZone'");
        finder.findRequiredView(obj, C1075R.C1077id.share_temporary_code_button, "method 'onClickShareCodeButton'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                shareTemporaryCodesView.onClickShareCodeButton();
            }
        });
    }

    public static void reset(ShareTemporaryCodesView shareTemporaryCodesView) {
        shareTemporaryCodesView.rBShareCode = null;
        shareTemporaryCodesView.rBShareFutureCode = null;
        shareTemporaryCodesView.dateContainer = null;
        shareTemporaryCodesView.iVCalendar = null;
        shareTemporaryCodesView.tVTemporaryCodeDate = null;
        shareTemporaryCodesView.tVTimeZone = null;
    }
}
