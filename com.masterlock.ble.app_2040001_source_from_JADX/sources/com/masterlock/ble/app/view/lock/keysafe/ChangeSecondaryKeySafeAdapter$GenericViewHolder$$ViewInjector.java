package com.masterlock.ble.app.view.lock.keysafe;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.lock.keysafe.ChangeSecondaryKeySafeAdapter.GenericViewHolder;

public class ChangeSecondaryKeySafeAdapter$GenericViewHolder$$ViewInjector {
    public static void inject(Finder finder, GenericViewHolder genericViewHolder, Object obj) {
        genericViewHolder.digitContainer1 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_1, "field 'digitContainer1'");
        genericViewHolder.digitContainer2 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_2, "field 'digitContainer2'");
        genericViewHolder.digitContainer3 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_3, "field 'digitContainer3'");
        genericViewHolder.digitContainer4 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_4, "field 'digitContainer4'");
        genericViewHolder.digitContainer5 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_5, "field 'digitContainer5'");
        genericViewHolder.digitContainer6 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_6, "field 'digitContainer6'");
        genericViewHolder.digitContainer7 = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.digit_container_7, "field 'digitContainer7'");
        genericViewHolder.digitContainer8 = finder.findRequiredView(obj, C1075R.C1077id.digit_container_8, "field 'digitContainer8'");
        genericViewHolder.newCodeTxt = (TextView) finder.findRequiredView(obj, C1075R.C1077id.text_new_code, "field 'newCodeTxt'");
        genericViewHolder.deleteButton = (ImageButton) finder.findRequiredView(obj, C1075R.C1077id.secondary_code_delete_button_1, "field 'deleteButton'");
        genericViewHolder.itemLayout = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.item_layout, "field 'itemLayout'");
    }

    public static void reset(GenericViewHolder genericViewHolder) {
        genericViewHolder.digitContainer1 = null;
        genericViewHolder.digitContainer2 = null;
        genericViewHolder.digitContainer3 = null;
        genericViewHolder.digitContainer4 = null;
        genericViewHolder.digitContainer5 = null;
        genericViewHolder.digitContainer6 = null;
        genericViewHolder.digitContainer7 = null;
        genericViewHolder.digitContainer8 = null;
        genericViewHolder.newCodeTxt = null;
        genericViewHolder.deleteButton = null;
        genericViewHolder.itemLayout = null;
    }
}
