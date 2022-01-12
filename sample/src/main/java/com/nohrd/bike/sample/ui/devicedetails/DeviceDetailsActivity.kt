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
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sample.bluetooth.connection.BleConnection
import com.nohrd.bike.sample.bluetooth.connection.BleConnectionFactory
import com.nohrd.bike.sample.bluetooth.connection.BleConnectionState
import com.nohrd.bike.sample.bluetooth.connection.ConnectedNohrdBikeDevice
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

    private var connectedDevice: ConnectedNohrdBikeDevice? = null

    private var resistanceMeasurementsListenerCancellable: Cancellable? = null
        set(value) {
            field?.cancel()
            field = value
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
                        onCalibrationChange()
                    },
                    onSetHighCalibrationClick = {
                        state = state.copy(
                            calibrationStatus = state.calibrationStatus.copy(highValue = it)
                        )
                        onCalibrationChange()
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
            resistanceMeasurementsListenerCancellable = null
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

        val device = ConnectedNohrdBikeDevice(connectionState.device)
        resistanceMeasurementsListenerCancellable = device.resistanceMeasurements(
            object : ResistanceMeasurementsListener {
                override fun onResistanceMeasurement(measurement: ResistanceMeasurement) {
                    state = state.copy(resistanceMeasurement = measurement)
                }
            }
        )
        this.connectedDevice = device
    }

    private fun onCalibrationChange() {
        val calibration = Calibration(
            lowValue = state.calibrationStatus.lowValue ?: return,
            highValue = state.calibrationStatus.highValue ?: return,
        )

        val device = connectedDevice ?: return

        bikeDataListenerCancellable = device.bikeData(
            calibration,
            object : BikeDataListener {
                override fun onCadence(cadence: Cadence?) {
                    state = state.copy(cadence = cadence)
                }

                override fun onDistance(distance: Distance) {
                    val millimeters = (state.distance?.millimeters ?: 0.0) + distance.millimeters
                    state = state.copy(distance = Distance.fromMillimeters(millimeters))
                }

                override fun onEnergy(energy: Energy) {
                    val joules = (state.energy?.joules ?: 0.0) + energy.joules
                    state = state.copy(energy = Energy.fromJoules(joules))
                }

                override fun onPower(power: Power?) {
                    state = state.copy(power = power)
                }

                override fun onResistance(resistance: Resistance) {
                    state = state.copy(resistance = resistance)
                }

                override fun onSpeed(speed: Speed?) {
                    state = state.copy(speed = speed)
                }
            }
        )
    }

    override fun onPause() {
        connectionStateListenerCancellable = null
        resistanceMeasurementsListenerCancellable = null
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
