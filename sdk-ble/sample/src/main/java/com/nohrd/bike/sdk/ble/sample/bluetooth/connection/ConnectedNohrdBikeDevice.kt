package com.nohrd.bike.sdk.ble.sample.bluetooth.connection

import com.nohrd.bike.sdk.BikeDataListener
import com.nohrd.bike.sdk.BytesReader
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.NohrdBike
import com.nohrd.bike.sdk.ResistanceMeasurementsListener
import com.nohrd.bike.sdk.ble.BikeCharacteristic
import com.nohrd.bike.sdk.ble.BikeService
import com.nohrd.bike.sdk.ble.sample.util.Cancellable

class ConnectedNohrdBikeDevice(
    private val delegate: ConnectedBleDevice,
) {

    private val bike = NohrdBike.create(BleBytesReader(delegate))

    fun resistanceMeasurements(listener: ResistanceMeasurementsListener): Cancellable {
        val cancellable = bike.resistanceMeasurements(listener)
        return Cancellable { cancellable.cancel() }
    }

    fun bikeData(calibration: Calibration, listener: BikeDataListener): Cancellable {
        val cancellable = bike.bikeData(calibration, listener)
        return Cancellable { cancellable.cancel() }
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
