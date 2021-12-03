package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Energy

internal val Number.joules: Energy get() = Energy.fromJoules(this)
