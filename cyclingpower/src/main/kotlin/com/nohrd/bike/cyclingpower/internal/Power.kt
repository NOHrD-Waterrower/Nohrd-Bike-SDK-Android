package com.nohrd.bike.cyclingpower.internal

import com.nohrd.bike.Power

internal inline val Number.watts: Power get() = Power.fromWatts(this)
