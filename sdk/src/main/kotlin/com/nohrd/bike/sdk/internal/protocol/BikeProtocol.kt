package com.nohrd.bike.sdk.internal.protocol

/**
 * A class that decodes bytes into DataPackets, following
 * the Nohrd Bike protocol.
 *
 * This class is not thread safe.
 */
internal class BikeProtocol {

    private val buffer = ByteArray(128)
    private var offset = 0

    fun offer(byte: Byte): DataPacket? {
        if (offset >= buffer.size) {
            // Deliberately throw data away.
            // If this is a problem, a solution could be to copy the trailing X bytes
            // to the start of the buffer and continuing from there.
            offset = 0
        }

        buffer[offset] = byte

        val packetIsComplete = byte == lineFeed && offset > 0 && buffer[offset - 1] == carriageReturn
        if (packetIsComplete) {
            val result = processPacket(buffer, offset)
            offset = 0
            return result
        }

        offset++
        return null
    }

    private fun processPacket(bytes: ByteArray, offset: Int): DataPacket? {
        if (offset <= 3) return null
        val value = ValueCalculator.calculateValue(
            flags = bytes[offset - 4],
            highByte = bytes[offset - 3],
            lowByte = bytes[offset - 2]
        )

        val packetType = bytes[offset - 4].toInt() shr 4
        if (packetType == 0b0101 && value == 2) {
            return AbortPacket
        }

        return when (packetType) {
            0b0001 -> SpeedPacket(value)
            0b0010 -> ResistancePacket(value)
            0b0011 -> BatteryPacket(value)
            else -> error("Invalid packet type: 0b${Integer.toBinaryString(packetType)}")
        }
    }

    companion object {

        const val carriageReturn: Byte = 13
        const val lineFeed: Byte = 10
    }
}
