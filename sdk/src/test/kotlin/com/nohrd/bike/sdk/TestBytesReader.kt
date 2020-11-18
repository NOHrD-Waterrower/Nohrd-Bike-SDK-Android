package com.nohrd.bike.sdk

import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

internal class TestBytesReader : BytesReader {

    private val queue = LinkedBlockingQueue<ByteArray>()

    override fun read(buffer: ByteArray): Int {
        val bytes: ByteArray = queue.poll(100, TimeUnit.MILLISECONDS)
            ?: return 0

        System.arraycopy(bytes, 0, buffer, 0, bytes.size)
        return bytes.size
    }

    fun append(bytes: ByteArray) {
        queue.add(bytes)
    }

    fun append(speedPacket: SpeedPacket) {
        queue.add(
            byteArrayOf(
                0b0001_0000,
                (speedPacket.numberOfTicksPerRevolution shr 8).toByte(),
                (speedPacket.numberOfTicksPerRevolution and 0xF).toByte(),
                13,
                10
            )
        )
    }
}
