package com.nohrd.bike.sdk.internal.protocol

import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelMeasurement

internal sealed class DataPacket

internal data class SpeedPacket(
    val numberOfTicksPerRevolution: Int,
) : DataPacket() {

    val flywheelMeasurement = FlywheelMeasurement(numberOfTicksPerRevolution)
}

internal data class ResistancePacket(
    val value: Int,
) : DataPacket()

internal data class BatteryPacket(
    val value: Int,
) : DataPacket()

internal object AbortPacket : DataPacket()
