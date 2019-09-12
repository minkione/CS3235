package com.masterlock.ble.app.util;

import android.content.Context;
import android.util.Log;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.FirmwareUpdate.Builder;
import com.masterlock.core.Lock;
import com.masterlock.core.LockMode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    private static final String FIRMWARE_FILE_EXT = ".fu";
    private static final String LOG_TAG = "FILE UTIL";
    private static final String PENDING_FU_CONFIRMATION_FILE_EXT = ".pc";
    private static final String PENDING_RESTORE_FILE_EXT = ".pr";
    private static FileUtil instance;
    private final Context mContext = MasterLockApp.get().getApplicationContext();

    private FileUtil() {
    }

    public static FileUtil getInstance() {
        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    public boolean existsFirmwareUpdateFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Checking. File name: ");
        sb.append(str);
        sb.append(FIRMWARE_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(FIRMWARE_FILE_EXT);
        boolean exists = new File(filesDir, sb2.toString()).exists();
        String str3 = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("File exists: ");
        sb3.append(exists);
        Log.d(str3, sb3.toString());
        return exists;
    }

    public void saveFirmwareUpdateToFile(FirmwareUpdate firmwareUpdate, String str) {
        String str2 = LOG_TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Saving. File name: ");
            sb.append(str);
            sb.append(FIRMWARE_FILE_EXT);
            Log.d(str2, sb.toString());
            File filesDir = this.mContext.getApplicationContext().getFilesDir();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(str);
            sb2.append(FIRMWARE_FILE_EXT);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filesDir, sb2.toString())));
            if (firmwareUpdate.kmsDeviceId == null) {
                bufferedWriter.write("\u0000\n");
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Writing kmsDeviceId: ");
                sb3.append(firmwareUpdate.kmsDeviceId);
                Log.d(str3, sb3.toString());
            } else {
                bufferedWriter.write(firmwareUpdate.kmsDeviceId);
                bufferedWriter.write("\n");
                String str4 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Writing kmsDeviceId: ");
                sb4.append(firmwareUpdate.kmsDeviceId);
                Log.d(str4, sb4.toString());
            }
            if (firmwareUpdate.kMSReferenceHandler == null) {
                bufferedWriter.write("\u0000\n");
                String str5 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Writing kmsReferenceHandler: ");
                sb5.append(firmwareUpdate.kMSReferenceHandler);
                Log.d(str5, sb5.toString());
            } else {
                bufferedWriter.write(firmwareUpdate.kMSReferenceHandler);
                bufferedWriter.write("\n");
                String str6 = LOG_TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Writing kmsReferenceHandler: ");
                sb6.append(firmwareUpdate.kMSReferenceHandler);
                Log.d(str6, sb6.toString());
            }
            for (Map map : firmwareUpdate.commands) {
                bufferedWriter.write((String) map.get("Command"));
                bufferedWriter.write("\n");
                String str7 = LOG_TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Writing command: ");
                sb7.append((String) map.get("Command"));
                Log.d(str7, sb7.toString());
            }
            bufferedWriter.close();
            Log.d(LOG_TAG, "File closed.");
        } catch (Exception e) {
            String str8 = LOG_TAG;
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Error on writing Firmware Update to file: ");
            sb8.append(e);
            Log.e(str8, sb8.toString());
        }
    }

    public FirmwareUpdate readFirmwareUpdateFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Reading. File name: ");
        sb.append(str);
        sb.append(FIRMWARE_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(FIRMWARE_FILE_EXT);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filesDir, sb2.toString())));
            Builder builder = new Builder();
            builder.kmsDeviceId(bufferedReader.readLine());
            builder.kMSReferenceHandler(bufferedReader.readLine());
            ArrayList arrayList = new ArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Read line: ");
                    sb3.append(readLine);
                    Log.d(str3, sb3.toString());
                    HashMap hashMap = new HashMap();
                    hashMap.put("Command", readLine);
                    arrayList.add(hashMap);
                } else {
                    builder.commands(arrayList);
                    bufferedReader.close();
                    Log.d(LOG_TAG, "Closing file.");
                    return builder.build();
                }
            }
        } catch (Exception e) {
            String str4 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Error on reading Firmware Update file: ");
            sb4.append(e);
            Log.e(str4, sb4.toString());
            return null;
        }
    }

    public void deleteFirmwareUpdateFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Deleting. File name: ");
        sb.append(str);
        sb.append(FIRMWARE_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(FIRMWARE_FILE_EXT);
        if (new File(filesDir, sb2.toString()).delete()) {
            Log.d(LOG_TAG, "Firmware Update file deleted.");
        } else {
            Log.d(LOG_TAG, "Error deleting Firmware Update file.");
        }
    }

    public boolean existsPendingConfirmationFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Checking. File name: ");
        sb.append(str);
        sb.append(PENDING_FU_CONFIRMATION_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(PENDING_FU_CONFIRMATION_FILE_EXT);
        boolean exists = new File(filesDir, sb2.toString()).exists();
        String str3 = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("File exists: ");
        sb3.append(exists);
        Log.d(str3, sb3.toString());
        return exists;
    }

    public void savePendingConfirmationFile(String str) {
        String str2 = LOG_TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Saving. File name: ");
            sb.append(str);
            sb.append(PENDING_FU_CONFIRMATION_FILE_EXT);
            Log.d(str2, sb.toString());
            File filesDir = this.mContext.getApplicationContext().getFilesDir();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(str);
            sb2.append(PENDING_FU_CONFIRMATION_FILE_EXT);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filesDir, sb2.toString())));
            bufferedWriter.write(str);
            bufferedWriter.close();
            Log.d(LOG_TAG, "File closed.");
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Error on writing confirmation to file: ");
            sb3.append(e);
            Log.e(str3, sb3.toString());
        }
    }

    public void deletePendingConfirmationFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Deleting. File name: ");
        sb.append(str);
        sb.append(PENDING_FU_CONFIRMATION_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(PENDING_FU_CONFIRMATION_FILE_EXT);
        if (new File(filesDir, sb2.toString()).delete()) {
            Log.d(LOG_TAG, "Pending Confirmation file deleted.");
        } else {
            Log.d(LOG_TAG, "Error deleting Pending Confirmation file.");
        }
    }

    public boolean existsRestoreConfigFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Checking. File name: ");
        sb.append(str);
        sb.append(PENDING_RESTORE_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(PENDING_RESTORE_FILE_EXT);
        boolean exists = new File(filesDir, sb2.toString()).exists();
        String str3 = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("File exists: ");
        sb3.append(exists);
        Log.d(str3, sb3.toString());
        return exists;
    }

    public void saveRestoreConfigFile(String str, LockMode lockMode, int i, String str2) {
        if (!existsRestoreConfigFile(str)) {
            String str3 = LOG_TAG;
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Saving. File name: ");
                sb.append(str);
                sb.append(PENDING_RESTORE_FILE_EXT);
                Log.d(str3, sb.toString());
                File filesDir = this.mContext.getApplicationContext().getFilesDir();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(str);
                sb2.append(PENDING_RESTORE_FILE_EXT);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filesDir, sb2.toString())));
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("To Write: Unlock Mode: ");
                sb3.append(lockMode);
                Log.d(str4, sb3.toString());
                bufferedWriter.write(String.valueOf(lockMode.getValue()));
                bufferedWriter.write(10);
                String str5 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("To Write: Relock time: ");
                sb4.append(i);
                Log.d(str5, sb4.toString());
                bufferedWriter.write(String.valueOf(i));
                bufferedWriter.write(10);
                String str6 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("To Write: Primary Code: ");
                sb5.append(str2);
                Log.d(str6, sb5.toString());
                bufferedWriter.write(String.valueOf(str2));
                bufferedWriter.write(10);
                bufferedWriter.close();
                Log.d(LOG_TAG, "File closed.");
            } catch (Exception e) {
                String str7 = LOG_TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Error on writing Restore Config to file: ");
                sb6.append(e);
                Log.e(str7, sb6.toString());
            }
        }
    }

    public void readRestoreConfigFile(Lock lock) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Reading. File name: ");
        sb.append(lock.getKmsDeviceKey().getDeviceId());
        sb.append(PENDING_RESTORE_FILE_EXT);
        Log.d(str, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(lock.getKmsDeviceKey().getDeviceId());
        sb2.append(PENDING_RESTORE_FILE_EXT);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filesDir, sb2.toString())));
            lock.setLockMode(LockMode.fromKey(Integer.parseInt(bufferedReader.readLine())));
            lock.setRelockTimeInSeconds(Integer.parseInt(bufferedReader.readLine()));
            lock.setPrimaryCode(bufferedReader.readLine());
            bufferedReader.close();
            Log.d(LOG_TAG, "Closing file.");
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Error on reading Firmware Update file: ");
            sb3.append(e);
            Log.e(str2, sb3.toString());
        }
    }

    public void deleteRestoreConfigFile(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Deleting. File name: ");
        sb.append(str);
        sb.append(PENDING_RESTORE_FILE_EXT);
        Log.d(str2, sb.toString());
        File filesDir = this.mContext.getApplicationContext().getFilesDir();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(str);
        sb2.append(PENDING_RESTORE_FILE_EXT);
        if (new File(filesDir, sb2.toString()).delete()) {
            Log.d(LOG_TAG, "Restore Config file deleted.");
        } else {
            Log.d(LOG_TAG, "Error deleting Restore Config file.");
        }
    }
}
