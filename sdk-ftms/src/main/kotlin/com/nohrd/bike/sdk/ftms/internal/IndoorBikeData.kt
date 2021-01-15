package com.nohrd.bike.sdk.ftms.internal

/**
 * A data class that contains decoded Indoor Bike Data.
 *
 * This class follows the Indoor Bike Data characteristic specification
 * as described in section 4.9 "Indoor Bike Data" of the
 * Fitness Machine Service (FTMS) Bluetooth Service specification,
 * revision v1.0.
 *
 * A copy of this specification can be found on
 * https://www.bluetooth.com/specifications/gatt/
 */
public data class IndoorBikeData(

    /**
     * The instantaneous power in watts.
     */
    val instantaneousPowerWatts: Int?,

    /**
     * The instantaneous cadence in rounds per minute.
     */
    val instantaneousCadence: Double?,

    /**
     * Denotes the resistance level applied to the bike (a value in 0f..1f)
     */
    val resistanceLevel: Float,
)
