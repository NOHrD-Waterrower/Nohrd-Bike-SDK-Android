package com.nohrd.bike.sdkv2.internal.bikedataspecification

import com.nohrd.bike.sdkv2.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdkv2.internal.gattspecification.Field
import com.nohrd.bike.sdkv2.internal.gattspecification.Format

internal object BikeDataTotalDistanceField : Field {

    override val name = "Total Distance"
    override val format = Format.UInt24

    private val requirement = BitRequirement(bitIndex = 4, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
