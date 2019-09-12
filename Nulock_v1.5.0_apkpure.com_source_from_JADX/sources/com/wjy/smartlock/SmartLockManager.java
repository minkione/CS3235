package com.wjy.smartlock;

import android.bluetooth.BluetoothDevice;
import com.wjy.smartlock.SmartLockEvent.EventType;
import java.util.ArrayList;
import java.util.Iterator;

public class SmartLockManager {
    private String TAG = "SmartLockManager";
    private ArrayList<OnSmartLockManagerListener> mListManagerListener = new ArrayList<>();
    private ArrayList<SmartLock> mListSmartLock = new ArrayList<>();

    public interface OnSmartLockManagerListener {
        void onAddSmartLock(SmartLock smartLock);

        void onRemoveSmartLock(SmartLock smartLock);

        void onScanSmartLock();

        void onSmartLockEvent(SmartLock smartLock, EventType eventType);

        void onSmartLockPretreatmentEvent(SmartLock smartLock, EventType eventType);

        void onStopScanSmartLock();
    }

    public void addSmartLock(SmartLock smLock) {
        if (!isContains(smLock)) {
            this.mListSmartLock.add(smLock);
        }
    }

    public void removeSmartLock(SmartLock smLock) {
        if (isContains(smLock)) {
            this.mListSmartLock.remove(smLock);
        }
    }

    public SmartLock get(int index) {
        if (index >= 0 && index < this.mListSmartLock.size()) {
            return (SmartLock) this.mListSmartLock.get(index);
        }
        return null;
    }

    public void removeSmartLock(int index) {
        removeSmartLock((SmartLock) this.mListSmartLock.get(index));
    }

    public int getSmLockSize() {
        return this.mListSmartLock.size();
    }

    public void clear() {
        this.mListSmartLock.clear();
    }

    public boolean isContains(SmartLock smLock) {
        if (smLock == null) {
            return false;
        }
        return isContains(smLock.getMac());
    }

    private boolean isContains(String address) {
        if (address == null) {
            return false;
        }
        Iterator it = this.mListSmartLock.iterator();
        while (it.hasNext()) {
            if (address.equals(((SmartLock) it.next()).getMac())) {
                return true;
            }
        }
        return false;
    }

    public SmartLock isContainsSmartLock(BluetoothDevice device) {
        String address = device.getAddress();
        Iterator it = this.mListSmartLock.iterator();
        while (it.hasNext()) {
            SmartLock smLock = (SmartLock) it.next();
            if (address.equals(smLock.getMac())) {
                return smLock;
            }
        }
        return null;
    }

    public SmartLock isContainsSmartLock(String macAddress) {
        if (macAddress == null) {
            return null;
        }
        Iterator it = this.mListSmartLock.iterator();
        while (it.hasNext()) {
            SmartLock smLock = (SmartLock) it.next();
            if (macAddress.equals(smLock.getMac())) {
                return smLock;
            }
        }
        return null;
    }

    public void scanSmartLock() {
    }

    public void addManagerListener(OnSmartLockManagerListener l) {
        if (!this.mListManagerListener.contains(l)) {
            this.mListManagerListener.add(l);
        }
    }

    public void removeManagerListener(OnSmartLockManagerListener l) {
        if (this.mListManagerListener.contains(l)) {
            this.mListManagerListener.remove(l);
        }
    }

    public void notifyManagerAddSmartLock(SmartLock smLock) {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onAddSmartLock(smLock);
        }
    }

    public void notifyManagerRemoveSmartLock(SmartLock smLock) {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onRemoveSmartLock(smLock);
        }
    }

    public void notifyManagerSmartLockEvent(SmartLock smLock, EventType type) {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onSmartLockEvent(smLock, type);
        }
    }

    public void notifyManagerSmartLockPreEvent(SmartLock smLock, EventType type) {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onSmartLockPretreatmentEvent(smLock, type);
        }
    }

    public void notifyManagerScanSmartLock() {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onScanSmartLock();
        }
    }

    public void notifyManagerStopScanSmartLock() {
        for (int i = 0; i < this.mListManagerListener.size(); i++) {
            ((OnSmartLockManagerListener) this.mListManagerListener.get(i)).onStopScanSmartLock();
        }
    }
}
