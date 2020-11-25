package com.nohrd.bike.sdk.ble.sample.bluetooth.connection

import com.nohrd.bike.sdk.BytesReader
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.NohrdBike
import com.nohrd.bike.sdk.ResistanceMeasurement
import com.nohrd.bike.sdk.ble.BikeCharacteristic
import com.nohrd.bike.sdk.ble.BikeService
import com.nohrd.bike.sdk.ble.sample.util.Cancellable

class ConnectedNohrdBikeDevice(
    private val delegate: ConnectedBleDevice,
) {

    fun bikeData(listener: NohrdBike.Listener): Cancellable {
        val bytesReader = BleBytesReader(delegate)

        val bike = NohrdBike.create(
            bytesReader,
            Calibration(
                ResistanceMeasurement(100),
                ResistanceMeasurement(2000),
            )
        )

        // Start processing of bike data
        val bikeCancellable = bike.registerListener(listener)

        return Cancellable {
            bikeCancellable.cancel()
        }
    }

    private class BleBytesReader(
        private val bleDevice: ConnectedBleDevice,
    ) : BytesReader {

        override fun start(callback: BytesReader.Callback): com.nohrd.bike.sdk.Cancellable {
            val bleCancellable = bleDevice.listen(
                serviceUUID = BikeService.uuid,
                characteristicUUID = BikeCharacteristic.uuid,
            ) { bytes ->
                callback.onBytesRead(bytes)
            }

            return com.nohrd.bike.sdk.Cancellable {
                bleCancellable.cancel()
            }
        }
    }
}