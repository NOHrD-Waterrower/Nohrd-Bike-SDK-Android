package com.nohrd.bike.sdk

public data class Distance(
    val millimeters: Double,
) {

    val meters: Double get() = millimeters / 1000
}

internal val Double.millimeters: Distance get() = Distance(this)

internal val Double.meters: Distance get() = (this * 1000).millimeters
internal val Int.meters: Distance get() = toDouble().meters
