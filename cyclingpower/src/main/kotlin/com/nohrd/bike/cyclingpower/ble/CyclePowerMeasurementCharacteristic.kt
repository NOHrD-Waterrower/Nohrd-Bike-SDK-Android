package com.nohrd.bike.cyclingpower.ble

import java.util.UUID

/**
 * Describes the Cycling Power Measurement Characteristic
 */
public object CyclePowerMeasurementCharacteristic {

    /**
     * The UUID value that identifies this characteristic.
     */
    public val uuid: UUID = UUID.fromString("00002A63-0000-1000-8000-00805F9B34FB")
}
