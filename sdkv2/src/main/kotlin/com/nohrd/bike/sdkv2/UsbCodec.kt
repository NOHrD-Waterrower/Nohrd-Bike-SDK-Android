package com.nohrd.bike.sdkv2

/**
 * This object can be used to decode/encode messages that are sent or received from the v2 Nohrd Bike through USB
 */
public object UsbCodec {

    public fun decode(input: ByteArray): ByteArray {
        val stuffed = input
            .flatMap {
                when (it) {
                    0xF1.toByte() -> listOf(0xF3.toByte(), 0x01.toByte())
                    0xF2.toByte() -> listOf(0xF3.toByte(), 0x02.toByte())
                    0xF3.toByte() -> listOf(0xF3.toByte(), 0x03.toByte())
                    else -> listOf(it)
                }
            }
            .toByteArray()

        return byteArrayOf(0xF1.toByte()) + stuffed + 0xF2.toByte()
    }

    public fun encode(input: ByteArray): ByteArray {
        assert(input.firstOrNull() == 0xF1.toByte())
        assert(input.lastOrNull() == 0xF2.toByte())

        return input
            .drop(1)
            .dropLast(1)
            .unstuff()
            .toByteArray()
    }

    private fun List<Byte>.unstuff(): List<Byte> {
        val output = mutableListOf<Byte>()
        var i = 0

        while (i <size) {
            if (this[i] == 0xF3.toByte()) {
                when (this.getOrNull(i + 1)) {
                    0x01.toByte() -> {
                        output += 0xF1.toByte()
                        i += 1
                    }
                    0x02.toByte() -> {
                        output += 0xF2.toByte()
                        i += 1
                    }
                    0x03.toByte() -> {
                        output += 0xF3.toByte()
                        i += 1
                    }
                    else -> error("Malformed ByteArray")
                }
            } else {
                output += this[i]
            }
            i += 1
        }

        return output
    }
}
