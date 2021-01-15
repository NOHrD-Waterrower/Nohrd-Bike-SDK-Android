package com.nohrd.bike.sdk.ble.sample.bluetooth.connection

import com.nohrd.bike.domain.BikeDataListener
import com.nohrd.bike.sdk.ble.FitnessMachineService
import com.nohrd.bike.sdk.ble.IndoorBikeDataCharacteristic
import com.nohrd.bike.sdk.ble.sample.util.Cancellable
import com.nohrd.bike.sdk.ftms.FTMSBike
import com.nohrd.bike.sdk.ftms.ServiceReader

class ConnectedFTMSBikeDevice(
    private val delegate: ConnectedBleDevice,
) {

    private val bike = FTMSBike.create(BleServicesReader(delegate))

    fun bikeData(listener: BikeDataListener): Cancellable {
        val cancellable = bike.bikeData(listener)
        return Cancellable { cancellable.cancel() }
    }

    private class BleServicesReader(
        private val bleDevice: ConnectedBleDevice,
    ) : ServiceReader {

        override fun start(callback: ServiceReader.Callback): com.nohrd.bike.domain.Cancellable {
            val bleCancellable = bleDevice.listen(
                serviceUUID = FitnessMachineService.uuid,
                characteristicUUID = IndoorBikeDataCharacteristic.uuid,
            ) { bytes ->
                callback.onReadBytes(bytes)
            }

            return com.nohrd.bike.domain.Cancellable {
                bleCancellable.cancel()
            }
        }
    }
}
