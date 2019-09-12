package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.NotificationEventSettingsService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class NotificationEventsPresenter$$InjectAdapter extends Binding<NotificationEventsPresenter> implements MembersInjector<NotificationEventsPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<NotificationEventSettingsService> mNotificationEventSettingsService;
    private Binding<IScheduler> mScheduler;
    private Binding<Presenter> supertype;

    public NotificationEventsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.settings.NotificationEventsPresenter", false, NotificationEventsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", NotificationEventsPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", NotificationEventsPresenter.class, getClass().getClassLoader());
        this.mNotificationEventSettingsService = linker.requestBinding("com.masterlock.ble.app.service.NotificationEventSettingsService", NotificationEventsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", NotificationEventsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.mNotificationEventSettingsService);
        set2.add(this.supertype);
    }

    public void injectMembers(NotificationEventsPresenter notificationEventsPresenter) {
        notificationEventsPresenter.mEventBus = (Bus) this.mEventBus.get();
        notificationEventsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        notificationEventsPresenter.mNotificationEventSettingsService = (NotificationEventSettingsService) this.mNotificationEventSettingsService.get();
        this.supertype.injectMembers(notificationEventsPresenter);
    }
}
