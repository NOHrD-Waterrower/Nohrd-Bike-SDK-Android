package com.nohrd.bike.sdk.ble.sample.ui.devicedetails

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Connected
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Connecting
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Disconnected
import com.nohrd.bike.sdk.ble.sample.ui.devicedetails.ConnectionStatus.Failed
import com.nohrd.bike.sdk.ble.sample.ui.theming.AppTheme

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
            // modifier = Modifier.
        ) {
            ConnectionRow(
                viewModel.connectionStatus,
                connectClick = connectClick,
                disconnectClick = disconnectClick
            )
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
    TextButton(onClick = connectClick, enabled = connectionStatus == Disconnected) {
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
            ),
            onUpClick = { println("Up clicked") },
            connectClick = { println("Connect clicked") },
            disconnectClick = { println("Disconnect clicked") }
        )
    }
}
