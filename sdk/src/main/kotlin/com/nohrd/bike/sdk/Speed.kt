package com.nohrd.bike.sdk

@Suppress("DataClassPrivateConstructor")
public data class Speed private constructor(val metersPerSecond: Double) {

    public companion object {

        @JvmStatic
        public fun fromMetersPerSecond(value: Number): Speed {
            return Speed(value.toDouble())
        }
    }
}

internal val Number.metersPerSecond: Speed get() = Speed.fromMetersPerSecond(this)
