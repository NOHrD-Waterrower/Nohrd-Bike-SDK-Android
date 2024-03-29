package com.nohrd.bike.sdkv1.ble.sample.bluetooth.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log.e
import android.util.Log.i
import android.util.Log.v
import android.util.Log.w
import com.nohrd.bike.sdkv1.ble.sample.util.Cancellable
import java.util.UUID

class AndroidBleScanner(
    private val bluetoothAdapter: BluetoothAdapter,
) {

    private val bluetoothLeScanner: BluetoothLeScanner? get() = bluetoothAdapter.bluetoothLeScanner

    fun startScan(serviceUuids: List<UUID>?, callback: ScanCallback): Cancellable {
        val scanner = bluetoothLeScanner
        if (scanner == null) {
            e("AndroidBleScanner", "No BluetoothLeScanner available. Is bluetooth turned on?")
            return Cancellable {}
        }

        if (serviceUuids != null) {
            i("AndroidBleScanner", "Starting scan for $serviceUuids")
        } else {
            i("AndroidBleScanner", "Starting scan without filters")
        }

        val listener = MyScanCallback(serviceUuids, callback)

        scanner.startScan(
            null,
            ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build(),
            listener
        )

        return Cancellable {
            if (serviceUuids != null) {
                i("AndroidBleScanner", "Stopping scan for $serviceUuids")
            } else {
                i("AndroidBleScanner", "Stopping scan without filters")
            }

            listener.cancel()
            bluetoothLeScanner?.stopScan(listener)
        }
    }

    /**
     * A ScanCallback that manually filters results.
     *
     * On some devices, using [android.bluetooth.le.ScanFilter] fails to
     * find scan results at all.
     */
    private class MyScanCallback(
        private val serviceUuids: Iterable<UUID>?,
        private val callback: ScanCallback,
    ) : ScanCallback(), Cancellable {

        private var cancelled = false

        override fun cancel() {
            cancelled = true
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (cancelled) return

            if (serviceUuids == null) {
                callback.onScanResult(callbackType, result)
                return
            }

            val availableServices = result.scanRecord?.serviceUuids ?: return
            if (availableServices.none { it.uuid in serviceUuids }) {
                return
            }

            v("AndroidBleScanner", "Scan result: [${result.device.address}, ${result.device.name}]")
            callback.onScanResult(callbackType, result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            if (cancelled) return

            if (serviceUuids == null) {
                callback.onBatchScanResults(results)
                return
            }

            val actualResults = results
                .filter { result ->
                    val availableServices = result.scanRecord?.serviceUuids ?: emptyList()

                    availableServices.any { it.uuid in serviceUuids }
                }

            if (actualResults.isEmpty()) return

            v("AndroidBleScanner", "Scan results: ${results.map { result -> "[${result.device.address}, ${result.device.name}]" }}")
            callback.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            w("AndroidBleScanner", "Scan failed: $errorCode")
            callback.onScanFailed(errorCode)
        }
    }
}
