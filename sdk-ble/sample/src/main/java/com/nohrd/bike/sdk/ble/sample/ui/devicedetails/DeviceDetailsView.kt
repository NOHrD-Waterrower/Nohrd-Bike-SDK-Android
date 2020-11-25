package com.nohrd.bike.sdk.ble.sample.ui.devicedetails

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.nohrd.bike.sdk.Cadence
import com.nohrd.bike.sdk.Distance
import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.Speed
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Connected
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Connecting
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Disconnected
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Failed
import com.nohrd.bike.sdk.ble.sample.ui.theming.AppTheme
import com.nohrd.bike.sdk.joules

@Composable
fun DeviceDetailsView(
    viewModel: DeviceDetailsViewModel,
    onUpClick: () -> Unit,
    connectClick: () -> Unit,
    disconnectClick: () -> Unit,
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

            if (viewModel.connectionStatus == Connected) {
                DataRow(title = "Cadence", value = "${viewModel.cadence?.value ?: "-"}/min")
                DataRow(title = "Distance", value = "${viewModel.distance?.meters ?: "-"}m")
                DataRow(title = "Energy", value = "${viewModel.energy?.joules ?: "-"}J")
                DataRow(title = "Power", value = "${viewModel.power?.watts ?: "-"}W")
                DataRow(title = "Resistance", value = "${viewModel.resistance?.value ?: "-"}")
                DataRow(title = "Speed", value = "${viewModel.speed?.metersPerSecond ?: "-"}m/s")
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
                Icon(asset = Icons.Filled.ArrowBack)
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
    AppTheme {
        DeviceDetailsView(
            DeviceDetailsViewModel(
                deviceName = "NOHrD Bike",
                connectionStatus = Connected,
                cadence = Cadence(20.0),
                energy = 200.joules,
                distance = Distance(231 * 1000.0),
                power = Power(300.0),
                resistance = Resistance.from(.4f),
                speed = Speed(5.4)
            ),
            onUpClick = { println("Up clicked") },
            connectClick = { println("Connect clicked") },
            disconnectClick = { println("Disconnect clicked") }
        )
    }
}
