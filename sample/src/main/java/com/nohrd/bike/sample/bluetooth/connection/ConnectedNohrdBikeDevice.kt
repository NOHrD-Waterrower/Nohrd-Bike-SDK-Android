package com.nohrd.bike.sample.bluetooth.connection

import com.nohrd.bike.sdkv1.BikeDataListener
import com.nohrd.bike.sdkv1.BytesReader
import com.nohrd.bike.sdkv1.Calibration
import com.nohrd.bike.sdkv1.NohrdBike
import com.nohrd.bike.sdkv1.ResistanceMeasurementsListener
import com.nohrd.bike.sdkv1.ble.BikeCharacteristic
import com.nohrd.bike.sdkv1.ble.BikeService
import com.nohrd.bike.sample.util.Cancellable

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

        override fun start(callback: BytesReader.Callback): com.nohrd.bike.Cancellable {
            val bleCancellable = bleDevice.listen(
                serviceUUID = BikeService.uuid,
                characteristicUUID = BikeCharacteristic.uuid,
            ) { bytes ->
                callback.onBytesRead(bytes)
            }

            return com.nohrd.bike.Cancellable {
                bleCancellable.cancel()
            }
        }
    }
}
