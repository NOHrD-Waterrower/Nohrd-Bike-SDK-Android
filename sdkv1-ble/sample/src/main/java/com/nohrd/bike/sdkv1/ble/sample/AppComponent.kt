package com.nohrd.bike.sdkv1.ble.sample

import android.bluetooth.BluetoothAdapter
import com.nohrd.bike.sdkv1.ble.sample.bluetooth.discovery.AndroidBleScanner

class AppComponent {

    val bleScanner by lazy {
        AndroidBleScanner(BluetoothAdapter.getDefaultAdapter())
    }
}
