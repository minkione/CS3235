package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.guest.ChangeSecondaryCodesKeySafePresenter;
import com.masterlock.ble.app.screens.LockScreens.ChangeSecondaryCodesKeySafe;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;
import java.util.ArrayList;
import java.util.List;

public class ChangeSecondaryCodesKeySafeView extends LinearLayout implements IAuthenticatedView, RecyclerViewItemInteractionListener, HandlesBack, HandlesUp {
    @InjectView(2131296333)
    Button btnAddSecondaryCode;
    private ChangeSecondaryKeySafeAdapter mAdapter;
    ChangeSecondaryCodesKeySafePresenter mChangeSecondaryCodesKeySafePresenter;
    private Lock mLock;
    @InjectView(2131296683)
    RecyclerView recyclerView;
    private List<String> secondaryPasscodesRvDataSource = new ArrayList();
    @InjectView(2131296778)
    TextView stateText;

    public void showPasscodeExpiredToast() {
    }

    public ChangeSecondaryCodesKeySafeView(Context context) {
        super(context);
    }

    public ChangeSecondaryCodesKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
        if (!isInEditMode()) {
            this.mLock = ((ChangeSecondaryCodesKeySafe) AppFlow.getScreen(getContext())).mLock;
            this.mChangeSecondaryCodesKeySafePresenter = new ChangeSecondaryCodesKeySafePresenter(this.mLock, this);
            this.secondaryPasscodesRvDataSource = this.mLock.getAllSecondaryCodes();
            this.mAdapter = new ChangeSecondaryKeySafeAdapter(getContext(), this.secondaryPasscodesRvDataSource, this);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setAdapter(this.mAdapter);
            this.recyclerView.setNestedScrollingEnabled(false);
            toggleEmptySecondaryCodesListMessage();
            toggleAddSecondaryCodesButton();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296333})
    public void onClickContinueButton() {
        if (!isSecondaryCodeListEmpty() || !SecondaryCodesUtil.isOperationsListEmpty()) {
            this.mChangeSecondaryCodesKeySafePresenter.goToApplyChanges();
        } else {
            this.mChangeSecondaryCodesKeySafePresenter.goToUpdateSecondaryCode(SecondaryCodeIndex.SECONDARY_PASSCODE_1);
        }
    }

    public void onItemDelete(int i) {
        this.mChangeSecondaryCodesKeySafePresenter.showDeleteSecondaryCodeModal(new OnClickListener(i) {
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                ChangeSecondaryCodesKeySafeView.lambda$onItemDelete$0(ChangeSecondaryCodesKeySafeView.this, this.f$1, view);
            }
        });
    }

    public static /* synthetic */ void lambda$onItemDelete$0(ChangeSecondaryCodesKeySafeView changeSecondaryCodesKeySafeView, int i, View view) {
        String str = "";
        changeSecondaryCodesKeySafeView.secondaryPasscodesRvDataSource.set(i, str);
        changeSecondaryCodesKeySafeView.mLock.setSecondaryCodeAt(SecondaryCodeIndex.fromValue(i), str);
        changeSecondaryCodesKeySafeView.mAdapter.notifyDataSetChanged();
        changeSecondaryCodesKeySafeView.toggleEmptySecondaryCodesListMessage();
        changeSecondaryCodesKeySafeView.toggleAddSecondaryCodesButton();
    }

    public void onItemClick(int i) {
        this.mChangeSecondaryCodesKeySafePresenter.goToUpdateSecondaryCode(SecondaryCodeIndex.fromValue(i));
    }

    private boolean isSecondaryCodeListEmpty() {
        return SecondaryCodesUtil.isListEmpty(this.mLock);
    }

    private SpannableString createSpannableText() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(C1075R.string.secondary_codes_instructions_1));
        sb.append(getResources().getString(C1075R.string.secondary_codes_instructions_2));
        SpannableString spannableString = new SpannableString(sb.toString());
        Drawable drawable = getResources().getDrawable(C1075R.C1076drawable.ic_backbutton);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannableString.setSpan(new ImageSpan(drawable), getResources().getString(C1075R.string.secondary_codes_instructions_1).length() + 1, getResources().getString(C1075R.string.secondary_codes_instructions_1).length() + 2, 18);
        return spannableString;
    }

    private void toggleEmptySecondaryCodesListMessage() {
        if (isSecondaryCodeListEmpty() && SecondaryCodesUtil.isOperationsListEmpty()) {
            this.stateText.setText(C1075R.string.no_secondary_code_registered);
            this.recyclerView.setVisibility(8);
            return;
        }
        this.stateText.setText(createSpannableText());
        this.recyclerView.setVisibility(0);
    }

    private void toggleAddSecondaryCodesButton() {
        int i;
        Context context;
        boolean z = true;
        if (!isSecondaryCodeListEmpty() && !SecondaryCodesUtil.hasPendingOperations(this.mLock)) {
            z = false;
        }
        this.btnAddSecondaryCode.setEnabled(z);
        this.btnAddSecondaryCode.setText((!isSecondaryCodeListEmpty() || SecondaryCodesUtil.hasPendingOperations(this.mLock)) ? C1075R.string.btn_update_secondary_codes : C1075R.string.btn_add_secondary_codes);
        Button button = this.btnAddSecondaryCode;
        if (z) {
            context = getContext();
            i = C1075R.color.primary;
        } else {
            context = getContext();
            i = C1075R.color.dark_grey;
        }
        button.setTextColor(ContextCompat.getColor(context, i));
    }

    public boolean onBackPressed() {
        return this.mChangeSecondaryCodesKeySafePresenter.showUpdateSecondaryCodesWarningModal();
    }

    public boolean onUpPressed() {
        return this.mChangeSecondaryCodesKeySafePresenter.showUpdateSecondaryCodesWarningModal();
    }
}
