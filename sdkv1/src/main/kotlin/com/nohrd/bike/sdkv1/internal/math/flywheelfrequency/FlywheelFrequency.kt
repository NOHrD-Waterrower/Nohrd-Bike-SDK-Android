package com.nohrd.bike.sdkv1.internal.math.flywheelfrequency

internal data class FlywheelFrequency(val revolutionsPerSecond: Double)

internal val Double.revolutionsPerSecond get() = FlywheelFrequency(this)
internal val Int.revolutionsPerSecond get() = this.toDouble().revolutionsPerSecond
