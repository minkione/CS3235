package com.masterlock.core;

import java.util.ArrayList;
import java.util.List;

public class SecondaryCodesUtil {
    private static Lock mLock;
    private static List<String> mOriginalCodes = new ArrayList(5);

    public static void setUtilForLock(Lock lock) {
        updateOriginalCodes(lock);
    }

    public static void updateOriginalCodes(Lock lock) {
        mLock = lock;
        mOriginalCodes.add(0, lock.getSecondaryCode1() != null ? lock.getSecondaryCode1() : "");
        mOriginalCodes.add(1, lock.getSecondaryCode2() != null ? lock.getSecondaryCode2() : "");
        mOriginalCodes.add(2, lock.getSecondaryCode3() != null ? lock.getSecondaryCode3() : "");
        mOriginalCodes.add(3, lock.getSecondaryCode4() != null ? lock.getSecondaryCode4() : "");
        mOriginalCodes.add(4, lock.getSecondaryCode5() != null ? lock.getSecondaryCode5() : "");
    }

    public static OperationType getOperationTypeForCode(int i, String str) {
        try {
            String str2 = (String) mOriginalCodes.get(i);
            if (str2.isEmpty() && !str.isEmpty()) {
                return OperationType.CREATE;
            }
            if (!str2.isEmpty() && !str.isEmpty() && !str2.equals(str)) {
                return OperationType.UPDATE;
            }
            if (str2.isEmpty() || !str.isEmpty()) {
                return OperationType.UNSPECIFIED;
            }
            return OperationType.REMOVE;
        } catch (IndexOutOfBoundsException unused) {
            return OperationType.UNSPECIFIED;
        }
    }

    public static boolean hasPendingOperations(Lock lock) {
        for (int i = 0; i < 5; i++) {
            if (getOperationTypeForCode(i, lock.getSecondaryCodeAt(SecondaryCodeIndex.fromValue(i))) != OperationType.UNSPECIFIED) {
                return true;
            }
        }
        return false;
    }

    public static boolean isListEmpty(Lock lock) {
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            if (lock.getSecondaryCodeAt(SecondaryCodeIndex.fromValue(i2)).isEmpty()) {
                i++;
            }
        }
        return i >= 5;
    }

    public static boolean isOperationsListEmpty() {
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            if (mLock.getSecondaryCodeAt(SecondaryCodeIndex.fromValue(i2)).isEmpty()) {
                i++;
            }
        }
        return i >= 5;
    }
}
