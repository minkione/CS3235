package com.masterlock.core;

import java.util.ArrayList;
import java.util.List;

public enum LockCodeDirection {
    UNKNOWN(""),
    LEFT("L"),
    UP("U"),
    RIGHT("R"),
    DOWN("D");
    
    private final String value;

    private LockCodeDirection(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public static LockCodeDirection fromKey(String str) {
        LockCodeDirection[] values;
        for (LockCodeDirection lockCodeDirection : values()) {
            if (str.equals(lockCodeDirection.getValue())) {
                return lockCodeDirection;
            }
        }
        return UNKNOWN;
    }

    public static List<LockCodeDirection> generateLockDirectionListFromStringCode(String str) {
        ArrayList arrayList = new ArrayList();
        if (str == null) {
            return arrayList;
        }
        for (char valueOf : str.toCharArray()) {
            arrayList.add(fromKey(String.valueOf(valueOf)));
        }
        return arrayList;
    }
}
