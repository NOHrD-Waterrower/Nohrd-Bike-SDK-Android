package com.nohrd.bike.sdk.internal.math.flywheelfrequency

internal inline class FlywheelFrequency(val revolutionsPerSecond: Double)

internal val Double.revolutionsPerSecond get() = FlywheelFrequency(this)
