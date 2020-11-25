package com.nohrd.bike.sdk.ble.sample.ui.devicedetails

import com.nohrd.bike.sdk.Cadence
import com.nohrd.bike.sdk.Distance
import com.nohrd.bike.sdk.Energy
import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.ResistanceMeasurement
import com.nohrd.bike.sdk.Speed

data class DeviceDetailsViewModel(
    val deviceName: String?,
    val connectionStatus: ConnectionStatus,
    val calibrationStatus: CalibrationStatus,
    val resistanceMeasurement: ResistanceMeasurement?,
    val cadence: Cadence?,
    val distance: Distance?,
    val energy: Energy?,
    val power: Power?,
    val resistance: Resistance?,
    val speed: Speed?,
)

data class CalibrationStatus(
    val lowValue: ResistanceMeasurement?,
    val highValue: ResistanceMeasurement?,
)
