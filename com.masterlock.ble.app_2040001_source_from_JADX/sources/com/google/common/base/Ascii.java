package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Ascii {
    public static final byte ACK = 6;
    public static final byte BEL = 7;

    /* renamed from: BS */
    public static final byte f80BS = 8;
    public static final byte CAN = 24;

    /* renamed from: CR */
    public static final byte f81CR = 13;
    public static final byte DC1 = 17;
    public static final byte DC2 = 18;
    public static final byte DC3 = 19;
    public static final byte DC4 = 20;
    public static final byte DEL = Byte.MAX_VALUE;
    public static final byte DLE = 16;

    /* renamed from: EM */
    public static final byte f82EM = 25;
    public static final byte ENQ = 5;
    public static final byte EOT = 4;
    public static final byte ESC = 27;
    public static final byte ETB = 23;
    public static final byte ETX = 3;

    /* renamed from: FF */
    public static final byte f83FF = 12;

    /* renamed from: FS */
    public static final byte f84FS = 28;

    /* renamed from: GS */
    public static final byte f85GS = 29;

    /* renamed from: HT */
    public static final byte f86HT = 9;

    /* renamed from: LF */
    public static final byte f87LF = 10;
    public static final char MAX = '';
    public static final char MIN = '\u0000';
    public static final byte NAK = 21;

    /* renamed from: NL */
    public static final byte f88NL = 10;
    public static final byte NUL = 0;

    /* renamed from: RS */
    public static final byte f89RS = 30;

    /* renamed from: SI */
    public static final byte f90SI = 15;

    /* renamed from: SO */
    public static final byte f91SO = 14;
    public static final byte SOH = 1;

    /* renamed from: SP */
    public static final byte f92SP = 32;
    public static final byte SPACE = 32;
    public static final byte STX = 2;
    public static final byte SUB = 26;
    public static final byte SYN = 22;

    /* renamed from: US */
    public static final byte f93US = 31;

    /* renamed from: VT */
    public static final byte f94VT = 11;
    public static final byte XOFF = 19;
    public static final byte XON = 17;

    public static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private Ascii() {
    }

    public static String toLowerCase(String str) {
        return toLowerCase((CharSequence) str);
    }

    public static String toLowerCase(CharSequence charSequence) {
        int length = charSequence.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(toLowerCase(charSequence.charAt(i)));
        }
        return sb.toString();
    }

    public static char toLowerCase(char c) {
        return isUpperCase(c) ? (char) (c ^ ' ') : c;
    }

    public static String toUpperCase(String str) {
        return toUpperCase((CharSequence) str);
    }

    public static String toUpperCase(CharSequence charSequence) {
        int length = charSequence.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(toUpperCase(charSequence.charAt(i)));
        }
        return sb.toString();
    }

    public static char toUpperCase(char c) {
        return isLowerCase(c) ? (char) (c & '_') : c;
    }
}
