package com.masterlock.ble.app.view.lock.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LastLocationInfoPadLockView$$ViewInjector {
    public static void inject(Finder finder, final LastLocationInfoPadLockView lastLocationInfoPadLockView, Object obj) {
        lastLocationInfoPadLockView.mMapView = (FrameLayout) finder.findRequiredView(obj, C1075R.C1077id.last_location_map, "field 'mMapView'");
        lastLocationInfoPadLockView.mAddress = (TextView) finder.findRequiredView(obj, C1075R.C1077id.last_location_address, "field 'mAddress'");
        lastLocationInfoPadLockView.mCoordinates = (TextView) finder.findRequiredView(obj, C1075R.C1077id.last_location_coordinates, "field 'mCoordinates'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.last_location_clear, "field 'mClear' and method 'clearLocation'");
        lastLocationInfoPadLockView.mClear = (TextView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lastLocationInfoPadLockView.clearLocation();
            }
        });
        lastLocationInfoPadLockView.mLockNotes = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.last_location_notes_floating_edit_text, "field 'mLockNotes'");
        finder.findRequiredView(obj, C1075R.C1077id.last_location_save, "method 'saveLocationNotes'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lastLocationInfoPadLockView.saveLocationNotes();
            }
        });
    }

    public static void reset(LastLocationInfoPadLockView lastLocationInfoPadLockView) {
        lastLocationInfoPadLockView.mMapView = null;
        lastLocationInfoPadLockView.mAddress = null;
        lastLocationInfoPadLockView.mCoordinates = null;
        lastLocationInfoPadLockView.mClear = null;
        lastLocationInfoPadLockView.mLockNotes = null;
    }
}
