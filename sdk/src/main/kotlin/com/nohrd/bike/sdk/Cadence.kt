package com.nohrd.bike.sdk

data class Cadence(val value: Double)

val Double.rpm get() = Cadence(this)
val Int.rpm get() = Cadence(this.toDouble())
