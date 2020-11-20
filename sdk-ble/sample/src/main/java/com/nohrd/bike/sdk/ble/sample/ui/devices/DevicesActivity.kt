package com.nohrd.bike.sdk.ble.sample.ui.devices

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent

class DevicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DevicesView(
                devices = emptyList(),
                onDeviceClick = {}
            )
        }
    }
}
