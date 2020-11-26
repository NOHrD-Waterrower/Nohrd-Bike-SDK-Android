package com.nohrd.bike.sdk

@Suppress("DataClassPrivateConstructor")
public data class Energy private constructor(val joules: Double) {

    public companion object {

        @JvmStatic
        public fun fromJoules(value: Number): Energy {
            return Energy(value.toDouble())
        }
    }
}

internal val Number.joules: Energy get() = Energy.fromJoules(this)
