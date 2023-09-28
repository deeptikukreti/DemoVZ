package com.example.demovz.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp()
open class AppController : Application() {

    companion object {
        var instance: AppController? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}