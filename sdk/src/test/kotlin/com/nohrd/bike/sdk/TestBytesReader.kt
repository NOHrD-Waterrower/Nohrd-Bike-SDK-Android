package com.nohrd.bike.sdk

import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import java.lang.Thread.sleep
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

internal class TestBytesReader : BytesReader {

    private val callbacks = LinkedBlockingQueue<BytesReader.Callback>()
    override fun start(callback: BytesReader.Callback): Cancellable {
        callbacks += callback
        return Cancellable {
            callbacks -= callback
        }
    }

    fun append(bytes: ByteArray) {
        sleep(10)
        thread {
            while (callbacks.isEmpty()) {
                sleep(10) // To overcome thread switching delays
            }
            callbacks.forEach { it.onBytesRead(bytes) }
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
