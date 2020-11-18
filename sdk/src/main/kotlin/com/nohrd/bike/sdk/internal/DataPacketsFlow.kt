package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.internal.protocol.BikeProtocol
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal fun Flow<Byte>.dataPackets(): Flow<DataPacket> {
    val protocol = BikeProtocol()
    return mapNotNull { protocol.offer(it) }
}
