package com.nohrd.bike.sdkv1.ble.sample.bluetooth.connection

sealed class BleConnectionState {
    object Disconnected : BleConnectionState()
    object Connecting : BleConnectionState()
    class Connected(val device: ConnectedBleDevice) : BleConnectionState()
    object Failed : BleConnectionState()
}
