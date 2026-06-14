package com.ctb.common.rest

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {
    fun <T> create(
        clazz: Class<T>,
        endpoint: String,
        client: OkHttpClient,
    ): T {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(client)
                .build()
        return retrofit.create(clazz)
    }
}
