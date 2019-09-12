package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.wjy.smartlock.C0073R;
import com.wjy.smartlock.MyApplication;

/* renamed from: hr.android.ble.smartlocck.widget.XListViewFooter */
public class XListViewFooter extends LinearLayout {
    public static final int STATE_LOADING = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    private View mContentView;
    private Context mContext;
    private TextView mHintView;
    private View mProgressBar;
    private int nMaxBottomMargin;
    private TextView tvLine;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        this.mHintView.setVisibility(4);
        this.mProgressBar.setVisibility(4);
        this.mHintView.setVisibility(4);
        if (state == 1) {
            this.mHintView.setVisibility(0);
            this.mHintView.setText("鏉惧紑杞藉叆鏇村");
        } else if (state == 2) {
            this.mProgressBar.setVisibility(0);
        } else {
            this.mHintView.setVisibility(0);
            this.mHintView.setText("鏌ョ湅鏇村");
        }
    }

    public void setBottomMargin(int height) {
        if (height >= 0) {
            if (height > this.nMaxBottomMargin) {
                height = this.nMaxBottomMargin;
            }
            LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
            lp.bottomMargin = height;
            this.mContentView.setLayoutParams(lp);
        }
    }

    public int getBottomMargin() {
        return ((LayoutParams) this.mContentView.getLayoutParams()).bottomMargin;
    }

    public void normal() {
        this.mHintView.setVisibility(0);
        this.mProgressBar.setVisibility(8);
    }

    public void loading() {
        this.mHintView.setVisibility(8);
        this.mProgressBar.setVisibility(0);
    }

    public void hide() {
        LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
        lp.height = 0;
        this.mContentView.setLayoutParams(lp);
    }

    public void show() {
        LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
        lp.height = -2;
        this.mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        this.mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(this.mContext).inflate(C0073R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(-1, -2));
        this.mContentView = moreView.findViewById(C0073R.C0075id.xlistview_footer_content);
        this.mProgressBar = moreView.findViewById(C0073R.C0075id.xlistview_footer_progressbar);
        this.mHintView = (TextView) moreView.findViewById(C0073R.C0075id.xlistview_footer_hint_textview);
        this.tvLine = (TextView) moreView.findViewById(C0073R.C0075id.xlistview_footer_line);
        this.nMaxBottomMargin = MyApplication.getInstance().dip2px(1.0f);
    }

    public void setLineVisibility(int visibility) {
        this.tvLine.setVisibility(visibility);
    }
}
