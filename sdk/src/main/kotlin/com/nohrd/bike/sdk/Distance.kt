package com.nohrd.bike.sdk

@Suppress("DataClassPrivateConstructor")
public data class Distance private constructor(
    val millimeters: Double,
) {

    val meters: Double get() = millimeters / 1000

    public companion object {

        @JvmStatic
        public fun fromMillimeters(value: Number): Distance {
            return Distance(value.toDouble())
        }
    }
}

internal val Number.millimeters: Distance get() = Distance.fromMillimeters(this)
internal val Number.meters: Distance get() = (this.toDouble() * 1000).millimeters
