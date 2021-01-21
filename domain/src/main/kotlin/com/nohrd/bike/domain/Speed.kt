package com.nohrd.bike.domain

@Suppress("DataClassPrivateConstructor")
public data class Speed private constructor(val metersPerSecond: Double) {

    public companion object {

        @JvmStatic
        public fun fromMetersPerSecond(value: Number): Speed {
            return Speed(value.toDouble())
        }
    }
}
