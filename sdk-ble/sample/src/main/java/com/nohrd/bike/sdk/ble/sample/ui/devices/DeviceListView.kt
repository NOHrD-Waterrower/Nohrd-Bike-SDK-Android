package com.nohrd.bike.sdk.ble.sample.ui.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.nohrd.bike.sdk.ble.sample.ui.Device

@Composable
fun DeviceListView(
    devices: List<Device>,
    onClick: (Device) -> Unit,
) {
    LazyColumnFor(
        items = devices
    ) { device ->
        DeviceRow(device = device, onClick = { onClick(device) })
    }
}

@Composable
private fun DeviceRow(
    device: Device,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                enabled = true
            )
            .padding(8.dp)
            .fillMaxWidth(),
        children = {
            Text(text = device.name)
        }
    )
}

@Preview
@Composable
fun DeviceListViewPreview() {
    DeviceListView(
        (1..20).map {
            Device(address = "Address $it", name = "Device $it")
        },
        onClick = { println("Clicked $it") }
    )
}
