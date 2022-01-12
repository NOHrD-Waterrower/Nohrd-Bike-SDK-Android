package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object AccumulatedEnergy : Field {

    override val name = "Accumulated Energy"
    override val format = Format.UInt16

    private val requirement = BitRequirement(bitIndex = 11, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
