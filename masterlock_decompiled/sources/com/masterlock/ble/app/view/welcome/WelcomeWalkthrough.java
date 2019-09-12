package com.masterlock.ble.app.view.welcome;

import android.content.Context;
import android.content.Intent;
import android.support.p000v4.view.PagerAdapter;
import android.support.p000v4.view.ViewPager;
import android.support.p000v4.view.ViewPager.OnPageChangeListener;
import android.support.p000v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.SignInActivity;
import com.masterlock.ble.app.activity.SignUpActivity;
import com.masterlock.ble.app.adapters.WelcomeWalkthroughPagerAdapter;
import com.masterlock.ble.app.adapters.WelcomeWalkthroughPagerAdapter.StateListener;
import com.masterlock.ble.app.presenter.welcome.WelcomeWalkthroughPresenter;
import com.masterlock.ble.app.util.IntentUtil;

public class WelcomeWalkthrough extends LinearLayout {
    public static final boolean HAS_SEEN_WALK_THROUGH = true;
    static final int NUM_PAGES = 5;
    LinearLayout circles;
    @InjectView(2131296760)
    Button createAccountButton;
    PagerAdapter mAdapter;
    @InjectView(2131296646)
    ViewPager mPager;
    WelcomeWalkthroughPresenter mWalkthroughPresenter;
    @InjectView(2131296758)
    Button singInButton;
    @InjectView(2131296762)
    Button skipWalkthroughButton;

    public abstract class BaseTransformer implements PageTransformer {
        /* access modifiers changed from: protected */
        public boolean hideOffscreenPages() {
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean isPagingEnabled() {
            return false;
        }

        /* access modifiers changed from: protected */
        public void onPostTransform(View view, float f) {
        }

        /* access modifiers changed from: protected */
        public abstract void onTransform(View view, float f);

        public BaseTransformer() {
        }

        public void transformPage(View view, float f) {
            onPreTransform(view, f);
            onTransform(view, f);
            onPostTransform(view, f);
        }

        /* access modifiers changed from: protected */
        public void onPreTransform(View view, float f) {
            float width = (float) view.getWidth();
            float f2 = 0.0f;
            view.setRotationX(0.0f);
            view.setRotationY(0.0f);
            view.setRotation(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            view.setPivotX(0.0f);
            view.setPivotY(0.0f);
            view.setTranslationY(0.0f);
            view.setTranslationX(isPagingEnabled() ? 0.0f : (-width) * f);
            if (hideOffscreenPages()) {
                if (f > -1.0f && f < 1.0f) {
                    f2 = 1.0f;
                }
                view.setAlpha(f2);
                return;
            }
            view.setAlpha(1.0f);
        }
    }

    public class ZoomOutTranformer extends BaseTransformer {
        public ZoomOutTranformer() {
            super();
        }

        /* access modifiers changed from: protected */
        public void onTransform(View view, float f) {
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            View view2 = view;
            float f9 = f;
            View findViewById = view2.findViewById(C1075R.C1077id.rl_welcome_epadlock_background);
            View findViewById2 = view2.findViewById(C1075R.C1077id.rl_welcome_ekeysafe_background);
            View findViewById3 = view2.findViewById(C1075R.C1077id.rl_welcome_epadlock);
            View findViewById4 = view2.findViewById(C1075R.C1077id.rl_welcome_ekeysafe);
            View findViewById5 = view2.findViewById(C1075R.C1077id.welcome_title);
            View findViewById6 = view2.findViewById(C1075R.C1077id.welcome_body);
            View findViewById7 = view2.findViewById(C1075R.C1077id.welcome_img_container);
            view2.findViewById(C1075R.C1077id.welcome_img);
            View findViewById8 = view2.findViewById(C1075R.C1077id.welcome_img_first);
            View findViewById9 = view2.findViewById(C1075R.C1077id.welcome_img_last);
            int width = view.getWidth();
            if (findViewById3 == null || findViewById4 == null) {
                f2 = 0.0f;
            } else {
                if (0.0f > f9 || f9 >= 1.0f) {
                    f7 = 0.0f;
                    f8 = -1.0f;
                } else {
                    float f10 = (float) width;
                    findViewById.setTranslationX(f10 * f9);
                    findViewById2.setTranslationX(f10 * (-f9));
                    WelcomeWalkthrough.this.createAccountButton.setAlpha(1.0f - Math.abs(f));
                    WelcomeWalkthrough.this.singInButton.setAlpha(1.0f - Math.abs(f));
                    f7 = 0.0f;
                    WelcomeWalkthrough.this.skipWalkthroughButton.setAlpha(Math.abs(f) + 0.0f);
                    f8 = -1.0f;
                }
                if (f8 >= f9 || f9 >= f7) {
                    f2 = 0.0f;
                } else {
                    float f11 = (float) width;
                    float f12 = (0.2f * f9) + f9;
                    findViewById.setTranslationX(f11 * f12);
                    findViewById2.setTranslationX((-f12) * f11);
                    findViewById3.setTranslationX(f11 * f9);
                    findViewById4.setTranslationX(f11 * (-f9));
                    WelcomeWalkthrough.this.createAccountButton.setAlpha(1.0f - Math.abs(f));
                    WelcomeWalkthrough.this.singInButton.setAlpha(1.0f - Math.abs(f));
                    f2 = 0.0f;
                    WelcomeWalkthrough.this.skipWalkthroughButton.setAlpha(Math.abs(f) + 0.0f);
                }
            }
            if (findViewById9 != null) {
                if (f2 > f9 || f9 >= 1.0f) {
                    f5 = 0.0f;
                    f6 = -1.0f;
                } else {
                    WelcomeWalkthrough.this.createAccountButton.setAlpha(1.0f - Math.abs(f));
                    WelcomeWalkthrough.this.singInButton.setAlpha(1.0f - Math.abs(f));
                    f5 = 0.0f;
                    WelcomeWalkthrough.this.skipWalkthroughButton.setAlpha(Math.abs(f) + 0.0f);
                    f6 = -1.0f;
                }
                if (f6 < f9 && f9 < f5) {
                    WelcomeWalkthrough.this.createAccountButton.setAlpha(1.0f - Math.abs(f));
                    WelcomeWalkthrough.this.singInButton.setAlpha(1.0f - Math.abs(f));
                    WelcomeWalkthrough.this.skipWalkthroughButton.setAlpha(Math.abs(f) + 0.0f);
                }
            }
            if (findViewById5 != null) {
                findViewById5.setAlpha(1.0f - Math.abs(f));
            }
            if (findViewById6 != null) {
                findViewById6.setAlpha(1.0f - Math.abs(f));
            }
            if (findViewById8 == null) {
                f3 = 0.0f;
            } else if (f9 > 0.0f) {
                if (f9 < 0.0f) {
                    f4 = f9 + 1.0f;
                } else {
                    f4 = Math.abs(1.0f - f9);
                }
                findViewById7.setScaleX(f4);
                findViewById7.setScaleY(f4);
                findViewById7.setPivotX(((float) findViewById7.getWidth()) * 0.5f);
                findViewById7.setPivotY(((float) findViewById7.getHeight()) * -5.0E-4f);
                f3 = 0.0f;
            } else {
                f3 = 0.0f;
            }
            if (f9 < f3 || f9 >= 1.0f) {
                view2.setAlpha((f9 < -1.0f || f9 > 1.0f) ? 0.0f : 1.0f - ((Math.abs(f) + 1.0f) - 1.0f));
            } else {
                view2.setAlpha(1.0f);
            }
        }
    }

    public WelcomeWalkthrough(Context context) {
        this(context, null);
    }

    public WelcomeWalkthrough(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mWalkthroughPresenter = new WelcomeWalkthroughPresenter(this);
            this.mWalkthroughPresenter.start();
            ButterKnife.inject((View) this);
            this.mAdapter = new WelcomeWalkthroughPagerAdapter(new StateListener() {
                public final void onChildDiffersFromPosition(int i) {
                    WelcomeWalkthrough.lambda$onAttachedToWindow$0(WelcomeWalkthrough.this, i);
                }
            });
            this.mPager.setAdapter(this.mAdapter);
            this.mPager.setPageTransformer(true, new ZoomOutTranformer());
            this.mPager.setOffscreenPageLimit(3);
            this.mPager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                }

                public void onPageSelected(int i) {
                    WelcomeWalkthrough.this.setIndicator(i);
                }
            });
            buildCircles();
        }
    }

    public static /* synthetic */ void lambda$onAttachedToWindow$0(WelcomeWalkthrough welcomeWalkthrough, int i) {
        welcomeWalkthrough.setIndicator(i);
        welcomeWalkthrough.createAccountButton.setAlpha(0.0f);
        welcomeWalkthrough.singInButton.setAlpha(0.0f);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mWalkthroughPresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296758})
    public void sigInWalkthrough() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        getContext().startActivity(intent);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296762, 2131296760})
    public void exitWalkthrough() {
        MasterLockSharedPreferences.getInstance().putAppHasShownWalkThrough(true);
        Intent intent = new Intent(getContext(), SignUpActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        getContext().startActivity(intent);
    }

    public void onBackPressed() {
        int currentItem = this.mPager.getCurrentItem();
        if (currentItem > 0) {
            this.mPager.setCurrentItem(currentItem - 1, true);
        } else {
            this.mWalkthroughPresenter.finishActivity();
        }
    }

    private void buildCircles() {
        this.circles = (LinearLayout) LinearLayout.class.cast(findViewById(C1075R.C1077id.circles));
        int i = (int) ((getResources().getDisplayMetrics().density * 5.0f) + 0.5f);
        for (int i2 = 0; i2 < 5; i2++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(C1075R.C1076drawable.ic_swipe_indicator_white_18dp);
            imageView.setLayoutParams(new LayoutParams(-2, -2));
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(i, 0, i, 0);
            this.circles.addView(imageView);
        }
        setIndicator(0);
    }

    /* access modifiers changed from: private */
    public void setIndicator(int i) {
        if (i < 5) {
            for (int i2 = 0; i2 < 5; i2++) {
                ImageView imageView = (ImageView) this.circles.getChildAt(i2);
                if (i2 == i) {
                    imageView.setColorFilter(getResources().getColor(C1075R.color.spruce));
                } else {
                    imageView.setColorFilter(getResources().getColor(C1075R.color.oak));
                }
            }
        }
    }
}
