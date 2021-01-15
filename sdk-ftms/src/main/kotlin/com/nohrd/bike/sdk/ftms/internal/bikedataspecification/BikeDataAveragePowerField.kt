package com.nohrd.bike.sdk.ftms.internal.bikedataspecification

import com.nohrd.bike.sdk.ftms.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Format

internal object BikeDataAveragePowerField : Field {

    override val name = "Average Power"
    override val format = Format.SInt16

    private val requirement = BitRequirement(bitIndex = 7, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
