package com.nohrd.bike.sdkv1.internal.math.power

import kotlin.math.pow

internal object TorqueCalculator {

    private val p = 2.5

    private val m_1 = 8.74
    private val m_0 = 0.0
    private val M_13 = -153.3216582
    private val M_12 = 289.3930729
    private val M_11 = -31.968328
    private val M_10 = 2.968478301

    private val kappa = -3.0
    private val lambda = 1.13

    fun torque(resistance: Double, cadence: Double): Double {
        val m = m_1 * resistance + m_0
        val M_1 = M_13 * (resistance exp 3) + M_12 * (resistance exp 2) + M_11 * resistance + M_10
        val M_2 = (M_1 / kappa)
        val M_3 = (M_1 - m) / (3 * (p exp 2))

        return (M_3 * (cadence exp 3) + M_2 * (cadence exp 2) + M_1 * cadence) * lambda
    }

    private infix fun Double.exp(power: Int): Double = this.pow(power)
}
