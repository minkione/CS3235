package com.masterlock.ble.app.view.lock.keysafe.shackle;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ShackleUnlockedView$$ViewInjector {
    public static void inject(Finder finder, ShackleUnlockedView shackleUnlockedView, Object obj) {
        shackleUnlockedView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.relock_time, "field 'mRelockTime'");
    }

    public static void reset(ShackleUnlockedView shackleUnlockedView) {
        shackleUnlockedView.mRelockTime = null;
    }
}
