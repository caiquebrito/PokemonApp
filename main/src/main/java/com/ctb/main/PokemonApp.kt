package com.ctb.main

object PokemonApp {
    fun init(
        baseURL: String,
        isDebug: Boolean,
    ) {
        PokemonModule.injectFeature(baseURL, isDebug)
    }
}
