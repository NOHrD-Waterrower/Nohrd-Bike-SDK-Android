package com.nohrd.bike.sample.ui.devicedetails

import com.nohrd.bike.Cadence
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv1.ResistanceMeasurement

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
