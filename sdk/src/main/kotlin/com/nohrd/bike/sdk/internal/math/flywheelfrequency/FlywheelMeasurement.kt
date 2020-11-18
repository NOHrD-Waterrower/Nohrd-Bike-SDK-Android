package com.nohrd.bike.sdk.internal.math.flywheelfrequency

internal inline class FlywheelMeasurement(val numberOfTicksPerRevolution: Int)

internal val Int.numberOfTicksPerRevolution get() = FlywheelMeasurement(this)
