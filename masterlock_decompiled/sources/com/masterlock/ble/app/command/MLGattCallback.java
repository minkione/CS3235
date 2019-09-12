package com.masterlock.ble.app.command;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ContentValues;
import android.os.Handler;
import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.service.LockListener;
import com.masterlock.ble.app.service.LockListener.Configuration;
import com.masterlock.ble.app.tape.ConfirmTask;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.core.AccessType;
import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.KmsLogEntry.Builder;
import com.masterlock.core.Lock;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import com.masterlock.core.ShackleStatus;
import com.masterlock.core.audit.events.AuditEventId;
import com.masterlock.core.audit.events.AuditLogBlob;
import com.masterlock.core.audit.events.EventCode;
import com.masterlock.mlclientapi.MlUser;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;

public class MLGattCallback extends BluetoothGattCallback {
    public static final UUID CONFIG_DESCRIPTOR = UUID.fromString(MasterLockApp.get().getString(C1075R.string.ml_descriptor_uuid));
    public static final UUID SECURE_DATA_CHARACTERISTIC = UUID.fromString(MasterLockApp.get().getString(C1075R.string.ml_characteristic_uuid));
    public static final UUID SECURE_FIRMWARE_UPDATE = UUID.fromString(MasterLockApp.get().getString(C1075R.string.ml_firmware_update_mode_uuid));
    public static final UUID SECURE_SERVICE = UUID.fromString(MasterLockApp.get().getString(C1075R.string.ml_service_uuid));
    /* access modifiers changed from: private */
    public LinkedList<IMlCommand> commandList;
    @Inject
    ConfirmTaskQueue mConfirmTaskQueue;
    /* access modifiers changed from: private */
    public IMlCommand mCurrentCommand;
    private Handler mHandler;
    private boolean mIsClosed = false;
    private List<KmsLogEntry> mKmsLogEntryList = new ArrayList();
    private Lock mLock;
    private LockListener mLockListener;
    private boolean mLongReadPerformed;
    private List<KmsDeviceTrait> mTraitList = new ArrayList();
    @Inject
    UploadTaskQueue mUploadTaskQueue;
    private MlUser mlUser;
    private boolean mustUpdateLocation = false;

    MLGattCallback(LinkedList<IMlCommand> linkedList, Lock lock, LockListener lockListener, MlUser mlUser2) {
        this.commandList = linkedList;
        this.mLock = lock;
        this.mLockListener = lockListener;
        this.mlUser = mlUser2;
        this.mHandler = new Handler();
        MasterLockApp.get().inject(this);
    }

    /* access modifiers changed from: 0000 */
    public void updateStatus(LockStatus lockStatus) {
        if (lockStatus != this.mLock.getLockStatus()) {
            this.mLock.setLockStatus(lockStatus);
            this.mLockListener.onStatusUpdate(this.mLock);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateShackleStatus(ShackleStatus shackleStatus) {
        if (shackleStatus != this.mLock.getShackleStatus()) {
            this.mLock.setShackleStatus(shackleStatus);
            this.mLockListener.onStatusUpdate(this.mLock);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updatePrimaryCode(String str, int i) {
        this.mLock.setPrimaryCode(str);
        this.mLock.setPrimaryCodeCounter((long) i);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.PRIMARY_CODE, str);
        contentValues.put(LocksColumns.PRIMARY_PASSCODE_COUNTER, Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
        this.mTraitList.add(KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.PRIMARYCODE));
    }

    /* access modifiers changed from: 0000 */
    public void updateSecondaryCodes(List<String> list, int i) {
        String str = (String) list.get(SecondaryCodeIndex.SECONDARY_PASSCODE_1.getValue());
        String str2 = (String) list.get(SecondaryCodeIndex.SECONDARY_PASSCODE_2.getValue());
        String str3 = (String) list.get(SecondaryCodeIndex.SECONDARY_PASSCODE_3.getValue());
        String str4 = (String) list.get(SecondaryCodeIndex.SECONDARY_PASSCODE_4.getValue());
        String str5 = (String) list.get(SecondaryCodeIndex.SECONDARY_PASSCODE_5.getValue());
        this.mLock.setSecondaryCode1(str);
        this.mLock.setSecondaryCode2(str2);
        this.mLock.setSecondaryCode3(str3);
        this.mLock.setSecondaryCode4(str4);
        this.mLock.setSecondaryCode5(str5);
        this.mLock.setSecondaryCodeCounter((long) i);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.SECONDARY_CODE_1, str);
        contentValues.put(LocksColumns.SECONDARY_CODE_2, str2);
        contentValues.put(LocksColumns.SECONDARY_CODE_3, str3);
        contentValues.put(LocksColumns.SECONDARY_CODE_4, str4);
        contentValues.put(LocksColumns.SECONDARY_CODE_5, str5);
        contentValues.put(LocksColumns.SECONDARY_PASSCODE_COUNTER, Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
        this.mTraitList = KmsDeviceTrait.generateTraitsForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE1, KmsDeviceTrait.SECONDARYCODE2, KmsDeviceTrait.SECONDARYCODE3, KmsDeviceTrait.SECONDARYCODE4, KmsDeviceTrait.SECONDARYCODE5);
    }

    /* access modifiers changed from: 0000 */
    public void updateBattery(int i, int i2) {
        this.mLock.setRemainingBatteryPercentage(i);
        this.mLock.setMeasurementCounter(i2);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.BATTERY_LEVEL, Integer.valueOf(i));
        contentValues.put(LocksColumns.MEASUREMENT_COUNTER, Integer.valueOf(i2));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    /* access modifiers changed from: 0000 */
    public void updateFirmwareCommandResponse(int i) {
        this.mLockListener.onFirmwareCommandResponse(this.mLock);
    }

    /* access modifiers changed from: 0000 */
    public void updatePrimaryCodeResponse() {
        this.mLockListener.onConfigAppliedSuccess(this.mLock, Configuration.PRIMARY_CODE);
        KmsDeviceTrait generateTraitForLock = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.PRIMARYCODE);
        this.mTraitList.add(generateTraitForLock);
        addLogForTrait(generateTraitForLock);
    }

    /* access modifiers changed from: 0000 */
    public void updateSecondaryCodeResponse(String str, SecondaryCodeIndex secondaryCodeIndex) {
        KmsDeviceTrait kmsDeviceTrait;
        this.mLockListener.onConfigAppliedSuccess(this.mLock, Configuration.SECONDARY_CODE);
        switch (secondaryCodeIndex) {
            case SECONDARY_PASSCODE_2:
                kmsDeviceTrait = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE2);
                break;
            case SECONDARY_PASSCODE_3:
                kmsDeviceTrait = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE3);
                break;
            case SECONDARY_PASSCODE_4:
                kmsDeviceTrait = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE4);
                break;
            case SECONDARY_PASSCODE_5:
                kmsDeviceTrait = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE5);
                break;
            default:
                kmsDeviceTrait = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.SECONDARYCODE1);
                break;
        }
        this.mTraitList.add(kmsDeviceTrait);
        Builder builder = new Builder();
        builder.kmsDeviceId(this.mLock.getKmsId()).firmwareCounter(Integer.valueOf(this.mLock.getFirmwareCounter())).eventSource(EventSource.DEVICE).createdOn(new Date(System.currentTimeMillis())).eventIndex(kmsDeviceTrait.getCounter()).eventValue(String.valueOf(secondaryCodeIndex.getValue() + 1)).kmsDeviceKeyAlias(Integer.valueOf(this.mLock.getKmsDeviceKey().getAlias()));
        switch (SecondaryCodesUtil.getOperationTypeForCode(secondaryCodeIndex.getValue(), str)) {
            case CREATE:
                builder.eventCode(EventCode.SECONDARYCODE_CREATED);
                break;
            case UPDATE:
                builder.eventCode(EventCode.SECONDARYCODE_CHANGED);
                break;
            case REMOVE:
                builder.eventCode(EventCode.SECONDARYCODE_DELETED);
                break;
        }
        this.mKmsLogEntryList.add(builder.build());
    }

    /* access modifiers changed from: 0000 */
    public void updateUnlockMode() {
        this.mLockListener.onConfigAppliedSuccess(this.mLock, Configuration.UNLOCK_MODE);
        KmsDeviceTrait generateTraitForLock = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.LOCKMODE);
        this.mTraitList.add(generateTraitForLock);
        addLogForTrait(generateTraitForLock);
    }

    /* access modifiers changed from: 0000 */
    public void updateRelockTime() {
        this.mLockListener.onConfigAppliedSuccess(this.mLock, Configuration.RELOCK_TIME);
        KmsDeviceTrait generateTraitForLock = KmsDeviceTrait.generateTraitForLock(this.mLock, KmsDeviceTrait.RELOCKINTERVAL);
        this.mTraitList.add(generateTraitForLock);
        addLogForTrait(generateTraitForLock);
        generateLocationTrait();
    }

    private void generateLocationTrait() {
        MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
        this.mLock.setLatitude(instance.getLatitude());
        this.mLock.setLongitude(instance.getLongitude());
        this.mLockListener.onLocationUpdated(this.mLock);
        KmsDeviceTrait generateLocationTrait = KmsDeviceTrait.generateLocationTrait(this.mLock, false);
        if (generateLocationTrait != null) {
            this.mTraitList.add(generateLocationTrait);
            addLogForTrait(generateLocationTrait);
            addUpdateLockTraitTask();
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00c8  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00de  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addLogForTrait(com.masterlock.api.entity.KmsDeviceTrait r6) {
        /*
            r5 = this;
            com.masterlock.core.KmsLogEntry$Builder r0 = new com.masterlock.core.KmsLogEntry$Builder
            r0.<init>()
            com.masterlock.core.Lock r1 = r5.mLock
            java.lang.String r1 = r1.getKmsId()
            com.masterlock.core.KmsLogEntry$Builder r1 = r0.kmsDeviceId(r1)
            com.masterlock.core.Lock r2 = r5.mLock
            int r2 = r2.getFirmwareCounter()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            com.masterlock.core.KmsLogEntry$Builder r1 = r1.firmwareCounter(r2)
            com.masterlock.core.EventSource r2 = com.masterlock.core.EventSource.DEVICE
            com.masterlock.core.KmsLogEntry$Builder r1 = r1.eventSource(r2)
            java.util.Date r2 = new java.util.Date
            long r3 = java.lang.System.currentTimeMillis()
            r2.<init>(r3)
            com.masterlock.core.KmsLogEntry$Builder r1 = r1.createdOn(r2)
            long r2 = r6.getCounter()
            com.masterlock.core.KmsLogEntry$Builder r1 = r1.eventIndex(r2)
            java.lang.String r2 = r6.getValue()
            com.masterlock.core.KmsLogEntry$Builder r1 = r1.eventValue(r2)
            com.masterlock.core.Lock r2 = r5.mLock
            com.masterlock.core.KmsDeviceKey r2 = r2.getKmsDeviceKey()
            int r2 = r2.getAlias()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r1.kmsDeviceKeyAlias(r2)
            java.lang.String r1 = r6.getName()
            int r2 = r1.hashCode()
            r3 = -1919513437(0xffffffff8d968ca3, float:-9.278321E-31)
            r4 = 0
            if (r2 == r3) goto L_0x008d
            r3 = -1611296843(0xffffffff9ff58fb5, float:-1.0399928E-19)
            if (r2 == r3) goto L_0x0083
            r3 = -1602264754(0xffffffffa07f614e, float:-2.1631536E-19)
            if (r2 == r3) goto L_0x0079
            r3 = 1285848783(0x4ca47ecf, float:8.6242936E7)
            if (r2 == r3) goto L_0x006f
            goto L_0x0097
        L_0x006f:
            java.lang.String r2 = "PRIMARYCODE"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0097
            r1 = 0
            goto L_0x0098
        L_0x0079:
            java.lang.String r2 = "LOCKMODE"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0097
            r1 = 1
            goto L_0x0098
        L_0x0083:
            java.lang.String r2 = "LOCATION"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0097
            r1 = 3
            goto L_0x0098
        L_0x008d:
            java.lang.String r2 = "RELOCKINTERVAL"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0097
            r1 = 2
            goto L_0x0098
        L_0x0097:
            r1 = -1
        L_0x0098:
            switch(r1) {
                case 0: goto L_0x00de;
                case 1: goto L_0x00c8;
                case 2: goto L_0x00c2;
                case 3: goto L_0x009c;
                default: goto L_0x009b;
            }
        L_0x009b:
            goto L_0x00e3
        L_0x009c:
            java.lang.String r6 = r6.getValue()
            boolean r6 = r6.isEmpty()
            if (r6 == 0) goto L_0x00a9
            com.masterlock.core.audit.events.EventCode r6 = com.masterlock.core.audit.events.EventCode.CLEAR_LAST_KNOWN_LOCATION
            goto L_0x00ab
        L_0x00a9:
            com.masterlock.core.audit.events.EventCode r6 = com.masterlock.core.audit.events.EventCode.UPDATE_LAST_KNOWN_LOCATION
        L_0x00ab:
            com.masterlock.core.KmsLogEntry$Builder r6 = r0.eventCode(r6)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            com.masterlock.core.KmsLogEntry$Builder r6 = r6.firmwareCounter(r1)
            r1 = 0
            com.masterlock.core.KmsLogEntry$Builder r6 = r6.eventIndex(r1)
            r1 = 0
            r6.eventValue(r1)
            goto L_0x00e3
        L_0x00c2:
            com.masterlock.core.audit.events.EventCode r6 = com.masterlock.core.audit.events.EventCode.UPDATE_RELOCKINTERVAL
            r0.eventCode(r6)
            goto L_0x00e3
        L_0x00c8:
            com.masterlock.core.audit.events.EventCode r1 = com.masterlock.core.audit.events.EventCode.UPDATE_LOCKMODE
            com.masterlock.core.KmsLogEntry$Builder r1 = r0.eventCode(r1)
            java.lang.String r6 = r6.getValue()
            int r6 = java.lang.Integer.parseInt(r6)
            java.lang.String r6 = com.masterlock.core.LockMode.getStringValue(r6)
            r1.eventValue(r6)
            goto L_0x00e3
        L_0x00de:
            com.masterlock.core.audit.events.EventCode r6 = com.masterlock.core.audit.events.EventCode.UPDATE_PRIMARYCODE
            r0.eventCode(r6)
        L_0x00e3:
            java.util.List<com.masterlock.core.KmsLogEntry> r6 = r5.mKmsLogEntryList
            com.masterlock.core.KmsLogEntry r0 = r0.build()
            r6.add(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.command.MLGattCallback.addLogForTrait(com.masterlock.api.entity.KmsDeviceTrait):void");
    }

    /* access modifiers changed from: 0000 */
    public void configChangeApplied() {
        this.mLockListener.onConfigChangeApplied(this.mLock);
    }

    /* access modifiers changed from: 0000 */
    public void keyReset(ResetKeysWrapper resetKeysWrapper) {
        this.mLockListener.onKeyReset(resetKeysWrapper);
    }

    /* access modifiers changed from: 0000 */
    public void disconnectLock() {
        this.mLockListener.onLockDisconnect(this.mLock);
    }

    public void onConnectionStateChange(final BluetoothGatt bluetoothGatt, int i, int i2) {
        if (i == 0 && i2 == 2) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    bluetoothGatt.discoverServices();
                }
            });
        } else if (i == 0 && i2 == 0) {
            closeConnection(bluetoothGatt);
        } else if (i != 0) {
            closeActiveConnection(bluetoothGatt);
        }
    }

    public void onServicesDiscovered(final BluetoothGatt bluetoothGatt, int i) {
        if (i != 0) {
            closeActiveConnection(bluetoothGatt);
        } else {
            this.mHandler.post(new Runnable() {
                /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|9) */
                /* JADX WARNING: Code restructure failed: missing block: B:5:0x0050, code lost:
                    r4.this$0.closeActiveConnection(r2);
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
                    return;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
                    return;
                 */
                /* JADX WARNING: Failed to process nested try/catch */
                /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0029 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r4 = this;
                        r0 = 1
                        r1 = 2
                        android.bluetooth.BluetoothGatt r2 = r2     // Catch:{ Exception -> 0x0029 }
                        java.util.UUID r3 = com.masterlock.ble.app.command.MLGattCallback.SECURE_SERVICE     // Catch:{ Exception -> 0x0029 }
                        android.bluetooth.BluetoothGattService r2 = r2.getService(r3)     // Catch:{ Exception -> 0x0029 }
                        java.util.UUID r3 = com.masterlock.ble.app.command.MLGattCallback.SECURE_DATA_CHARACTERISTIC     // Catch:{ Exception -> 0x0029 }
                        android.bluetooth.BluetoothGattCharacteristic r2 = r2.getCharacteristic(r3)     // Catch:{ Exception -> 0x0029 }
                        r2.setWriteType(r1)     // Catch:{ Exception -> 0x0029 }
                        android.bluetooth.BluetoothGatt r3 = r2     // Catch:{ Exception -> 0x0029 }
                        r3.setCharacteristicNotification(r2, r0)     // Catch:{ Exception -> 0x0029 }
                        java.util.UUID r3 = com.masterlock.ble.app.command.MLGattCallback.CONFIG_DESCRIPTOR     // Catch:{ Exception -> 0x0029 }
                        android.bluetooth.BluetoothGattDescriptor r2 = r2.getDescriptor(r3)     // Catch:{ Exception -> 0x0029 }
                        byte[] r3 = android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE     // Catch:{ Exception -> 0x0029 }
                        r2.setValue(r3)     // Catch:{ Exception -> 0x0029 }
                        android.bluetooth.BluetoothGatt r3 = r2     // Catch:{ Exception -> 0x0029 }
                        r3.writeDescriptor(r2)     // Catch:{ Exception -> 0x0029 }
                        goto L_0x0057
                    L_0x0029:
                        android.bluetooth.BluetoothGatt r2 = r2     // Catch:{ Exception -> 0x0050 }
                        java.util.UUID r3 = com.masterlock.ble.app.command.MLGattCallback.SECURE_FIRMWARE_UPDATE     // Catch:{ Exception -> 0x0050 }
                        android.bluetooth.BluetoothGattService r2 = r2.getService(r3)     // Catch:{ Exception -> 0x0050 }
                        java.util.UUID r3 = com.masterlock.ble.app.command.MLGattCallback.SECURE_DATA_CHARACTERISTIC     // Catch:{ Exception -> 0x0050 }
                        android.bluetooth.BluetoothGattCharacteristic r2 = r2.getCharacteristic(r3)     // Catch:{ Exception -> 0x0050 }
                        r2.setWriteType(r1)     // Catch:{ Exception -> 0x0050 }
                        android.bluetooth.BluetoothGatt r1 = r2     // Catch:{ Exception -> 0x0050 }
                        r1.setCharacteristicNotification(r2, r0)     // Catch:{ Exception -> 0x0050 }
                        java.util.UUID r0 = com.masterlock.ble.app.command.MLGattCallback.CONFIG_DESCRIPTOR     // Catch:{ Exception -> 0x0050 }
                        android.bluetooth.BluetoothGattDescriptor r0 = r2.getDescriptor(r0)     // Catch:{ Exception -> 0x0050 }
                        byte[] r1 = android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE     // Catch:{ Exception -> 0x0050 }
                        r0.setValue(r1)     // Catch:{ Exception -> 0x0050 }
                        android.bluetooth.BluetoothGatt r1 = r2     // Catch:{ Exception -> 0x0050 }
                        r1.writeDescriptor(r0)     // Catch:{ Exception -> 0x0050 }
                        goto L_0x0057
                    L_0x0050:
                        com.masterlock.ble.app.command.MLGattCallback r0 = com.masterlock.ble.app.command.MLGattCallback.this
                        android.bluetooth.BluetoothGatt r1 = r2
                        r0.closeActiveConnection(r1)
                    L_0x0057:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.command.MLGattCallback.C11122.run():void");
                }
            });
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0016 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDescriptorWrite(android.bluetooth.BluetoothGatt r1, android.bluetooth.BluetoothGattDescriptor r2, int r3) {
        /*
            r0 = this;
            if (r3 == 0) goto L_0x0006
            r0.closeActiveConnection(r1)
            return
        L_0x0006:
            java.util.UUID r2 = SECURE_SERVICE     // Catch:{ Exception -> 0x0016 }
            android.bluetooth.BluetoothGattService r2 = r1.getService(r2)     // Catch:{ Exception -> 0x0016 }
            java.util.UUID r3 = SECURE_DATA_CHARACTERISTIC     // Catch:{ Exception -> 0x0016 }
            android.bluetooth.BluetoothGattCharacteristic r2 = r2.getCharacteristic(r3)     // Catch:{ Exception -> 0x0016 }
            r0.executeNextCommand(r1, r2)     // Catch:{ Exception -> 0x0016 }
            goto L_0x0025
        L_0x0016:
            java.util.UUID r2 = SECURE_FIRMWARE_UPDATE     // Catch:{ Exception -> 0x0025 }
            android.bluetooth.BluetoothGattService r2 = r1.getService(r2)     // Catch:{ Exception -> 0x0025 }
            java.util.UUID r3 = SECURE_DATA_CHARACTERISTIC     // Catch:{ Exception -> 0x0025 }
            android.bluetooth.BluetoothGattCharacteristic r2 = r2.getCharacteristic(r3)     // Catch:{ Exception -> 0x0025 }
            r0.executeNextCommand(r1, r2)     // Catch:{ Exception -> 0x0025 }
        L_0x0025:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.command.MLGattCallback.onDescriptorWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattDescriptor, int):void");
    }

    public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (i != 0) {
            closeActiveConnection(bluetoothGatt);
        } else {
            this.mHandler.post(new Runnable(bluetoothGattCharacteristic, bluetoothGatt) {
                private final /* synthetic */ BluetoothGattCharacteristic f$1;
                private final /* synthetic */ BluetoothGatt f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void run() {
                    MLGattCallback.lambda$onCharacteristicRead$0(MLGattCallback.this, this.f$1, this.f$2);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$onCharacteristicRead$0(MLGattCallback mLGattCallback, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt) {
        mLGattCallback.mLongReadPerformed = true;
        mLGattCallback.mCurrentCommand.execute(bluetoothGattCharacteristic, bluetoothGatt, mLGattCallback);
    }

    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (i != 0) {
            closeActiveConnection(bluetoothGatt);
        }
    }

    public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.mLongReadPerformed = false;
        executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
    }

    private String connectionState(int i) {
        switch (i) {
            case 0:
                return "disconnected";
            case 1:
                return "connecting";
            case 2:
                return "connected";
            case 3:
                return "disconnecting";
            default:
                return String.valueOf(i);
        }
    }

    private String getCurrentCommandName() {
        IMlCommand iMlCommand = this.mCurrentCommand;
        return iMlCommand == null ? "" : iMlCommand.getClass().getSimpleName();
    }

    public void executeNextCommand(final BluetoothGatt bluetoothGatt, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (MLGattCallback.this.commandList.peek() != null) {
                    MLGattCallback mLGattCallback = MLGattCallback.this;
                    mLGattCallback.mCurrentCommand = (IMlCommand) mLGattCallback.commandList.poll();
                    MLGattCallback.this.mCurrentCommand.execute(bluetoothGattCharacteristic, bluetoothGatt, MLGattCallback.this);
                }
            }
        });
    }

    public void closeActiveConnection(BluetoothGatt bluetoothGatt) {
        if (!this.mIsClosed) {
            this.mIsClosed = true;
            this.commandList.clear();
            this.mHandler.post(new Runnable(bluetoothGatt) {
                private final /* synthetic */ BluetoothGatt f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    MLGattCallback.lambda$closeActiveConnection$1(MLGattCallback.this, this.f$1);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$closeActiveConnection$1(MLGattCallback mLGattCallback, BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        mLGattCallback.disconnectLock();
    }

    public void closeConnection(BluetoothGatt bluetoothGatt) {
        if (!this.mIsClosed) {
            this.mIsClosed = true;
            this.commandList.clear();
            this.mHandler.post(new Runnable(bluetoothGatt) {
                private final /* synthetic */ BluetoothGatt f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    MLGattCallback.lambda$closeConnection$2(MLGattCallback.this, this.f$1);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$closeConnection$2(MLGattCallback mLGattCallback, BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }
        mLGattCallback.disconnectLock();
    }

    public void addCommand(IMlCommand iMlCommand) {
        if (!this.mIsClosed) {
            this.commandList.add(iMlCommand);
        }
    }

    public void addAsNext(IMlCommand iMlCommand) {
        if (!this.mIsClosed) {
            this.commandList.addFirst(iMlCommand);
        }
    }

    public void addCommand(LinkedList<IMlCommand> linkedList) {
        if (!this.mIsClosed) {
            this.commandList.addAll(linkedList);
        }
    }

    public MlUser getMlUser() {
        return this.mlUser;
    }

    public void addUpdateLockTraitTask() {
        List<KmsDeviceTrait> list = this.mTraitList;
        if (list != null && !list.isEmpty()) {
            this.mUploadTaskQueue.add(new UploadTask(new KmsUpdateTraitsRequest(this.mLock, this.mTraitList)));
            this.mTraitList.clear();
            addUploadAuditLogsTask();
        }
    }

    public void addUploadAuditLogsTask() {
        List<KmsLogEntry> list = this.mKmsLogEntryList;
        if (list != null && !list.isEmpty()) {
            this.mUploadTaskQueue.add(new UploadTask(this.mKmsLogEntryList));
            this.mKmsLogEntryList.clear();
            this.mustUpdateLocation = false;
        }
    }

    public void uploadLogs(List<KmsLogEntry> list) {
        if (list != null && !list.isEmpty()) {
            this.mUploadTaskQueue.add(new UploadTask(list));
        }
    }

    public void uploadKeyReset(ResetKeysWrapper resetKeysWrapper) {
        if (resetKeysWrapper != null) {
            this.mConfirmTaskQueue.add(new ConfirmTask(resetKeysWrapper));
        }
    }

    public void addLockConfigTraits(int i, int i2, int i3) {
        this.mLock.setRelockTimeInSeconds(i2);
        this.mLock.setLockMode(LockMode.lockModeForLimitedDiscoveryTime(i3));
        this.mLock.setPublicConfigCounter(i);
        this.mTraitList = KmsDeviceTrait.generateTraitsForLock(this.mLock, KmsDeviceTrait.RELOCKINTERVAL, KmsDeviceTrait.BATTERYLEVEL, KmsDeviceTrait.LOCKMODE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.RELOCK_TIME, Integer.valueOf(i2));
        contentValues.put(LocksColumns.LOCK_MODE, Integer.valueOf(LockMode.lockModeForLimitedDiscoveryTime(i3).getValue()));
        contentValues.put(LocksColumns.PUBLIC_CONFIG_COUNTER, Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    public int getCurrentPublicCounter() {
        return this.mLock.getPublicConfigCounter();
    }

    public long getPrimaryCodeCounter() {
        return this.mLock.getPrimaryCodeCounter();
    }

    public long getSecondaryCodeCounter() {
        return this.mLock.getSecondaryCodeCounter();
    }

    public boolean isUserGuest() {
        return this.mLock.getAccessType().equals(AccessType.GUEST);
    }

    public void updateFirmwareCounter(int i) {
        this.mLock.setFirmwareCounter(i);
        ContentValues contentValues = new ContentValues();
        contentValues.put("firmware_counter", Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    public void updatePrimaryCodeCounter(int i) {
        this.mLock.setFirmwareCounter(i);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.PRIMARY_PASSCODE_COUNTER, Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    /* access modifiers changed from: 0000 */
    public void updateSecondaryCodesCounter(int i) {
        this.mLock.setSecondaryCodeCounter((long) i);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.SECONDARY_PASSCODE_COUNTER, Integer.valueOf(i));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    public long getLastLogReferenceId() {
        return this.mLock.getLastLogReferenceId();
    }

    public void parseAuditLogdata(ByteBuffer byteBuffer) {
        AuditLogBlob auditLogBlob = new AuditLogBlob(byteBuffer, MasterLockSharedPreferences.getInstance().getUsername());
        do {
            KmsLogEntry generateKmsLogEntry = auditLogBlob.generateKmsLogEntry(this.mLock);
            if (!(generateKmsLogEntry == null || auditLogBlob.getEventId() == AuditEventId.FIRMWARE_UPDATE_COMPLETED.getValue())) {
                this.mKmsLogEntryList.add(generateKmsLogEntry);
                switch (generateKmsLogEntry.getEventCode()) {
                    case LOCK_OPENED:
                    case DOOR_OPENED:
                    case SHACKLE_OPENED:
                    case RELOCK_AUTOMATIC:
                    case RELOCK_AUTOMATIC_DOOR:
                    case RELOCK_AUTOMATIC_SHACKLE:
                    case RELOCK_MANUAL:
                    case RELOCK_MANUAL_DOOR:
                    case RELOCK_MANUAL_SHACKLE:
                        if (!this.mustUpdateLocation) {
                            generateLocationTrait();
                            this.mustUpdateLocation = true;
                            break;
                        }
                        break;
                }
            }
        } while (auditLogBlob.moveToNextEvent());
        int eventIndex = auditLogBlob.getEventIndex() - 1;
        this.mLock.setLastLogReferenceId((long) eventIndex);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.LAST_LOG_REFERENCE_ID, Integer.valueOf(eventIndex));
        this.mLockListener.onLockUpdate(this.mLock, contentValues);
    }

    public Lock getLock() {
        return this.mLock;
    }

    public boolean isLongReadPerformed() {
        return this.mLongReadPerformed;
    }

    public LinkedList<IMlCommand> getCommandList() {
        return this.commandList;
    }
}
