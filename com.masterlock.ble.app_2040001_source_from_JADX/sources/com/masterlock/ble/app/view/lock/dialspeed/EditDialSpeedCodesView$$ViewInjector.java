package com.masterlock.ble.app.view.lock.dialspeed;

import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class EditDialSpeedCodesView$$ViewInjector {
    public static void inject(Finder finder, final EditDialSpeedCodesView editDialSpeedCodesView, Object obj) {
        editDialSpeedCodesView.mCodeName1ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_1_name, "field 'mCodeName1ET'");
        editDialSpeedCodesView.mCode1ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_1, "field 'mCode1ET'");
        editDialSpeedCodesView.mCodeName2ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_2_name, "field 'mCodeName2ET'");
        editDialSpeedCodesView.mCode2ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_2, "field 'mCode2ET'");
        editDialSpeedCodesView.mCodeName3ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_3_name, "field 'mCodeName3ET'");
        editDialSpeedCodesView.mCode3ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_3, "field 'mCode3ET'");
        editDialSpeedCodesView.mCodeName4ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_4_name, "field 'mCodeName4ET'");
        editDialSpeedCodesView.mCode4ET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_code_4, "field 'mCode4ET'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save, "method 'saveCodes'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editDialSpeedCodesView.saveCodes();
            }
        });
    }

    public static void reset(EditDialSpeedCodesView editDialSpeedCodesView) {
        editDialSpeedCodesView.mCodeName1ET = null;
        editDialSpeedCodesView.mCode1ET = null;
        editDialSpeedCodesView.mCodeName2ET = null;
        editDialSpeedCodesView.mCode2ET = null;
        editDialSpeedCodesView.mCodeName3ET = null;
        editDialSpeedCodesView.mCode3ET = null;
        editDialSpeedCodesView.mCodeName4ET = null;
        editDialSpeedCodesView.mCode4ET = null;
    }
}
