package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object LastCrankEventTime : Field {

    override val name = "Last Crank Event Time"
    override val format = Format.UInt16

    private val requirement = BitRequirement(bitIndex = 5, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
