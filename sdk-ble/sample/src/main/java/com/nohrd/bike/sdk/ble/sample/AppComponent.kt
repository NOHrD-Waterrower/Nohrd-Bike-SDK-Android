package com.nohrd.bike.sdk.ble.sample

import android.bluetooth.BluetoothAdapter
import com.nohrd.bike.sdk.ble.sample.bluetooth.discovery.AndroidBleScanner

class AppComponent {

    val bleScanner by lazy {
        AndroidBleScanner(BluetoothAdapter.getDefaultAdapter())
    }
}
