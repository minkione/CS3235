package com.masterlock.ble.app.view.lock.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LastLocationInfoKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final LastLocationInfoKeySafeView lastLocationInfoKeySafeView, Object obj) {
        lastLocationInfoKeySafeView.mMapView = (FrameLayout) finder.findRequiredView(obj, C1075R.C1077id.last_location_map, "field 'mMapView'");
        lastLocationInfoKeySafeView.mAddress = (TextView) finder.findRequiredView(obj, C1075R.C1077id.last_location_address, "field 'mAddress'");
        lastLocationInfoKeySafeView.mCoordinates = (TextView) finder.findRequiredView(obj, C1075R.C1077id.last_location_coordinates, "field 'mCoordinates'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.last_location_clear, "field 'mClear' and method 'clearLocation'");
        lastLocationInfoKeySafeView.mClear = (TextView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lastLocationInfoKeySafeView.clearLocation();
            }
        });
        lastLocationInfoKeySafeView.mLockNotes = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.last_location_notes_floating_edit_text, "field 'mLockNotes'");
        finder.findRequiredView(obj, C1075R.C1077id.last_location_save, "method 'saveLocationNotes'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lastLocationInfoKeySafeView.saveLocationNotes();
            }
        });
    }

    public static void reset(LastLocationInfoKeySafeView lastLocationInfoKeySafeView) {
        lastLocationInfoKeySafeView.mMapView = null;
        lastLocationInfoKeySafeView.mAddress = null;
        lastLocationInfoKeySafeView.mCoordinates = null;
        lastLocationInfoKeySafeView.mClear = null;
        lastLocationInfoKeySafeView.mLockNotes = null;
    }
}
