package com.nohrd.bike.sample.bluetooth.connection

<<<<<<< HEAD
import com.nohrd.bike.sample.util.Cancellable
import com.nohrd.bike.sdkv1.BikeDataListener
import com.nohrd.bike.sdkv1.BytesReader
import com.nohrd.bike.sdkv1.Calibration
import com.nohrd.bike.sdkv1.NohrdBike
import com.nohrd.bike.sdkv1.ResistanceMeasurementsListener
import com.nohrd.bike.sdkv1.ble.BikeCharacteristic
import com.nohrd.bike.sdkv1.ble.BikeService
=======
import com.nohrd.bike.cyclingpower.CyclingPowerBike
import com.nohrd.bike.cyclingpower.CyclingPowerDataListener
import com.nohrd.bike.cyclingpower.ServiceReader
import com.nohrd.bike.cyclingpower.ble.CyclePowerMeasurementCharacteristic
import com.nohrd.bike.cyclingpower.ble.CyclingPowerService
import com.nohrd.bike.sample.util.Cancellable
>>>>>>> e873375 (WIP modify sample to use cyclingpower)

class ConnectedCyclingPowerDevice(
    private val delegate: ConnectedBleDevice,
) {

    private val bike = CyclingPowerBike.create(BleBytesReader(delegate))

    fun powerData(listener: CyclingPowerDataListener): Cancellable {
        val cancellable = bike.powerData(listener)
        return Cancellable { cancellable.cancel() }
    }

    private class BleBytesReader(
        private val bleDevice: ConnectedBleDevice,
    ) : ServiceReader {

        override fun start(callback: ServiceReader.Callback): com.nohrd.bike.Cancellable {
            val bleCancellable = bleDevice.listen(
                serviceUUID = CyclingPowerService.uuid,
                characteristicUUID = CyclePowerMeasurementCharacteristic.uuid,
            ) { bytes ->
                callback.onReadBytes(bytes)
            }

            return com.nohrd.bike.Cancellable {
                bleCancellable.cancel()
            }
        }
    }
}
