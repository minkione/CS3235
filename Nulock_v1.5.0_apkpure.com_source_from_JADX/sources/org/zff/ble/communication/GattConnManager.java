package org.zff.ble.communication;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.zff.ble.communication.message.CommMessage;

public class GattConnManager {
    private String TAG = "GattConnManager";
    private ArrayList<GattConnection> mListConnection = new ArrayList<>();
    private OnGattConnManagerListener mOnGattConnManager = null;

    public interface OnGattConnManagerListener {
        void onGattConnected(GattConnection gattConnection);

        void onGattDisconnect(GattConnection gattConnection);

        void onGattDiscoverServiceFailed(GattConnection gattConnection);

        void onGattDiscoverServiceSuccess(GattConnection gattConnection);

        void onGattReceiveMessage(GattConnection gattConnection, CommMessage commMessage);
    }

    public GattConnManager(OnGattConnManagerListener l) {
        this.mOnGattConnManager = l;
    }

    public void addGatt(GattConnection gatt) {
        if (!this.mListConnection.contains(gatt)) {
            gatt.setOnGattConnManagerListener(this.mOnGattConnManager);
            this.mListConnection.add(gatt);
            Log.i(this.TAG, "addGatt, list.size-->" + gatt.getMacAddress() + ", " + size());
        }
    }

    public void removeGatt(GattConnection gatt) {
        if (this.mListConnection.contains(gatt)) {
            gatt.setOnGattConnManagerListener(null);
            this.mListConnection.remove(gatt);
            Log.i(this.TAG, "removeGatt, list.size-->" + gatt.getMacAddress() + ", " + size());
        }
    }

    public GattConnection get(int index) {
        return (GattConnection) this.mListConnection.get(index);
    }

    public int size() {
        return this.mListConnection.size();
    }

    public void clear() {
        this.mListConnection.clear();
    }

    public boolean isContains(GattConnection gatt) {
        return this.mListConnection.contains(gatt);
    }

    public GattConnection isContainsGattConn(BluetoothDevice device) {
        String address = device.getAddress();
        Iterator it = this.mListConnection.iterator();
        while (it.hasNext()) {
            GattConnection gatt = (GattConnection) it.next();
            String mac = gatt.getMacAddress();
            if (address.equals(mac)) {
                Log.i(this.TAG, new StringBuilder(String.valueOf(mac)).append(":isContains").toString());
                return gatt;
            }
        }
        return null;
    }

    public GattConnection isContainsGattConn(String macAddress) {
        if (macAddress != null) {
            Iterator it = this.mListConnection.iterator();
            while (it.hasNext()) {
                GattConnection gatt = (GattConnection) it.next();
                if (macAddress.equals(gatt.getMacAddress())) {
                    return gatt;
                }
            }
        }
        return null;
    }
}
