package com.masterlock.ble.app.command;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.masterlock.api.entity.Command;
import com.masterlock.ble.app.service.LockListener;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.core.AvailableCommandType;
import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockCodeDirection;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import com.masterlock.core.ShackleStatus;
import com.masterlock.core.audit.events.EventCode;
import com.masterlock.core.bluetooth.util.PrimaryCodeUtils;
import com.masterlock.core.bluetooth.util.SecondaryCodeUtils;
import com.masterlock.mlclientapi.MlClientApi;
import com.masterlock.mlclientapi.MlCommand;
import com.masterlock.mlclientapi.MlKey;
import com.masterlock.mlclientapi.MlNonce;
import com.masterlock.mlclientapi.MlUser;
import com.masterlock.mlclientapi.MlUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public class LockCommander {
    public boolean firstFirmwareUpdateCommand = false;
    private final Context mContext;
    /* access modifiers changed from: private */
    public boolean stopFirmwareUpdate = false;

    private class Builder {
        LinkedList<IMlCommand> commandList;

        private class ClearAllSecondaryPasscodesRequestCommand extends RequestCommand {
            public ClearAllSecondaryPasscodesRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.deleteAllSecondaryCodeCommand(mlUser, this.lock);
            }
        }

        private class ClearAllSecondaryPasscodesResponseCommand extends ResponseCommand {
            private int secondaryCodeCounter = 0;

            public ClearAllSecondaryPasscodesResponseCommand(Lock lock, int i) {
                super(lock);
                this.secondaryCodeCounter = i;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updateSecondaryCodes(null, this.secondaryCodeCounter);
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ClearSecondaryPasscodeByIdRequestCommand extends RequestCommand {
            SecondaryCodeIndex secondaryCodeIndex;

            public ClearSecondaryPasscodeByIdRequestCommand(Lock lock, SecondaryCodeIndex secondaryCodeIndex2) {
                super(lock);
                this.secondaryCodeIndex = secondaryCodeIndex2;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.deleteSecondaryCodeByIdCommand(mlUser, this.secondaryCodeIndex);
            }
        }

        private class ClearSecondaryPasscodeByIdResponseCommand extends ResponseCommand {
            private SecondaryCodeIndex secondaryCodeIndex;

            public ClearSecondaryPasscodeByIdResponseCommand(Lock lock, SecondaryCodeIndex secondaryCodeIndex2) {
                super(lock);
                this.secondaryCodeIndex = secondaryCodeIndex2;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updateSecondaryCodeResponse("", this.secondaryCodeIndex);
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class DisconnectClearKeypadRequestCommand extends RequestCommand {
            public DisconnectClearKeypadRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                this.characteristic.setWriteType(1);
                return MlCommand.disconnect(mlUser, DisconnectType.CLEAR_KEYPAD);
            }
        }

        private class DisconnectPreventAdvertisingRequestCommand extends RequestCommand {
            public DisconnectPreventAdvertisingRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                this.characteristic.setWriteType(1);
                return MlCommand.disconnect(mlUser, DisconnectType.PREVENT_ADVERTISING);
            }
        }

        private class DisconnectRequestCommand extends RequestCommand {
            public DisconnectRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                this.characteristic.setWriteType(1);
                return MlCommand.disconnect(mlUser, DisconnectType.DEFAULT);
            }
        }

        private class FinishSecondaryPasscodeWrite implements IMlCommand {
            private FinishSecondaryPasscodeWrite() {
            }

            public void execute(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                mLGattCallback.addUpdateLockTraitTask();
                mLGattCallback.addUploadAuditLogsTask();
                mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
            }
        }

        class LockStateResponse {
            private byte keypadActive = 4;
            /* access modifiers changed from: private */
            public byte lockState;
            private byte unlocked = 1;
            private byte unshackled = 2;

            public LockStateResponse(byte b) {
                this.lockState = b;
            }

            public boolean isUnlocked() {
                return (this.lockState & this.unlocked) > 0;
            }

            public boolean isLocked() {
                return !isUnlocked();
            }

            public boolean isKeypadActive() {
                return (this.lockState & this.keypadActive) > 0;
            }

            public boolean isUnshackled() {
                return (this.lockState & this.unshackled) > 0;
            }

            public boolean isShackled() {
                return !isUnshackled();
            }
        }

        private class NudgeTimeRequestCommand extends RequestCommand {
            public NudgeTimeRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.nudgeTime(mlUser);
            }
        }

        private class NudgeTimeResponseCommand extends ResponseCommand {
            public NudgeTimeResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                ReadableInstant readableInstant;
                byte b = byteBuffer.get();
                StringBuilder sb = new StringBuilder();
                sb.append("handleResponse: ");
                sb.append(mLGattCallback.isUserGuest());
                Log.d("LockCommander", sb.toString());
                if (b == 0) {
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else if (b == 2) {
                    DateTime dateTime = new DateTime();
                    ReadableInstant readableInstant2 = null;
                    try {
                        readableInstant = new DateTime(MLDateUtils.parseServerDate(mLGattCallback.getLock().getKmsDeviceKey().getModifiedOn()).getTime());
                        try {
                            readableInstant2 = new DateTime(MLDateUtils.parseServerDate(mLGattCallback.getLock().getKmsDeviceKey().getExpiresOn()).getTime());
                        } catch (ParseException unused) {
                        }
                    } catch (ParseException unused2) {
                        readableInstant = null;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handleResponse: ");
                    sb2.append(mLGattCallback.isUserGuest());
                    Log.d("LockCommander", sb2.toString());
                    if (!mLGattCallback.isUserGuest() && dateTime.isAfter(readableInstant) && dateTime.isBefore(readableInstant2)) {
                        mLGattCallback.addAsNext(new WriteTimeResponseCommand(this.lock));
                        mLGattCallback.addAsNext(new WriteTimeRequestCommand(this.lock));
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class ProcessStateForProximity extends ResponseCommand {
            public ProcessStateForProximity(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    LockStateResponse lockStateResponse = new LockStateResponse(byteBuffer.get());
                    if (lockStateResponse.isLocked()) {
                        if (mLGattCallback.getLock().getLockStatus() != LockStatus.UNLOCKING) {
                            mLGattCallback.updateStatus(LockStatus.LOCK_FOUND);
                        }
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnlocked() && lockStateResponse.isShackled()) {
                        mLGattCallback.updateStatus(LockStatus.UNLOCKED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnshackled()) {
                        mLGattCallback.updateStatus(LockStatus.OPENED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else {
                        mLGattCallback.updateStatus(LockStatus.UNKNOWN);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    }
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class ReadAuditLogRequestCommand extends RequestCommand {
            private final String logTag = ReadAuditLogRequestCommand.class.getSimpleName();

            public ReadAuditLogRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readAuditTrail(mlUser, getMlGattCallback().getLastLogReferenceId());
            }
        }

        private class ReadAuditLogResponseCommand extends ResponseCommand {
            private final String logTag = ReadAuditLogResponseCommand.class.getSimpleName();

            public ReadAuditLogResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                byte b = byteBuffer.get();
                if (b == 0) {
                    mLGattCallback.parseAuditLogdata(byteBuffer);
                    mLGattCallback.addAsNext(new ReadAuditLogResponseCommand(this.lock));
                    mLGattCallback.addAsNext(new ReadAuditLogRequestCommand(this.lock));
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else if (b == 4) {
                    mLGattCallback.addUploadAuditLogsTask();
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class ReadBatteryRequestCommand extends RequestCommand {
            public ReadBatteryRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readBatteryCmd(mlUser);
            }
        }

        private class ReadBatteryResponseCommand extends ResponseCommand {
            public ReadBatteryResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                byte b = byteBuffer.get();
                if (b == 0) {
                    int i = byteBuffer.getInt();
                    byteBuffer.getInt();
                    mLGattCallback.updateBattery(byteBuffer.get(), i);
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else if (b == 4) {
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class ReadConfigurationRequestCommand extends RequestCommand {
            public ReadConfigurationRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readConfigurationCounterCommand(mlUser, this.lock);
            }
        }

        private class ReadConfigurationResponseCommand extends ResponseCommand {
            public static final int THREE_BYTES = 3;
            public static final int TWELVE_BYTES = 12;
            private boolean shouldUpdateTraits = true;

            public ReadConfigurationResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    int i = byteBuffer.getInt();
                    int i2 = byteBuffer.getInt();
                    byteBuffer.position(byteBuffer.position() + 3);
                    byte b = byteBuffer.get();
                    byteBuffer.position(byteBuffer.position() + 12);
                    byte b2 = byteBuffer.get();
                    if (((long) i2) != mLGattCallback.getPrimaryCodeCounter() && !mLGattCallback.isUserGuest()) {
                        mLGattCallback.addAsNext(new ReadPrimaryCodeResponseCommand(this.lock, i2));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeRequestCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeCounterResponseCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeCounterRequestCommand(this.lock));
                        this.shouldUpdateTraits = true;
                    }
                    if (i != mLGattCallback.getCurrentPublicCounter()) {
                        mLGattCallback.addLockConfigTraits(i, b2, b);
                    } else {
                        this.shouldUpdateTraits = false;
                    }
                    if (this.shouldUpdateTraits) {
                        mLGattCallback.addUpdateLockTraitTask();
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ReadFirmwareCounterRequestCommand extends RequestCommand {
            public ReadFirmwareCounterRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readFirmwareCounterCommand(mlUser, this.lock);
            }
        }

        private class ReadFirmwareCounterResponseCommand extends ResponseCommand {
            public ReadFirmwareCounterResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    int i = byteBuffer.getInt();
                    if (i > mLGattCallback.getLock().getFirmwareCounter()) {
                        mLGattCallback.updateFirmwareCounter(i);
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ReadKeySafeConfigurationResponseCommand extends ResponseCommand {
            public static final int THREE_BYTES = 3;
            public static final int TWELVE_BYTES = 12;
            private boolean shouldUpdateTraits = true;

            public ReadKeySafeConfigurationResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    int i = byteBuffer.getInt();
                    int i2 = byteBuffer.getInt();
                    int i3 = byteBuffer.getInt();
                    byteBuffer.position(byteBuffer.position() + 3);
                    byte b = byteBuffer.get();
                    byteBuffer.position(byteBuffer.position() + 12);
                    byte b2 = byteBuffer.get();
                    if (((long) i2) != mLGattCallback.getPrimaryCodeCounter() && !mLGattCallback.isUserGuest()) {
                        mLGattCallback.addAsNext(new ReadPrimaryCodeResponseCommand(this.lock, i2));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeRequestCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeCounterResponseCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadPrimaryCodeCounterRequestCommand(this.lock));
                        this.shouldUpdateTraits = true;
                    }
                    if (((long) i3) != mLGattCallback.getSecondaryCodeCounter() && !mLGattCallback.isUserGuest()) {
                        mLGattCallback.addAsNext(new ReadSecondaryCodesResponseCommand(this.lock, i3));
                        mLGattCallback.addAsNext(new ReadSecondaryCodesRequestCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadSecondaryCodesCounterResponseCommand(this.lock));
                        mLGattCallback.addAsNext(new ReadSecondaryCodesCounterRequestCommand(this.lock));
                        this.shouldUpdateTraits = true;
                    }
                    if (i != mLGattCallback.getCurrentPublicCounter()) {
                        mLGattCallback.addLockConfigTraits(i, b2, b);
                    } else {
                        this.shouldUpdateTraits = false;
                    }
                    if (this.shouldUpdateTraits) {
                        mLGattCallback.addUpdateLockTraitTask();
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ReadPrimaryCodeCounterRequestCommand extends RequestCommand {
            public ReadPrimaryCodeCounterRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readPrimaryCodeCounterCommand(mlUser, this.lock);
            }
        }

        private class ReadPrimaryCodeCounterResponseCommand extends ResponseCommand {
            public ReadPrimaryCodeCounterResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    int i = byteBuffer.getInt();
                    if (((long) i) > mLGattCallback.getLock().getPrimaryCodeCounter()) {
                        mLGattCallback.updatePrimaryCodeCounter(i);
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ReadPrimaryCodeRequestCommand extends RequestCommand {
            public ReadPrimaryCodeRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readPrimaryCodeMemoryCommand(mlUser, this.lock);
            }
        }

        private class ReadPrimaryCodeResponseCommand extends ResponseCommand {
            private int primaryCodeCounter = 0;

            public ReadPrimaryCodeResponseCommand(Lock lock, int i) {
                super(lock);
                this.primaryCodeCounter = i;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                String str;
                if (byteBuffer.get() != 0 || byteBuffer.array().length <= 3) {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                    return;
                }
                if (this.lock.isPadLock()) {
                    str = PrimaryCodeUtils.primaryCodeArrayToLockDirections(Arrays.copyOfRange(byteBuffer.array(), 1, 5));
                } else {
                    str = PrimaryCodeUtils.primaryCodeArrayToKeySafe(Arrays.copyOfRange(byteBuffer.array(), 1, 8));
                }
                int i = this.primaryCodeCounter;
                this.primaryCodeCounter = i + 1;
                mLGattCallback.updatePrimaryCode(str, i);
                mLGattCallback.addUpdateLockTraitTask();
                mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
            }
        }

        private class ReadSecondaryCodesCounterRequestCommand extends RequestCommand {
            public ReadSecondaryCodesCounterRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readSecondaryCodeCounterCommand(mlUser, this.lock);
            }
        }

        private class ReadSecondaryCodesCounterResponseCommand extends ResponseCommand {
            public ReadSecondaryCodesCounterResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    int i = byteBuffer.getInt();
                    if (((long) i) > mLGattCallback.getLock().getSecondaryCodeCounter()) {
                        mLGattCallback.updateSecondaryCodesCounter(i);
                    }
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ReadSecondaryCodesRequestCommand extends RequestCommand {
            public ReadSecondaryCodesRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readSecondaryCodesMemoryCommand(mlUser, this.lock);
            }
        }

        private class ReadSecondaryCodesResponseCommand extends ResponseCommand {
            private int secondaryCodesCounter = 0;

            public ReadSecondaryCodesResponseCommand(Lock lock, int i) {
                super(lock);
                this.secondaryCodesCounter = i;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() != 0 || byteBuffer.array().length <= 3) {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    byte[] bArr = new byte[8];
                    byteBuffer.get(bArr, 0, 8);
                    arrayList.add(i, SecondaryCodeUtils.secondaryCodeArrayToValue(bArr));
                }
                mLGattCallback.updateSecondaryCodes(arrayList, this.secondaryCodesCounter);
                mLGattCallback.addUpdateLockTraitTask();
                mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
            }
        }

        private class RequestLockStateCommand extends RequestCommand {
            public RequestLockStateCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readStateCmd(mlUser);
            }
        }

        private class ResetKeyRequestCommand extends RequestCommand {
            final byte[] mResetKeyData;

            public ResetKeyRequestCommand(Lock lock, byte[] bArr) {
                super(lock);
                this.mResetKeyData = bArr;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.resetKey(mlUser, this.mResetKeyData);
            }
        }

        private class ResetKeyResponseCommand extends ResponseCommand {
            final ResetKeysWrapper resetKeysWrapper;

            public ResetKeyResponseCommand(Lock lock, ResetKeysWrapper resetKeysWrapper2) {
                super(lock);
                this.resetKeysWrapper = resetKeysWrapper2;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.keyReset(this.resetKeysWrapper);
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.uploadKeyReset(this.resetKeysWrapper);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ShackleStateRequest extends RequestCommand {
            public ShackleStateRequest(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.readShackleStateCmd(mlUser);
            }
        }

        private class ShackleStateResponse extends ResponseCommand {
            public ShackleStateResponse(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    LockStateResponse lockStateResponse = new LockStateResponse(byteBuffer.get());
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleResponse: ");
                    sb.append(lockStateResponse.lockState);
                    Log.d("TAG", sb.toString());
                    if (lockStateResponse.isLocked()) {
                        if (mLGattCallback.getLock().getShackleStatus() != ShackleStatus.UNLOCKING) {
                            mLGattCallback.updateShackleStatus(ShackleStatus.LOCK_FOUND);
                        }
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnlocked() && lockStateResponse.isShackled()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.UNLOCKED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnshackled()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.OPENED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (!lockStateResponse.isShackled() || !lockStateResponse.isLocked()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.UNKNOWN);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else {
                        mLGattCallback.updateShackleStatus(ShackleStatus.LOCKED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    }
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class StartSessionRequestCommand extends RequestCommand {
            byte[] mEncryptedProfile;

            public StartSessionRequestCommand(Lock lock) {
                super(lock);
                this.mEncryptedProfile = Base64.decode(lock.getKmsDeviceKey().getProfile(), 0);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.startUserSession(this.mEncryptedProfile);
            }
        }

        private class StartSessionResponseCommand implements IMlCommand {
            private final String logTag;

            private StartSessionResponseCommand() {
                this.logTag = StartSessionResponseCommand.class.getSimpleName();
            }

            public void execute(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                byte[] value = bluetoothGattCharacteristic.getValue();
                if (value[0] == 0 && value.length == 18) {
                    mLGattCallback.getMlUser().setNonce(new MlNonce(Arrays.copyOfRange(value, 1, 7)));
                    MlCommand.decryptResponse(Arrays.copyOfRange(value, 7, value.length), mLGattCallback.getMlUser());
                    if (MlClientApi.getLastError() != 0) {
                        mLGattCallback.closeActiveConnection(bluetoothGatt);
                        return;
                    }
                    mLGattCallback.getMlUser().advanceNonce();
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class UnlockCommand extends RequestCommand {
            public UnlockCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.unlockCmd(mlUser);
            }
        }

        private class UnlockResponseCommand extends ResponseCommand {
            public UnlockResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.updateStatus(LockStatus.UNLOCKED);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class UnlockShackleCommand extends RequestCommand {
            public UnlockShackleCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.unlockShackleCmd(mlUser);
            }
        }

        private class UnlockShackleResponseCommand extends ResponseCommand {
            public UnlockShackleResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.updateShackleStatus(ShackleStatus.UNLOCKED);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class UpdateLockModeRequestCommand extends RequestCommand {
            final byte[] mLockModeConfig;

            public UpdateLockModeRequestCommand(Lock lock, byte[] bArr) {
                super(lock);
                this.mLockModeConfig = bArr;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.updateLockMode(mlUser, this.lock, this.mLockModeConfig);
            }
        }

        private class UpdateLockModeResponseCommand extends ResponseCommand {
            public UpdateLockModeResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updateUnlockMode();
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.addUpdateLockTraitTask();
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class ValidateStateForTouchShackleUnlock extends ResponseCommand {
            public ValidateStateForTouchShackleUnlock(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    LockStateResponse lockStateResponse = new LockStateResponse(byteBuffer.get());
                    if (lockStateResponse.isKeypadActive() && lockStateResponse.isLocked()) {
                        mLGattCallback.addCommand((IMlCommand) new UnlockShackleCommand(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new UnlockShackleResponseCommand(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnlocked() && lockStateResponse.isShackled()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.UNLOCKED);
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnshackled()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.OPENED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (!lockStateResponse.isShackled() || !lockStateResponse.isLocked()) {
                        mLGattCallback.updateShackleStatus(ShackleStatus.UNKNOWN);
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else {
                        mLGattCallback.updateShackleStatus(ShackleStatus.LOCKED);
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    }
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class ValidateStateForTouchUnlock extends ResponseCommand {
            public ValidateStateForTouchUnlock(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    LockStateResponse lockStateResponse = new LockStateResponse(byteBuffer.get());
                    if (lockStateResponse.isKeypadActive() && lockStateResponse.isLocked()) {
                        mLGattCallback.addCommand((IMlCommand) new UnlockCommand(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new UnlockResponseCommand(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnlocked() && lockStateResponse.isShackled()) {
                        mLGattCallback.updateStatus(LockStatus.UNLOCKED);
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (lockStateResponse.isUnshackled()) {
                        mLGattCallback.updateStatus(LockStatus.OPENED);
                        mLGattCallback.addCommand(Builder.this.buildReadConfigCommandList(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new DisconnectPreventAdvertisingRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else if (!lockStateResponse.isShackled() || !lockStateResponse.isLocked()) {
                        mLGattCallback.updateStatus(LockStatus.UNKNOWN);
                        mLGattCallback.addCommand((IMlCommand) new DisconnectRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    } else {
                        mLGattCallback.updateStatus(LockStatus.LOCKED);
                        mLGattCallback.addCommand(Builder.this.buildReadConfigCommandList(this.lock));
                        mLGattCallback.addCommand((IMlCommand) new DisconnectPreventAdvertisingRequestCommand(this.lock));
                        mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    }
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private class WriteFirmwareRequestCommand extends RequestCommand {
            final byte[] firmwareCommandData;

            public WriteFirmwareRequestCommand(Lock lock, byte[] bArr) {
                super(lock);
                this.firmwareCommandData = bArr;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.writeFirmwareCommand(mlUser, this.firmwareCommandData);
            }
        }

        private class WriteFirmwareResponseCommand extends ResponseCommand {
            public WriteFirmwareResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() != 0) {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                } else if (LockCommander.this.stopFirmwareUpdate) {
                    LockCommander.this.stopFirmwareUpdate = false;
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                } else {
                    mLGattCallback.updateFirmwareCommandResponse(mLGattCallback.getCommandList().size());
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                }
            }
        }

        private class WritePrimaryCodeRequestCommand extends RequestCommand {
            final List<LockCodeDirection> primaryCode;
            final String primaryCodeS;

            public WritePrimaryCodeRequestCommand(Lock lock, List<LockCodeDirection> list) {
                super(lock);
                this.primaryCode = list;
                this.primaryCodeS = null;
            }

            public WritePrimaryCodeRequestCommand(Lock lock, String str) {
                super(lock);
                this.primaryCodeS = str;
                this.primaryCode = null;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.writePrimaryCodeCmd(mlUser, this.lock, this.lock.isPadLock() ? PrimaryCodeUtils.primaryCodeDirectionsToByteArray(this.primaryCode) : PrimaryCodeUtils.primaryCodeKeySafeToByteArray(this.primaryCodeS));
            }
        }

        private class WritePrimaryCodeResponseCommand extends WriteResponseCommand {
            public WritePrimaryCodeResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updatePrimaryCodeResponse();
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.addUpdateLockTraitTask();
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }

            public KmsLogEntry getAuditEntry() {
                return new com.masterlock.core.KmsLogEntry.Builder().eventCode(EventCode.UPDATE_PRIMARYCODE).eventSource(EventSource.APP).kmsDeviceId(this.lock.getKmsId()).kmsDeviceKeyAlias(Integer.valueOf(this.lock.getKmsDeviceKey().getAlias())).createdOn(new Date()).build();
            }
        }

        private class WriteRelockTimeRequestCommand extends RequestCommand {
            final int relockTimeInSeconds;

            public WriteRelockTimeRequestCommand(Lock lock, int i) {
                super(lock);
                this.relockTimeInSeconds = i;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                ByteBuffer allocate = ByteBuffer.allocate(1);
                allocate.order(ByteOrder.LITTLE_ENDIAN);
                allocate.put((byte) this.relockTimeInSeconds);
                return MlCommand.writeRelockTimeCmd(mlUser, this.lock, allocate.array());
            }
        }

        private class WriteRelockTimeResponseCommand extends WriteResponseCommand {
            public WriteRelockTimeResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updateRelockTime();
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    mLGattCallback.addUpdateLockTraitTask();
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }

            public KmsLogEntry getAuditEntry() {
                return new com.masterlock.core.KmsLogEntry.Builder().eventCode(EventCode.UPDATE_RELOCKINTERVAL).eventSource(EventSource.APP).eventValue(String.valueOf(this.lock.getRelockTimeInSeconds())).kmsDeviceId(this.lock.getKmsId()).kmsDeviceKeyAlias(Integer.valueOf(this.lock.getKmsDeviceKey().getAlias())).createdOn(new Date()).build();
            }
        }

        private class WriteResponseCommand extends ResponseCommand {
            public KmsLogEntry getAuditEntry() {
                return null;
            }

            public WriteResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.configChangeApplied();
                    processLogs(mLGattCallback);
                    if (LockCommander.this.stopFirmwareUpdate) {
                        LockCommander.this.stopFirmwareUpdate = false;
                        mLGattCallback.closeActiveConnection(bluetoothGatt);
                        return;
                    }
                    mLGattCallback.addCommand(Builder.this.buildReadConfigCommandList(this.lock));
                    mLGattCallback.addCommand((IMlCommand) new DisconnectClearKeypadRequestCommand(this.lock));
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }

            private void processLogs(MLGattCallback mLGattCallback) {
                KmsLogEntry auditEntry = getAuditEntry();
                if (auditEntry != null) {
                    mLGattCallback.uploadLogs(Arrays.asList(new KmsLogEntry[]{auditEntry}));
                }
            }
        }

        private class WriteSecondaryPasscodeRequestCommand extends RequestCommand {
            private SecondaryCodeIndex index;
            final String secondaryPasscode;

            public WriteSecondaryPasscodeRequestCommand(Lock lock, String str, SecondaryCodeIndex secondaryCodeIndex) {
                super(lock);
                this.secondaryPasscode = str;
                this.index = secondaryCodeIndex;
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.writeSecondaryCodeCommand(mlUser, this.lock, this.index, SecondaryCodeUtils.secondaryCodeToByteArray(this.secondaryPasscode));
            }
        }

        private class WriteSecondaryPasscodeResponseCommand extends WriteResponseCommand {
            private SecondaryCodeIndex index;
            final String secondaryPasscode;

            public WriteSecondaryPasscodeResponseCommand(Lock lock, String str, SecondaryCodeIndex secondaryCodeIndex) {
                super(lock);
                this.secondaryPasscode = str;
                this.index = secondaryCodeIndex;
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.updateSecondaryCodeResponse(this.secondaryPasscode, this.index);
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                    return;
                }
                mLGattCallback.closeActiveConnection(bluetoothGatt);
            }
        }

        private class WriteTimeRequestCommand extends RequestCommand {
            public WriteTimeRequestCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public byte[] generateRequest(MlUser mlUser) {
                return MlCommand.writeTime(mlUser);
            }
        }

        private class WriteTimeResponseCommand extends ResponseCommand {
            public WriteTimeResponseCommand(Lock lock) {
                super(lock);
            }

            /* access modifiers changed from: 0000 */
            public void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
                if (byteBuffer.get() == 0) {
                    mLGattCallback.executeNextCommand(bluetoothGatt, bluetoothGattCharacteristic);
                } else {
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                }
            }
        }

        private Builder() {
            this.commandList = new LinkedList<>();
        }

        public Builder startSession(Lock lock) {
            this.commandList.add(new StartSessionRequestCommand(lock));
            this.commandList.add(new StartSessionResponseCommand());
            return this;
        }

        public Builder unlock(Lock lock) {
            this.commandList.add(new UnlockCommand(lock));
            this.commandList.add(new UnlockResponseCommand(lock));
            return this;
        }

        public Builder unlockResponse(Lock lock) {
            this.commandList.add(new UnlockResponseCommand(lock));
            return this;
        }

        public Builder requestLockState(Lock lock) {
            this.commandList.add(new RequestLockStateCommand(lock));
            return this;
        }

        public Builder validateStateForTouchUnlock(Lock lock) {
            this.commandList.add(new ValidateStateForTouchUnlock(lock));
            return this;
        }

        public Builder processProximityLockState(Lock lock) {
            this.commandList.add(new ProcessStateForProximity(lock));
            return this;
        }

        public Builder requestBattery(Lock lock) {
            this.commandList.add(new RequestLockStateCommand(lock));
            return this;
        }

        public Builder disconnect(Lock lock) {
            this.commandList.add(new DisconnectRequestCommand(lock));
            return this;
        }

        public Builder disconnectClearKeypad(Lock lock) {
            this.commandList.add(new DisconnectClearKeypadRequestCommand(lock));
            return this;
        }

        public Builder disconnectPreventAdvertising(Lock lock) {
            this.commandList.add(new DisconnectPreventAdvertisingRequestCommand(lock));
            return this;
        }

        public Builder readPrimaryCodeCounterCommand(Lock lock) {
            this.commandList.add(new ReadPrimaryCodeCounterRequestCommand(lock));
            this.commandList.add(new ReadPrimaryCodeCounterResponseCommand(lock));
            return this;
        }

        public Builder writePrimaryCode(Lock lock, List<LockCodeDirection> list) {
            this.commandList.add(new WritePrimaryCodeRequestCommand(lock, list));
            return this;
        }

        public Builder writePrimaryCode(Lock lock, String str) {
            this.commandList.add(new WritePrimaryCodeRequestCommand(lock, str));
            return this;
        }

        public Builder writeRelockTime(Lock lock, int i) {
            this.commandList.add(new WriteRelockTimeRequestCommand(lock, i));
            return this;
        }

        public Builder updateLockMode(Lock lock, byte[] bArr) {
            this.commandList.add(new UpdateLockModeRequestCommand(lock, bArr));
            this.commandList.add(new UpdateLockModeResponseCommand(lock));
            return this;
        }

        public Builder resetKey(Lock lock, byte[] bArr) {
            this.commandList.add(new ResetKeyRequestCommand(lock, bArr));
            return this;
        }

        public Builder writeFirmwareCommand(Lock lock, byte[] bArr) {
            this.commandList.add(new WriteFirmwareRequestCommand(lock, bArr));
            return this;
        }

        public Builder verifyWriteFirmwareCommand(Lock lock) {
            this.commandList.add(new WriteFirmwareResponseCommand(lock));
            return this;
        }

        public Builder addCommandToQueue(WriteFirmwareRequestCommand writeFirmwareRequestCommand) {
            this.commandList.add(writeFirmwareRequestCommand);
            return this;
        }

        public Builder verifyWriteRelockTimeCommand(Lock lock) {
            this.commandList.add(new WriteRelockTimeResponseCommand(lock));
            return this;
        }

        public Builder verifyWritePrimaryCodeCommand(Lock lock) {
            this.commandList.add(new WritePrimaryCodeResponseCommand(lock));
            return this;
        }

        public Builder verifyWriteCommand(Lock lock) {
            this.commandList.add(new WriteResponseCommand(lock));
            return this;
        }

        public Builder verifyResetKeyCommand(Lock lock, ResetKeysWrapper resetKeysWrapper) {
            this.commandList.add(new ResetKeyResponseCommand(lock, resetKeysWrapper));
            return this;
        }

        public Builder syncStateCommand(Lock lock) {
            this.commandList.addAll(buildReadConfigCommandList(lock));
            return this;
        }

        public Builder readShackleState(Lock lock) {
            this.commandList.add(new ShackleStateRequest(lock));
            this.commandList.add(new ShackleStateResponse(lock));
            return this;
        }

        public Builder validateStateForTouchShackleUnlock(Lock lock) {
            this.commandList.add(new ShackleStateRequest(lock));
            this.commandList.add(new ValidateStateForTouchShackleUnlock(lock));
            return this;
        }

        public Builder unlockShackle(Lock lock) {
            this.commandList.add(new UnlockShackleCommand(lock));
            this.commandList.add(new UnlockShackleResponseCommand(lock));
            return this;
        }

        public Builder readSecondaryCodesCounter(Lock lock) {
            this.commandList.add(new ReadSecondaryCodesCounterRequestCommand(lock));
            this.commandList.add(new ReadSecondaryCodesCounterResponseCommand(lock));
            return this;
        }

        public Builder writeSecondaryPasscode(Lock lock, String str, SecondaryCodeIndex secondaryCodeIndex) {
            this.commandList.add(new WriteSecondaryPasscodeRequestCommand(lock, str, secondaryCodeIndex));
            this.commandList.add(new WriteSecondaryPasscodeResponseCommand(lock, str, secondaryCodeIndex));
            return this;
        }

        public Builder deleteSecondaryPasscode(Lock lock, SecondaryCodeIndex secondaryCodeIndex) {
            this.commandList.add(new ClearSecondaryPasscodeByIdRequestCommand(lock, secondaryCodeIndex));
            this.commandList.add(new ClearSecondaryPasscodeByIdResponseCommand(lock, secondaryCodeIndex));
            return this;
        }

        public Builder finishWriteSecondaryPasscodes() {
            this.commandList.add(new FinishSecondaryPasscodeWrite());
            return this;
        }

        public Builder restoreSecondaryPasscodes(Lock lock) {
            for (int i = 0; i < 5; i++) {
                this.commandList.add(new WriteSecondaryPasscodeRequestCommand(lock, lock.getSecondaryCodeAt(SecondaryCodeIndex.fromValue(i)), SecondaryCodeIndex.fromValue(i)));
            }
            return this;
        }

        public LinkedList<IMlCommand> build() {
            return this.commandList;
        }

        /* access modifiers changed from: private */
        public LinkedList<IMlCommand> buildReadConfigCommandList(Lock lock) {
            LinkedList<IMlCommand> linkedList = new LinkedList<>();
            linkedList.add(new ReadFirmwareCounterRequestCommand(lock));
            linkedList.add(new ReadFirmwareCounterResponseCommand(lock));
            linkedList.add(new NudgeTimeRequestCommand(lock));
            linkedList.add(new NudgeTimeResponseCommand(lock));
            linkedList.add(new ReadBatteryRequestCommand(lock));
            linkedList.add(new ReadBatteryResponseCommand(lock));
            if (lock.isPadLock()) {
                linkedList.add(new ReadConfigurationRequestCommand(lock));
                linkedList.add(new ReadConfigurationResponseCommand(lock));
            } else {
                linkedList.add(new ReadConfigurationRequestCommand(lock));
                linkedList.add(new ReadKeySafeConfigurationResponseCommand(lock));
            }
            linkedList.add(new ReadAuditLogRequestCommand(lock));
            linkedList.add(new ReadAuditLogResponseCommand(lock));
            return linkedList;
        }
    }

    interface IMlCommand {
        void execute(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback);
    }

    abstract class RequestCommand implements IMlCommand {
        BluetoothGattCharacteristic characteristic;
        BluetoothGatt gatt;
        protected Lock lock;
        private final String logTag = getClass().getSimpleName();
        protected MLGattCallback mlGattCallback;

        /* access modifiers changed from: 0000 */
        public abstract byte[] generateRequest(MlUser mlUser);

        public RequestCommand(Lock lock2) {
            this.lock = lock2;
        }

        public void execute(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
            this.mlGattCallback = mLGattCallback;
            this.characteristic = bluetoothGattCharacteristic;
            this.gatt = bluetoothGatt;
            byte[] generateRequest = generateRequest(mLGattCallback.getMlUser());
            StringBuilder sb = new StringBuilder();
            sb.append("Lock: ");
            sb.append(this.lock.getKmsDeviceKey().getDeviceId());
            sb.append(" SENT: ");
            sb.append(getClass().getSimpleName());
            sb.append(" : ");
            sb.append(MlUtils.byteToHexString(generateRequest));
            Log.i("TAG", sb.toString());
            bluetoothGattCharacteristic.setValue(generateRequest);
            bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }

        public MLGattCallback getMlGattCallback() {
            return this.mlGattCallback;
        }
    }

    abstract class ResponseCommand implements IMlCommand {
        protected Lock lock;
        protected final String logTag = getClass().getSimpleName();

        /* access modifiers changed from: 0000 */
        public abstract void handleResponse(ByteBuffer byteBuffer, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback);

        public ResponseCommand(Lock lock2) {
            this.lock = lock2;
        }

        public void execute(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt, MLGattCallback mLGattCallback) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value[0] == 0) {
                ByteBuffer decryptResponse = MlCommand.decryptResponse(Arrays.copyOfRange(value, 1, value.length), mLGattCallback.getMlUser());
                int lastError = MlClientApi.getLastError();
                if (lastError != 0) {
                    switch (lastError) {
                        case 101:
                        case 102:
                            if (!mLGattCallback.isLongReadPerformed()) {
                                bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
                                return;
                            }
                            break;
                    }
                    mLGattCallback.closeActiveConnection(bluetoothGatt);
                    return;
                }
                mLGattCallback.getMlUser().advanceNonce();
                ByteBuffer order = decryptResponse.order(ByteOrder.LITTLE_ENDIAN);
                StringBuilder sb = new StringBuilder();
                sb.append("Lock: ");
                sb.append(this.lock.getKmsDeviceKey().getDeviceId());
                sb.append(" GOT: ");
                sb.append(getClass().getSimpleName());
                sb.append(" : ");
                sb.append(MlUtils.byteToHexString(order.array()));
                Log.i("TAG", sb.toString());
                handleResponse(order, bluetoothGattCharacteristic, bluetoothGatt, mLGattCallback);
                return;
            }
            mLGattCallback.closeActiveConnection(bluetoothGatt);
        }
    }

    public LockCommander(Context context) {
        this.mContext = context;
    }

    public void proximityUnlock(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_UNLOCK_PRIMARY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).unlock(lock).disconnect(lock).build());
        }
    }

    public void proximityReadState(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        LinkedList linkedList;
        if (lock.canSendCommand(AvailableCommandType.CMD_READ_STATE_PRIMARY)) {
            if (lock.isShackledKeySafe()) {
                linkedList = new Builder().startSession(lock).readShackleState(lock).requestLockState(lock).processProximityLockState(lock).disconnect(lock).build();
            } else {
                linkedList = new Builder().startSession(lock).requestLockState(lock).processProximityLockState(lock).disconnect(lock).build();
            }
            connectGatt(lock, bluetoothDevice, lockListener, linkedList);
        }
    }

    public void syncState(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_READ_MEMORY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).syncStateCommand(lock).disconnect(lock).build());
        }
    }

    public void touchUnlock(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        LinkedList linkedList;
        if (lock.canSendCommand(AvailableCommandType.CMD_READ_STATE_PRIMARY)) {
            if (lock.isShackledKeySafe()) {
                linkedList = new Builder().startSession(lock).readShackleState(lock).requestLockState(lock).validateStateForTouchUnlock(lock).build();
            } else {
                linkedList = new Builder().startSession(lock).requestLockState(lock).validateStateForTouchUnlock(lock).build();
            }
            connectGatt(lock, bluetoothDevice, lockListener, linkedList);
        }
    }

    public void writePrimaryCode(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        LinkedList linkedList;
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_MEMORY)) {
            if (lock.isPadLock()) {
                linkedList = new Builder().startSession(lock).readPrimaryCodeCounterCommand(lock).writePrimaryCode(lock, lock.generateLockPrimaryCodeList()).verifyWritePrimaryCodeCommand(lock).disconnect(lock).build();
            } else {
                linkedList = new Builder().startSession(lock).readPrimaryCodeCounterCommand(lock).writePrimaryCode(lock, lock.getPrimaryCode()).verifyWritePrimaryCodeCommand(lock).disconnect(lock).build();
            }
            connectGatt(lock, bluetoothDevice, lockListener, linkedList);
        }
    }

    public void writeRelockTime(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_MEMORY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).writeRelockTime(lock, lock.getRelockTimeInSeconds()).verifyWriteRelockTimeCommand(lock).disconnect(lock).build());
        }
    }

    public void changeLockMode(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_MEMORY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).updateLockMode(lock, MlUtils.hexStringToByteArray(lock.getLockMode() == LockMode.TOUCH ? lock.getTouchModeConfiguration() : lock.getProximitySwipeModeConfiguration())).verifyWriteCommand(lock).disconnect(lock).build());
        }
    }

    public void resetKeys(BluetoothDevice bluetoothDevice, ResetKeysWrapper resetKeysWrapper, LockListener lockListener) {
        if (resetKeysWrapper.getLock().canSendCommand(AvailableCommandType.CMD_WRITE_AUTHENTICATED_MEMORY)) {
            connectGatt(resetKeysWrapper.getLock(), bluetoothDevice, lockListener, new Builder().startSession(resetKeysWrapper.getLock()).resetKey(resetKeysWrapper.getLock(), Base64.decode(((Command) resetKeysWrapper.getCommandsResponse().commandList.get(0)).commandString, 0)).verifyResetKeyCommand(resetKeysWrapper.getLock(), resetKeysWrapper).disconnectPreventAdvertising(resetKeysWrapper.getLock()).build());
        }
    }

    public void writeFirmwareUpdate(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_AUTHENTICATED_MEMORY)) {
            String firmwareUpdateCommand = lock.getFirmwareUpdateCommand();
            byte[] decode = Base64.decode(firmwareUpdateCommand, 0);
            if (firmwareUpdateCommand.equals(lock.getFirmwareFirstCommand())) {
                this.firstFirmwareUpdateCommand = true;
                connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).writeFirmwareCommand(lock, decode).verifyWriteFirmwareCommand(lock).disconnect(lock).build());
            } else {
                this.firstFirmwareUpdateCommand = false;
                LinkedList build = new Builder().startSession(lock).build();
                int i = 0;
                int i2 = 0;
                do {
                    build.add(new Builder().writeFirmwareCommand(lock, Base64.decode((String) ((Map) lock.getFirmwareUpdateCommands().get(i)).get("Command"), 0)).build().getFirst());
                    build.add(new Builder().verifyWriteFirmwareCommand(lock).build().getFirst());
                    i2++;
                    i++;
                } while (i < lock.getFirmwareUpdateCommands().size());
                lock.setTotalNumberOfCommands(i2);
                lock.setNumberOfCommands(i2);
                lock.getFirmwareUpdateCommands().clear();
                lock.setLockStatus(LockStatus.UNKNOWN);
                build.add(new Builder().disconnect(lock).build().getFirst());
                connectGatt(lock, bluetoothDevice, lockListener, build);
            }
        }
    }

    public void preventAdvertising(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_DISCONNECT)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).disconnectPreventAdvertising(lock).build());
        }
    }

    public void reconfigureLock(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        LinkedList linkedList;
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_MEMORY)) {
            if (lock.isPadLock()) {
                linkedList = new Builder().startSession(lock).writePrimaryCode(lock, lock.generateLockPrimaryCodeList()).verifyWritePrimaryCodeCommand(lock).updateLockMode(lock, MlUtils.hexStringToByteArray(lock.getLockMode() == LockMode.TOUCH ? lock.getTouchModeConfiguration() : lock.getProximitySwipeModeConfiguration())).writeRelockTime(lock, lock.getRelockTimeInSeconds()).verifyWriteRelockTimeCommand(lock).build();
            } else {
                linkedList = new Builder().startSession(lock).writePrimaryCode(lock, lock.getPrimaryCode()).verifyWritePrimaryCodeCommand(lock).updateLockMode(lock, MlUtils.hexStringToByteArray(lock.getLockMode() == LockMode.TOUCH ? lock.getTouchModeConfiguration() : lock.getProximitySwipeModeConfiguration())).writeRelockTime(lock, lock.getRelockTimeInSeconds()).verifyWriteRelockTimeCommand(lock).restoreSecondaryPasscodes(lock).build();
            }
            connectGatt(lock, bluetoothDevice, lockListener, linkedList);
        }
    }

    public void touchUnlockShackle(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_UNLOCK_SECONDARY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).validateStateForTouchShackleUnlock(lock).build());
        }
    }

    public void proximityUnlockShackle(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_UNLOCK_SECONDARY)) {
            connectGatt(lock, bluetoothDevice, lockListener, new Builder().startSession(lock).readShackleState(lock).unlockShackle(lock).disconnect(lock).build());
        }
    }

    public void writeSecondaryCodes(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        if (lock.canSendCommand(AvailableCommandType.CMD_WRITE_MEMORY)) {
            LinkedList build = new Builder().startSession(lock).readSecondaryCodesCounter(lock).build();
            for (int i = 0; i < 5; i++) {
                String secondaryCodeAt = lock.getSecondaryCodeAt(SecondaryCodeIndex.fromValue(i));
                switch (SecondaryCodesUtil.getOperationTypeForCode(i, secondaryCodeAt)) {
                    case CREATE:
                        build.addAll(new Builder().writeSecondaryPasscode(lock, secondaryCodeAt, SecondaryCodeIndex.fromValue(i)).build());
                        break;
                    case UPDATE:
                        build.addAll(new Builder().writeSecondaryPasscode(lock, secondaryCodeAt, SecondaryCodeIndex.fromValue(i)).build());
                        break;
                    case REMOVE:
                        build.addAll(new Builder().deleteSecondaryPasscode(lock, SecondaryCodeIndex.fromValue(i)).build());
                        break;
                }
            }
            build.add(new Builder().finishWriteSecondaryPasscodes().build().getFirst());
            build.add(new Builder().disconnectClearKeypad(lock).build().getFirst());
            connectGatt(lock, bluetoothDevice, lockListener, build);
        }
    }

    private void connectGatt(Lock lock, BluetoothDevice bluetoothDevice, LockListener lockListener, LinkedList<IMlCommand> linkedList) {
        try {
            if (bluetoothDevice.connectGatt(this.mContext, false, new MLGattCallback(linkedList, lock, lockListener, new MlUser(new MlKey(Base64.decode(lock.getKmsDeviceKey().getValue(), 0))))) == null) {
                lockListener.onLockDisconnect(lock);
            }
        } catch (Throwable unused) {
            lockListener.onLockDisconnect(lock);
        }
    }

    public void setStopFirmwareUpdate(boolean z) {
        this.stopFirmwareUpdate = z;
    }
}
