package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableCommands;
import com.masterlock.core.AvailableCommand;
import com.masterlock.core.AvailableCommand.Builder;
import com.masterlock.core.AvailableCommandType;
import java.util.ArrayList;

public class AvailableCommandBuilder {
    private AvailableCommandBuilder() {
    }

    public static ContentValues buildContentValues(AvailableCommand availableCommand) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AvailableCommandsColumns.AVAILABLE_COMMAND_UUID, availableCommand.getUuid());
        contentValues.put("kms_device_id", availableCommand.getKmsDeviceId());
        contentValues.put(AvailableCommandsColumns.AVAILABLE_COMMAND_ID, Byte.valueOf(availableCommand.getId().getValue()));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(AvailableCommand availableCommand) {
        return ContentProviderOperation.newInsert(AvailableCommands.CONTENT_URI).withValue(AvailableCommandsColumns.AVAILABLE_COMMAND_UUID, availableCommand.getUuid()).withValue("kms_device_id", availableCommand.getKmsDeviceId()).withValue(AvailableCommandsColumns.AVAILABLE_COMMAND_ID, Byte.valueOf(availableCommand.getId().getValue())).build();
    }

    public static ArrayList<AvailableCommand> buildAvailableCommands(Cursor cursor) {
        ArrayList<AvailableCommand> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildAvailableCommand(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static AvailableCommand buildAvailableCommand(Cursor cursor) {
        return new Builder().setId(AvailableCommandType.fromKey((byte) cursor.getInt(cursor.getColumnIndexOrThrow(AvailableCommandsColumns.AVAILABLE_COMMAND_ID)))).setKmsDeviceid(cursor.getString(cursor.getColumnIndexOrThrow("kms_device_id"))).build();
    }
}
