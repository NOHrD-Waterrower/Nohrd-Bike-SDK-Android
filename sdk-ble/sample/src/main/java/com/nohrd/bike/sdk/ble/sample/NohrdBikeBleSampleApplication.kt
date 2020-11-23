package com.nohrd.bike.sdk.ble.sample

import android.app.Application
import android.content.Context

val Context.nohrdBikeBleSampleApplication: NohrdBikeBleSampleApplication
    get() = (applicationContext as NohrdBikeBleSampleApplication)

class NohrdBikeBleSampleApplication : Application()
