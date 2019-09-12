package com.masterlock.ble.app.view.settings;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class UnlockModeCard$$ViewInjector {
    public static void inject(Finder finder, UnlockModeCard unlockModeCard, Object obj) {
        unlockModeCard.mCardTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.unlock_type_title, "field 'mCardTitle'");
        unlockModeCard.mBetaMode = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.unlock_mode_beta, "field 'mBetaMode'");
        unlockModeCard.mCardDesc = (TextView) finder.findRequiredView(obj, C1075R.C1077id.what_is_this_description, "field 'mCardDesc'");
        unlockModeCard.mWhatIsThisTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.what_is_this_title, "field 'mWhatIsThisTitle'");
        unlockModeCard.mImage = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.unlock_mode_image, "field 'mImage'");
        unlockModeCard.mSelectButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.select_mode_button, "field 'mSelectButton'");
        unlockModeCard.mCurrentUnlockModeText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.current_unlock_mode_text, "field 'mCurrentUnlockModeText'");
    }

    public static void reset(UnlockModeCard unlockModeCard) {
        unlockModeCard.mCardTitle = null;
        unlockModeCard.mBetaMode = null;
        unlockModeCard.mCardDesc = null;
        unlockModeCard.mWhatIsThisTitle = null;
        unlockModeCard.mImage = null;
        unlockModeCard.mSelectButton = null;
        unlockModeCard.mCurrentUnlockModeText = null;
    }
}
