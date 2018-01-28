package com.koresuniku.wishmaster_v4.core.domain.client

import okhttp3.Interceptor
import okhttp3.HttpUrl
import java.io.IOException
import javax.inject.Inject


/**
 * Created by koresuniku on 15.01.18.
 */

class HostSelectionInterceptor @Inject constructor(): Interceptor {
    @Volatile private var host: String? = null

    fun setHost(host: String) {
        this.host = HttpUrl.parse(host)?.host()
    }

    fun getHost() = this.host

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val reqUrl = request.url().host()

        val host = this.host
        if (host != null) {
            val newUrl = request.url().newBuilder()
                    .host(host)
                    .build()
            request = request.newBuilder()
                    .url(newUrl)
                    .build()
        }
        return chain.proceed(request)
    }
}