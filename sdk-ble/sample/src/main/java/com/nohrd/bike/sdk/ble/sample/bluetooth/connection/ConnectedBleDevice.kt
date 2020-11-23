package com.nohrd.bike.sdk.ble.sample.bluetooth.connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import com.nohrd.bike.sdk.ble.sample.bluetooth.connection.internal.BleConnectionBluetoothGattCallback
import com.nohrd.bike.sdk.ble.sample.util.Cancellable
import java.util.UUID

/**
 * Represents an active connection with a [BluetoothDevice].
 */
class ConnectedBleDevice internal constructor(
    private val callback: BleConnectionBluetoothGattCallback,
) {

    /**
     * Enables characteristic notifications for given [serviceUUID] and [characteristicUUID]
     * and invokes [callback] when notifications arrive.
     *
     * For simplicity for this sample app, this function does not disable the characteristic
     * notifications when invoking the resulting cancellable.
     */
    fun listen(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        callback: (ByteArray) -> Unit,
    ): Cancellable {
        val listener: (BluetoothGattCharacteristic) -> Unit = { characteristic ->
            if (characteristic.uuid == characteristicUUID) {
                // Copy the byte array: Android internals may recycle the instance.
                val bytes = ByteArray(characteristic.value.size) { index -> characteristic.value[index] }
                callback.invoke(bytes)
            }
        }

        this.callback.addCharacteristicChangedListener(listener)
        this.callback.enableNotifications(serviceUUID, characteristicUUID)

        return Cancellable {
            this.callback.removeCharacteristicChangedListener(listener)
        }
    }
}
