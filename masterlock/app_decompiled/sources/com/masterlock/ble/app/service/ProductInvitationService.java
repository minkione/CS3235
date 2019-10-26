package com.masterlock.ble.app.service;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Parcelable;
import com.masterlock.api.client.ProductInvitationClient;
import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.entity.InvitationValidateResponse;
import com.masterlock.api.entity.ProductInvitationGuestRequest;
import com.masterlock.api.entity.ProductInvitationRequest;
import com.masterlock.api.entity.TempCodeResponse;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Lock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import net.sqlcipher.database.SQLiteDatabase;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import retrofit.client.Response;

public class ProductInvitationService {
    public static final String API_KEY = MasterLockApp.get().getString(C1075R.string.api_key);
    private AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    @Inject
    InvitationDAO mInvitationDAO;
    /* access modifiers changed from: private */
    public ProductInvitationClient mProductInvitationClient;
    private MasterLockSharedPreferences mSharedPreferences;

    public ProductInvitationService(ProductInvitationClient productInvitationClient) {
        this(productInvitationClient, MasterLockSharedPreferences.getInstance());
    }

    public ProductInvitationService(ProductInvitationClient productInvitationClient, AuthenticationStore authenticationStore) {
        this(productInvitationClient, authenticationStore, MasterLockApp.get().getContentResolver());
    }

    public ProductInvitationService(ProductInvitationClient productInvitationClient, AuthenticationStore authenticationStore, ContentResolver contentResolver) {
        this.mProductInvitationClient = productInvitationClient;
        this.mAuthStore = authenticationStore;
        this.mContentResolver = contentResolver;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
        this.mSharedPreferences = MasterLockSharedPreferences.getInstance();
        MasterLockApp.get().inject(this);
    }

    public Observable<List<Invitation>> getInvitationsForProduct(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Invitation>>() {
            public void call(Subscriber<? super List<Invitation>> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.getInvitationList(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), str));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> delete(final String str, final String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                ThreadUtil.errorOnUIThread();
                DeleteResponse deleteInvitation = ProductInvitationService.this.mProductInvitationClient.deleteInvitation(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), str, str2);
                if (deleteInvitation.serviceResult != 1) {
                    subscriber.onError(new Exception(deleteInvitation.message));
                    return;
                }
                subscriber.onNext(str2);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return ProductInvitationService.this.remove((String) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<Boolean> remove(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                boolean z = true;
                if (ProductInvitationService.this.mContentResolver.delete(Invitations.buildInvitationUri(str), null, null) != 1) {
                    z = false;
                }
                subscriber.onNext(Boolean.valueOf(z));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Invitation> createInvitation(final ProductInvitationRequest productInvitationRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.createInvitation(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), productInvitationRequest.getProductId(), productInvitationRequest));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return ProductInvitationService.this.insertNewInvitation((Invitation) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<Invitation> insertNewInvitation(Invitation invitation) {
        return this.mInvitationDAO.insertNewInvitation(invitation);
    }

    public Observable<Invitation> getInvitationByEmail(String str, String str2) {
        return this.mInvitationDAO.getInvitationByEmail(str, str2);
    }

    public Observable<Invitation> getInvitationByMobile(String str, String str2) {
        return this.mInvitationDAO.getInvitationByMobile(str, str2);
    }

    public Observable<Invitation> updateInvitation(final Invitation invitation) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.updateInvitation(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), invitation.getGuestPermissions().getId(), invitation.getGuestPermissions()));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1(invitation) {
            private final /* synthetic */ Invitation f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return ProductInvitationService.this.mInvitationDAO.updateInvitation(this.f$1);
            }
        });
    }

    public Observable<InvitationValidateResponse> validateInvitation(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<InvitationValidateResponse>() {
            public void call(Subscriber<? super InvitationValidateResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.validateInvitation(str, ProductInvitationService.API_KEY));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> acceptInvitation(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.acceptInvitation(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), str));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> processInvitation(final String str) {
        return validateInvitation(str).flatMap(new Func1<InvitationValidateResponse, Observable<Response>>() {
            public Observable<Response> call(InvitationValidateResponse invitationValidateResponse) {
                return ProductInvitationService.this.acceptInvitation(str);
            }
        });
    }

    public Observable<Invitation> createGuestAndInvitation(final String str, final ProductInvitationGuestRequest productInvitationGuestRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.createGuestAndInvitation(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), str, productInvitationGuestRequest));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Invitation>> findInvitationsForGuests(String str, List<Guest> list) {
        return this.mInvitationDAO.findInvitationsForGuests(str, list);
    }

    public Observable<TempCodeResponse> getTempCode(final Lock lock, final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<TempCodeResponse>() {
            public void call(Subscriber<? super TempCodeResponse> subscriber) {
                subscriber.onNext(ProductInvitationService.this.mProductInvitationClient.getTempCode(ProductInvitationService.this.mAuthenticatedRequestBuilder.build(), str, lock.getKmsId()));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<String> sendTempCode(Lock lock, String str, Resources resources) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str, lock, resources) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ Lock f$2;
            private final /* synthetic */ Resources f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void call(Object obj) {
                ProductInvitationService.lambda$sendTempCode$2(ProductInvitationService.this, this.f$1, this.f$2, this.f$3, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$sendTempCode$2(ProductInvitationService productInvitationService, String str, Lock lock, Resources resources, Subscriber subscriber) {
        String str2;
        String str3;
        String str4;
        ProductInvitationService productInvitationService2 = productInvitationService;
        String str5 = str;
        Resources resources2 = resources;
        TempCodeResponse tempCode = productInvitationService2.mProductInvitationClient.getTempCode(productInvitationService2.mAuthenticatedRequestBuilder.build(), str5, lock.getKmsId());
        String format = String.format(resources2.getString(C1075R.string.guest_detail_send_temporary_title), new Object[]{lock.getName(), lock.getKmsDeviceKey().getDeviceId()});
        if (lock.isPadLock()) {
            if (str5 == null) {
                str4 = String.format(resources2.getString(C1075R.string.guest_detail_send_temporary_body), new Object[]{productInvitationService2.mSharedPreferences.getUserFullName(), lock.getName(), lock.getKmsDeviceKey().getDeviceId(), tempCode.getServiceCodeFormated(resources2.getString(C1075R.string.guest_detail_temporary_code_up), resources2.getString(C1075R.string.guest_detail_temporary_code_down), resources2.getString(C1075R.string.guest_detail_temporary_code_left), resources2.getString(C1075R.string.guest_detail_temporary_code_right)), tempCode.localizeDate(tempCode.getExpiresOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), lock.getLocalizedTimeZone()});
            } else {
                str4 = String.format(resources2.getString(C1075R.string.guest_detail_send_future_temporary_body), new Object[]{productInvitationService2.mSharedPreferences.getUserFullName(), lock.getName(), lock.getKmsDeviceKey().getDeviceId(), tempCode.getServiceCodeFormated(resources2.getString(C1075R.string.guest_detail_temporary_code_up), resources2.getString(C1075R.string.guest_detail_temporary_code_down), resources2.getString(C1075R.string.guest_detail_temporary_code_left), resources2.getString(C1075R.string.guest_detail_temporary_code_right)), tempCode.localizeDate(tempCode.getRollsOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), tempCode.localizeDate(tempCode.getExpiresOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), lock.getLocalizedTimeZone()});
            }
            str2 = str4;
        } else {
            if (str5 == null) {
                str3 = String.format(resources2.getString(C1075R.string.guest_detail_send_temporary_body), new Object[]{productInvitationService2.mSharedPreferences.getUserFullName(), lock.getName(), lock.getKmsDeviceKey().getDeviceId(), tempCode.getServiceCode(), tempCode.localizeDate(tempCode.getExpiresOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), lock.getLocalizedTimeZone()});
            } else {
                str3 = String.format(resources2.getString(C1075R.string.guest_detail_send_future_temporary_body), new Object[]{productInvitationService2.mSharedPreferences.getUserFullName(), lock.getName(), lock.getKmsDeviceKey().getDeviceId(), tempCode.getServiceCode(), tempCode.localizeDate(tempCode.getRollsOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), tempCode.localizeDate(tempCode.getExpiresOn(), resources2.getString(C1075R.string.regional_full_date_format), lock.getTimezone()), lock.getLocalizedTimeZone()});
            }
            str2 = str3;
        }
        productInvitationService.shareCodeDefaultChooser(format, str2, null, null, C1075R.string.send_temp_code, resources);
        StringBuilder sb = new StringBuilder();
        sb.append(tempCode.getRollsOn());
        sb.append("|");
        sb.append(tempCode.getExpiresOn());
        subscriber.onNext(sb.toString());
        subscriber.onCompleted();
    }

    public Intent createShareApplicationIntent(String str, String str2, String str3, String str4, int i, Resources resources) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        if (str != null) {
            intent.putExtra("android.intent.extra.SUBJECT", str);
        }
        intent.putExtra("android.intent.extra.TEXT", str2);
        if (str4 != null) {
            intent.putExtra("address", str4);
        }
        if (str3 != null) {
            intent.putExtra("android.intent.extra.EMAIL", new String[]{str3});
        }
        if (str != null) {
            intent.putExtra("android.intent.extra.SUBJECT", str);
        }
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (ResolveInfo resolveInfo : MasterLockApp.get().getPackageManager().queryIntentActivities(intent, 65536)) {
            String str5 = resolveInfo.activityInfo.packageName;
            if (!str5.equals("com.facebook.katana") && !hashMap.containsKey(str5)) {
                hashMap.put(str5, resolveInfo);
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                intent2.putExtra("android.intent.extra.TEXT", str2);
                intent2.setPackage(str5.toLowerCase());
                arrayList.add(intent2);
            }
        }
        arrayList.remove(0);
        Intent intent3 = new Intent("android.intent.action.CHOOSER");
        intent3.setAction("android.intent.action.PICK_ACTIVITY");
        intent3.putExtra("android.intent.extra.TITLE", resources.getString(i));
        intent3.putExtra("android.intent.extra.INTENT", intent);
        intent3.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[0]));
        return intent3;
    }

    public void shareCodeDefaultChooser(String str, String str2, String str3, String str4, int i, Resources resources) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        if (str != null) {
            intent.putExtra("android.intent.extra.SUBJECT", str);
        }
        intent.putExtra("android.intent.extra.TEXT", str2);
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (ResolveInfo resolveInfo : MasterLockApp.get().getPackageManager().queryIntentActivities(intent, 65536)) {
            String str5 = resolveInfo.activityInfo.packageName;
            if (!str5.equals("com.facebook.katana") && !hashMap.containsKey(str5)) {
                hashMap.put(str5, resolveInfo);
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                if (str != null) {
                    intent2.putExtra("android.intent.extra.SUBJECT", str);
                }
                if (str3 != null) {
                    intent2.putExtra("android.intent.extra.EMAIL", new String[]{str3});
                }
                if (str4 != null) {
                    intent2.putExtra("address", str4);
                }
                intent2.putExtra("android.intent.extra.TEXT", str2);
                intent2.setPackage(str5);
                arrayList.add(intent2);
            }
        }
        Intent createChooser = Intent.createChooser((Intent) arrayList.remove(0), resources.getString(i));
        createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[0]));
        createChooser.setFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
        if (createChooser.resolveActivity(MasterLockApp.get().getPackageManager()) != null) {
            MasterLockApp.get().startActivity(createChooser);
        }
    }
}
