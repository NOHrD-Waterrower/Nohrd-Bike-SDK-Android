package com.nohrd.bike.cyclingpower.internal

import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.readIntValue
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.AccumulatedEnergy
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.AccumulatedTorque
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.AccumulatedTorqueSource
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.BottomDeadSpotAngle
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.CumulativeCrankRevolutions
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.CumulativeWheelRevolutions
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.ExtremeAngles
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.ExtremeForceMagnitudes
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.ExtremeTorqueMagnitudes
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.FlagsField
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.InstantaneousPower
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.LastCrankEventTime
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.LastWheelEventTime
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.OffsetCompensationIndicator
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.PedalPowerBalanceField
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.PedalPowerBalanceReferenceField
import com.nohrd.bike.cyclingpower.internal.powerdataspecification.TopDeadSpotAngle

public object CyclingPowerMeasurementDataCharacteristicDecoder {

    /**
     * Decodes given [bytes] into a [CyclingPowerData] instance.
     *
     * Due to restrictions in the byte buffer size some of the [CyclingPowerData]
     * properties will be absent, which is represented as a `nil` value.
     *
     * @param bytes A [ByteArray] instance that contains the encoded data
     *              as described in the Rower Data characteristic specification.
     *
     * @return A [CyclingPowerData] instance with the decoded properties.
     *         Properties will be `nil` if not present in the encoded data.
     */
    public fun decode(bytes: ByteArray): CyclingPowerData {
        return CyclingPowerData(
            instantaneousPowerWatts = instantaneousPower(bytes),
            instantaneousCadence = instantaneousCadence(bytes),
        )
    }

    private val fields = listOf(
        FlagsField,
        InstantaneousPower,
        PedalPowerBalanceField,
        PedalPowerBalanceReferenceField,
        AccumulatedTorque,
        AccumulatedTorqueSource,
        CumulativeWheelRevolutions,
        LastWheelEventTime,
        CumulativeCrankRevolutions,
        LastCrankEventTime,
        ExtremeForceMagnitudes,
        ExtremeTorqueMagnitudes,
        ExtremeAngles,
        TopDeadSpotAngle,
        BottomDeadSpotAngle,
        AccumulatedEnergy,
        OffsetCompensationIndicator,
    )

    private fun instantaneousPower(bytes: ByteArray): Int? {
        return readIntValue(bytes, InstantaneousPower)
    }

    private var previousCrankEventTime: Int? = null
    private var previousInstantaneousCadence: Double? = null
    private fun instantaneousCadence(bytes: ByteArray): Double? {
        val lastCrankEventTime = readIntValue(bytes, LastCrankEventTime) ?: return null
        println(lastCrankEventTime)
        previousCrankEventTime.let {
            previousCrankEventTime = lastCrankEventTime
            return if (it == null) {
                null
            } else {
                println("now = $lastCrankEventTime. previous = $it")
                if (it != lastCrankEventTime) {
                    if (lastCrankEventTime < it) {
                        previousInstantaneousCadence = 60.0 / (lastCrankEventTime + 65535 - it) * 1024.0
                    } else {
                        previousInstantaneousCadence = 60.0 / (lastCrankEventTime - it) * 1024.0
                    }
                }
                return previousInstantaneousCadence
            }
        }

        // TODO roll over?
    }

    private fun readIntValue(bytes: ByteArray, field: Field): Int? {
        if (!field.isPresentIn(bytes)) {
            return null
        }

        var offset = 0
        for (i in 0..fields.count()) {
            val f = fields[i]
            if (f.name == field.name) {
                val intValue = bytes.readIntValue(field.format, offset)
                return intValue
            }

            if (f.isPresentIn(bytes)) {
                offset += f.format.numberOfBytes()
            }
        }

        return null
    }
}
