package com.masterlock.ble.app.view.signup;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.presenter.signup.TermsOfServicePresenter;
import com.masterlock.ble.app.screens.SignUpScreens.TermsOfService;
import com.masterlock.ble.app.view.FlowTransitionCallback;
import com.square.flow.appflow.AppFlow;
import flow.Flow.Direction;

public class TermsOfServiceView extends RelativeLayout implements FlowTransitionCallback {
    @InjectView(2131296361)
    LinearLayout buttonBar;
    private TermsOfServicePresenter termsOfServicePresenter;
    @InjectView(2131296793)
    TextView termsOfServiceTextView;

    public TermsOfServiceView(Context context) {
        this(context, null);
    }

    public TermsOfServiceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.termsOfServicePresenter = new TermsOfServicePresenter(this);
            if (!((TermsOfService) AppFlow.getScreen(getContext())).mShouldShowOKButton) {
                this.buttonBar.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.termsOfServicePresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296792})
    public void dismissTOS() {
        this.termsOfServicePresenter.dismiss();
    }

    public void updateTermsOfService(String str) {
        this.termsOfServiceTextView.setText(Html.fromHtml(str));
        this.termsOfServiceTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void transitionStarted(Direction direction) {
        if (direction == Direction.FORWARD) {
            if (this.termsOfServicePresenter == null) {
                this.termsOfServicePresenter = new TermsOfServicePresenter(this);
            }
            this.termsOfServicePresenter.start();
        }
    }

    public void transitionComplete(Direction direction) {
        if (direction == Direction.FORWARD) {
            if (this.termsOfServicePresenter == null) {
                this.termsOfServicePresenter = new TermsOfServicePresenter(this);
            }
            this.termsOfServicePresenter.refresh();
        }
    }
}
