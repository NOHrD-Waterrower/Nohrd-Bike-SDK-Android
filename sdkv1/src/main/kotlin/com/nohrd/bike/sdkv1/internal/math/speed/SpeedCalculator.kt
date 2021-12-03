package com.nohrd.bike.sdkv1.internal.math.speed

import com.nohrd.bike.Power
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv1.internal.metersPerSecond

internal class SpeedCalculator {

    fun calculateSpeed(power: Power): Speed {
        val airDensity = (1.293 - 0.00426 * temperatureCelsius) * Math.exp(-elevationMeters / 7000.0)
        val totalWeightNewtons = 9.8 * (riderWeightKilograms + bikeWeightKilograms)

        val airResistance = 0.5 * frontalArea * airDensity
        val totalResistance = totalWeightNewtons * rollingResistance

        return newton(airResistance, 0.0, totalResistance, transVelocityConstant, power.watts).metersPerSecond
    }

    /** "Newton's method" */
    private fun newton(aero: Double, hw: Double, tr: Double, tran: Double, p: Double): Double {
        var vel = 20.0 // Initial guess
        val TOL = 0.05 // tolerance
        for (i in 1..10) {
            val tv = vel + hw
            val aeroEff = if (tv > 0.0) aero else -aero // wind in face, must reverse effect
            val f = vel * (aeroEff * tv * tv + tr) - tran * p // the function
            val fp = aeroEff * (3.0 * vel + hw) * tv + tr // the derivative
            val vNew = if (fp == 0.0) 0.0 else vel - f / fp
            if (Math.abs(vNew - vel) < TOL) return vNew // success
            vel = vNew
        }
        return 0.0 // failed to converge
    }

    private companion object {

        /* Details of environment we're using to calculate the progress */
        private const val riderWeightKilograms = 70
        private const val bikeWeightKilograms = 9
        private const val rollingResistance = 0.005
        private const val frontalArea = 0.388
        private const val temperatureCelsius = 25
        private const val elevationMeters = 100
        private const val transVelocityConstant = 0.95 // Even the original source of these calculations is not sure what this is, but it is a required constant
    }
}
