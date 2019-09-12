package org.joda.time;

import java.io.Serializable;
import java.util.Comparator;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

public class DateTimeComparator implements Comparator<Object>, Serializable {
    private static final DateTimeComparator ALL_INSTANCE = new DateTimeComparator(null, null);
    private static final DateTimeComparator DATE_INSTANCE = new DateTimeComparator(DateTimeFieldType.dayOfYear(), null);
    private static final DateTimeComparator TIME_INSTANCE = new DateTimeComparator(null, DateTimeFieldType.dayOfYear());
    private static final long serialVersionUID = -6097339773320178364L;
    private final DateTimeFieldType iLowerLimit;
    private final DateTimeFieldType iUpperLimit;

    public static DateTimeComparator getInstance() {
        return ALL_INSTANCE;
    }

    public static DateTimeComparator getInstance(DateTimeFieldType dateTimeFieldType) {
        return getInstance(dateTimeFieldType, null);
    }

    public static DateTimeComparator getInstance(DateTimeFieldType dateTimeFieldType, DateTimeFieldType dateTimeFieldType2) {
        if (dateTimeFieldType == null && dateTimeFieldType2 == null) {
            return ALL_INSTANCE;
        }
        if (dateTimeFieldType == DateTimeFieldType.dayOfYear() && dateTimeFieldType2 == null) {
            return DATE_INSTANCE;
        }
        if (dateTimeFieldType == null && dateTimeFieldType2 == DateTimeFieldType.dayOfYear()) {
            return TIME_INSTANCE;
        }
        return new DateTimeComparator(dateTimeFieldType, dateTimeFieldType2);
    }

    public static DateTimeComparator getDateOnlyInstance() {
        return DATE_INSTANCE;
    }

    public static DateTimeComparator getTimeOnlyInstance() {
        return TIME_INSTANCE;
    }

    protected DateTimeComparator(DateTimeFieldType dateTimeFieldType, DateTimeFieldType dateTimeFieldType2) {
        this.iLowerLimit = dateTimeFieldType;
        this.iUpperLimit = dateTimeFieldType2;
    }

    public DateTimeFieldType getLowerLimit() {
        return this.iLowerLimit;
    }

    public DateTimeFieldType getUpperLimit() {
        return this.iUpperLimit;
    }

    public int compare(Object obj, Object obj2) {
        InstantConverter instantConverter = ConverterManager.getInstance().getInstantConverter(obj);
        Chronology chronology = null;
        Chronology chronology2 = instantConverter.getChronology(obj, chronology);
        long instantMillis = instantConverter.getInstantMillis(obj, chronology2);
        InstantConverter instantConverter2 = ConverterManager.getInstance().getInstantConverter(obj2);
        Chronology chronology3 = instantConverter2.getChronology(obj2, chronology);
        long instantMillis2 = instantConverter2.getInstantMillis(obj2, chronology3);
        DateTimeFieldType dateTimeFieldType = this.iLowerLimit;
        if (dateTimeFieldType != null) {
            instantMillis = dateTimeFieldType.getField(chronology2).roundFloor(instantMillis);
            instantMillis2 = this.iLowerLimit.getField(chronology3).roundFloor(instantMillis2);
        }
        DateTimeFieldType dateTimeFieldType2 = this.iUpperLimit;
        if (dateTimeFieldType2 != null) {
            instantMillis = dateTimeFieldType2.getField(chronology2).remainder(instantMillis);
            instantMillis2 = this.iUpperLimit.getField(chronology3).remainder(instantMillis2);
        }
        if (instantMillis < instantMillis2) {
            return -1;
        }
        return instantMillis > instantMillis2 ? 1 : 0;
    }

    private Object readResolve() {
        return getInstance(this.iLowerLimit, this.iUpperLimit);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0031, code lost:
        if (r0.equals(r4.getUpperLimit()) != false) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001b, code lost:
        if (r0.equals(r4.getLowerLimit()) != false) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            boolean r0 = r4 instanceof org.joda.time.DateTimeComparator
            r1 = 0
            if (r0 == 0) goto L_0x0035
            org.joda.time.DateTimeComparator r4 = (org.joda.time.DateTimeComparator) r4
            org.joda.time.DateTimeFieldType r0 = r3.iLowerLimit
            org.joda.time.DateTimeFieldType r2 = r4.getLowerLimit()
            if (r0 == r2) goto L_0x001d
            org.joda.time.DateTimeFieldType r0 = r3.iLowerLimit
            if (r0 == 0) goto L_0x0034
            org.joda.time.DateTimeFieldType r2 = r4.getLowerLimit()
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0034
        L_0x001d:
            org.joda.time.DateTimeFieldType r0 = r3.iUpperLimit
            org.joda.time.DateTimeFieldType r2 = r4.getUpperLimit()
            if (r0 == r2) goto L_0x0033
            org.joda.time.DateTimeFieldType r0 = r3.iUpperLimit
            if (r0 == 0) goto L_0x0034
            org.joda.time.DateTimeFieldType r4 = r4.getUpperLimit()
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0034
        L_0x0033:
            r1 = 1
        L_0x0034:
            return r1
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.joda.time.DateTimeComparator.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        DateTimeFieldType dateTimeFieldType = this.iLowerLimit;
        int i = 0;
        int hashCode = dateTimeFieldType == null ? 0 : dateTimeFieldType.hashCode();
        DateTimeFieldType dateTimeFieldType2 = this.iUpperLimit;
        if (dateTimeFieldType2 != null) {
            i = dateTimeFieldType2.hashCode();
        }
        return hashCode + (i * 123);
    }

    public String toString() {
        if (this.iLowerLimit == this.iUpperLimit) {
            StringBuilder sb = new StringBuilder();
            sb.append("DateTimeComparator[");
            DateTimeFieldType dateTimeFieldType = this.iLowerLimit;
            sb.append(dateTimeFieldType == null ? "" : dateTimeFieldType.getName());
            sb.append("]");
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("DateTimeComparator[");
        DateTimeFieldType dateTimeFieldType2 = this.iLowerLimit;
        sb2.append(dateTimeFieldType2 == null ? "" : dateTimeFieldType2.getName());
        sb2.append("-");
        DateTimeFieldType dateTimeFieldType3 = this.iUpperLimit;
        sb2.append(dateTimeFieldType3 == null ? "" : dateTimeFieldType3.getName());
        sb2.append("]");
        return sb2.toString();
    }
}
