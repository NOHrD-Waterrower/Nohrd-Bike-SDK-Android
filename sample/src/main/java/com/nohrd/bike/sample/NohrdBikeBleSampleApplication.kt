package com.nohrd.bike.sample

import android.app.Application
import android.content.Context

val Context.nohrdBikeBleSampleApplication: NohrdBikeBleSampleApplication
    get() = (applicationContext as NohrdBikeBleSampleApplication)

class NohrdBikeBleSampleApplication : Application() {

    val appComponent by lazy { AppComponent() }
}
