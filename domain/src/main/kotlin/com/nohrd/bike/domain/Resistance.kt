package com.nohrd.bike.domain

/**
 * Denotes the resistance level applied to the bike.
 *
 * @param value A value in (0f, 1f), where 0f denotes the
 * lowest resistance, and 1f the highest resistance value.
 */
public class Resistance private constructor(public val value: Float) {

    public companion object {

        public fun from(fraction: Float): Resistance {
            return Resistance(fraction.coerceIn(0f, 1f))
        }
    }

    override fun toString(): String {
        return "Resistance(value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resistance

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
