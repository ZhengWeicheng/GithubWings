package com.wings.githubwings.framework

import android.app.Application
import com.google.gson.Gson

class BaseCore {
    companion object {
        lateinit var app: Application
        val baseGson = Gson()
    }
}