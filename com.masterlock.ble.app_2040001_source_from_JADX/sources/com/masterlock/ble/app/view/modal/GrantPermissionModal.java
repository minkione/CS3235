package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.LayerDrawable;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.PermissionUtil.PermissionType;

public class GrantPermissionModal extends CardView {
    @InjectView(2131296327)
    Button bTGrantPermission;
    @InjectView(2131296551)
    ImageView iVPermission;
    private boolean isPermissionDenied;
    @InjectView(2131296317)
    TextView mBody;
    @InjectView(2131296320)
    LinearLayout mBodyContainer;
    private PermissionType mPermissionType;
    @InjectView(2131296802)
    TextView mTitle;
    @InjectView(2131296833)
    TextView tVSkipForNow;

    public GrantPermissionModal(Context context) {
        this(context, null);
    }

    public GrantPermissionModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.grant_permission_modal_view, this);
        ButterKnife.inject((View) this);
        TextView textView = this.tVSkipForNow;
        textView.setPaintFlags(textView.getPaintFlags() | 8);
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle.setText(str);
    }

    public TextView getBody() {
        return this.mBody;
    }

    public void setBody(String str) {
        this.mBody.setText(str);
    }

    public void setMainButtonText(String str) {
        this.bTGrantPermission.setText(str);
    }

    public void setPermissionImage(int i) {
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), C1075R.C1076drawable.permission_modal_drawable);
        layerDrawable.setDrawableByLayerId(C1075R.C1077id.permission_image, ContextCompat.getDrawable(getContext(), i));
        Bitmap createBitmap = Bitmap.createBitmap(layerDrawable.getIntrinsicWidth(), layerDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        layerDrawable.draw(canvas);
        int width = createBitmap.getWidth();
        double height = (double) createBitmap.getHeight();
        Double.isNaN(height);
        Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, width, (int) (height * 0.75d), null, false);
        createBitmap.recycle();
        this.iVPermission.setImageBitmap(createBitmap2);
    }

    public LinearLayout getBodyContainer() {
        return this.mBodyContainer;
    }

    public void setGrantPermissionButtonClickListener(OnClickListener onClickListener) {
        this.bTGrantPermission.setOnClickListener(onClickListener);
    }

    public void setSkipForNowClickListener(OnClickListener onClickListener) {
        this.tVSkipForNow.setOnClickListener(onClickListener);
    }

    public void initializeUI(PermissionType permissionType, boolean z) {
        String str;
        int i;
        String str2;
        this.mPermissionType = permissionType;
        this.isPermissionDenied = z;
        String string = getContext().getString(z ? C1075R.string.permissions_go_settings_button : C1075R.string.permissions_grant_permission_button);
        switch (this.mPermissionType) {
            case CONTACTS:
                str = getContext().getString(C1075R.string.permissions_contacts_title);
                str2 = getContext().getString(C1075R.string.permissions_contacts_description);
                i = C1075R.C1076drawable.img_contacts_permission;
                break;
            case LOCATION:
                str = getContext().getString(C1075R.string.permissions_location_services_title);
                str2 = getContext().getString(C1075R.string.permissions_location_services_description);
                i = C1075R.C1076drawable.img_location_permission;
                break;
            case NOTIFICATIONS:
                str = getContext().getString(C1075R.string.permissions_notifications_title);
                str2 = getContext().getString(C1075R.string.permissions_notifications_description);
                i = C1075R.C1076drawable.img_notification_permission;
                break;
            default:
                throw new UnsupportedOperationException("Unknown permission");
        }
        setTitle(str);
        setBody(str2);
        setMainButtonText(string);
        setPermissionImage(i);
    }
}
