package com.nohrd.bike.sdkv2.ble

import java.util.UUID

/**
 * Describes the Custom Service used by the Nohrd Bike v2
 */
public object CustomNohrdService {

    /**
     * The UUID value that identifies this service.
     */
    public val uuid: UUID = UUID.fromString("00001001-C042-66BA-1335-90118F542C77")
}
