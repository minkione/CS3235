package com.masterlock.ble.app.view.welcome;

import android.support.p000v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class WelcomeWalkthrough$$ViewInjector {
    public static void inject(Finder finder, final WelcomeWalkthrough welcomeWalkthrough, Object obj) {
        welcomeWalkthrough.mPager = (ViewPager) finder.findRequiredView(obj, C1075R.C1077id.pager, "field 'mPager'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.sign_in_button_walkthrough, "field 'singInButton' and method 'sigInWalkthrough'");
        welcomeWalkthrough.singInButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                welcomeWalkthrough.sigInWalkthrough();
            }
        });
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.skip_walkthrough, "field 'skipWalkthroughButton' and method 'exitWalkthrough'");
        welcomeWalkthrough.skipWalkthroughButton = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                welcomeWalkthrough.exitWalkthrough();
            }
        });
        View findRequiredView3 = finder.findRequiredView(obj, C1075R.C1077id.sign_up_button_walkthrough, "field 'createAccountButton' and method 'exitWalkthrough'");
        welcomeWalkthrough.createAccountButton = (Button) findRequiredView3;
        findRequiredView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                welcomeWalkthrough.exitWalkthrough();
            }
        });
    }

    public static void reset(WelcomeWalkthrough welcomeWalkthrough) {
        welcomeWalkthrough.mPager = null;
        welcomeWalkthrough.singInButton = null;
        welcomeWalkthrough.skipWalkthroughButton = null;
        welcomeWalkthrough.createAccountButton = null;
    }
}
