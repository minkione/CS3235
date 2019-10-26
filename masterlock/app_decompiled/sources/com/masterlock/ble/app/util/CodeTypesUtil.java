package com.masterlock.ble.app.util;

import android.util.Log;
import com.masterlock.api.entity.MasterBackupResponse;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.core.Lock;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.functions.Func1;

public class CodeTypesUtil {
    @Inject
    KMSDeviceService mKMSDeviceService;

    public CodeTypesUtil() {
        MasterLockApp.get().inject(this);
    }

    /* access modifiers changed from: private */
    public boolean isCodeValid(String str, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String substring = ((String) list.get(i)).length() > str.length() ? ((String) list.get(i)).substring(0, str.length()) : (String) list.get(i);
            if (str.startsWith(substring)) {
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("isCodeValid: ");
                sb.append(str);
                sb.append(" startsWith ");
                sb.append(substring);
                sb.append(" : ");
                sb.append(str.startsWith(substring));
                Log.d(simpleName, sb.toString());
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public List<String> createCodeBucket(String str, String str2, List<String> list) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str2);
        if (str != null) {
            arrayList.add(str);
        }
        for (String str3 : list) {
            if (!str3.trim().isEmpty()) {
                arrayList.add(str3);
            }
        }
        return arrayList;
    }

    public Observable<Boolean> validatePrimaryCode(final Lock lock, final String str) {
        return this.mKMSDeviceService.getMasterCode(lock).flatMap(new Func1<MasterBackupResponse, Observable<Boolean>>() {
            public Observable<Boolean> call(MasterBackupResponse masterBackupResponse) {
                return Observable.just(Boolean.valueOf(CodeTypesUtil.this.isCodeValid(str, CodeTypesUtil.this.createCodeBucket(null, masterBackupResponse.masterBackupCode, lock.getAllSecondaryCodes()))));
            }
        });
    }

    public Observable<Boolean> validateSecondaryCode(final Lock lock, final String str, final List<String> list) {
        return this.mKMSDeviceService.getMasterCode(lock).flatMap(new Func1<MasterBackupResponse, Observable<Boolean>>() {
            public Observable<Boolean> call(MasterBackupResponse masterBackupResponse) {
                return Observable.just(Boolean.valueOf(CodeTypesUtil.this.isCodeValid(str, CodeTypesUtil.this.createCodeBucket(lock.getPrimaryCode(), masterBackupResponse.masterBackupCode, list))));
            }
        });
    }
}
