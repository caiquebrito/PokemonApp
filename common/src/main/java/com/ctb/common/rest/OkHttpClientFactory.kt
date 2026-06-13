package com.ctb.common.rest

import android.util.Log
import com.ctb.common.BuildConfig
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor? = null,
        timeout: Long? = null,
    ): OkHttpClient {
        val certificatePinner =
            CertificatePinner
                .Builder()
                .add(
                    BuildConfig.DOMAIN_URL,
                    BuildConfig.SHA256,
                ).build()
        val builder =
            OkHttpClient
                .Builder()
                .certificatePinner(certificatePinner)
                .connectTimeout(timeout ?: DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(timeout ?: DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        httpLoggingInterceptor?.let {
            builder.addInterceptor(it)
        }

        return builder.build()
    }

    fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
        loggingInterceptor.level =
            if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        return loggingInterceptor
    }

    private const val DEFAULT_TIMEOUT = 20L
}
