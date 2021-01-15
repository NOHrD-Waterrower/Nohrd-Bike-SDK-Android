package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Energy

internal val Number.joules: Energy get() = Energy.fromJoules(this)
