package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.api.entity.TermsOfService;
import com.masterlock.ble.app.C1075R;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public class TermsOfServiceDialog extends CardView {
    @InjectView(2131296816)
    WebView mTOSWebView;
    private final TermsOfService mTermsOfService;
    @InjectView(2131296631)
    Button negativeButton;
    @InjectView(2131296655)
    Button positiveButton;

    public TermsOfServiceDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTermsOfService = null;
        init();
    }

    public TermsOfServiceDialog(Context context, AttributeSet attributeSet, TermsOfService termsOfService) {
        super(context, attributeSet);
        this.mTermsOfService = termsOfService;
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.terms_of_service_dialog, this);
        ButterKnife.inject((View) this);
        this.mTOSWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return true;
            }
        });
        this.mTOSWebView.loadData(this.mTermsOfService.getValue(), "text/html", HttpRequest.CHARSET_UTF8);
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }

    public void setNegativeButtonClickListener(OnClickListener onClickListener) {
        this.negativeButton.setOnClickListener(onClickListener);
    }
}
