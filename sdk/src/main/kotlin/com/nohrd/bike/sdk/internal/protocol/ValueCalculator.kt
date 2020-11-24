package com.nohrd.bike.sdk.internal.protocol

/**
 * The last four bits of the flags byte indicate whether one of
 * the high or low byte values was supposed to be `10` or `13`.
 *
 * Since these are the carriage return (CR) and line feed (LF)
 * characters, these values cannot be included in the byte packet.
 *
 * This class calculates the proper values using these flags.
 */
internal object ValueCalculator {

    fun calculateValue(
        flags: Byte,
        highByte: Byte,
        lowByte: Byte,
    ): Int {
        val correctedHighByte = when (flags.toInt() and 0xf) {
            0b0001, 0b0101, 0b0111 -> 10
            0b0010, 0b1000, 0b1001 -> 13
            else -> highByte.toInt() and 0xff
        }

        val correctedLowByte = when (flags.toInt() and 0xf) {
            0b0011, 0b0101, 0b1000 -> 10
            0b0100, 0b0111, 0b1001 -> 13
            else -> lowByte.toInt() and 0xff
        }

        return (correctedHighByte shl 8) + correctedLowByte
    }
}
