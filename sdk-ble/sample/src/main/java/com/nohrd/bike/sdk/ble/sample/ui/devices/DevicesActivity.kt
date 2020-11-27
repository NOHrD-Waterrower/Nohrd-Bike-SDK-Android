package com.nohrd.bike.sdk.ble.sample.ui.devices

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import com.nohrd.bike.sdk.ble.BikeService
import com.nohrd.bike.sdk.ble.sample.nohrdBikeBleSampleApplication
import com.nohrd.bike.sdk.ble.sample.ui.Device
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.DeviceDetailsActivity
import com.nohrd.bike.sdk.ble.sample.ui.theming.AppTheme
import com.nohrd.bike.sdk.ble.sample.util.Cancellable

class DevicesActivity : AppCompatActivity() {

    private var devices by mutableStateOf(listOf<Device>())

    private var scanCancellable: Cancellable? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                DevicesView(
                    devices,
                    onDeviceClick = { device ->
                        startActivity(DeviceDetailsActivity.intent(this, device))
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        scanCancellable = nohrdBikeBleSampleApplication.appComponent.bleScanner
            .startScan(
                listOf(BikeService.uuid),
                object : ScanCallback() {

                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        handleScanResult(result)
                    }
                }
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 42)
            }
        }
    }

    private fun handleScanResult(scanResult: ScanResult) {
        if (scanResult.device?.name == null) return

        if (devices.any { it.address == scanResult.device.address }) return

        devices += Device(
            scanResult.device.address,
            scanResult.device.name,
        )
    }

    override fun onPause() {
        scanCancellable = null
        super.onPause()
    }
}
