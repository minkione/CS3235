package android.support.p000v4.graphics;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

@RequiresApi(21)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatApi21Impl */
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor parcelFileDescriptor) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        } catch (ErrnoException unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0058, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0059, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0063, code lost:
        if (r7 != null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0058 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0018] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r5, android.os.CancellationSignal r6, @android.support.annotation.NonNull android.support.p000v4.provider.FontsContractCompat.FontInfo[] r7, int r8) {
        /*
            r4 = this;
            int r0 = r7.length
            r1 = 0
            r2 = 1
            if (r0 >= r2) goto L_0x0006
            return r1
        L_0x0006:
            android.support.v4.provider.FontsContractCompat$FontInfo r7 = r4.findBestInfo(r7, r8)
            android.content.ContentResolver r8 = r5.getContentResolver()
            android.net.Uri r7 = r7.getUri()     // Catch:{ IOException -> 0x006d }
            java.lang.String r0 = "r"
            android.os.ParcelFileDescriptor r6 = r8.openFileDescriptor(r7, r0, r6)     // Catch:{ IOException -> 0x006d }
            java.io.File r7 = r4.getFile(r6)     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            if (r7 == 0) goto L_0x002f
            boolean r8 = r7.canRead()     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            if (r8 != 0) goto L_0x0025
            goto L_0x002f
        L_0x0025:
            android.graphics.Typeface r5 = android.graphics.Typeface.createFromFile(r7)     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            if (r6 == 0) goto L_0x002e
            r6.close()     // Catch:{ IOException -> 0x006d }
        L_0x002e:
            return r5
        L_0x002f:
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            java.io.FileDescriptor r8 = r6.getFileDescriptor()     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            r7.<init>(r8)     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            android.graphics.Typeface r5 = super.createFromInputStream(r5, r7)     // Catch:{ Throwable -> 0x0048, all -> 0x0045 }
            r7.close()     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
            if (r6 == 0) goto L_0x0044
            r6.close()     // Catch:{ IOException -> 0x006d }
        L_0x0044:
            return r5
        L_0x0045:
            r5 = move-exception
            r8 = r1
            goto L_0x004e
        L_0x0048:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x004a }
        L_0x004a:
            r8 = move-exception
            r3 = r8
            r8 = r5
            r5 = r3
        L_0x004e:
            if (r8 == 0) goto L_0x0054
            r7.close()     // Catch:{ Throwable -> 0x0057, all -> 0x0058 }
            goto L_0x0057
        L_0x0054:
            r7.close()     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
        L_0x0057:
            throw r5     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
        L_0x0058:
            r5 = move-exception
            r7 = r1
            goto L_0x0061
        L_0x005b:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x005d }
        L_0x005d:
            r7 = move-exception
            r3 = r7
            r7 = r5
            r5 = r3
        L_0x0061:
            if (r6 == 0) goto L_0x006c
            if (r7 == 0) goto L_0x0069
            r6.close()     // Catch:{ Throwable -> 0x006c }
            goto L_0x006c
        L_0x0069:
            r6.close()     // Catch:{ IOException -> 0x006d }
        L_0x006c:
            throw r5     // Catch:{ IOException -> 0x006d }
        L_0x006d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
