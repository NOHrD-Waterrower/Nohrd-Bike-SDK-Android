package com.nohrd.bike.sdk.ftms.internal.bikedataspecification

import com.nohrd.bike.sdk.ftms.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Format

internal object BikeDataInstantaneousPowerField : Field {

    override val name = "Instantaneous Power"
    override val format = Format.SInt16

    private val requirement = BitRequirement(bitIndex = 6, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
