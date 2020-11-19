package com.nohrd.bike.sdk

public data class Energy(val joules: Double)

public val Double.joules: Energy get() = Energy(this)
public val Int.joules: Energy get() = toDouble().joules
