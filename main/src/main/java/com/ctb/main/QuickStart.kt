package com.ctb.main

object QuickStart {
    fun init(
        baseURL: String,
        isDebug: Boolean,
    ) {
        QuickStartModule.injectFeature(baseURL, isDebug)
    }
}
