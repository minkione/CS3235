package com.masterlock.ble.app.adapters;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.ShowMode;
import com.daimajia.swipe.SwipeLayout.Status;
import com.daimajia.swipe.SwipeLayout.SwipeListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.adapters.LockAdapter.LockViewHolder;
import com.masterlock.ble.app.bus.ManageLockEvent;
import com.masterlock.ble.app.gamma.TopGravityDrawable;
import com.masterlock.ble.app.screens.LockScreens.DialSpeedDetails;
import com.masterlock.ble.app.screens.LockScreens.GenericLock;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.LockScreens.Steps;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.LockStatusResourceManager;
import com.masterlock.ble.app.util.LockUpdateUtil;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ScheduleType;
import com.masterlock.core.comparator.LockComparator;
import com.masterlock.core.comparator.LockComparator.SortId;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

public class LockAdapter extends ArrayAdapter<Lock> {
    private static final String TAG = "LockAdapter";
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;
    private List<Lock> items;
    private final SparseArray<Animator> mAnimators;
    @Inject
    Bus mBus;
    /* access modifiers changed from: private */
    public final SparseArray<LockCountDownTimer> mTimers;
    /* access modifiers changed from: private */
    public Resources resources;

    public class HeaderViewHolder {
        @InjectView(2131296861)
        TextView header;
        public String headerTitle;
        public int position;

        public HeaderViewHolder(View view, String str, int i) {
            ButterKnife.inject((Object) this, view);
            this.headerTitle = str;
            this.position = i;
        }
    }

    public class LockCountDownTimer extends CountDownTimer {
        LockViewHolder holder;
        Lock lock;
        int secondsLeft;

        public LockCountDownTimer(LockViewHolder lockViewHolder) {
            super(((long) (lockViewHolder.lock.getRelockTimeInSeconds() * 1000)) - (System.currentTimeMillis() - lockViewHolder.lock.getLastUnlocked()), 250);
            this.holder = lockViewHolder;
            this.lock = lockViewHolder.lock;
        }

        public void setView(LockViewHolder lockViewHolder) {
            this.holder = lockViewHolder;
            updateTime();
        }

        public void remove() {
            this.holder = null;
            this.lock = null;
            cancel();
        }

        public void onTick(long j) {
            float f = ((float) j) / 1000.0f;
            if (Math.round(f) != this.secondsLeft) {
                this.secondsLeft = Math.round(f);
                updateTime();
            }
        }

        public void onFinish() {
            LockAdapter.this.mTimers.remove(this.lock.hashCode());
            Lock lock2 = this.lock;
            if (lock2 != null) {
                lock2.setLockStatus(LockStatus.UNREACHABLE);
            }
            LockAdapter.this.notifyDataSetChanged();
        }

        private void updateTime() {
            LockViewHolder lockViewHolder = this.holder;
            if (lockViewHolder != null) {
                lockViewHolder.timerText.setText(LockAdapter.this.resources.getString(C1075R.string.relock_time, new Object[]{Integer.valueOf(this.secondsLeft)}));
            }
        }
    }

    public class LockViewHolder {
        @InjectView(2131296869)
        public TextView activity;
        @InjectView(2131296313)
        public ImageView batteryIndicator;
        @InjectView(2131296848)
        public TextView batteryText;
        @InjectView(2131296528)
        public ImageView check;
        @InjectView(2131296595)
        public View container;
        @InjectView(2131296854)
        public TextView coowner;
        @InjectView(2131296856)
        public TextView deviceId;
        @InjectView(2131296532)
        public ImageView image;
        public SwipeListener listener;
        public Lock lock;
        @InjectView(2131296871)
        public TextView name;
        @InjectView(2131296783)
        public SwipeLayout swipe;
        @InjectView(2131296533)
        public LinearLayout swipeImageContainer;
        @InjectView(2131296534)
        public ImageView swipeLarge;
        @InjectView(2131296535)
        public ImageView swipeMedium;
        @InjectView(2131296536)
        public ImageView swipeSmall;
        @InjectView(2131296889)
        public TextView timerText;

        public LockViewHolder(View view, Lock lock2) {
            ButterKnife.inject((Object) this, view);
            this.lock = lock2;
            if (VERSION.SDK_INT >= 21) {
                this.container.setOnTouchListener(new OnTouchListener() {
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        return LockViewHolder.lambda$new$0(LockViewHolder.this, view, motionEvent);
                    }
                });
            }
        }

        public static /* synthetic */ boolean lambda$new$0(LockViewHolder lockViewHolder, View view, MotionEvent motionEvent) {
            Drawable background = lockViewHolder.container.getBackground();
            if (background != null && (background instanceof RippleDrawable)) {
                ((RippleDrawable) background).setHotspot(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }

        @OnClick({2131296496})
        public void click(View view) {
            if (this.swipe.getOpenStatus() != Status.Close) {
                this.swipe.requestDisallowInterceptTouchEvent(true);
                return;
            }
            if (this.lock.isDialSpeedLock() || this.lock.isBiometricPadLock()) {
                LockAdapter.this.mBus.post(new ManageLockEvent(new DialSpeedDetails(this.lock)));
            } else if (this.lock.isMechanicalLock()) {
                LockAdapter.this.mBus.post(new ManageLockEvent(new GenericLock(this.lock)));
            } else if (LockUpdateUtil.getInstance().getUpdateAvailableForLock(this.lock.getLockId())) {
                AppFlow.get(LockAdapter.this.getContext()).replaceTo(new Steps(this.lock));
            } else {
                AppFlow.get(LockAdapter.this.getContext()).goTo(new LockLanding(this.lock));
            }
            LockAdapter.this.mBus.unregister(this);
        }
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public LockAdapter(Context context) {
        this(context, new ArrayList());
    }

    public LockAdapter(Context context, ArrayList<Lock> arrayList) {
        super(context, 0, 0, arrayList);
        this.mAnimators = new SparseArray<>();
        this.mTimers = new SparseArray<>();
        this.items = arrayList;
        this.resources = context.getResources();
        MasterLockApp.get().inject(this);
        this.mBus.register(this);
    }

    public void addAll(Collection<? extends Lock> collection) {
        ArrayList arrayList = new ArrayList(collection);
        Collections.sort(arrayList, LockComparator.getComparator(LockComparator.SECTION_SORT, getComparator()));
        this.items = arrayList;
        super.addAll(arrayList);
    }

    public void addAll(Lock... lockArr) {
        Arrays.sort(lockArr, LockComparator.getComparator(LockComparator.SECTION_SORT, getComparator()));
        super.addAll(lockArr);
    }

    public LockComparator getComparator() {
        String sortBy = MasterLockSharedPreferences.getInstance().getSortBy();
        if (sortBy.equals(SortId.OWNER_FIRST_SORT.getSortId())) {
            return LockComparator.SECTION_SORT;
        }
        if (sortBy.equals(SortId.LOCK_ID_SORT.getSortId())) {
            return LockComparator.LOCK_ID_SORT;
        }
        if (sortBy.equals(SortId.NAME_SORT.getSortId())) {
            return LockComparator.NAME_SORT;
        }
        if (sortBy.equals(SortId.USER_TYPE_SORT.getSortId())) {
            return LockComparator.USER_TYPE_SORT;
        }
        if (sortBy.equals(SortId.LOCK_NAME_ASC_SORT.getSortId())) {
            return LockComparator.LOCK_NAME_ASC_SORT;
        }
        if (sortBy.equals(SortId.LOCK_NAME_DES_SORT.getSortId())) {
            return LockComparator.LOCK_NAME_DES_SORT;
        }
        if (sortBy.equals(SortId.PRODUCT_TYPE_SORT.getSortId())) {
            return LockComparator.PRODUCT_TYPE_SORT;
        }
        if (sortBy.equals(SortId.RECENTLY_ACCESSED_SORT.getSortId())) {
            return LockComparator.RECENTLY_ACCESSED_SORT;
        }
        return LockComparator.SECTION_SORT;
    }

    public Lock getItem(int i) {
        if (getItemViewType(i) == 1) {
            return null;
        }
        return (Lock) this.items.get(getListPositionForAdapterPosition(i));
    }

    public int getItemViewType(int i) {
        if (isOnlyOwnerLocks()) {
            return 0;
        }
        return (i == 0 || i == getGuestSectionStart() || i == getOtherSectionStart()) ? 1 : 0;
    }

    public int getGuestSectionStart() {
        for (int i = 0; i < this.items.size(); i++) {
            Lock lock = (Lock) this.items.get(i);
            if (lock.getAccessType() == AccessType.GUEST || lock.getAccessType() == AccessType.CO_OWNER) {
                return i + (hasOwnerHeader() ? 1 : 0);
            }
        }
        int i2 = 1;
        if (this.items.size() > 0) {
            int size = this.items.size();
            if (hasOwnerHeader() && hasOtherHeader()) {
                i2 = 2;
            }
            i2 += size;
        }
        return i2;
    }

    public int getOtherSectionStart() {
        int i;
        int i2 = 0;
        while (true) {
            i = 1;
            if (i2 < this.items.size()) {
                Lock lock = (Lock) this.items.get(i2);
                if (!lock.isMechanicalLock() && !lock.isBiometricPadLock() && !lock.isDialSpeedLock()) {
                    i2++;
                }
            } else {
                if (this.items.size() > 0) {
                    i = this.items.size() + 2;
                }
                return i;
            }
        }
        if (hasGuestCoownerHeader()) {
            if (hasOwnerHeader()) {
                i = 2;
            }
        } else if (!hasOwnerHeader()) {
            i = 0;
        }
        return i2 + i;
    }

    public int getCount() {
        if (isOnlyOwnerLocks()) {
            return this.items.size();
        }
        int i = 0;
        if (hasOwnerHeader()) {
            i = 1;
        }
        if (hasGuestCoownerHeader()) {
            i++;
        }
        if (hasOtherHeader()) {
            i++;
        }
        if (i > 0) {
            return this.items.size() + i;
        }
        return this.items.size();
    }

    public boolean isEnabled(int i) {
        return getItemViewType(i) == 0;
    }

    public boolean isOnlyOwnerLocks() {
        return hasOwnerHeader() && !hasGuestCoownerHeader() && !hasOtherHeader();
    }

    public boolean hasOwnerHeader() {
        for (Lock accessType : this.items) {
            if (accessType.getAccessType() == AccessType.OWNER) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOtherHeader() {
        for (Lock lock : this.items) {
            if (lock.isDialSpeedLock() || lock.isBiometricPadLock()) {
                return true;
            }
            if (lock.isMechanicalLock()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGuestCoownerHeader() {
        for (Lock lock : this.items) {
            if (lock.getAccessType() != AccessType.GUEST) {
                if (lock.getAccessType() == AccessType.CO_OWNER) {
                }
            }
            return true;
        }
        return false;
    }

    private View newView(Context context, ViewGroup viewGroup, Lock lock) {
        View inflate = LayoutInflater.from(context).inflate(C1075R.layout.lock_list_item, viewGroup, false);
        inflate.setTag(new LockViewHolder(inflate, lock));
        return inflate;
    }

    private View newHeaderView(Context context, ViewGroup viewGroup, String str, int i) {
        View inflate = LayoutInflater.from(context).inflate(C1075R.layout.list_header, viewGroup, false);
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(inflate, str, i);
        headerViewHolder.header.setText(str);
        inflate.setTag(headerViewHolder);
        return inflate;
    }

    private int getListPositionForAdapterPosition(int i) {
        if (isOnlyOwnerLocks()) {
            return i;
        }
        if (i == 0) {
            return -1;
        }
        int guestSectionStart = getGuestSectionStart();
        if (i == guestSectionStart || i == getOtherSectionStart()) {
            return -1;
        }
        int i2 = 2;
        if (i > guestSectionStart && i < getOtherSectionStart()) {
            if (!hasOwnerHeader()) {
                i2 = 1;
            }
            return i - i2;
        } else if (i <= getOtherSectionStart()) {
            return i - 1;
        } else {
            if (hasGuestCoownerHeader()) {
                if (hasOwnerHeader()) {
                    i2 = 3;
                }
            } else if (!hasOwnerHeader()) {
                i2 = 1;
            }
            return i - i2;
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        String str;
        String str2;
        if (getItemViewType(i) == 1) {
            if (i == 0 && hasOwnerHeader()) {
                str2 = this.resources.getString(C1075R.string.your_locks);
            } else if (i == getGuestSectionStart()) {
                str2 = this.resources.getString(C1075R.string.guest_header);
            } else {
                str2 = this.resources.getString(C1075R.string.other_header);
            }
            if (view == null) {
                view = newHeaderView(viewGroup.getContext(), viewGroup, str2, i);
            }
            ((HeaderViewHolder) view.getTag()).header.setText(str2);
            return view;
        }
        Lock lock = (Lock) this.items.get(getListPositionForAdapterPosition(i));
        if (view == null) {
            view = newView(viewGroup.getContext(), viewGroup, lock);
        }
        LockViewHolder lockViewHolder = (LockViewHolder) view.getTag();
        cancelExistingAnimation(lockViewHolder);
        cancelTimer(lockViewHolder);
        lockViewHolder.swipe.setSwipeEnabled(false);
        if (lockViewHolder.listener != null) {
            lockViewHolder.swipe.removeSwipeListener(lockViewHolder.listener);
        }
        lockViewHolder.lock = lock;
        lockViewHolder.name.setText(lock.getName());
        lockViewHolder.coowner.setVisibility(lock.getAccessType() == AccessType.CO_OWNER ? 0 : 8);
        TextView textView = lockViewHolder.deviceId;
        if (lock.isMechanicalLock()) {
            str = lock.getModelName();
        } else if (lock.isDialSpeedLock()) {
            StringBuilder sb = new StringBuilder();
            sb.append(lock.getModelName());
            sb.append(" ");
            sb.append(lock.getModelNumber());
            str = sb.toString();
        } else {
            str = String.format(lock.getKmsDeviceKey().getDeviceId(), new Object[0]);
        }
        textView.setText(str);
        if (lock.isLockerMode()) {
            lockViewHolder.image.setImageResource(LockStatusResourceManager.getResIdForLockStatus(lock, LockStatus.NO_COMMUNICATION));
            showActivityText(this.resources.getString(lock.isPadLock() ? C1075R.string.locker_mode_enabled_alert : C1075R.string.lock_communications_disabled_alert), lockViewHolder);
            showBattery(lock.getRemainingBatteryPercentage(), lockViewHolder);
        } else if (lock.getAccessType() != AccessType.GUEST || AccessWindowUtil.isWithinAccessWindow(lock)) {
            lockViewHolder.image.setImageResource(LockStatusResourceManager.getResIdForLockStatus(lock));
            switch (lock.getLockStatus()) {
                case UNLOCKING:
                    showActivityText(this.resources.getString(C1075R.string.unlocking), lockViewHolder);
                    showBattery(lock.getRemainingBatteryPercentage(), lockViewHolder);
                    break;
                case UNLOCKED:
                    showActivityText(this.resources.getString(C1075R.string.ready_to_be_opened), lockViewHolder);
                    showTimer(lockViewHolder);
                    break;
                case LOCKED:
                    setupRow(lock, lockViewHolder);
                    break;
                case LOCK_FOUND:
                    if (lock.getLockMode() != LockMode.PROXIMITYSWIPE) {
                        setupRow(lock, lockViewHolder);
                        break;
                    } else {
                        showActivityText(this.resources.getString(C1075R.string.detected), lockViewHolder);
                        showSwipe(lockViewHolder);
                        break;
                    }
                case OPENED:
                    showActivityText(this.resources.getString(C1075R.string.opened_state_body), lockViewHolder);
                    showBattery(lock.getRemainingBatteryPercentage(), lockViewHolder);
                    break;
                case UPDATE_MODE:
                    showActivityText(this.resources.getString(C1075R.string.firmware_update_state_body), lockViewHolder);
                    showBattery(lock.getRemainingBatteryPercentage(), lockViewHolder);
                    break;
                default:
                    setupRow(lock, lockViewHolder);
                    break;
            }
        } else {
            setupRow(lock, lockViewHolder);
        }
        if (LockUpdateUtil.getInstance().getUpdateAvailableForLock(lock.getLockId())) {
            TextView textView2 = lockViewHolder.activity;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(lockViewHolder.activity.getText());
            sb2.append(", ");
            sb2.append(this.resources.getString(C1075R.string.firmware_update_list_activity));
            textView2.setText(sb2.toString());
        }
        return view;
    }

    private void updateTextViewBoundsForLargeText(LockViewHolder lockViewHolder, boolean z) {
        boolean z2 = !z;
        if (z) {
            lockViewHolder.activity.setSingleLine(z2);
            LayoutParams layoutParams = new LayoutParams(-2, -1);
            layoutParams.setMargins(0, 0, 0, (int) getContext().getResources().getDimension(C1075R.dimen.space_m0));
            lockViewHolder.activity.setLayoutParams(layoutParams);
        }
    }

    private void setupRow(Lock lock, LockViewHolder lockViewHolder) {
        String str;
        String str2;
        updateTextViewBoundsForLargeText(lockViewHolder, false);
        if (lock.getAccessType() != AccessType.GUEST) {
            str = parseLastLockRecord(lock);
        } else if (lock.getKmsDeviceKey() == null) {
            str = "";
        } else {
            switch (lock.getPermissions().getScheduleType()) {
                case TWENTY_FOUR_SEVEN:
                    str2 = this.resources.getString(C1075R.string.unlimited);
                    break;
                case SEVEN_AM_TO_SEVEN_PM:
                    str2 = this.resources.getString(C1075R.string.day);
                    break;
                case SEVEN_PM_TO_SEVEN_AM:
                    str2 = this.resources.getString(C1075R.string.night);
                    break;
                default:
                    str2 = "";
                    break;
            }
            lockViewHolder.image.setImageResource(LockStatusResourceManager.getResIdForLockStatus(lock));
            if (lock.getPermissions().getGuestInterface() == GuestInterface.ADVANCED) {
                if (!AccessWindowUtil.hasStarted(lock)) {
                    String print = DateTimeFormat.forPattern(getContext().getString(C1075R.string.regional_full_date_format)).print((ReadableInstant) new DateTime((Object) lock.getPermissions().getStartAtDate()));
                    StringBuilder sb = new StringBuilder();
                    sb.append(getContext().getString(C1075R.string.guest_detail_access_start));
                    sb.append(print);
                    str = sb.toString();
                    lockViewHolder.image.setImageResource(C1075R.C1076drawable.guest_no_access_today);
                } else if (AccessWindowUtil.hasExpired(lock)) {
                    Log.d(getClass().getSimpleName(), "setupRow: Expired invitation, awaiting implementation");
                } else if (!AccessWindowUtil.isAllowedToday(lock)) {
                    lockViewHolder.image.setImageResource(C1075R.C1076drawable.guest_no_access_today);
                    str = String.format(this.resources.getString(C1075R.string.guest_access_on), new Object[]{str2, lock.getPermissions().getSelectedDaysFormattedFullName()});
                    updateTextViewBoundsForLargeText(lockViewHolder, true);
                } else if (!AccessWindowUtil.isInsideSchedule(lock)) {
                    if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM) {
                        lockViewHolder.image.setImageResource(C1075R.C1076drawable.ic_out_of_schedule_day_list_gray);
                    } else if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_PM_TO_SEVEN_AM) {
                        lockViewHolder.image.setImageResource(C1075R.C1076drawable.ic_out_of_schedule_night_list_gray);
                    }
                }
            } else if (!AccessWindowUtil.isInsideSchedule(lock)) {
                if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM) {
                    lockViewHolder.image.setImageResource(C1075R.C1076drawable.ic_out_of_schedule_day_list_gray);
                } else if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_PM_TO_SEVEN_AM) {
                    lockViewHolder.image.setImageResource(C1075R.C1076drawable.ic_out_of_schedule_night_list_gray);
                }
            }
            str = str2;
        }
        showActivityText(str, lockViewHolder);
        showBattery((lock.isMechanicalLock() || lock.isDialSpeedLock() || lock.isBiometricPadLock()) ? 100 : lock.getRemainingBatteryPercentage(), lockViewHolder);
    }

    private String parseLastLockRecord(Lock lock) {
        String str = "";
        if (lock == null || lock.getLogs() == null || lock.getLogs().size() <= 0) {
            return str;
        }
        KmsLogEntry kmsLogEntry = (KmsLogEntry) lock.getLogs().get(0);
        StringBuilder sb = new StringBuilder(kmsLogEntry.getMessage());
        sb.append(" • ");
        try {
            sb.append(MLDateUtils.parseServerDateFormat(kmsLogEntry.getCreatedOn(), this.resources));
        } catch (ParseException e) {
            Log.e(TAG, "getView: failed parsing date", e);
        }
        return sb.toString();
    }

    public void cancelAll() {
        for (int i = 0; i < this.mAnimators.size(); i++) {
            SparseArray<Animator> sparseArray = this.mAnimators;
            Animator animator = (Animator) sparseArray.get(sparseArray.keyAt(i));
            animator.removeAllListeners();
            animator.cancel();
        }
        this.mAnimators.clear();
        for (int i2 = 0; i2 < this.mTimers.size(); i2++) {
            SparseArray<LockCountDownTimer> sparseArray2 = this.mTimers;
            ((CountDownTimer) sparseArray2.get(sparseArray2.keyAt(i2))).cancel();
        }
        this.mTimers.clear();
    }

    /* access modifiers changed from: 0000 */
    public void cancelExistingAnimation(LockViewHolder lockViewHolder) {
        int hashCode = lockViewHolder.swipeImageContainer.hashCode();
        Animator animator = (Animator) this.mAnimators.get(hashCode);
        if (animator != null) {
            this.mAnimators.remove(hashCode);
            animator.removeAllListeners();
            animator.cancel();
        }
    }

    /* access modifiers changed from: 0000 */
    public void cancelTimer(LockViewHolder lockViewHolder) {
        int hashCode = lockViewHolder.lock.hashCode();
        LockCountDownTimer lockCountDownTimer = (LockCountDownTimer) this.mTimers.get(hashCode);
        if (lockCountDownTimer != null) {
            lockCountDownTimer.remove();
            lockCountDownTimer.cancel();
            this.mTimers.remove(hashCode);
        }
    }

    private void startTimer(LockViewHolder lockViewHolder) {
        int hashCode = lockViewHolder.lock.hashCode();
        LockCountDownTimer lockCountDownTimer = (LockCountDownTimer) this.mTimers.get(hashCode);
        if (lockCountDownTimer == null) {
            LockCountDownTimer lockCountDownTimer2 = new LockCountDownTimer(lockViewHolder);
            lockCountDownTimer2.start();
            this.mTimers.put(hashCode, lockCountDownTimer2);
            return;
        }
        lockCountDownTimer.setView(lockViewHolder);
    }

    private void showActivityText(String str, LockViewHolder lockViewHolder) {
        Drawable drawable;
        lockViewHolder.activity.setText(str);
        lockViewHolder.lock.getLockStatus();
        if (AccessWindowUtil.hasStarted(lockViewHolder.lock)) {
            AccessType accessType = lockViewHolder.lock.getAccessType();
            AccessType accessType2 = AccessType.GUEST;
            int i = C1075R.C1076drawable.ic_calender_daytime;
            if (accessType == accessType2) {
                switch (lockViewHolder.lock.getPermissions().getScheduleType()) {
                    case TWENTY_FOUR_SEVEN:
                    case SEVEN_AM_TO_SEVEN_PM:
                        break;
                    case SEVEN_PM_TO_SEVEN_AM:
                        i = C1075R.C1076drawable.ic_calender_nighttime;
                        break;
                }
            }
            i = 0;
            drawable = new TopGravityDrawable(this.resources, BitmapFactory.decodeResource(this.resources, i));
        } else {
            drawable = null;
        }
        lockViewHolder.activity.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    private void showBattery(int i, LockViewHolder lockViewHolder) {
        if (i < 0) {
            lockViewHolder.batteryText.setVisibility(8);
            lockViewHolder.batteryIndicator.setVisibility(8);
        } else if (i <= this.resources.getInteger(C1075R.integer.low_battery_percentage)) {
            lockViewHolder.batteryIndicator.setVisibility(0);
        } else {
            lockViewHolder.batteryText.setVisibility(8);
            lockViewHolder.batteryIndicator.setVisibility(4);
        }
        lockViewHolder.swipeImageContainer.setVisibility(8);
        lockViewHolder.timerText.setVisibility(8);
    }

    private void showTimer(LockViewHolder lockViewHolder) {
        lockViewHolder.timerText.setText(null);
        lockViewHolder.timerText.setVisibility(0);
        startTimer(lockViewHolder);
        lockViewHolder.batteryText.setVisibility(8);
        lockViewHolder.batteryIndicator.setVisibility(8);
        lockViewHolder.swipeImageContainer.setVisibility(8);
    }

    private void showSwipe(LockViewHolder lockViewHolder) {
        lockViewHolder.swipeImageContainer.setVisibility(0);
        lockViewHolder.batteryText.setVisibility(8);
        lockViewHolder.batteryIndicator.setVisibility(8);
        lockViewHolder.timerText.setVisibility(8);
        final Lock lock = lockViewHolder.lock;
        final ImageView imageView = lockViewHolder.check;
        lockViewHolder.swipe.setSwipeEnabled(true);
        lockViewHolder.swipe.setShowMode(ShowMode.LayDown);
        lockViewHolder.listener = new SwipeListener() {
            public void onHandRelease(SwipeLayout swipeLayout, float f, float f2) {
            }

            public void onStartOpen(SwipeLayout swipeLayout) {
            }

            public void onOpen(SwipeLayout swipeLayout) {
                swipeLayout.requestDisallowInterceptTouchEvent(true);
                swipeLayout.close(true);
                LockAdapter.this.sendUnlockIntent(lock);
                LockAdapter.this.notifyDataSetChanged();
            }

            public void onStartClose(SwipeLayout swipeLayout) {
                swipeLayout.setSwipeEnabled(false);
            }

            public void onClose(SwipeLayout swipeLayout) {
                swipeLayout.requestDisallowInterceptTouchEvent(true);
                swipeLayout.setSwipeEnabled(true);
            }

            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {
                float abs = Math.abs(((float) i) / ((float) swipeLayout.getDragDistance()));
                imageView.setAlpha(abs);
                ImageView imageView = imageView;
                imageView.setY((1.0f - abs) * ((float) imageView.getHeight()));
            }
        };
        lockViewHolder.swipe.addSwipeListener(lockViewHolder.listener);
        startArrowAnimation(lockViewHolder);
    }

    /* access modifiers changed from: private */
    public void sendUnlockIntent(Lock lock) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(lock.getKmsDeviceKey().getDeviceId());
        Intent intent = new Intent(getContext(), BackgroundScanService.class);
        intent.setAction(BackgroundScanService.ACTION_UNLOCK);
        intent.putExtra(BackgroundScanService.LOCK_DEVICE_IDS, arrayList);
        getContext().startService(intent);
    }

    private void startArrowAnimation(LockViewHolder lockViewHolder) {
        PropertyValuesHolder ofKeyframe = PropertyValuesHolder.ofKeyframe("alpha", new Keyframe[]{Keyframe.ofFloat(0.0f, 0.3f), Keyframe.ofFloat(0.5f, 1.0f), Keyframe.ofFloat(1.0f, 0.3f)});
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(lockViewHolder.swipeLarge, new PropertyValuesHolder[]{ofKeyframe});
        ObjectAnimator ofPropertyValuesHolder2 = ObjectAnimator.ofPropertyValuesHolder(lockViewHolder.swipeMedium, new PropertyValuesHolder[]{ofKeyframe});
        ObjectAnimator ofPropertyValuesHolder3 = ObjectAnimator.ofPropertyValuesHolder(lockViewHolder.swipeSmall, new PropertyValuesHolder[]{ofKeyframe});
        AnimatorSet duration = new AnimatorSet().setDuration(700);
        ofPropertyValuesHolder2.setStartDelay(166);
        ofPropertyValuesHolder3.setStartDelay(333);
        duration.playTogether(new Animator[]{ofPropertyValuesHolder, ofPropertyValuesHolder2, ofPropertyValuesHolder3});
        duration.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                animator.setStartDelay(200);
                animator.start();
            }
        });
        duration.start();
        this.mAnimators.put(lockViewHolder.swipeImageContainer.hashCode(), duration);
    }

    public void clearItems() {
        List<Lock> list = this.items;
        if (list != null) {
            list.clear();
        }
    }
}
