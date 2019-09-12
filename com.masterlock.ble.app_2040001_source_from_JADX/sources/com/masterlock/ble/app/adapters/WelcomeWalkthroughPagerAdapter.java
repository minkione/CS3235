package com.masterlock.ble.app.adapters;

import android.support.p000v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.masterlock.ble.app.C1075R;

public class WelcomeWalkthroughPagerAdapter extends PagerAdapter {
    private StateListener mListener;

    public interface StateListener {
        void onChildDiffersFromPosition(int i);
    }

    private int getResIdForPosition(int i) {
        switch (i) {
            case 0:
                return C1075R.layout.welcom_walkthrough_step0;
            case 1:
                return C1075R.layout.welcom_walkthrough_step1;
            case 2:
                return C1075R.layout.welcom_walkthrough_step2;
            case 3:
                return C1075R.layout.welcom_walkthrough_step3;
            case 4:
                return C1075R.layout.welcom_walkthrough_step4;
            default:
                return 0;
        }
    }

    public int getCount() {
        return 5;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public WelcomeWalkthroughPagerAdapter(StateListener stateListener) {
        this.mListener = stateListener;
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater");
        int resIdForPosition = getResIdForPosition(i);
        if (viewGroup.getChildCount() < i) {
            this.mListener.onChildDiffersFromPosition(i);
            for (int i2 = 0; i2 < i; i2++) {
                viewGroup.addView(layoutInflater.inflate(getResIdForPosition(i), null));
            }
        }
        View inflate = layoutInflater.inflate(resIdForPosition, null);
        viewGroup.addView(inflate, i);
        return inflate;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeViewAt(i);
    }

    public CharSequence getPageTitle(int i) {
        return Integer.toString(i);
    }
}
