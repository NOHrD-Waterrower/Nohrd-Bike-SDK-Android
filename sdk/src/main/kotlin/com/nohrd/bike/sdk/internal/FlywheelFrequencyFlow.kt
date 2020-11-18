package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequencyCalculator
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal fun Flow<DataPacket>.flywheelFrequency(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Flow<FlywheelFrequency?> {
    val calculator = FlywheelFrequencyCalculator()
    return filterIsInstance<SpeedPacket>()
        .map { calculator.offer(it.flywheelMeasurement) }
        .flatMapLatest { value -> cadenceWithTimeoutFor(value, dispatcher) }
}

private fun cadenceWithTimeoutFor(
    value: FlywheelFrequency,
    dispatcher: CoroutineDispatcher,
) = flow {
    emit(value)
    val delayMillis = (1 / value.revolutionsPerSecond * 10000).toLong().coerceAtMost(10000)
    delay(delayMillis)
    emit(null)
}.flowOn(dispatcher)
