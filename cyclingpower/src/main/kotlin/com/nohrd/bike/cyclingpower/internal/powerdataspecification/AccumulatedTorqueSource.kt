package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.BitRequirement
import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object AccumulatedTorqueSource : Field {

    override val name = "Accumulated Torque Source"
    override val format = Format.None

    private val requirement = BitRequirement(bitIndex = 3, bitValue = 1)

    // When present it is crank based, else wheel based
    override fun isPresentIn(bytes: ByteArray): Boolean {
        return requirement.checkIn(bytes)
    }
}
