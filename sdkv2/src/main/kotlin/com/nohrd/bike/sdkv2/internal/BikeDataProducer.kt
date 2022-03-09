package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Cadence
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv2.BikeDataListener
import com.nohrd.bike.sdkv2.HeartRate
import com.nohrd.bike.sdkv2.ServiceReader
import com.nohrd.bike.sdkv2.internal.math.SpeedCalculator

internal class BikeDataProducer(
    private val listener: BikeDataListener,
) : ServiceReader.Callback {

    override fun onReadBytes(byteArray: ByteArray) {
        val data = IndoorBikeDataCharacteristicDecoder.decode(byteArray)

        listener.onCadenceUpdate(data.instantaneousCadence?.let(::Cadence))
        listener.onPowerUpdate(data.instantaneousPowerWatts?.watts)
        listener.onResistanceUpdate(Resistance.from(data.resistanceLevel))
        listener.onHeartRateUpdate(data.heartRate?.let(::HeartRate))

        val speed = speedFrom(data)
        listener.onSpeedUpdate(speed)
        listener.onEnergyUpdate(energyUpdateFrom(data))
        listener.onDistanceUpdate(distanceUpdateFrom(speed))
    }

    private fun speedFrom(bikeData: IndoorBikeData): Speed? {
        val power = bikeData.instantaneousPowerWatts?.watts ?: return null
        return SpeedCalculator.calculateSpeed(power)
    }

    private val energyProducer = ExpendedEnergyProducer()
    private fun energyUpdateFrom(bikeData: IndoorBikeData): Energy {
        return energyProducer.with(power = bikeData.instantaneousPowerWatts?.watts)
    }

    private val distanceProducer = DistanceProducer()
    private fun distanceUpdateFrom(speed: Speed?): Distance {
        return distanceProducer.with(speed)
    }
}