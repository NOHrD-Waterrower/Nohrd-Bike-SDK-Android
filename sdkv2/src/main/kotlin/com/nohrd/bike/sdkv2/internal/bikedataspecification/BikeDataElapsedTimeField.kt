package com.nohrd.bike.sdkv2.internal.bikedataspecification

import com.nohrd.bike.sdkv2.internal.gattspecification.BitRequirement
import com.nohrd.bike.sdkv2.internal.gattspecification.Field
import com.nohrd.bike.sdkv2.internal.gattspecification.Format

internal object BikeDataElapsedTimeField : Field {

    override val name = "Elapsed Time"
    override val format = Format.UInt16

    private val requirement = BitRequirement(bitIndex = 11, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
