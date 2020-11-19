package com.nohrd.bike.sdk

public data class Speed(val metersPerSecond: Double)

public val Double.metersPerSecond: Speed get() = Speed(this)
public val Int.metersPerSecond: Speed get() = toDouble().metersPerSecond
