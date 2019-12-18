package com.sunragav.indiecampers.remotedata.http

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val publicKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("apikey", publicKey)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}