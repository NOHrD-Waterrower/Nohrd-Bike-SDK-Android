package com.nohrd.bike.sdk.ble.sample.ui.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import com.nohrd.bike.sdk.ble.sample.ui.theming.AppTheme

@Composable
fun DevicesView(
    devices: List<Device>,
    onDeviceClick: (Device) -> Unit,
) {
    Column {
        TopAppBar(title = { Text("Available devices") })
        DeviceListView(
            devices = devices,
            onClick = onDeviceClick
        )
    }
}

@Preview
@Composable
fun DevicesViewPreview() {
    AppTheme {
        DevicesView(
            (1..20).map {
                Device(address = "Address $it", name = "Device $it")
            },
            onDeviceClick = { device -> println("Device clicked: $device") }
        )
    }
}
