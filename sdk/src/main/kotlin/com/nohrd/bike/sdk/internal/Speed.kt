package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Speed

internal val Number.metersPerSecond: Speed get() = Speed.fromMetersPerSecond(this)
