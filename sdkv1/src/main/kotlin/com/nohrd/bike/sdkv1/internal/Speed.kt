package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Speed

internal val Number.metersPerSecond: Speed get() = Speed.fromMetersPerSecond(this)
