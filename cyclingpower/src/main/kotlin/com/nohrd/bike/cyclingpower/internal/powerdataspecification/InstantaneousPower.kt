package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object InstantaneousPower : Field {

    override val name = "Instantaneous Power"
    override val format = Format.SInt16

    private val requirement = BitRequirement(bitIndex = 0, bitValue = 0)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return true
    }
}
