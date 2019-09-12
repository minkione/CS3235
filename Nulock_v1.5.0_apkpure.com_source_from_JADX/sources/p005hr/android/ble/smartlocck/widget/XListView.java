package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.wjy.smartlock.C0073R;

/* renamed from: hr.android.ble.smartlocck.widget.XListView */
public class XListView extends ListView implements OnScrollListener {
    private static final float OFFSET_RADIO = 1.8f;
    private static final int PULL_LOAD_MORE_DELTA = 1;
    private static final int SCROLLBACK_FOOTER = 1;
    private static final int SCROLLBACK_HEADER = 0;
    private static final int SCROLL_DURATION = 400;
    private boolean bRefresh = false;
    private boolean mEnablePullLoad;
    private boolean mEnablePullRefresh = true;
    private XListViewFooter mFooterView;
    private TextView mHeaderTimeView;
    private TextView mHeaderTimeViewTag;
    private XListViewHeader mHeaderView;
    /* access modifiers changed from: private */
    public RelativeLayout mHeaderViewContent;
    /* access modifiers changed from: private */
    public int mHeaderViewHeight;
    private boolean mIsFooterReady = false;
    private float mLastY = -1.0f;
    private IXListViewListener mListViewListener;
    private boolean mPullLoading;
    private boolean mPullRefreshing = false;
    private int mScrollBack;
    private OnScrollListener mScrollListener;
    private Scroller mScroller;
    private int mTotalItemCount;

    /* renamed from: hr.android.ble.smartlocck.widget.XListView$IXListViewListener */
    public interface IXListViewListener {
        void onLoadMore();

        void onRefresh();
    }

    /* renamed from: hr.android.ble.smartlocck.widget.XListView$OnXScrollListener */
    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        this.mHeaderView = new XListViewHeader(context);
        this.mHeaderViewContent = (RelativeLayout) this.mHeaderView.findViewById(C0073R.C0075id.xlistview_header_content);
        this.mHeaderTimeView = (TextView) this.mHeaderView.findViewById(C0073R.C0075id.xlistview_header_time);
        this.mHeaderTimeViewTag = (TextView) this.mHeaderView.findViewById(C0073R.C0075id.xlistview_header_time_tag);
        addHeaderView(this.mHeaderView);
        this.mFooterView = new XListViewFooter(context);
        this.mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                XListView.this.mHeaderViewHeight = XListView.this.mHeaderViewContent.getHeight();
                XListView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void setAdapter(ListAdapter adapter) {
        if (!this.mIsFooterReady) {
            this.mIsFooterReady = true;
            addFooterView(this.mFooterView);
        }
        super.setAdapter(adapter);
    }

    public void setPullRefreshEnable(boolean enable) {
        this.mEnablePullRefresh = enable;
        if (!this.mEnablePullRefresh) {
            this.mHeaderViewContent.setVisibility(4);
        } else {
            this.mHeaderViewContent.setVisibility(0);
        }
    }

    public void setPullLoadEnable(boolean enable) {
        this.mEnablePullLoad = enable;
        if (!this.mEnablePullLoad) {
            this.mFooterView.hide();
            this.mFooterView.setOnClickListener(null);
            return;
        }
        this.mPullLoading = false;
        this.mFooterView.show();
        this.mFooterView.setState(0);
        this.mFooterView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                XListView.this.startLoadMore();
            }
        });
    }

    public void stopRefresh() {
        if (this.mPullRefreshing) {
            this.mPullRefreshing = false;
            resetHeaderHeight();
            this.bRefresh = false;
        }
    }

    public void setHeadLine(int visibility) {
        if (this.mHeaderView != null) {
            this.mHeaderView.setLineVisibility(visibility);
        }
    }

    public void setFootLine(int visibility) {
        if (this.mFooterView != null) {
            this.mFooterView.setLineVisibility(visibility);
        }
    }

    public void closeRefreshTime(boolean isClose) {
        if (isClose) {
            this.mHeaderTimeView.setVisibility(8);
            this.mHeaderTimeViewTag.setVisibility(8);
            return;
        }
        this.mHeaderTimeView.setVisibility(0);
        this.mHeaderTimeViewTag.setVisibility(0);
    }

    public void stopLoadMore() {
        if (this.mPullLoading) {
            this.mPullLoading = false;
            this.mFooterView.setState(0);
        }
    }

    public void setRefreshTime(String time) {
        this.mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (this.mScrollListener instanceof OnXScrollListener) {
            ((OnXScrollListener) this.mScrollListener).onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        this.mHeaderView.setVisiableHeight(((int) delta) + this.mHeaderView.getVisiableHeight());
        if (this.mEnablePullRefresh && !this.mPullRefreshing) {
            if (this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                this.mHeaderView.setState(1);
            } else {
                this.mHeaderView.setState(0);
            }
        }
        setSelection(0);
    }

    private void resetHeaderHeight() {
        int height = this.mHeaderView.getVisiableHeight();
        if (height != 0) {
            if (!this.mPullRefreshing || height > this.mHeaderViewHeight) {
                int finalHeight = 0;
                if (this.mPullRefreshing && height > this.mHeaderViewHeight) {
                    finalHeight = this.mHeaderViewHeight;
                }
                this.mScrollBack = 0;
                this.mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
                invalidate();
            }
        }
    }

    private void updateFooterHeight(float delta) {
        int height = this.mFooterView.getBottomMargin() + ((int) delta);
        if (this.mEnablePullLoad && !this.mPullLoading) {
            if (height > 1) {
                this.mFooterView.setState(1);
            } else {
                this.mFooterView.setState(0);
            }
        }
        this.mFooterView.setBottomMargin(height);
    }

    private void resetFooterHeight() {
        int bottomMargin = this.mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            this.mScrollBack = 1;
            this.mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    public void startLoadMore() {
        this.mPullLoading = true;
        this.mFooterView.setState(2);
        if (this.mListViewListener != null) {
            this.mListViewListener.onLoadMore();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mLastY == -1.0f) {
            this.mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case 0:
                this.mLastY = ev.getRawY();
                break;
            case 2:
                float deltaY = ev.getRawY() - this.mLastY;
                this.mLastY = ev.getRawY();
                if (getFirstVisiblePosition() != 0 || (this.mHeaderView.getVisiableHeight() <= 0 && deltaY <= 0.0f)) {
                    if (getLastVisiblePosition() == this.mTotalItemCount - 1 && (this.mFooterView.getBottomMargin() > 0 || deltaY < 0.0f)) {
                        updateFooterHeight((-deltaY) / OFFSET_RADIO);
                        break;
                    }
                } else {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                    break;
                }
                break;
            default:
                this.mLastY = -1.0f;
                if (getFirstVisiblePosition() != 0) {
                    if (getLastVisiblePosition() == this.mTotalItemCount - 1) {
                        if (this.mEnablePullLoad && this.mFooterView.getBottomMargin() > 1) {
                            startLoadMore();
                        }
                        resetFooterHeight();
                        break;
                    }
                } else {
                    if (this.mEnablePullRefresh && this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                        this.mPullRefreshing = true;
                        this.mHeaderView.setState(2);
                        if (this.mListViewListener != null && !this.bRefresh) {
                            this.bRefresh = true;
                            this.mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                    break;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (this.mScrollBack == 0) {
                this.mHeaderView.setVisiableHeight(this.mScroller.getCurrY());
            } else {
                this.mFooterView.setBottomMargin(this.mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mScrollListener = l;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mTotalItemCount = totalItemCount;
        if (this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        this.mListViewListener = l;
    }
}
