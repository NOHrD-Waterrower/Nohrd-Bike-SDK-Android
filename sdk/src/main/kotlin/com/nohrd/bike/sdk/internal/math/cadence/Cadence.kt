package com.nohrd.bike.sdk.internal.math.cadence

internal inline class Cadence(val value: Double)

internal val Double.rpm get() = Cadence(this)
internal val Int.rpm get() = Cadence(this.toDouble())
