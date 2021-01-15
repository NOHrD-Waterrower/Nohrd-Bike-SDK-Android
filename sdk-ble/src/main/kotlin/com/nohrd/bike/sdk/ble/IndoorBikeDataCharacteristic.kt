package com.nohrd.bike.sdk.ble

import java.util.UUID

/**
 * Describes the Indoor Bike Data Characteristic
 *
 */
public object IndoorBikeDataCharacteristic {

    /**
     * The UUID value that identifies this characteristic.
     */
    public val uuid: UUID = UUID.fromString("00002AD2-0000-1000-8000-00805F9B34FB")
}
