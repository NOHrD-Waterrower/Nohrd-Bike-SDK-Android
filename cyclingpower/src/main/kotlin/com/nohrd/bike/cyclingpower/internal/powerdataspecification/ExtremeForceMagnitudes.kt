package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object ExtremeForceMagnitudes : Field {

    override val name = "Extreme Force Magnitudes"
    override val format = Format.SInt16

    private val requirement = BitRequirement(bitIndex = 6, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
