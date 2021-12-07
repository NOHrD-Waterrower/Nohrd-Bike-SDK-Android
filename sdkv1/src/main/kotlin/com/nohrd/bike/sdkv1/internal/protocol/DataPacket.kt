package com.nohrd.bike.sdkv1.internal.protocol

internal sealed class DataPacket

internal data class SpeedPacket(
    val numberOfTicksPerRevolution: Int,
) : DataPacket()

internal data class ResistancePacket(
    val value: Int,
) : DataPacket()

internal data class BatteryPacket(
    val value: Int,
) : DataPacket()

internal object AbortPacket : DataPacket()
