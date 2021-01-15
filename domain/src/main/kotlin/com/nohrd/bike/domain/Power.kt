package com.nohrd.bike.domain

@Suppress("DataClassPrivateConstructor")
public data class Power private constructor(val watts: Double) {

    public companion object {

        @JvmStatic
        public fun fromWatts(value: Number): Power {
            return Power(value.toDouble())
        }
    }
}
