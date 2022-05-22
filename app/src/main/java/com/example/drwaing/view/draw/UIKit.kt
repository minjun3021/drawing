package com.example.drwaing.view.draw

import android.app.Application
import android.content.res.Resources


object UIKit {

    lateinit var application: Application
    internal var initialized = false
    private var resources: Resources? = null

    fun initialize(application: Application) {
        initialized = true
        UIKit.application = application
    }

    fun setResources(resources: Resources) {
        UIKit.resources = resources
    }

    fun getResources() = when {
        resources != null -> resources
        initialized -> application.resources
        else -> null
    }
}