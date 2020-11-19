package com.nohrd.bike.sdk

public data class Cadence(val value: Double)

public val Double.rpm: Cadence get() = Cadence(this)
public val Int.rpm: Cadence get() = Cadence(this.toDouble())
