package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import p008io.fabric.sdk.android.services.events.EventsFilesManager;

@GwtCompatible
public enum CaseFormat {
    LOWER_HYPHEN(CharMatcher.m33is('-'), "-") {
        /* access modifiers changed from: 0000 */
        public String normalizeWord(String str) {
            return Ascii.toLowerCase(str);
        }

        /* access modifiers changed from: 0000 */
        public String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_UNDERSCORE) {
                return str.replace('-', '_');
            }
            if (caseFormat == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(str.replace('-', '_'));
            }
            return CaseFormat.super.convert(caseFormat, str);
        }
    },
    LOWER_UNDERSCORE(CharMatcher.m33is('_'), EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR) {
        /* access modifiers changed from: 0000 */
        public String normalizeWord(String str) {
            return Ascii.toLowerCase(str);
        }

        /* access modifiers changed from: 0000 */
        public String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_HYPHEN) {
                return str.replace('_', '-');
            }
            if (caseFormat == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(str);
            }
            return CaseFormat.super.convert(caseFormat, str);
        }
    },
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        /* access modifiers changed from: 0000 */
        public String normalizeWord(String str) {
            return CaseFormat.firstCharOnlyToUpper(str);
        }
    },
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        /* access modifiers changed from: 0000 */
        public String normalizeWord(String str) {
            return CaseFormat.firstCharOnlyToUpper(str);
        }
    },
    UPPER_UNDERSCORE(CharMatcher.m33is('_'), EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR) {
        /* access modifiers changed from: 0000 */
        public String normalizeWord(String str) {
            return Ascii.toUpperCase(str);
        }

        /* access modifiers changed from: 0000 */
        public String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_HYPHEN) {
                return Ascii.toLowerCase(str.replace('_', '-'));
            }
            if (caseFormat == LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(str);
            }
            return CaseFormat.super.convert(caseFormat, str);
        }
    };
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    /* access modifiers changed from: 0000 */
    public abstract String normalizeWord(String str);

    private CaseFormat(CharMatcher charMatcher, String str) {
        this.wordBoundary = charMatcher;
        this.wordSeparator = str;
    }

    /* renamed from: to */
    public final String mo12182to(CaseFormat caseFormat, String str) {
        Preconditions.checkNotNull(caseFormat);
        Preconditions.checkNotNull(str);
        return caseFormat == this ? str : convert(caseFormat, str);
    }

    /* access modifiers changed from: 0000 */
    public String convert(CaseFormat caseFormat, String str) {
        int i = 0;
        StringBuilder sb = null;
        int i2 = -1;
        while (true) {
            i2 = this.wordBoundary.indexIn(str, i2 + 1);
            if (i2 == -1) {
                break;
            }
            if (i == 0) {
                sb = new StringBuilder(str.length() + (this.wordSeparator.length() * 4));
                sb.append(caseFormat.normalizeFirstWord(str.substring(i, i2)));
            } else {
                sb.append(caseFormat.normalizeWord(str.substring(i, i2)));
            }
            sb.append(caseFormat.wordSeparator);
            i = this.wordSeparator.length() + i2;
        }
        if (i == 0) {
            return caseFormat.normalizeFirstWord(str);
        }
        sb.append(caseFormat.normalizeWord(str.substring(i)));
        return sb.toString();
    }

    private String normalizeFirstWord(String str) {
        return this == LOWER_CAMEL ? Ascii.toLowerCase(str) : normalizeWord(str);
    }

    /* access modifiers changed from: private */
    public static String firstCharOnlyToUpper(String str) {
        if (str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Ascii.toUpperCase(str.charAt(0)));
        sb.append(Ascii.toLowerCase(str.substring(1)));
        return sb.toString();
    }
}
