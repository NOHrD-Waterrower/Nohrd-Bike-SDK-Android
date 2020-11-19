package com.nohrd.bike.sdk

data class Energy(val joules: Double)

val Double.joules get() = Energy(this)
val Int.joules get() = toDouble().joules
