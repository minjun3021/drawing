package com.kmj.ssgssg.view.draw

import android.app.Application

class DrawerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UIKit.initialize(this)
        UIKit.setResources(resources)
    }
}