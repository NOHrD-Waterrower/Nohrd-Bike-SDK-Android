package com.nohrd.bike.sdk

public data class Cadence(val value: Double)

internal val Double.rpm: Cadence get() = Cadence(this)
internal val Int.rpm: Cadence get() = Cadence(this.toDouble())
