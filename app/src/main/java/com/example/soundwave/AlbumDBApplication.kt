package com.example.soundwave

import android.app.Application
import com.example.soundwave.database.AppContainer
import com.example.soundwave.database.DefaultAppContainer

class AlbumDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}