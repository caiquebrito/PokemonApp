package com.ctb.quickstart

import android.app.Application
import android.content.Context
import com.ctb.common.Common
import com.ctb.main.QuickStart
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuickStartApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        startKoin {
            androidContext(base)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Common.init()
        QuickStart.init(baseURL = BuildConfig.DEFAULT_API_URL, isDebug = true)
    }
}
