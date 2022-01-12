package com.nohrd.bike.sample

import android.bluetooth.BluetoothAdapter
import com.nohrd.bike.sample.bluetooth.discovery.AndroidBleScanner

class AppComponent {

    val bleScanner by lazy {
        AndroidBleScanner(BluetoothAdapter.getDefaultAdapter())
    }
}
