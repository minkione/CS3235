package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.wjy.smartlock.C0073R;
import com.wjy.smartlock.MyApplication;

/* renamed from: hr.android.ble.smartlocck.widget.XListViewHeader */
public class XListViewHeader extends LinearLayout {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = 180;
    private ImageView mArrowImageView;
    private LinearLayout mContainer;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;
    private Animation mRotateDownAnim;
    private Animation mRotateUpAnim;
    private int mState = 0;
    private int nMaxHeight;
    private TextView tvLine;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutParams lp = new LayoutParams(-1, 0);
        this.mContainer = (LinearLayout) LayoutInflater.from(context).inflate(C0073R.layout.xlistview_header, null);
        addView(this.mContainer, lp);
        setGravity(80);
        this.mArrowImageView = (ImageView) findViewById(C0073R.C0075id.xlistview_header_arrow);
        this.mHintTextView = (TextView) findViewById(C0073R.C0075id.xlistview_header_hint_textview);
        this.mProgressBar = (ProgressBar) findViewById(C0073R.C0075id.xlistview_header_progressbar);
        this.tvLine = (TextView) findViewById(C0073R.C0075id.xlistview_header_line);
        this.mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateUpAnim.setDuration(180);
        this.mRotateUpAnim.setFillAfter(true);
        this.mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateDownAnim.setDuration(180);
        this.mRotateDownAnim.setFillAfter(true);
        this.nMaxHeight = MyApplication.getInstance().dip2px(51.0f);
    }

    public void setState(int state) {
        if (state != this.mState) {
            if (state == 2) {
                this.mArrowImageView.clearAnimation();
                this.mArrowImageView.setVisibility(4);
                this.mProgressBar.setVisibility(0);
            } else {
                this.mArrowImageView.setVisibility(0);
                this.mProgressBar.setVisibility(4);
            }
            switch (state) {
                case 0:
                    if (this.mState == 1) {
                        this.mArrowImageView.startAnimation(this.mRotateDownAnim);
                    }
                    if (this.mState == 2) {
                        this.mArrowImageView.clearAnimation();
                    }
                    this.mHintTextView.setText("下拉刷新");
                    break;
                case 1:
                    if (this.mState != 1) {
                        this.mArrowImageView.clearAnimation();
                        this.mArrowImageView.startAnimation(this.mRotateUpAnim);
                        this.mHintTextView.setText("松开刷新数据");
                        break;
                    }
                    break;
                case 2:
                    this.mHintTextView.setText("正在加载");
                    break;
            }
            this.mState = state;
        }
    }

    public int getState() {
        return this.mState;
    }

    public void setVisiableHeight(int height) {
        if (height < 0) {
            height = 0;
        } else if (height > this.nMaxHeight) {
            height = this.nMaxHeight;
        }
        LayoutParams lp = (LayoutParams) this.mContainer.getLayoutParams();
        lp.height = height;
        this.mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return this.mContainer.getHeight();
    }

    public void setLineVisibility(int visibility) {
        this.tvLine.setVisibility(visibility);
    }
}
