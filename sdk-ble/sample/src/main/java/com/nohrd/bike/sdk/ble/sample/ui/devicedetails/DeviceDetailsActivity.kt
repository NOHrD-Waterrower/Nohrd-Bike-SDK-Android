package com.nohrd.bike.sdk.ble.sample.ui.devicedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import com.nohrd.bike.sdk.ble.sample.bluetooth.connection.BleConnection
import com.nohrd.bike.sdk.ble.sample.bluetooth.connection.BleConnectionFactory
import com.nohrd.bike.sdk.ble.sample.bluetooth.connection.BleConnectionState
import com.nohrd.bike.sdk.ble.sample.ui.Device
import com.nohrd.bike.sdk.ble.sample.ui.theming.AppTheme
import com.nohrd.bike.sdk.ble.sample.util.Cancellable

class DeviceDetailsActivity : AppCompatActivity() {

    private val device: Device
        get() {
            return Device(
                intent.deviceAddress,
                intent.deviceName
            )
        }

    private var state: DeviceDetailsViewModel by mutableStateOf(
        DeviceDetailsViewModel(
            deviceName = null,
            connectionStatus = ConnectionStatus.Disconnected,
        )
    )

    private val connection by lazy {
        BleConnectionFactory.from(this)
            .createConnection(device.address)
    }

    private var connectionStateListenerCancellable: Cancellable? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = state.copy(deviceName = device.name)

        setContent {
            AppTheme {
                DeviceDetailsView(
                    state,
                    onUpClick = { finish() },
                    connectClick = { connection.open() },
                    disconnectClick = { connection.close() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        connectionStateListenerCancellable = connection.addConnectionStateListener(
            object : BleConnection.ConnectionStateListener {
                override fun onConnectionStateChanged(connectionState: BleConnectionState) {
                    runOnUiThread { handle(connectionState) }
                }
            }
        )
    }

    private fun handle(connectionState: BleConnectionState) {
        state = state.copy(
            connectionStatus = when (connectionState) {
                is BleConnectionState.Disconnected -> ConnectionStatus.Disconnected
                is BleConnectionState.Connecting -> ConnectionStatus.Connecting
                is BleConnectionState.Connected -> ConnectionStatus.Connected
                is BleConnectionState.Failed -> ConnectionStatus.Failed
            }
        )
    }

    override fun onPause() {
        connectionStateListenerCancellable = null
        super.onPause()
    }

    override fun onDestroy() {
        connection.close()
        super.onDestroy()
    }

    companion object {

        private var Intent.deviceName: String
            get() {
                return getStringExtra("device_name")!!
            }
            set(value) {
                putExtra("device_name", value)
            }

        private var Intent.deviceAddress: String
            get() {
                return getStringExtra("device_address")!!
            }
            set(value) {
                putExtra("device_address", value)
            }

        fun intent(context: Context, device: Device): Intent {
            val intent = Intent(context, DeviceDetailsActivity::class.java)
            intent.deviceName = device.name
            intent.deviceAddress = device.address
            return intent
        }
    }
}
