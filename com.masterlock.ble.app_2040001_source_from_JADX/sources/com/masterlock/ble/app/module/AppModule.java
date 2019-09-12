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
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.activity.FlowActivity;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.activity.SignInActivity;
import com.masterlock.ble.app.activity.SignUpActivity;
import com.masterlock.ble.app.activity.SplashActivity;
import com.masterlock.ble.app.activity.WelcomeActivity;
import com.masterlock.ble.app.adapters.LockAdapter;
import com.masterlock.ble.app.bus.MainThreadBus;
import com.masterlock.ble.app.command.LockCommander;
import com.masterlock.ble.app.command.MLGattCallback;
import com.masterlock.ble.app.dao.GuestDAO;
import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.dao.LockDAO;
import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.presenter.guest.ChangeSecondaryCodesKeySafePresenter;
import com.masterlock.ble.app.presenter.guest.EditGuestDetailsPresenter;
import com.masterlock.ble.app.presenter.guest.ExistingGuestListPresenter;
import com.masterlock.ble.app.presenter.guest.GrantAccessPresenter;
import com.masterlock.ble.app.presenter.guest.InvitationListPresenter;
import com.masterlock.ble.app.presenter.lock.AddLockPresenter;
import com.masterlock.ble.app.presenter.lock.AddMechanicalLockPresenter;
import com.masterlock.ble.app.presenter.lock.ApplyChangesPresenter;
import com.masterlock.ble.app.presenter.lock.LockListPresenter;
import com.masterlock.ble.app.presenter.lock.dialspeed.DialSpeedDetailsPresenter;
import com.masterlock.ble.app.presenter.lock.dialspeed.EditDialSpeedCodesPresenter;
import com.masterlock.ble.app.presenter.lock.generic.GenericLockDetailsPresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.BatteryDetailKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.HistoryKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.LastLocationInfoKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.LockDetailsKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.MoreBatteryInfoKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.PrimaryCodeUpdateKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.SecondaryCodeUpdateKeySafePresenter;
import com.masterlock.ble.app.presenter.lock.keysafe.UnlockShacklePresenter;
import com.masterlock.ble.app.presenter.lock.padlock.BatteryDetailPadLockPresenter;
import com.masterlock.ble.app.presenter.lock.padlock.HistoryPadLockPresenter;
import com.masterlock.ble.app.presenter.lock.padlock.LastLocationInfoPadLockPresenter;
import com.masterlock.ble.app.presenter.lock.padlock.LockDetailsPadLockPresenter;
import com.masterlock.ble.app.presenter.lock.padlock.MoreBatteryInfoPadLockPresenter;
import com.masterlock.ble.app.presenter.lock.padlock.PrimaryCodeUpdatePadLockPresenter;
import com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter;
import com.masterlock.ble.app.presenter.login.ForgotPasscodePresenter;
import com.masterlock.ble.app.presenter.login.ForgotUsernamePresenter;
import com.masterlock.ble.app.presenter.login.SignInPresenter;
import com.masterlock.ble.app.presenter.nav.AccountProfilePresenter;
import com.masterlock.ble.app.presenter.nav.AccountSettingsPresenter;
import com.masterlock.ble.app.presenter.nav.NavPresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangeEmailPresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangeNamePresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangePasswordPresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangePhoneNumberPresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangeTimeZonePresenter;
import com.masterlock.ble.app.presenter.nav.settings.ChangeUsernamePresenter;
import com.masterlock.ble.app.presenter.nav.settings.LanguageSelectionPresenter;
import com.masterlock.ble.app.presenter.nav.settings.NotificationEventsPresenter;
import com.masterlock.ble.app.presenter.nav.settings.VerifyNewEmailPresenter;
import com.masterlock.ble.app.presenter.nav.settings.VerifyNewPhonePresenter;
import com.masterlock.ble.app.presenter.settings.LockCalibrationPresenter;
import com.masterlock.ble.app.presenter.settings.LockSettingsPresenter;
import com.masterlock.ble.app.presenter.settings.ShareTemporaryCodesPresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.AboutLockKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.AdjustRelockTimeKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.BackupMasterCodeKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.DownloadFirmwareUpdateKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.LockNameKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.LockNotesKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.LockTimezoneKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.ResetKeysKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.ShowRelockTimeKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.keysafe.UnlockModeKeySafePresenter;
import com.masterlock.ble.app.presenter.settings.padlock.AboutLockPadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.AdjustRelockTimePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.BackupMasterCodePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.DownloadFirmwareUpdatePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.LockNamePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.LockNotesPadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.LockTimezonePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.ResetKeysPadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.ShowRelockTimePadLockPresenter;
import com.masterlock.ble.app.presenter.settings.padlock.UnlockModePadLockPresenter;
import com.masterlock.ble.app.presenter.signup.AccountDetailsPresenter;
import com.masterlock.ble.app.presenter.signup.ResendEmailPresenter;
import com.masterlock.ble.app.presenter.signup.ResendSmsPresenter;
import com.masterlock.ble.app.presenter.signup.SignUpPresenter;
import com.masterlock.ble.app.presenter.signup.TermsOfServicePresenter;
import com.masterlock.ble.app.presenter.splash.SplashPresenter;
import com.masterlock.ble.app.presenter.welcome.WelcomeWalkthroughPresenter;
import com.masterlock.ble.app.receiver.NetworkReceiver;
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
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.FirmwareUpdateService;
import com.masterlock.ble.app.service.scan.ForegroundScanService;
import com.masterlock.ble.app.service.scan.ScanService;
import com.masterlock.ble.app.tape.ConfirmTask;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.tape.ConfirmTaskService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.tape.UploadTaskService;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.LocalResourcesHelper;
import com.masterlock.ble.app.util.LockUpdateUtil;
import com.masterlock.ble.app.util.MasterlockScheduler;
import com.masterlock.ble.app.util.PasscodeTimeoutTask;
import com.masterlock.ble.app.util.PasscodeUtil;
import com.masterlock.ble.app.util.PermissionUtil;
import com.masterlock.ble.app.util.ResourceWrapper;
import com.masterlock.ble.app.util.SignUpHelper;
import com.masterlock.ble.app.view.NavigationDrawerView;
import com.masterlock.ble.app.view.lock.keysafe.LastLocationInfoKeySafeView;
import com.masterlock.ble.app.view.lock.keysafe.LockDetailsKeySafeView;
import com.masterlock.ble.app.view.lock.padlock.LastLocationInfoPadLockView;
import com.masterlock.ble.app.view.modal.AddGuestModal;
import com.masterlock.ble.app.view.modal.CountriesModal;
import com.masterlock.ble.app.view.modal.ExistingGuestModal;
import com.masterlock.ble.app.view.signup.AccountDetailsView;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(complete = false, injects = {LockListPresenter.class, LockDetailsPadLockPresenter.class, LockDetailsKeySafePresenter.class, LockDetailsKeySafeView.class, LockSettingsPresenter.class, BackgroundScanService.class, FlowActivity.class, LockActivity.class, SplashActivity.class, SignUpActivity.class, SignInActivity.class, WelcomeActivity.class, WelcomeWalkthroughPresenter.class, TermsOfServicePresenter.class, SignUpPresenter.class, ResendEmailPresenter.class, ResendSmsPresenter.class, AccountDetailsPresenter.class, TermsOfServiceService.class, LockService.class, KMSDeviceService.class, SplashPresenter.class, SignUpService.class, MasterlockScheduler.class, SignInPresenter.class, LockCommander.class, ScanService.class, BackgroundScanService.class, ForegroundScanService.class, AddLockPresenter.class, LockLandingPresenter.class, PrimaryCodeUpdatePadLockPresenter.class, UnlockModePadLockPresenter.class, PrimaryCodeUpdateKeySafePresenter.class, UnlockModeKeySafePresenter.class, PasscodeTimeoutTask.class, AuthenticatedPresenter.class, LockNamePadLockPresenter.class, BackupMasterCodePadLockPresenter.class, LockNotesPadLockPresenter.class, AboutLockPadLockPresenter.class, LockNameKeySafePresenter.class, BackupMasterCodeKeySafePresenter.class, LockNotesKeySafePresenter.class, AboutLockKeySafePresenter.class, AdjustRelockTimePadLockPresenter.class, ShowRelockTimePadLockPresenter.class, BatteryDetailPadLockPresenter.class, AdjustRelockTimeKeySafePresenter.class, ShowRelockTimeKeySafePresenter.class, BatteryDetailKeySafePresenter.class, NavigationDrawerView.class, MoreBatteryInfoPadLockPresenter.class, LockTimezonePadLockPresenter.class, MoreBatteryInfoKeySafePresenter.class, LockTimezoneKeySafePresenter.class, ApplyChangesPresenter.class, EditGuestDetailsPresenter.class, InvitationListPresenter.class, GuestService.class, ProductInvitationService.class, GrantAccessPresenter.class, ForgotUsernamePresenter.class, ForgotPasscodePresenter.class, AddGuestModal.class, ExistingGuestModal.class, ExistingGuestListPresenter.class, HistoryPadLockPresenter.class, HistoryKeySafePresenter.class, NavPresenter.class, UploadTask.class, UploadTaskService.class, NetworkReceiver.class, MLGattCallback.class, ResetKeysPadLockPresenter.class, ResetKeysKeySafePresenter.class, LockService.class, TimezoneService.class, LockCalibrationPresenter.class, DownloadFirmwareUpdatePadLockPresenter.class, DownloadFirmwareUpdateKeySafePresenter.class, SecondaryCodeUpdateKeySafePresenter.class, ChangeSecondaryCodesKeySafePresenter.class, FirmwareUpdateService.class, LockUpdateUtil.class, ConfirmTask.class, ConfirmTaskService.class, LockDAO.class, SignUpDAO.class, ShareTemporaryCodesPresenter.class, SignUpHelper.class, LocationService.class, PermissionUtil.class, CountriesModal.class, CodeTypesUtil.class, PasscodeUtil.class, LastLocationInfoKeySafeView.class, LastLocationInfoPadLockView.class, LastLocationInfoKeySafePresenter.class, LastLocationInfoPadLockPresenter.class, AccountDetailsView.class, GrantAccessPresenter.class, NotificationEventsPresenter.class, NotificationEventSettingsService.class, UnlockShacklePresenter.class, AccountSettingsPresenter.class, AccountProfilePresenter.class, ChangePhoneNumberPresenter.class, VerifyNewEmailPresenter.class, ChangeEmailPresenter.class, ChangeNamePresenter.class, ChangeUsernamePresenter.class, LanguageSelectionPresenter.class, ChangeTimeZonePresenter.class, ChangePasswordPresenter.class, ProfileUpdateService.class, VerifyNewPhonePresenter.class, AddMechanicalLockPresenter.class, GenericLockDetailsPresenter.class, DialSpeedDetailsPresenter.class, EditDialSpeedCodesPresenter.class, LockAdapter.class, LocalResourcesHelper.class}, library = true)
public class AppModule {
    private Context context;

    public AppModule(MasterLockApp masterLockApp) {
        this.context = masterLockApp.getApplicationContext();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public Context context() {
        return this.context;
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public Bus providesBus() {
        return new MainThreadBus("MasterLock");
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public TermsOfServiceService providesTermsOfServiceService(TermsOfServiceClient termsOfServiceClient) {
        return new TermsOfServiceService(termsOfServiceClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public IScheduler scheduler() {
        return new MasterlockScheduler();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SignUpService providesSignUpService(TermsOfServiceService termsOfServiceService, AccountClient accountClient) {
        return new SignUpService(termsOfServiceService, accountClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SignInService providesSignInService(TermsOfServiceClient termsOfServiceClient, AccountClient accountClient) {
        return new SignInService(termsOfServiceClient, accountClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public LockService providesLockService(ProductClient productClient, KMSDeviceLogClient kMSDeviceLogClient, KMSDeviceClient kMSDeviceClient) {
        return new LockService(productClient, kMSDeviceLogClient, kMSDeviceClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SignUpHelper providesSignUpHelper() {
        return new SignUpHelper();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public LockCommander providesLockCommander(Context context2) {
        return new LockCommander(context2);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ContentResolver providesContentResolver(Context context2) {
        return context2.getContentResolver();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public KMSDeviceService providesKMSDeviceService(KMSDeviceClient kMSDeviceClient) {
        return new KMSDeviceService(kMSDeviceClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public KMSDeviceLogService providesKMSDeviceLogService(KMSDeviceLogClient kMSDeviceLogClient) {
        return new KMSDeviceLogService(kMSDeviceLogClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public GuestService providesGuestService(GuestClient guestClient) {
        return new GuestService(guestClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public NotificationEventSettingsService providesNotificationEventsSettingService(AccountClient accountClient) {
        return new NotificationEventSettingsService(accountClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ProfileUpdateService providesProfileUpdateService(AccountClient accountClient) {
        return new ProfileUpdateService(accountClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public GuestDAO providesGuestDAO() {
        return new GuestDAO();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ProductInvitationService providesProductInvitationService(ProductInvitationClient productInvitationClient) {
        return new ProductInvitationService(productInvitationClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public InvitationDAO providesInvitationDAO() {
        return new InvitationDAO();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public IResourceWrapper resourceWrapper() {
        return new ResourceWrapper(this.context);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public UploadTaskQueue providesUploadTaskQueue(Gson gson) {
        return UploadTaskQueue.create(this.context, gson);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ConfirmTaskQueue providesConfirmTaskQueue(Gson gson) {
        return ConfirmTaskQueue.create(this.context, gson);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public LockDAO providesLockDAO() {
        return new LockDAO();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SignUpDAO providesSignUpDAO() {
        return new SignUpDAO();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public TimezoneService providesTimezoneService(TimezoneClient timezoneClient) {
        return new TimezoneService(timezoneClient);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public LocationService providesLocationService() {
        return new LocationService();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public PermissionUtil providesPermissionUtil() {
        return new PermissionUtil();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public CodeTypesUtil providesCodeTypesUtil() {
        return new CodeTypesUtil();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public LocalResourcesHelper providesLocalResourcesHelper() {
        return new LocalResourcesHelper();
    }
}
