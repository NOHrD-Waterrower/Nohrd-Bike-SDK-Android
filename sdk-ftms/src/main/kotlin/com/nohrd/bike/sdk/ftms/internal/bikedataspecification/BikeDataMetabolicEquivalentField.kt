package com.nohrd.bike.sdk.ftms.internal.bikedataspecification

import com.nohrd.bike.sdk.ftms.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Format

internal object BikeDataMetabolicEquivalentField : Field {

    override val name = "Metabolic Equivalent"
    override val format = Format.UInt8

    private val requirement = BitRequirement(bitIndex = 10, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
