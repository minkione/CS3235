package com.masterlock.ble.app.module;

import android.content.ContentResolver;
import android.content.Context;
import com.google.gson.Gson;
import com.masterlock.api.client.AccountClient;
import com.masterlock.api.client.GuestClient;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.client.ProductInvitationClient;
import com.masterlock.api.client.TermsOfServiceClient;
import com.masterlock.api.client.TimezoneClient;
import com.masterlock.api.util.IResourceWrapper;
import com.masterlock.ble.app.command.LockCommander;
import com.masterlock.ble.app.dao.GuestDAO;
import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.dao.LockDAO;
import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LocationService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.NotificationEventSettingsService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.ProfileUpdateService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.service.TermsOfServiceService;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.LocalResourcesHelper;
import com.masterlock.ble.app.util.PermissionUtil;
import com.masterlock.ble.app.util.SignUpHelper;
import com.squareup.otto.Bus;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;

public final class AppModule$$ModuleAdapter extends ModuleAdapter<AppModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = {"members/com.masterlock.ble.app.presenter.lock.LockListPresenter", "members/com.masterlock.ble.app.presenter.lock.padlock.LockDetailsPadLockPresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.LockDetailsKeySafePresenter", "members/com.masterlock.ble.app.view.lock.keysafe.LockDetailsKeySafeView", "members/com.masterlock.ble.app.presenter.settings.LockSettingsPresenter", "members/com.masterlock.ble.app.service.scan.BackgroundScanService", "members/com.masterlock.ble.app.activity.FlowActivity", "members/com.masterlock.ble.app.activity.LockActivity", "members/com.masterlock.ble.app.activity.SplashActivity", "members/com.masterlock.ble.app.activity.SignUpActivity", "members/com.masterlock.ble.app.activity.SignInActivity", "members/com.masterlock.ble.app.activity.WelcomeActivity", "members/com.masterlock.ble.app.presenter.welcome.WelcomeWalkthroughPresenter", "members/com.masterlock.ble.app.presenter.signup.TermsOfServicePresenter", "members/com.masterlock.ble.app.presenter.signup.SignUpPresenter", "members/com.masterlock.ble.app.presenter.signup.ResendEmailPresenter", "members/com.masterlock.ble.app.presenter.signup.ResendSmsPresenter", "members/com.masterlock.ble.app.presenter.signup.AccountDetailsPresenter", "members/com.masterlock.ble.app.service.TermsOfServiceService", "members/com.masterlock.ble.app.service.LockService", "members/com.masterlock.ble.app.service.KMSDeviceService", "members/com.masterlock.ble.app.presenter.splash.SplashPresenter", "members/com.masterlock.ble.app.service.SignUpService", "members/com.masterlock.ble.app.util.MasterlockScheduler", "members/com.masterlock.ble.app.presenter.login.SignInPresenter", "members/com.masterlock.ble.app.command.LockCommander", "members/com.masterlock.ble.app.service.scan.ScanService", "members/com.masterlock.ble.app.service.scan.BackgroundScanService", "members/com.masterlock.ble.app.service.scan.ForegroundScanService", "members/com.masterlock.ble.app.presenter.lock.AddLockPresenter", "members/com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter", "members/com.masterlock.ble.app.presenter.lock.padlock.PrimaryCodeUpdatePadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.UnlockModePadLockPresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.PrimaryCodeUpdateKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.UnlockModeKeySafePresenter", "members/com.masterlock.ble.app.util.PasscodeTimeoutTask", "members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.LockNamePadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.BackupMasterCodePadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.LockNotesPadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.AboutLockPadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.LockNameKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.BackupMasterCodeKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.LockNotesKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.AboutLockKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.AdjustRelockTimePadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.ShowRelockTimePadLockPresenter", "members/com.masterlock.ble.app.presenter.lock.padlock.BatteryDetailPadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.AdjustRelockTimeKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.ShowRelockTimeKeySafePresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.BatteryDetailKeySafePresenter", "members/com.masterlock.ble.app.view.NavigationDrawerView", "members/com.masterlock.ble.app.presenter.lock.padlock.MoreBatteryInfoPadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.LockTimezonePadLockPresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.MoreBatteryInfoKeySafePresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.LockTimezoneKeySafePresenter", "members/com.masterlock.ble.app.presenter.lock.ApplyChangesPresenter", "members/com.masterlock.ble.app.presenter.guest.EditGuestDetailsPresenter", "members/com.masterlock.ble.app.presenter.guest.InvitationListPresenter", "members/com.masterlock.ble.app.service.GuestService", "members/com.masterlock.ble.app.service.ProductInvitationService", "members/com.masterlock.ble.app.presenter.guest.GrantAccessPresenter", "members/com.masterlock.ble.app.presenter.login.ForgotUsernamePresenter", "members/com.masterlock.ble.app.presenter.login.ForgotPasscodePresenter", "members/com.masterlock.ble.app.view.modal.AddGuestModal", "members/com.masterlock.ble.app.view.modal.ExistingGuestModal", "members/com.masterlock.ble.app.presenter.guest.ExistingGuestListPresenter", "members/com.masterlock.ble.app.presenter.lock.padlock.HistoryPadLockPresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.HistoryKeySafePresenter", "members/com.masterlock.ble.app.presenter.nav.NavPresenter", "members/com.masterlock.ble.app.tape.UploadTask", "members/com.masterlock.ble.app.tape.UploadTaskService", "members/com.masterlock.ble.app.receiver.NetworkReceiver", "members/com.masterlock.ble.app.command.MLGattCallback", "members/com.masterlock.ble.app.presenter.settings.padlock.ResetKeysPadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.ResetKeysKeySafePresenter", "members/com.masterlock.ble.app.service.LockService", "members/com.masterlock.ble.app.service.TimezoneService", "members/com.masterlock.ble.app.presenter.settings.LockCalibrationPresenter", "members/com.masterlock.ble.app.presenter.settings.padlock.DownloadFirmwareUpdatePadLockPresenter", "members/com.masterlock.ble.app.presenter.settings.keysafe.DownloadFirmwareUpdateKeySafePresenter", "members/com.masterlock.ble.app.presenter.lock.keysafe.SecondaryCodeUpdateKeySafePresenter", "members/com.masterlock.ble.app.presenter.guest.ChangeSecondaryCodesKeySafePresenter", "members/com.masterlock.ble.app.service.scan.FirmwareUpdateService", "members/com.masterlock.ble.app.util.LockUpdateUtil", "members/com.masterlock.ble.app.tape.ConfirmTask", "members/com.masterlock.ble.app.tape.ConfirmTaskService", "members/com.masterlock.ble.app.dao.LockDAO", "members/com.masterlock.ble.app.dao.SignUpDAO", "members/com.masterlock.ble.app.presenter.settings.ShareTemporaryCodesPresenter", "members/com.masterlock.ble.app.util.SignUpHelper", "members/com.masterlock.ble.app.service.LocationService", "members/com.masterlock.ble.app.util.PermissionUtil", "members/com.masterlock.ble.app.view.modal.CountriesModal", "members/com.masterlock.ble.app.util.CodeTypesUtil", "members/com.masterlock.ble.app.util.PasscodeUtil", "members/com.masterlock.ble.app.view.lock.keysafe.LastLocationInfoKeySafeView", "members/com.masterlock.ble.app.view.lock.padlock.LastLocationInfoPadLockView", "members/com.masterlock.ble.app.presenter.lock.keysafe.LastLocationInfoKeySafePresenter", "members/com.masterlock.ble.app.presenter.lock.padlock.LastLocationInfoPadLockPresenter", "members/com.masterlock.ble.app.view.signup.AccountDetailsView", "members/com.masterlock.ble.app.presenter.guest.GrantAccessPresenter", "members/com.masterlock.ble.app.presenter.nav.settings.NotificationEventsPresenter", "members/com.masterlock.ble.app.service.NotificationEventSettingsService", "members/com.masterlock.ble.app.presenter.lock.keysafe.UnlockShacklePresenter", "members/com.masterlock.ble.app.presenter.nav.AccountSettingsPresenter", "members/com.masterlock.ble.app.presenter.nav.AccountProfilePresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangePhoneNumberPresenter", "members/com.masterlock.ble.app.presenter.nav.settings.VerifyNewEmailPresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangeEmailPresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangeNamePresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangeUsernamePresenter", "members/com.masterlock.ble.app.presenter.nav.settings.LanguageSelectionPresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangeTimeZonePresenter", "members/com.masterlock.ble.app.presenter.nav.settings.ChangePasswordPresenter", "members/com.masterlock.ble.app.service.ProfileUpdateService", "members/com.masterlock.ble.app.presenter.nav.settings.VerifyNewPhonePresenter", "members/com.masterlock.ble.app.presenter.lock.AddMechanicalLockPresenter", "members/com.masterlock.ble.app.presenter.lock.generic.GenericLockDetailsPresenter", "members/com.masterlock.ble.app.presenter.lock.dialspeed.DialSpeedDetailsPresenter", "members/com.masterlock.ble.app.presenter.lock.dialspeed.EditDialSpeedCodesPresenter", "members/com.masterlock.ble.app.adapters.LockAdapter", "members/com.masterlock.ble.app.util.LocalResourcesHelper"};
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ContextProvidesAdapter extends ProvidesBinding<Context> implements Provider<Context> {
        private final AppModule module;

        public ContextProvidesAdapter(AppModule appModule) {
            super("android.content.Context", true, "com.masterlock.ble.app.module.AppModule", "context");
            this.module = appModule;
            setLibrary(true);
        }

        public Context get() {
            return this.module.context();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesBusProvidesAdapter extends ProvidesBinding<Bus> implements Provider<Bus> {
        private final AppModule module;

        public ProvidesBusProvidesAdapter(AppModule appModule) {
            super("com.squareup.otto.Bus", true, "com.masterlock.ble.app.module.AppModule", "providesBus");
            this.module = appModule;
            setLibrary(true);
        }

        public Bus get() {
            return this.module.providesBus();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesCodeTypesUtilProvidesAdapter extends ProvidesBinding<CodeTypesUtil> implements Provider<CodeTypesUtil> {
        private final AppModule module;

        public ProvidesCodeTypesUtilProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.util.CodeTypesUtil", true, "com.masterlock.ble.app.module.AppModule", "providesCodeTypesUtil");
            this.module = appModule;
            setLibrary(true);
        }

        public CodeTypesUtil get() {
            return this.module.providesCodeTypesUtil();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesConfirmTaskQueueProvidesAdapter extends ProvidesBinding<ConfirmTaskQueue> implements Provider<ConfirmTaskQueue> {
        private Binding<Gson> gson;
        private final AppModule module;

        public ProvidesConfirmTaskQueueProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.tape.ConfirmTaskQueue", true, "com.masterlock.ble.app.module.AppModule", "providesConfirmTaskQueue");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.gson = linker.requestBinding("com.google.gson.Gson", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.gson);
        }

        public ConfirmTaskQueue get() {
            return this.module.providesConfirmTaskQueue((Gson) this.gson.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesContentResolverProvidesAdapter extends ProvidesBinding<ContentResolver> implements Provider<ContentResolver> {
        private Binding<Context> context;
        private final AppModule module;

        public ProvidesContentResolverProvidesAdapter(AppModule appModule) {
            super("android.content.ContentResolver", true, "com.masterlock.ble.app.module.AppModule", "providesContentResolver");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public ContentResolver get() {
            return this.module.providesContentResolver((Context) this.context.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesGuestDAOProvidesAdapter extends ProvidesBinding<GuestDAO> implements Provider<GuestDAO> {
        private final AppModule module;

        public ProvidesGuestDAOProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.dao.GuestDAO", true, "com.masterlock.ble.app.module.AppModule", "providesGuestDAO");
            this.module = appModule;
            setLibrary(true);
        }

        public GuestDAO get() {
            return this.module.providesGuestDAO();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesGuestServiceProvidesAdapter extends ProvidesBinding<GuestService> implements Provider<GuestService> {
        private Binding<GuestClient> guestClient;
        private final AppModule module;

        public ProvidesGuestServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.GuestService", true, "com.masterlock.ble.app.module.AppModule", "providesGuestService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.guestClient = linker.requestBinding("com.masterlock.api.client.GuestClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.guestClient);
        }

        public GuestService get() {
            return this.module.providesGuestService((GuestClient) this.guestClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesInvitationDAOProvidesAdapter extends ProvidesBinding<InvitationDAO> implements Provider<InvitationDAO> {
        private final AppModule module;

        public ProvidesInvitationDAOProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.dao.InvitationDAO", true, "com.masterlock.ble.app.module.AppModule", "providesInvitationDAO");
            this.module = appModule;
            setLibrary(true);
        }

        public InvitationDAO get() {
            return this.module.providesInvitationDAO();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesKMSDeviceLogServiceProvidesAdapter extends ProvidesBinding<KMSDeviceLogService> implements Provider<KMSDeviceLogService> {
        private Binding<KMSDeviceLogClient> kmsDeviceLogClient;
        private final AppModule module;

        public ProvidesKMSDeviceLogServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.KMSDeviceLogService", true, "com.masterlock.ble.app.module.AppModule", "providesKMSDeviceLogService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.kmsDeviceLogClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceLogClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.kmsDeviceLogClient);
        }

        public KMSDeviceLogService get() {
            return this.module.providesKMSDeviceLogService((KMSDeviceLogClient) this.kmsDeviceLogClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesKMSDeviceServiceProvidesAdapter extends ProvidesBinding<KMSDeviceService> implements Provider<KMSDeviceService> {
        private Binding<KMSDeviceClient> kmsDeviceClient;
        private final AppModule module;

        public ProvidesKMSDeviceServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.KMSDeviceService", true, "com.masterlock.ble.app.module.AppModule", "providesKMSDeviceService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.kmsDeviceClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.kmsDeviceClient);
        }

        public KMSDeviceService get() {
            return this.module.providesKMSDeviceService((KMSDeviceClient) this.kmsDeviceClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesLocalResourcesHelperProvidesAdapter extends ProvidesBinding<LocalResourcesHelper> implements Provider<LocalResourcesHelper> {
        private final AppModule module;

        public ProvidesLocalResourcesHelperProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.util.LocalResourcesHelper", true, "com.masterlock.ble.app.module.AppModule", "providesLocalResourcesHelper");
            this.module = appModule;
            setLibrary(true);
        }

        public LocalResourcesHelper get() {
            return this.module.providesLocalResourcesHelper();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesLocationServiceProvidesAdapter extends ProvidesBinding<LocationService> implements Provider<LocationService> {
        private final AppModule module;

        public ProvidesLocationServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.LocationService", true, "com.masterlock.ble.app.module.AppModule", "providesLocationService");
            this.module = appModule;
            setLibrary(true);
        }

        public LocationService get() {
            return this.module.providesLocationService();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesLockCommanderProvidesAdapter extends ProvidesBinding<LockCommander> implements Provider<LockCommander> {
        private Binding<Context> context;
        private final AppModule module;

        public ProvidesLockCommanderProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.command.LockCommander", true, "com.masterlock.ble.app.module.AppModule", "providesLockCommander");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.context);
        }

        public LockCommander get() {
            return this.module.providesLockCommander((Context) this.context.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesLockDAOProvidesAdapter extends ProvidesBinding<LockDAO> implements Provider<LockDAO> {
        private final AppModule module;

        public ProvidesLockDAOProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.dao.LockDAO", true, "com.masterlock.ble.app.module.AppModule", "providesLockDAO");
            this.module = appModule;
            setLibrary(true);
        }

        public LockDAO get() {
            return this.module.providesLockDAO();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesLockServiceProvidesAdapter extends ProvidesBinding<LockService> implements Provider<LockService> {
        private Binding<KMSDeviceClient> kmsDeviceClient;
        private Binding<KMSDeviceLogClient> kmsDeviceLogClient;
        private final AppModule module;
        private Binding<ProductClient> productClient;

        public ProvidesLockServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.LockService", true, "com.masterlock.ble.app.module.AppModule", "providesLockService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.productClient = linker.requestBinding("com.masterlock.api.client.ProductClient", AppModule.class, getClass().getClassLoader());
            this.kmsDeviceLogClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceLogClient", AppModule.class, getClass().getClassLoader());
            this.kmsDeviceClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.productClient);
            set.add(this.kmsDeviceLogClient);
            set.add(this.kmsDeviceClient);
        }

        public LockService get() {
            return this.module.providesLockService((ProductClient) this.productClient.get(), (KMSDeviceLogClient) this.kmsDeviceLogClient.get(), (KMSDeviceClient) this.kmsDeviceClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesNotificationEventsSettingServiceProvidesAdapter extends ProvidesBinding<NotificationEventSettingsService> implements Provider<NotificationEventSettingsService> {
        private Binding<AccountClient> accountClient;
        private final AppModule module;

        public ProvidesNotificationEventsSettingServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.NotificationEventSettingsService", true, "com.masterlock.ble.app.module.AppModule", "providesNotificationEventsSettingService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.accountClient = linker.requestBinding("com.masterlock.api.client.AccountClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.accountClient);
        }

        public NotificationEventSettingsService get() {
            return this.module.providesNotificationEventsSettingService((AccountClient) this.accountClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesPermissionUtilProvidesAdapter extends ProvidesBinding<PermissionUtil> implements Provider<PermissionUtil> {
        private final AppModule module;

        public ProvidesPermissionUtilProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.util.PermissionUtil", true, "com.masterlock.ble.app.module.AppModule", "providesPermissionUtil");
            this.module = appModule;
            setLibrary(true);
        }

        public PermissionUtil get() {
            return this.module.providesPermissionUtil();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesProductInvitationServiceProvidesAdapter extends ProvidesBinding<ProductInvitationService> implements Provider<ProductInvitationService> {
        private final AppModule module;
        private Binding<ProductInvitationClient> productInvitationClient;

        public ProvidesProductInvitationServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.ProductInvitationService", true, "com.masterlock.ble.app.module.AppModule", "providesProductInvitationService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.productInvitationClient = linker.requestBinding("com.masterlock.api.client.ProductInvitationClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.productInvitationClient);
        }

        public ProductInvitationService get() {
            return this.module.providesProductInvitationService((ProductInvitationClient) this.productInvitationClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesProfileUpdateServiceProvidesAdapter extends ProvidesBinding<ProfileUpdateService> implements Provider<ProfileUpdateService> {
        private Binding<AccountClient> accountClient;
        private final AppModule module;

        public ProvidesProfileUpdateServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.ProfileUpdateService", true, "com.masterlock.ble.app.module.AppModule", "providesProfileUpdateService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.accountClient = linker.requestBinding("com.masterlock.api.client.AccountClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.accountClient);
        }

        public ProfileUpdateService get() {
            return this.module.providesProfileUpdateService((AccountClient) this.accountClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesSignInServiceProvidesAdapter extends ProvidesBinding<SignInService> implements Provider<SignInService> {
        private Binding<AccountClient> accountClient;
        private final AppModule module;
        private Binding<TermsOfServiceClient> termsOfServiceClient;

        public ProvidesSignInServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.SignInService", true, "com.masterlock.ble.app.module.AppModule", "providesSignInService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.termsOfServiceClient = linker.requestBinding("com.masterlock.api.client.TermsOfServiceClient", AppModule.class, getClass().getClassLoader());
            this.accountClient = linker.requestBinding("com.masterlock.api.client.AccountClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.termsOfServiceClient);
            set.add(this.accountClient);
        }

        public SignInService get() {
            return this.module.providesSignInService((TermsOfServiceClient) this.termsOfServiceClient.get(), (AccountClient) this.accountClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesSignUpDAOProvidesAdapter extends ProvidesBinding<SignUpDAO> implements Provider<SignUpDAO> {
        private final AppModule module;

        public ProvidesSignUpDAOProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.dao.SignUpDAO", true, "com.masterlock.ble.app.module.AppModule", "providesSignUpDAO");
            this.module = appModule;
            setLibrary(true);
        }

        public SignUpDAO get() {
            return this.module.providesSignUpDAO();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesSignUpHelperProvidesAdapter extends ProvidesBinding<SignUpHelper> implements Provider<SignUpHelper> {
        private final AppModule module;

        public ProvidesSignUpHelperProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.util.SignUpHelper", true, "com.masterlock.ble.app.module.AppModule", "providesSignUpHelper");
            this.module = appModule;
            setLibrary(true);
        }

        public SignUpHelper get() {
            return this.module.providesSignUpHelper();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesSignUpServiceProvidesAdapter extends ProvidesBinding<SignUpService> implements Provider<SignUpService> {
        private Binding<AccountClient> accountClient;
        private final AppModule module;
        private Binding<TermsOfServiceService> tosService;

        public ProvidesSignUpServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.SignUpService", true, "com.masterlock.ble.app.module.AppModule", "providesSignUpService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.tosService = linker.requestBinding("com.masterlock.ble.app.service.TermsOfServiceService", AppModule.class, getClass().getClassLoader());
            this.accountClient = linker.requestBinding("com.masterlock.api.client.AccountClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.tosService);
            set.add(this.accountClient);
        }

        public SignUpService get() {
            return this.module.providesSignUpService((TermsOfServiceService) this.tosService.get(), (AccountClient) this.accountClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesTermsOfServiceServiceProvidesAdapter extends ProvidesBinding<TermsOfServiceService> implements Provider<TermsOfServiceService> {
        private Binding<TermsOfServiceClient> client;
        private final AppModule module;

        public ProvidesTermsOfServiceServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.TermsOfServiceService", true, "com.masterlock.ble.app.module.AppModule", "providesTermsOfServiceService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.client = linker.requestBinding("com.masterlock.api.client.TermsOfServiceClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.client);
        }

        public TermsOfServiceService get() {
            return this.module.providesTermsOfServiceService((TermsOfServiceClient) this.client.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesTimezoneServiceProvidesAdapter extends ProvidesBinding<TimezoneService> implements Provider<TimezoneService> {
        private final AppModule module;
        private Binding<TimezoneClient> timezoneClient;

        public ProvidesTimezoneServiceProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.service.TimezoneService", true, "com.masterlock.ble.app.module.AppModule", "providesTimezoneService");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.timezoneClient = linker.requestBinding("com.masterlock.api.client.TimezoneClient", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.timezoneClient);
        }

        public TimezoneService get() {
            return this.module.providesTimezoneService((TimezoneClient) this.timezoneClient.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ProvidesUploadTaskQueueProvidesAdapter extends ProvidesBinding<UploadTaskQueue> implements Provider<UploadTaskQueue> {
        private Binding<Gson> gson;
        private final AppModule module;

        public ProvidesUploadTaskQueueProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.tape.UploadTaskQueue", true, "com.masterlock.ble.app.module.AppModule", "providesUploadTaskQueue");
            this.module = appModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.gson = linker.requestBinding("com.google.gson.Gson", AppModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.gson);
        }

        public UploadTaskQueue get() {
            return this.module.providesUploadTaskQueue((Gson) this.gson.get());
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class ResourceWrapperProvidesAdapter extends ProvidesBinding<IResourceWrapper> implements Provider<IResourceWrapper> {
        private final AppModule module;

        public ResourceWrapperProvidesAdapter(AppModule appModule) {
            super("com.masterlock.api.util.IResourceWrapper", true, "com.masterlock.ble.app.module.AppModule", "resourceWrapper");
            this.module = appModule;
            setLibrary(true);
        }

        public IResourceWrapper get() {
            return this.module.resourceWrapper();
        }
    }

    /* compiled from: AppModule$$ModuleAdapter */
    public static final class SchedulerProvidesAdapter extends ProvidesBinding<IScheduler> implements Provider<IScheduler> {
        private final AppModule module;

        public SchedulerProvidesAdapter(AppModule appModule) {
            super("com.masterlock.ble.app.util.IScheduler", true, "com.masterlock.ble.app.module.AppModule", "scheduler");
            this.module = appModule;
            setLibrary(true);
        }

        public IScheduler get() {
            return this.module.scheduler();
        }
    }

    public AppModule$$ModuleAdapter() {
        super(AppModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public void getBindings(BindingsGroup bindingsGroup, AppModule appModule) {
        bindingsGroup.contributeProvidesBinding("android.content.Context", new ContextProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.squareup.otto.Bus", new ProvidesBusProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.TermsOfServiceService", new ProvidesTermsOfServiceServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.util.IScheduler", new SchedulerProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.SignUpService", new ProvidesSignUpServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.SignInService", new ProvidesSignInServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.LockService", new ProvidesLockServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.util.SignUpHelper", new ProvidesSignUpHelperProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.command.LockCommander", new ProvidesLockCommanderProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("android.content.ContentResolver", new ProvidesContentResolverProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.KMSDeviceService", new ProvidesKMSDeviceServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.KMSDeviceLogService", new ProvidesKMSDeviceLogServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.GuestService", new ProvidesGuestServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.NotificationEventSettingsService", new ProvidesNotificationEventsSettingServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.ProfileUpdateService", new ProvidesProfileUpdateServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.dao.GuestDAO", new ProvidesGuestDAOProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.ProductInvitationService", new ProvidesProductInvitationServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.dao.InvitationDAO", new ProvidesInvitationDAOProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.util.IResourceWrapper", new ResourceWrapperProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.tape.UploadTaskQueue", new ProvidesUploadTaskQueueProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.tape.ConfirmTaskQueue", new ProvidesConfirmTaskQueueProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.dao.LockDAO", new ProvidesLockDAOProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.dao.SignUpDAO", new ProvidesSignUpDAOProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.TimezoneService", new ProvidesTimezoneServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.service.LocationService", new ProvidesLocationServiceProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.util.PermissionUtil", new ProvidesPermissionUtilProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.util.CodeTypesUtil", new ProvidesCodeTypesUtilProvidesAdapter(appModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.ble.app.util.LocalResourcesHelper", new ProvidesLocalResourcesHelperProvidesAdapter(appModule));
    }
}
