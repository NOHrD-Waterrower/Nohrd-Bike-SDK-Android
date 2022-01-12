package com.nohrd.bike.cyclingpower.internal.gattspecification

internal fun ByteArray.readIntValue(
    format: Format,
    offset: Int,
): Int {
    return when (format) {
        Format.None -> error("This can't be read")
        Format.UInt8 -> unsignedByteToInt(this[offset])
        Format.UInt16 -> unsignedBytesToInt(this[offset], this[offset + 1])
        Format.UInt24 -> unsignedBytesToInt(this[offset], this[offset + 1], this[offset + 2])
        Format.UInt32 -> unsignedBytesToInt(this[offset], this[offset + 1], this[offset + 2], this[offset + 3]) // TODO this won't fit in java (signed) int
        Format.SInt16 -> unsignedToSigned(unsignedBytesToInt(this[offset], this[offset + 1]), size = 16)
    }
}

/**
 * Convert a signed byte to an unsigned int.
 */
private fun unsignedByteToInt(b: Byte): Int {
    return b.toInt() and 0xFF
}

/**
 * Convert signed bytes to a 16-bit unsigned int.
 */
private fun unsignedBytesToInt(b0: Byte, b1: Byte): Int {
    return unsignedByteToInt(b0) + (unsignedByteToInt(b1) shl 8)
}

/**
 * Convert signed bytes to a 24-bit unsigned int.
 */
private fun unsignedBytesToInt(b0: Byte, b1: Byte, b2: Byte): Int {
    return (
        unsignedByteToInt(b0) + (unsignedByteToInt(b1) shl 8) +
            (unsignedByteToInt(b2) shl 16)
        )
}

/**
 * Convert signed bytes to a 32-bit unsigned int.
 */
private fun unsignedBytesToInt(b0: Byte, b1: Byte, b2: Byte, b3: Byte): Int {
    return (
        unsignedByteToInt(b0) + (unsignedByteToInt(b1) shl 8) + (
            unsignedByteToInt(
                b2
            ) shl 16
            ) + (unsignedByteToInt(b3) shl 24)
        )
}

/**
 * Convert an unsigned integer value to a two's-complement encoded
 * signed value.
 */
private fun unsignedToSigned(unsigned: Int, size: Int): Int {
    if (unsigned and (1 shl size - 1) == 0) {
        return unsigned
    }
    return -1 * ((1 shl size - 1) - (unsigned and (1 shl size - 1) - 1))
}
