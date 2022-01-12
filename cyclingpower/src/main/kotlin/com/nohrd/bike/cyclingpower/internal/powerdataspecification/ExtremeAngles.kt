package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object ExtremeAngles : Field {

    override val name = "Extreme Angles"
    override val format = Format.UInt16 // Unknown

    private val requirement = BitRequirement(bitIndex = 8, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
