package com.nohrd.bike.cyclingpower.internal

/**
 * A data class that contains decoded Cycling Power Data.
 *
 * This class follows the Cyling Power Measure characteristic specification
 * as described in section 3.2 "Cyling Power Measurement" of the
 * Cycling Power Service Bluetooth Service specification,
 * revision v1.1.
 *
 * A copy of this specification can be found on
 * https://www.bluetooth.com/specifications/gatt/
 */
public data class CyclingPowerData(

    /**
     * The instantaneous power in watts.
     */
    val instantaneousPowerWatts: Int?,

    /**
     * The instantaneous cadence in rounds per minute.
     */
    val instantaneousCadence: Double?,

)
