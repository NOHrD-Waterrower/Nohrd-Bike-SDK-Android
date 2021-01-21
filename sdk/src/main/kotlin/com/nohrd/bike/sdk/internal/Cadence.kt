package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Cadence

internal val Double.rpm: Cadence get() = Cadence(this)
internal val Int.rpm: Cadence get() = Cadence(this.toDouble())
