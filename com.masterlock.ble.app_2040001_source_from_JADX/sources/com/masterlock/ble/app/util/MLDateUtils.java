package com.masterlock.ble.app.util;

import android.content.res.Resources;
import android.util.Log;
import com.masterlock.ble.app.C1075R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class MLDateUtils {
    private static final String TAG = "MLDateUtils";

    private MLDateUtils() {
    }

    public static Date parseServerDate(String str) throws ParseException {
        DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("parseServerDate: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("parseServerDate: ");
        sb2.append(dateTimeParser.parseDateTime(str).toDate());
        Log.d(str3, sb2.toString());
        return dateTimeParser.parseDateTime(str).toDate();
    }

    public static String formatDateToUTC(Date date) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("formatDateToUTC: ");
        sb.append(date);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("formatDateToUTC: ");
        sb2.append(new DateTime(date.getTime(), DateTimeZone.UTC).toDateTimeISO().toString());
        Log.d(str2, sb2.toString());
        return new DateTime(date.getTime(), DateTimeZone.UTC).toDateTimeISO().toString();
    }

    public static String parseServerDateFormat(String str, Resources resources) throws ParseException {
        DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
        String string = resources.getString(C1075R.string.regional_full_date_format);
        String format = new SimpleDateFormat(string).format(dateTimeParser.parseDateTime(str).toDate());
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("parseServerDateFormat: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("parseServerDateFormat: ");
        sb2.append(string);
        Log.d(str3, sb2.toString());
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("parseServerDateFormat: ");
        sb3.append(format);
        Log.d(str4, sb3.toString());
        return format;
    }
}
