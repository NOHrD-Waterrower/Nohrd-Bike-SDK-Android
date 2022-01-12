package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object WheelRevolutionData : Field {

    override val name = "Wheel Revolution Data"
    override val format = Format.UInt32

    private val requirement = BitRequirement(bitIndex = 4, bitValue = 1)

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
