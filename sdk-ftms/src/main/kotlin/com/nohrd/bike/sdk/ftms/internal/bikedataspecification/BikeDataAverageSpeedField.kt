package com.nohrd.bike.sdk.ftms.internal.bikedataspecification

import com.nohrd.bike.sdk.ftms.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Format

internal object BikeDataAverageSpeedField : Field {

    override val name = "Average Speed"
    override val format = Format.UInt16

    private val requirement = BitRequirement(bitIndex = 1, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
