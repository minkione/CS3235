package com.crashlytics.android.core;

import android.content.Context;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.services.common.CommonUtils;

final class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static void writeStackTraceIfNotNull(Throwable th, OutputStream outputStream) {
        if (outputStream != null) {
            writeStackTrace(th, outputStream);
        }
    }

    public static void writeStackTrace(Context context, Throwable th, String str) {
        PrintWriter printWriter = null;
        try {
            PrintWriter printWriter2 = new PrintWriter(context.openFileOutput(str, 0));
            try {
                writeStackTrace(th, (Writer) printWriter2);
                CommonUtils.closeOrLog(printWriter2, "Failed to close stack trace writer.");
            } catch (Exception e) {
                e = e;
                printWriter = printWriter2;
                try {
                    Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Failed to create PrintWriter", e);
                    CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                } catch (Throwable th2) {
                    th = th2;
                    CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                printWriter = printWriter2;
                CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Failed to create PrintWriter", e);
            CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
        }
    }

    private static void writeStackTrace(Throwable th, OutputStream outputStream) {
        PrintWriter printWriter = null;
        try {
            PrintWriter printWriter2 = new PrintWriter(outputStream);
            try {
                writeStackTrace(th, (Writer) printWriter2);
                CommonUtils.closeOrLog(printWriter2, "Failed to close stack trace writer.");
            } catch (Exception e) {
                e = e;
                printWriter = printWriter2;
                try {
                    Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Failed to create PrintWriter", e);
                    CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                } catch (Throwable th2) {
                    th = th2;
                    CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                printWriter = printWriter2;
                CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Failed to create PrintWriter", e);
            CommonUtils.closeOrLog(printWriter, "Failed to close stack trace writer.");
        }
    }

    private static void writeStackTrace(Throwable th, Writer writer) {
        StackTraceElement[] stackTrace;
        boolean z = true;
        while (th != null) {
            try {
                String message = getMessage(th);
                if (message == null) {
                    message = "";
                }
                String str = z ? "" : "Caused by: ";
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(th.getClass().getName());
                sb.append(": ");
                sb.append(message);
                sb.append("\n");
                writer.write(sb.toString());
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("\tat ");
                    sb2.append(stackTraceElement.toString());
                    sb2.append("\n");
                    writer.write(sb2.toString());
                }
                th = th.getCause();
                z = false;
            } catch (Exception e) {
                Fabric.getLogger().mo21796e(CrashlyticsCore.TAG, "Could not write stack trace", e);
                return;
            }
        }
    }

    private static String getMessage(Throwable th) {
        String localizedMessage = th.getLocalizedMessage();
        if (localizedMessage == null) {
            return null;
        }
        return localizedMessage.replaceAll("(\r\n|\n|\f)", " ");
    }
}
