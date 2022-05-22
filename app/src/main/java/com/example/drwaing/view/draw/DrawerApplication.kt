package com.example.drwaing.view.draw

import android.app.Application

class DrawerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UIKit.initialize(this)
        UIKit.setResources(resources)
    }
}