package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.sdkv1.internal.protocol.BikeProtocol
import com.nohrd.bike.sdkv1.internal.protocol.DataPacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal fun Flow<Byte>.dataPackets(): Flow<DataPacket> {
    val protocol = BikeProtocol()
    return mapNotNull { protocol.offer(it) }
}
