package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataAverageCadenceField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataAveragePowerField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataAverageSpeedField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataElapsedTimeField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataEnergyPerHourField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataEnergyPerMinuteField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataFlagsField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataHeartRateField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataInstantaneousCadenceField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataInstantaneousPowerField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataInstantaneousSpeedField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataMetabolicEquivalentField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataRemainingTimeField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataResistanceLevelField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataTotalDistanceField
import com.nohrd.bike.sdk.ftms.internal.bikedataspecification.BikeDataTotalEnergyField
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.readIntValue

public object IndoorBikeDataCharacteristicDecoder {

    /**
     * Decodes given [bytes] into a [IndoorBikeData] instance.
     *
     * Due to restrictions in the byte buffer size some of the [IndoorBikeData]
     * properties will be absent, which is represented as a `nil` value.
     *
     * @param bytes A [ByteArray] instance that contains the encoded data
     *              as described in the Rower Data characteristic specification.
     *
     * @return A [IndoorBikeData] instance with the decoded properties.
     *         Properties will be `nil` if not present in the encoded data.
     */
    public fun decode(bytes: ByteArray): IndoorBikeData {
        return IndoorBikeData(
            instantaneousPowerWatts = instantaneousPower(bytes),
            instantaneousCadence = instantaneousCadence(bytes),
            resistanceLevel = resistanceLevel(bytes),
            heartRate = heartRate(bytes),
        )
    }

    private val fields = listOf(
        BikeDataFlagsField,
        BikeDataInstantaneousSpeedField,
        BikeDataAverageSpeedField,
        BikeDataInstantaneousCadenceField,
        BikeDataAverageCadenceField,
        BikeDataTotalDistanceField,
        BikeDataResistanceLevelField,
        BikeDataInstantaneousPowerField,
        BikeDataAveragePowerField,
        BikeDataTotalEnergyField,
        BikeDataEnergyPerHourField,
        BikeDataEnergyPerMinuteField,
        BikeDataHeartRateField,
        BikeDataMetabolicEquivalentField,
        BikeDataElapsedTimeField,
        BikeDataRemainingTimeField,
    )

    private fun instantaneousPower(bytes: ByteArray): Int? {
        return readIntValue(bytes, BikeDataInstantaneousPowerField)
    }

    private fun instantaneousCadence(bytes: ByteArray): Double? {
        val intValue = readIntValue(bytes, BikeDataInstantaneousCadenceField) ?: return null

        return intValue / 2.0
    }

    private fun resistanceLevel(bytes: ByteArray): Float {
        val intValue = readIntValue(bytes, BikeDataResistanceLevelField) ?: return 0f

        return intValue / 2f
    }

    private fun heartRate(bytes: ByteArray): Int? {
        return readIntValue(bytes, BikeDataHeartRateField)
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
