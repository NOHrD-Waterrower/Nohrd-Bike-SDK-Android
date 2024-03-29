package com.nohrd.bike.sdkv1.ble.sample.bluetooth.connection

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log.e
import android.util.Log.i
import com.nohrd.bike.sdkv1.ble.sample.bluetooth.connection.BleConnection.ConnectionStateListener
import com.nohrd.bike.sdkv1.ble.sample.bluetooth.connection.internal.BleConnectionBluetoothGattCallback
import com.nohrd.bike.sdkv1.ble.sample.util.Cancellable

/**
 * Represents a connection to a BLE device.
 *
 * The connection can be opened using [open], use the
 * [ConnectionStateListener] to eventually receive a [ConnectedBleDevice].
 *
 * @see BleConnectionFactory to obtain instances of a [BleConnection].
 */
class BleConnection(
    private val context: Context,
    private val bluetoothDevice: BluetoothDevice,
) {

    private val bleConnectionCallback = BleConnectionBluetoothGattCallback()
    private val bleConnectionCallbackListener = BleConnectionCallbackConnectionStateListener()

    private var connectionState: BleConnectionState = BleConnectionState.Disconnected
        set(value) {
            field = value
            listeners.forEach { it.onConnectionStateChanged(value) }
        }

    private var listeners = emptyList<ConnectionStateListener>()

    /**
     * Opens a connection to the [bluetoothDevice].
     *
     * Listeners registered with [addConnectionStateListener] will be invoked with
     * connection status updates; use these updates to obtain a [ConnectedBleDevice]
     * instance.
     */
    fun open() {
        i("BleConnection", "Opening connection to $bluetoothDevice")
        val gatt = bluetoothDevice.connectGatt(context, true, bleConnectionCallback)
        if (gatt == null) {
            e("BleConnection", "Could not open connection: gatt unavailable.")
            connectionState = BleConnectionState.Failed
            return
        }

        bleConnectionCallback.onGattAvailable(gatt)
    }

    /**
     * Closes the connection with the [bluetoothDevice].
     */
    fun close() {
        i("BleConnection", "Closing connection to $bluetoothDevice")
        bleConnectionCallback.disconnect()
    }

    /**
     * Registers given listener to receive connection state changes.
     *
     * The listener will immediately be notified of the current connection state.
     * The [BleConnectionState.Connected] instance will have a reference to a
     * [ConnectedBleDevice] which can be used to communicate with the BLE device.
     *
     * @return a [Cancellable] that can be invoked to stop listening.
     */
    fun addConnectionStateListener(listener: ConnectionStateListener): Cancellable {
        listeners += listener

        if (listeners.size == 1) {
            // Registering with the BleConnectionBluetoothCallback results in a direct update
            bleConnectionCallback.addConnectionStateListener(bleConnectionCallbackListener)
        } else {
            listener.onConnectionStateChanged(connectionState)
        }

        return Cancellable { removeListener(listener) }
    }

    private fun removeListener(listener: ConnectionStateListener) {
        listeners -= listener

        if (listeners.isEmpty()) {
            bleConnectionCallback.removeConnectionStateListener(bleConnectionCallbackListener)
        }
    }

    interface ConnectionStateListener {

        fun onConnectionStateChanged(connectionState: BleConnectionState)
    }

    private inner class BleConnectionCallbackConnectionStateListener : BleConnectionBluetoothGattCallback.ConnectionStateListener {

        override fun onConnectionStateChanged(connectionState: BleConnectionBluetoothGattCallback.ConnectionState) {
            this@BleConnection.connectionState = when (connectionState) {
                is BleConnectionBluetoothGattCallback.ConnectionState.Disconnected -> BleConnectionState.Disconnected
                is BleConnectionBluetoothGattCallback.ConnectionState.Connecting -> BleConnectionState.Connecting
                is BleConnectionBluetoothGattCallback.ConnectionState.Failed -> BleConnectionState.Failed
                is BleConnectionBluetoothGattCallback.ConnectionState.Connected -> BleConnectionState.Connected(ConnectedBleDevice(connectionState.callback))
            }
        }
    }
}
