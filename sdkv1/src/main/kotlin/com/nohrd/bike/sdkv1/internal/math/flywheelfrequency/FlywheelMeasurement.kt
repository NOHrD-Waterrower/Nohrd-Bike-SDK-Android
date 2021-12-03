package com.nohrd.bike.sdkv1.internal.math.flywheelfrequency

internal inline class FlywheelMeasurement(val numberOfTicksPerRevolution: Int)

internal val Int.numberOfTicksPerRevolution get() = FlywheelMeasurement(this)
