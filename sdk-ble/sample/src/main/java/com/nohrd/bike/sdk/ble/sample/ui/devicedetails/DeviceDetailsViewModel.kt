package com.nohrd.bike.sdk.ble.sample.ui.devicedetails

import com.nohrd.bike.domain.Cadence
import com.nohrd.bike.domain.Distance
import com.nohrd.bike.domain.Energy
import com.nohrd.bike.domain.Power
import com.nohrd.bike.domain.Resistance
import com.nohrd.bike.domain.Speed
import com.nohrd.bike.sdk.ResistanceMeasurement

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
