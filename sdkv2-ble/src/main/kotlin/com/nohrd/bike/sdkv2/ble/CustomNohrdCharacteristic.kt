package com.nohrd.bike.sdkv2.ble

import java.util.UUID

/**
 * Describes the custom characteristic used by the Nohrd Bike v2
 */
public object CustomNohrdCharacteristic {

    /**
     * The UUID value that identifies this characteristic.
     */
    public val uuid: UUID = UUID.fromString("8EC92001-F315-4F60-9FB8-838830DAEA50")
}
