package com.nohrd.bike.sample.ui.devicedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.setContent
import com.nohrd.bike.Cadence
import com.nohrd.bike.Power
<<<<<<< HEAD
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
=======
import com.nohrd.bike.cyclingpower.CyclingPowerDataListener
>>>>>>> e873375 (WIP modify sample to use cyclingpower)
import com.nohrd.bike.sample.bluetooth.connection.BleConnection
import com.nohrd.bike.sample.bluetooth.connection.BleConnectionFactory
import com.nohrd.bike.sample.bluetooth.connection.BleConnectionState
import com.nohrd.bike.sample.bluetooth.connection.ConnectedCyclingPowerDevice
import com.nohrd.bike.sample.ui.Device
import com.nohrd.bike.sample.ui.theming.AppTheme
import com.nohrd.bike.sample.util.Cancellable
import com.nohrd.bike.sdkv1.BikeDataListener
import com.nohrd.bike.sdkv1.Calibration
import com.nohrd.bike.sdkv1.ResistanceMeasurement
import com.nohrd.bike.sdkv1.ResistanceMeasurementsListener

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
            calibrationStatus = CalibrationStatus(null, null),
            resistanceMeasurement = null,
            cadence = null,
            distance = null,
            energy = null,
            power = null,
            resistance = null,
            speed = null
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

    private var connectedDevice: ConnectedCyclingPowerDevice? = null
        set(value) {
            field = value
            bikeDataListenerCancellable = value?.powerData(
                object : CyclingPowerDataListener {
                    override fun onCadence(cadence: Cadence?) {
                        state = state.copy(cadence = cadence)
                    }

                    override fun onPower(power: Power?) {
                        state = state.copy(power = power)
                    }
                }
            )
        }

    private var bikeDataListenerCancellable: Cancellable? = null
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
                    disconnectClick = { connection.close() },
                    onSetLowCalibrationClick = {
                        state = state.copy(
                            calibrationStatus = state.calibrationStatus.copy(lowValue = it)
                        )
                    },
                    onSetHighCalibrationClick = {
                        state = state.copy(
                            calibrationStatus = state.calibrationStatus.copy(highValue = it)
                        )
                    }
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

        if (connectionState !is BleConnectionState.Connected) {
            connectedDevice = null
            bikeDataListenerCancellable = null

            state = state.copy(
                cadence = null,
                distance = null,
                energy = null,
                power = null,
                resistance = null,
                speed = null
            )
            return
        }

        val device = ConnectedCyclingPowerDevice(connectionState.device)
        this.connectedDevice = device
    }

    override fun onPause() {
        connectionStateListenerCancellable = null
        bikeDataListenerCancellable = null
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
