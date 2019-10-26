package com.masterlock.core;

public enum CodeType {
    SECONDARY_CODE(5);
    
    int mValue;

    private CodeType(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static CodeType fromValue(int i) {
        CodeType[] values;
        for (CodeType codeType : values()) {
            if (codeType.getValue() == i) {
                return codeType;
            }
        }
        return null;
    }
}
