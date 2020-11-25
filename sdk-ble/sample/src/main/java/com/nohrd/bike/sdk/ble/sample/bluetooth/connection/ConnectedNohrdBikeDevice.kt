package com.nohrd.bike.sdk.ble.sample.bluetooth.connection

import com.nohrd.bike.sdk.BytesReader
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.NohrdBike
import com.nohrd.bike.sdk.ResistanceMeasurement
import com.nohrd.bike.sdk.ble.BikeCharacteristic
import com.nohrd.bike.sdk.ble.BikeService
import com.nohrd.bike.sdk.ble.sample.util.Cancellable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ConnectedNohrdBikeDevice(
    private val delegate: ConnectedBleDevice,
) {

    fun bikeData(listener: NohrdBike.Listener): Cancellable {
        val bytesReader = BlockingBytesReader()

        val bike = NohrdBike.create(
            bytesReader,
            Calibration(
                ResistanceMeasurement(100),
                ResistanceMeasurement(2000),
            )
        )

        // Start processing of bike data
        val c1 = bike.registerListener(listener)

        // Start listening for bike bytes
        val c2 = delegate.listen(
            serviceUUID = BikeService.uuid,
            characteristicUUID = BikeCharacteristic.uuid,
        ) { bytes ->
            bytesReader.offer(bytes)
        }

        return Cancellable {
            c1.cancel()
            c2.cancel()
        }
    }

    private class BlockingBytesReader : BytesReader {

        private val queue = LinkedBlockingQueue<ByteArray>()

        fun offer(bytes: ByteArray) {
            queue.offer(bytes, 1, TimeUnit.SECONDS)
        }

        override fun read(buffer: ByteArray): Int {
            val bytes: ByteArray? = queue.poll(1, TimeUnit.SECONDS)
            if (bytes == null) return 0

            System.arraycopy(bytes, 0, buffer, 0, bytes.size)
            return bytes.size
        }
    }
}
