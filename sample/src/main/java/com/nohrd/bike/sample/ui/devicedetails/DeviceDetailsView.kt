@file:OptIn(ExperimentalAnimationApi::class)

package com.nohrd.bike.sample.ui.devicedetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nohrd.bike.Cadence
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sample.ui.devicedetails.ConnectionStatus.Connected
import com.nohrd.bike.sample.ui.devicedetails.ConnectionStatus.Connecting
import com.nohrd.bike.sample.ui.devicedetails.ConnectionStatus.Disconnected
import com.nohrd.bike.sample.ui.devicedetails.ConnectionStatus.Failed
import com.nohrd.bike.sample.ui.theming.AppTheme
import com.nohrd.bike.sdkv1.ResistanceMeasurement

@Composable
fun DeviceDetailsView(
    viewModel: DeviceDetailsViewModel,
    onUpClick: () -> Unit,
    connectClick: () -> Unit,
    disconnectClick: () -> Unit,
    onSetLowCalibrationClick: ((ResistanceMeasurement?) -> Unit)?,
    onSetHighCalibrationClick: ((ResistanceMeasurement?) -> Unit)?,
) {
    if (viewModel.deviceName == null) return

    Column {
        DeviceDetailsTopAppBar(
            deviceName = viewModel.deviceName,
            onUpClick = onUpClick
        )
        ScrollableColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConnectionRow(
                viewModel.connectionStatus,
                connectClick = connectClick,
                disconnectClick = disconnectClick
            )

            AnimatedVisibility(visible = false) {
                CalibrationRow(
                    viewModel.resistanceMeasurement,
                    viewModel.calibrationStatus,
                    onSetLowCalibrationClick,
                    onSetHighCalibrationClick
                )
            }

            AnimatedVisibility(visible = viewModel.connectionStatus == Connected) {
                BikeData(viewModel)
            }
        }
    }
}

@Composable
private fun DeviceDetailsTopAppBar(
    deviceName: String,
    onUpClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(deviceName) },
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                Icon(imageVector = Icons.Filled.ArrowBack)
            }
        }
    )
}

@Composable
private fun ConnectionRow(
    connectionStatus: ConnectionStatus,
    connectClick: () -> Unit,
    disconnectClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        ConnectionStatusText(connectionStatus)
        Spacer(modifier = Modifier.weight(1f))
        ConnectionButton(
            connectionStatus,
            connectClick = connectClick,
            disconnectClick = disconnectClick
        )
    }
}

@Composable
private fun ConnectionStatusText(connectionStatus: ConnectionStatus) {
    Text(connectionStatus.name)
}

@Composable
private fun ConnectionButton(
    connectionStatus: ConnectionStatus,
    connectClick: () -> Unit,
    disconnectClick: () -> Unit,
) {
    when (connectionStatus) {
        Disconnected, Connecting, Failed -> ConnectButton(connectionStatus, connectClick = connectClick)
        Connected -> DisconnectButton(disconnectClick = disconnectClick)
    }
}

@Composable
private fun ConnectButton(connectionStatus: ConnectionStatus, connectClick: () -> Unit) {
    TextButton(
        onClick = connectClick,
        enabled = connectionStatus == Disconnected || connectionStatus == Failed
    ) {
        Text("Connect")
    }
}

@Composable
private fun DisconnectButton(disconnectClick: () -> Unit) {
    TextButton(onClick = disconnectClick) {
        Text("Disconnect")
    }
}

@Composable
private fun CalibrationRow(
    resistanceMeasurement: ResistanceMeasurement?,
    calibrationStatus: CalibrationStatus,
    onSetLowCalibrationClick: ((ResistanceMeasurement?) -> Unit)?,
    onSetHighCalibrationClick: ((ResistanceMeasurement?) -> Unit)?,
) {
    Column {
        Divider()
        DataRow(title = "Resistance measurement", value = "${resistanceMeasurement?.value ?: "-"}")

        Row(modifier = Modifier.padding(8.dp)) {
            Text("Low")
            AnimatedVisibility(visible = calibrationStatus.lowValue != null) {
                Text("(${calibrationStatus.lowValue?.value})")
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { onSetLowCalibrationClick?.invoke(resistanceMeasurement) }) { Text(text = "Set") }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Text("High")
            AnimatedVisibility(visible = calibrationStatus.highValue != null) {
                Text("(${calibrationStatus.highValue?.value})")
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { onSetHighCalibrationClick?.invoke(resistanceMeasurement) }) { Text(text = "Set") }
        }
    }
}

@Composable
private fun BikeData(viewModel: DeviceDetailsViewModel) {
    Column {
        Divider()
        DataRow(title = "Cadence", value = "${viewModel.cadence?.revolutionPerMinute ?: "-"}/min")
        DataRow(title = "Distance", value = "${viewModel.distance?.meters ?: "-"}m")
        DataRow(title = "Energy", value = "${viewModel.energy?.joules ?: "-"}J")
        DataRow(title = "Power", value = "${viewModel.power?.watts ?: "-"}W")
        DataRow(title = "Resistance", value = "${viewModel.resistance?.value ?: "-"}")
        DataRow(title = "Speed", value = "${viewModel.speed?.metersPerSecond ?: "-"}m/s")
    }
}

@Composable
private fun DataRow(
    title: String,
    value: Any?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(title)
        Spacer(modifier = Modifier.weight(1f))
        Text(value?.toString() ?: "-")
    }
}

@Preview
@Composable
fun DeviceDetailsViewPreview() {
    var viewModel by mutableStateOf(
        DeviceDetailsViewModel(
            deviceName = "NOHrD Bike",
            connectionStatus = Disconnected,
            calibrationStatus = CalibrationStatus(
                lowValue = null,
                highValue = null
            ),
            resistanceMeasurement = ResistanceMeasurement(50),
            cadence = Cadence(20.0),
            energy = Energy.fromJoules(200),
            distance = Distance.fromMillimeters(231 * 1000.0),
            power = Power.fromWatts(300.0),
            resistance = Resistance.from(.4f),
            speed = Speed.fromMetersPerSecond(5.4)
        )
    )

    AppTheme {
        Surface(color = Color.White) {
            DeviceDetailsView(
                viewModel,
                onUpClick = { println("Up clicked") },
                connectClick = {
                    viewModel = viewModel.copy(
                        connectionStatus = Connected
                    )
                },
                disconnectClick = {
                    viewModel = viewModel.copy(
                        connectionStatus = Disconnected,
                        calibrationStatus = CalibrationStatus(null, null)
                    )
                },
                onSetLowCalibrationClick = {
                    viewModel = viewModel.copy(
                        calibrationStatus = viewModel.calibrationStatus.copy(
                            lowValue = ResistanceMeasurement(50)
                        ),
                        resistanceMeasurement = ResistanceMeasurement(3000)
                    )
                },
                onSetHighCalibrationClick = {
                    viewModel = viewModel.copy(
                        calibrationStatus = viewModel.calibrationStatus.copy(
                            highValue = ResistanceMeasurement(3000)
                        ),
                    )
                }
            )
        }
    }
}
