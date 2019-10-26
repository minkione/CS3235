package org.joda.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.FormatUtils;
import org.joda.time.p011tz.DefaultNameProvider;
import org.joda.time.p011tz.FixedDateTimeZone;
import org.joda.time.p011tz.NameProvider;
import org.joda.time.p011tz.Provider;
import org.joda.time.p011tz.UTCProvider;
import org.joda.time.p011tz.ZoneInfoProvider;

public abstract class DateTimeZone implements Serializable {
    private static final int MAX_MILLIS = 86399999;
    public static final DateTimeZone UTC = UTCDateTimeZone.INSTANCE;
    private static Set<String> cAvailableIDs = null;
    private static volatile DateTimeZone cDefault = null;
    private static NameProvider cNameProvider = null;
    private static DateTimeFormatter cOffsetFormatter = null;
    private static Provider cProvider = null;
    private static Map<String, String> cZoneIdConversion = null;
    private static Map<String, SoftReference<DateTimeZone>> iFixedOffsetCache = null;
    private static final long serialVersionUID = 5546345482340108586L;
    private final String iID;

    private static final class Stub implements Serializable {
        private static final long serialVersionUID = -6471952376487863581L;
        private transient String iID;

        Stub(String str) {
            this.iID = str;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeUTF(this.iID);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException {
            this.iID = objectInputStream.readUTF();
        }

        private Object readResolve() throws ObjectStreamException {
            return DateTimeZone.forID(this.iID);
        }
    }

    public abstract boolean equals(Object obj);

    public abstract String getNameKey(long j);

    public abstract int getOffset(long j);

    public abstract int getStandardOffset(long j);

    public abstract boolean isFixed();

    public abstract long nextTransition(long j);

    public abstract long previousTransition(long j);

    static {
        setProvider0(null);
        setNameProvider0(null);
    }

    public static DateTimeZone getDefault() {
        DateTimeZone dateTimeZone = cDefault;
        if (dateTimeZone == null) {
            synchronized (DateTimeZone.class) {
                dateTimeZone = cDefault;
                if (dateTimeZone == null) {
                    dateTimeZone = null;
                    try {
                        String property = System.getProperty("user.timezone");
                        if (property != null) {
                            dateTimeZone = forID(property);
                        }
                    } catch (RuntimeException unused) {
                    }
                    if (dateTimeZone == null) {
                        try {
                            dateTimeZone = forTimeZone(TimeZone.getDefault());
                        } catch (IllegalArgumentException unused2) {
                        }
                    }
                    if (dateTimeZone == null) {
                        dateTimeZone = UTC;
                    }
                    cDefault = dateTimeZone;
                }
            }
        }
        return dateTimeZone;
    }

    public static void setDefault(DateTimeZone dateTimeZone) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setDefault"));
        }
        if (dateTimeZone != null) {
            synchronized (DateTimeZone.class) {
                cDefault = dateTimeZone;
            }
            return;
        }
        throw new IllegalArgumentException("The datetime zone must not be null");
    }

    @FromString
    public static DateTimeZone forID(String str) {
        if (str == null) {
            return getDefault();
        }
        if (str.equals("UTC")) {
            return UTC;
        }
        DateTimeZone zone = cProvider.getZone(str);
        if (zone != null) {
            return zone;
        }
        if (str.startsWith("+") || str.startsWith("-")) {
            int parseOffset = parseOffset(str);
            if (((long) parseOffset) == 0) {
                return UTC;
            }
            return fixedOffsetZone(printOffset(parseOffset), parseOffset);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The datetime zone id '");
        sb.append(str);
        sb.append("' is not recognised");
        throw new IllegalArgumentException(sb.toString());
    }

    public static DateTimeZone forOffsetHours(int i) throws IllegalArgumentException {
        return forOffsetHoursMinutes(i, 0);
    }

    public static DateTimeZone forOffsetHoursMinutes(int i, int i2) throws IllegalArgumentException {
        int i3;
        if (i == 0 && i2 == 0) {
            return UTC;
        }
        if (i < -23 || i > 23) {
            StringBuilder sb = new StringBuilder();
            sb.append("Hours out of range: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 < -59 || i2 > 59) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Minutes out of range: ");
            sb2.append(i2);
            throw new IllegalArgumentException(sb2.toString());
        } else if (i <= 0 || i2 >= 0) {
            int i4 = i * 60;
            if (i4 < 0) {
                try {
                    i3 = i4 - Math.abs(i2);
                } catch (ArithmeticException unused) {
                    throw new IllegalArgumentException("Offset is too large");
                }
            } else {
                i3 = i4 + i2;
            }
            return forOffsetMillis(FieldUtils.safeMultiply(i3, 60000));
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Positive hours must not have negative minutes: ");
            sb3.append(i2);
            throw new IllegalArgumentException(sb3.toString());
        }
    }

    public static DateTimeZone forOffsetMillis(int i) {
        if (i >= -86399999 && i <= MAX_MILLIS) {
            return fixedOffsetZone(printOffset(i), i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Millis out of range: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public static DateTimeZone forTimeZone(TimeZone timeZone) {
        if (timeZone == null) {
            return getDefault();
        }
        String id = timeZone.getID();
        if (id == null) {
            throw new IllegalArgumentException("The TimeZone id must not be null");
        } else if (id.equals("UTC")) {
            return UTC;
        } else {
            DateTimeZone dateTimeZone = null;
            String convertedId = getConvertedId(id);
            if (convertedId != null) {
                dateTimeZone = cProvider.getZone(convertedId);
            }
            if (dateTimeZone == null) {
                dateTimeZone = cProvider.getZone(id);
            }
            if (dateTimeZone != null) {
                return dateTimeZone;
            }
            if (convertedId != null || (!id.startsWith("GMT+") && !id.startsWith("GMT-"))) {
                StringBuilder sb = new StringBuilder();
                sb.append("The datetime zone id '");
                sb.append(id);
                sb.append("' is not recognised");
                throw new IllegalArgumentException(sb.toString());
            }
            int parseOffset = parseOffset(id.substring(3));
            if (((long) parseOffset) == 0) {
                return UTC;
            }
            return fixedOffsetZone(printOffset(parseOffset), parseOffset);
        }
    }

    private static synchronized DateTimeZone fixedOffsetZone(String str, int i) {
        synchronized (DateTimeZone.class) {
            if (i == 0) {
                DateTimeZone dateTimeZone = UTC;
                return dateTimeZone;
            }
            if (iFixedOffsetCache == null) {
                iFixedOffsetCache = new HashMap();
            }
            Reference reference = (Reference) iFixedOffsetCache.get(str);
            if (reference != null) {
                DateTimeZone dateTimeZone2 = (DateTimeZone) reference.get();
                if (dateTimeZone2 != null) {
                    return dateTimeZone2;
                }
            }
            FixedDateTimeZone fixedDateTimeZone = new FixedDateTimeZone(str, null, i, i);
            iFixedOffsetCache.put(str, new SoftReference(fixedDateTimeZone));
            return fixedDateTimeZone;
        }
    }

    public static Set<String> getAvailableIDs() {
        return cAvailableIDs;
    }

    public static Provider getProvider() {
        return cProvider;
    }

    public static void setProvider(Provider provider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setProvider"));
        }
        setProvider0(provider);
    }

    private static void setProvider0(Provider provider) {
        if (provider == null) {
            provider = getDefaultProvider();
        }
        Set<String> availableIDs = provider.getAvailableIDs();
        if (availableIDs == null || availableIDs.size() == 0) {
            throw new IllegalArgumentException("The provider doesn't have any available ids");
        } else if (!availableIDs.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        } else if (UTC.equals(provider.getZone("UTC"))) {
            cProvider = provider;
            cAvailableIDs = availableIDs;
        } else {
            throw new IllegalArgumentException("Invalid UTC zone provided");
        }
    }

    private static Provider getDefaultProvider() {
        Provider provider = null;
        try {
            String property = System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (property != null) {
                provider = (Provider) Class.forName(property).newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (SecurityException unused) {
        }
        if (provider == null) {
            try {
                provider = new ZoneInfoProvider("org/joda/time/tz/data");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return provider == null ? new UTCProvider() : provider;
    }

    public static NameProvider getNameProvider() {
        return cNameProvider;
    }

    public static void setNameProvider(NameProvider nameProvider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setNameProvider"));
        }
        setNameProvider0(nameProvider);
    }

    private static void setNameProvider0(NameProvider nameProvider) {
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider = nameProvider;
    }

    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider = null;
        try {
            String property = System.getProperty("org.joda.time.DateTimeZone.NameProvider");
            if (property != null) {
                nameProvider = (NameProvider) Class.forName(property).newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (SecurityException unused) {
        }
        return nameProvider == null ? new DefaultNameProvider() : nameProvider;
    }

    private static synchronized String getConvertedId(String str) {
        String str2;
        synchronized (DateTimeZone.class) {
            Map map = cZoneIdConversion;
            if (map == null) {
                map = new HashMap();
                map.put("GMT", "UTC");
                map.put("WET", "WET");
                map.put("CET", "CET");
                map.put("MET", "CET");
                map.put("ECT", "CET");
                map.put("EET", "EET");
                map.put("MIT", "Pacific/Apia");
                map.put("HST", "Pacific/Honolulu");
                map.put("AST", "America/Anchorage");
                map.put("PST", "America/Los_Angeles");
                map.put("MST", "America/Denver");
                map.put("PNT", "America/Phoenix");
                map.put("CST", "America/Chicago");
                map.put("EST", "America/New_York");
                map.put("IET", "America/Indiana/Indianapolis");
                map.put("PRT", "America/Puerto_Rico");
                map.put("CNT", "America/St_Johns");
                map.put("AGT", "America/Argentina/Buenos_Aires");
                map.put("BET", "America/Sao_Paulo");
                map.put("ART", "Africa/Cairo");
                map.put("CAT", "Africa/Harare");
                map.put("EAT", "Africa/Addis_Ababa");
                map.put("NET", "Asia/Yerevan");
                map.put("PLT", "Asia/Karachi");
                map.put("IST", "Asia/Kolkata");
                map.put("BST", "Asia/Dhaka");
                map.put("VST", "Asia/Ho_Chi_Minh");
                map.put("CTT", "Asia/Shanghai");
                map.put("JST", "Asia/Tokyo");
                map.put("ACT", "Australia/Darwin");
                map.put("AET", "Australia/Sydney");
                map.put("SST", "Pacific/Guadalcanal");
                map.put("NST", "Pacific/Auckland");
                cZoneIdConversion = map;
            }
            str2 = (String) map.get(str);
        }
        return str2;
    }

    private static int parseOffset(String str) {
        return -((int) offsetFormatter().withChronology(new BaseChronology() {
            private static final long serialVersionUID = -3128740902654445468L;

            public DateTimeZone getZone() {
                return null;
            }

            public Chronology withUTC() {
                return this;
            }

            public Chronology withZone(DateTimeZone dateTimeZone) {
                return this;
            }

            public String toString() {
                return getClass().getName();
            }
        }).parseMillis(str));
    }

    private static String printOffset(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i >= 0) {
            stringBuffer.append('+');
        } else {
            stringBuffer.append('-');
            i = -i;
        }
        int i2 = i / DateTimeConstants.MILLIS_PER_HOUR;
        FormatUtils.appendPaddedInteger(stringBuffer, i2, 2);
        int i3 = i - (i2 * DateTimeConstants.MILLIS_PER_HOUR);
        int i4 = i3 / 60000;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger(stringBuffer, i4, 2);
        int i5 = i3 - (i4 * 60000);
        if (i5 == 0) {
            return stringBuffer.toString();
        }
        int i6 = i5 / 1000;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger(stringBuffer, i6, 2);
        int i7 = i5 - (i6 * 1000);
        if (i7 == 0) {
            return stringBuffer.toString();
        }
        stringBuffer.append('.');
        FormatUtils.appendPaddedInteger(stringBuffer, i7, 3);
        return stringBuffer.toString();
    }

    private static synchronized DateTimeFormatter offsetFormatter() {
        DateTimeFormatter dateTimeFormatter;
        synchronized (DateTimeZone.class) {
            if (cOffsetFormatter == null) {
                cOffsetFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset(null, true, 2, 4).toFormatter();
            }
            dateTimeFormatter = cOffsetFormatter;
        }
        return dateTimeFormatter;
    }

    protected DateTimeZone(String str) {
        if (str != null) {
            this.iID = str;
            return;
        }
        throw new IllegalArgumentException("Id must not be null");
    }

    @ToString
    public final String getID() {
        return this.iID;
    }

    public final String getShortName(long j) {
        return getShortName(j, null);
    }

    public String getShortName(long j, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(j);
        if (nameKey == null) {
            return this.iID;
        }
        String shortName = cNameProvider.getShortName(locale, this.iID, nameKey);
        if (shortName != null) {
            return shortName;
        }
        return printOffset(getOffset(j));
    }

    public final String getName(long j) {
        return getName(j, null);
    }

    public String getName(long j, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(j);
        if (nameKey == null) {
            return this.iID;
        }
        String name = cNameProvider.getName(locale, this.iID, nameKey);
        if (name != null) {
            return name;
        }
        return printOffset(getOffset(j));
    }

    public final int getOffset(ReadableInstant readableInstant) {
        if (readableInstant == null) {
            return getOffset(DateTimeUtils.currentTimeMillis());
        }
        return getOffset(readableInstant.getMillis());
    }

    public boolean isStandardOffset(long j) {
        return getOffset(j) == getStandardOffset(j);
    }

    public int getOffsetFromLocal(long j) {
        int offset = getOffset(j);
        long j2 = j - ((long) offset);
        int offset2 = getOffset(j2);
        if (offset != offset2) {
            if (offset - offset2 < 0) {
                long nextTransition = nextTransition(j2);
                if (nextTransition == j2) {
                    nextTransition = Long.MAX_VALUE;
                }
                long j3 = j - ((long) offset2);
                long nextTransition2 = nextTransition(j3);
                if (nextTransition2 == j3) {
                    nextTransition2 = Long.MAX_VALUE;
                }
                if (nextTransition != nextTransition2) {
                    return offset;
                }
            }
        } else if (offset >= 0) {
            long previousTransition = previousTransition(j2);
            if (previousTransition < j2) {
                int offset3 = getOffset(previousTransition);
                if (j2 - previousTransition <= ((long) (offset3 - offset))) {
                    return offset3;
                }
            }
        }
        return offset2;
    }

    public long convertUTCToLocal(long j) {
        long offset = (long) getOffset(j);
        long j2 = j + offset;
        if ((j ^ j2) >= 0 || (j ^ offset) < 0) {
            return j2;
        }
        throw new ArithmeticException("Adding time zone offset caused overflow");
    }

    public long convertLocalToUTC(long j, boolean z, long j2) {
        int offset = getOffset(j2);
        long j3 = j - ((long) offset);
        if (getOffset(j3) == offset) {
            return j3;
        }
        return convertLocalToUTC(j, z);
    }

    public long convertLocalToUTC(long j, boolean z) {
        long j2;
        int offset = getOffset(j);
        long j3 = j - ((long) offset);
        int offset2 = getOffset(j3);
        if (offset != offset2 && (z || offset < 0)) {
            long nextTransition = nextTransition(j3);
            long j4 = Long.MAX_VALUE;
            if (nextTransition == j3) {
                nextTransition = Long.MAX_VALUE;
            }
            long j5 = j - ((long) offset2);
            long nextTransition2 = nextTransition(j5);
            if (nextTransition2 != j5) {
                j4 = nextTransition2;
            }
            if (nextTransition != j4) {
                if (z) {
                    throw new IllegalInstantException(j, getID());
                }
                long j6 = (long) offset;
                j2 = j - j6;
                if ((j ^ j2) < 0 || (j ^ j6) >= 0) {
                    return j2;
                }
                throw new ArithmeticException("Subtracting time zone offset caused overflow");
            }
        }
        offset = offset2;
        long j62 = (long) offset;
        j2 = j - j62;
        if ((j ^ j2) < 0) {
        }
        return j2;
    }

    public long getMillisKeepLocal(DateTimeZone dateTimeZone, long j) {
        DateTimeZone dateTimeZone2 = dateTimeZone == null ? getDefault() : dateTimeZone;
        if (dateTimeZone2 == this) {
            return j;
        }
        return dateTimeZone2.convertLocalToUTC(convertUTCToLocal(j), false, j);
    }

    public boolean isLocalDateTimeGap(LocalDateTime localDateTime) {
        if (isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime(this);
            return false;
        } catch (IllegalInstantException unused) {
            return true;
        }
    }

    public long adjustOffset(long j, boolean z) {
        long j2 = j - 10800000;
        long offset = (long) getOffset(j2);
        long offset2 = (long) getOffset(10800000 + j);
        if (offset <= offset2) {
            return j;
        }
        long j3 = offset - offset2;
        long nextTransition = nextTransition(j2);
        long j4 = nextTransition - j3;
        long j5 = nextTransition + j3;
        if (j < j4 || j >= j5) {
            return j;
        }
        if (j - j4 >= j3) {
            if (!z) {
                j -= j3;
            }
            return j;
        }
        if (z) {
            j += j3;
        }
        return j;
    }

    public TimeZone toTimeZone() {
        return TimeZone.getTimeZone(this.iID);
    }

    public int hashCode() {
        return getID().hashCode() + 57;
    }

    public String toString() {
        return getID();
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        return new Stub(this.iID);
    }
}
