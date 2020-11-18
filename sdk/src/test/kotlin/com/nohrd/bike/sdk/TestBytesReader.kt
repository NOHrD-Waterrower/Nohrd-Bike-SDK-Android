package com.nohrd.bike.sdk

import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import java.lang.Thread.sleep
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

internal class TestBytesReader : BytesReader {

    private val queue = LinkedBlockingQueue<ByteArray>()

    override fun read(buffer: ByteArray): Int {
        val bytes: ByteArray = queue.poll(100, TimeUnit.MILLISECONDS)
            ?: return 0

        System.arraycopy(bytes, 0, buffer, 0, bytes.size)
        return bytes.size
    }

    fun append(bytes: ByteArray) {
        thread {
            sleep(10) // To overcome thread switching delays
            queue.add(bytes)
        }
    }

    fun append(speedPacket: SpeedPacket) {
        append(
            byteArrayOf(
                0b0001_0000,
                (speedPacket.numberOfTicksPerRevolution shr 8).toByte(),
                (speedPacket.numberOfTicksPerRevolution and 0xF).toByte(),
                13,
                10
            )
        )
    }

    fun append(resistancePacket: ResistancePacket) {
        append(
            byteArrayOf(
                0b0010_0000,
                (resistancePacket.value shr 8).toByte(),
                (resistancePacket.value and 0xF).toByte(),
                13,
                10
            )
        )
    }
}
