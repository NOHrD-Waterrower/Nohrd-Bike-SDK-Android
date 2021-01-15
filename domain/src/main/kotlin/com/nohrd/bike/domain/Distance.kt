package com.nohrd.bike.domain

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
