package com.masterlock.mlclientapi;

import com.google.common.base.Ascii;
import com.masterlock.ble.app.command.DisconnectType;
import com.masterlock.core.AvailableCommandType;
import com.masterlock.core.AvailableSetting;
import com.masterlock.core.AvailableSettingType;
import com.masterlock.core.Lock;
import com.masterlock.core.SecondaryCodeIndex;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MlCommand {
    public static final byte CMD_START_SESSION = 0;
    public static final byte CMD_USER_COMMAND = 1;
    public static final String LOG_TAG = "BLE_COMMAND";
    public static final short MAX_LOG_READ_SIZE = 496;
    public static final byte RSP_DATA_NOT_AVAIL = 4;
    public static final byte RSP_INVALID_OPERATION = 1;
    public static final byte RSP_INVALID_TIME = 2;
    public static final byte RSP_LEN_NONCE = 6;
    public static final byte RSP_LEN_READ_STATE = 13;
    public static final byte RSP_LEN_START = 18;
    public static final byte RSP_LEN_UNLOCK = 12;
    public static final byte RSP_NOT_PERMITTED = 3;
    public static final byte RSP_OK = 0;

    private MlCommand() {
    }

    public static byte[] startUserSession(byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 1);
        allocate.put(0);
        allocate.put(bArr);
        return allocate.array();
    }

    public static byte[] unlockCmd(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.put(AvailableCommandType.CMD_UNLOCK_PRIMARY.getValue());
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readStateCmd(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.put(AvailableCommandType.CMD_READ_STATE_PRIMARY.getValue());
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readBatteryCmd(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(1);
        allocate.put(AvailableCommandType.CMD_READ_BATTERY_LEVEL.getValue());
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readPrimaryCodeMemoryCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_PRIMARY_PASSCODE.getValue())).getAddress();
        int size = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_PRIMARY_PASSCODE.getValue())).getSize();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put((byte) size);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readSecondaryCodesMemoryCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_1.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(40);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readConfigurationCounterCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_PUBLIC_CONFIG_COUNTER.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(lock.isPadLock() ? Ascii.SUB : Ascii.f85GS);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readFirmwareCounterCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_FIRMWARE_COUNTER.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(4);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readPrimaryCodeCounterCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_PRIMARY_PASSCODE_COUNTER.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(4);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readSecondaryCodeCounterCommand(MlUser mlUser, Lock lock) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_COUNTER.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_READ_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(4);
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] writePrimaryCodeCmd(MlUser mlUser, Lock lock, byte[] bArr) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_PRIMARY_PASSCODE.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 3);
        allocate.put(AvailableCommandType.CMD_WRITE_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] writeRelockTimeCmd(MlUser mlUser, Lock lock, byte[] bArr) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_DEFAULT_UNLOCK_TIMEOUT.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.put(AvailableCommandType.CMD_WRITE_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] updateLockMode(MlUser mlUser, Lock lock, byte[] bArr) {
        int address = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_WIRELESS_CONFIG.getValue())).getAddress();
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 3);
        allocate.put(AvailableCommandType.CMD_WRITE_MEMORY.getValue());
        allocate.put((byte) address);
        allocate.put((byte) (address >> 8));
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] resetKey(MlUser mlUser, byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 1);
        allocate.put(AvailableCommandType.CMD_WRITE_AUTHENTICATED_MEMORY.getValue());
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] writeFirmwareCommand(MlUser mlUser, byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 1);
        allocate.put(AvailableCommandType.CMD_WRITE_AUTHENTICATED_MEMORY.getValue());
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] writeTime(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_WRITE_TIME.getValue());
        allocate.order(ByteOrder.LITTLE_ENDIAN).putInt((int) (System.currentTimeMillis() / 1000));
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] nudgeTime(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(AvailableCommandType.CMD_NUDGE_TIME.getValue());
        allocate.order(ByteOrder.LITTLE_ENDIAN).putInt((int) (System.currentTimeMillis() / 1000));
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readAuditTrail(MlUser mlUser, long j) {
        ByteBuffer byteBuffer;
        if (j > -1) {
            byteBuffer = ByteBuffer.allocate(11);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put(AvailableCommandType.CMD_READ_AUDIT_TRIAL.getValue());
            byteBuffer.putShort(MAX_LOG_READ_SIZE);
            byteBuffer.putInt((int) j);
        } else {
            byteBuffer = ByteBuffer.allocate(3);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put(AvailableCommandType.CMD_READ_AUDIT_TRIAL.getValue());
            byteBuffer.putShort(MAX_LOG_READ_SIZE);
        }
        return encryptCommand(mlUser, byteBuffer).array();
    }

    public static byte[] retrieveNextAuditEventIndex(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(1);
        allocate.put(AvailableCommandType.CMD_READ_AUDIT_TRAIL_EVENT_INDEX.getValue());
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] disconnect(MlUser mlUser, DisconnectType disconnectType) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.put(AvailableCommandType.CMD_DISCONNECT.getValue());
        allocate.put(disconnectType.getUnlockPayload());
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] readShackleStateCmd(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.put(AvailableCommandType.CMD_READ_STATE_SECONDARY.getValue());
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] unlockShackleCmd(MlUser mlUser) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.put(AvailableCommandType.CMD_UNLOCK_SECONDARY.getValue());
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] writeSecondaryCodeCommand(MlUser mlUser, Lock lock, SecondaryCodeIndex secondaryCodeIndex, byte[] bArr) {
        int i;
        switch (secondaryCodeIndex) {
            case SECONDARY_PASSCODE_1:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_1.getValue())).getAddress();
                break;
            case SECONDARY_PASSCODE_2:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_2.getValue())).getAddress();
                break;
            case SECONDARY_PASSCODE_3:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_3.getValue())).getAddress();
                break;
            case SECONDARY_PASSCODE_4:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_4.getValue())).getAddress();
                break;
            case SECONDARY_PASSCODE_5:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_5.getValue())).getAddress();
                break;
            default:
                i = ((AvailableSetting) lock.getAvailableSettings().get(AvailableSettingType.ADDR_SECONDARY_PASSCODE_1.getValue())).getAddress();
                break;
        }
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 3);
        allocate.put(AvailableCommandType.CMD_WRITE_MEMORY.getValue());
        allocate.put((byte) i);
        allocate.put((byte) (i >> 8));
        allocate.put(bArr);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] deleteSecondaryCodeByIdCommand(MlUser mlUser, SecondaryCodeIndex secondaryCodeIndex) {
        ByteBuffer allocate = ByteBuffer.allocate(3);
        allocate.put(AvailableCommandType.CMD_CLEAR_INDIVIDUAL_SECONDARY_PASSCODE_BY_INDEX.getValue());
        allocate.put(secondaryCodeIndex.getByte());
        allocate.put(0);
        return encryptCommand(mlUser, allocate).array();
    }

    public static byte[] deleteAllSecondaryCodeCommand(MlUser mlUser, Lock lock) {
        ByteBuffer allocate = ByteBuffer.allocate(1);
        allocate.put(AvailableCommandType.CMD_CLEAR_ALL_SECONDARY_PASSCODES.getValue());
        return encryptCommand(mlUser, allocate).array();
    }

    private static ByteBuffer encryptCommand(MlUser mlUser, ByteBuffer byteBuffer) {
        byte[] encrypt = MlClientApi.encrypt(mlUser.getKey().getBytes(), mlUser.getNonce().getBytes(), byteBuffer.array());
        mlUser.advanceNonce();
        ByteBuffer allocate = ByteBuffer.allocate(encrypt.length + 1);
        allocate.put(1);
        allocate.put(encrypt);
        return allocate;
    }

    public static ByteBuffer decryptResponse(byte[] bArr, MlUser mlUser) {
        return ByteBuffer.wrap(MlClientApi.decrypt(mlUser.getKey().getBytes(), mlUser.getNonce().getBytes(), bArr));
    }
}
