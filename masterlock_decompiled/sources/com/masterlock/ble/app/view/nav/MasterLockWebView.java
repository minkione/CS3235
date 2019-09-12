package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.NavPresenter;

public class MasterLockWebView extends WebView {
    NavPresenter mNavPresenter;

    public MasterLockWebView(Context context) {
        super(context);
    }

    public MasterLockWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MasterLockWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mNavPresenter = new NavPresenter(this);
            this.mNavPresenter.start();
            getSettings().setJavaScriptEnabled(true);
            setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                    Intent intent;
                    if (str.startsWith("http:") || str.startsWith("https:")) {
                        return false;
                    }
                    if (str.startsWith("tel:")) {
                        MasterLockWebView.this.getContext().startActivity(new Intent("android.intent.action.DIAL", Uri.parse(str)));
                        return true;
                    }
                    if (MailTo.isMailTo(str)) {
                        MailTo parse = MailTo.parse(str);
                        intent = new Intent("android.intent.action.SENDTO", Uri.parse(str));
                        intent.putExtra("android.intent.extra.EMAIL", new String[]{parse.getTo()});
                        if (intent.resolveActivity(MasterLockWebView.this.getContext().getPackageManager()) != null) {
                            MasterLockWebView.this.getContext().startActivity(intent);
                            return true;
                        }
                    } else {
                        intent = null;
                    }
                    if (intent == null || intent.resolveActivity(MasterLockWebView.this.getContext().getPackageManager()) == null) {
                        return super.shouldOverrideUrlLoading(webView, str);
                    }
                    MasterLockWebView.this.getContext().startActivity(intent);
                    return true;
                }

                public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                    super.onPageStarted(webView, str, bitmap);
                    MasterLockWebView.this.mNavPresenter.startProgress();
                }

                public void onPageFinished(WebView webView, String str) {
                    super.onPageFinished(webView, str);
                    MasterLockWebView.this.mNavPresenter.stopProgress();
                }

                public void onReceivedError(WebView webView, int i, String str, String str2) {
                    super.onReceivedError(webView, i, str, str2);
                    MasterLockWebView.this.mNavPresenter.stopProgress();
                    if (i != -2) {
                        switch (i) {
                            case -8:
                            case -7:
                            case -6:
                                break;
                            default:
                                Toast.makeText(MasterLockWebView.this.getContext(), str, 1).show();
                                return;
                        }
                    }
                    Toast.makeText(MasterLockWebView.this.getContext(), MasterLockWebView.this.getResources().getString(C1075R.string.no_connection_alert_body), 1).show();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mNavPresenter.finish();
    }
}
