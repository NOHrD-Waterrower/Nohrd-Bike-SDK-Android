package com.nohrd.bike.cyclingpower.internal.powerdataspecification

import com.nohrd.bike.cyclingpower.internal.gattspecification.Field
import com.nohrd.bike.cyclingpower.internal.gattspecification.Format

internal object FlagsField : Field {

    override val name = "Flags"
    override val format = Format.UInt16

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return true
    }
}
