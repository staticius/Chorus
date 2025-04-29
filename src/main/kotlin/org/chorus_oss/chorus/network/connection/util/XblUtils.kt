package org.chorus_oss.chorus.network.connection.util

import io.netty.util.internal.logging.InternalLogger
import io.netty.util.internal.logging.InternalLoggerFactory

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool

object XblUtils {
    private val log: InternalLogger = InternalLoggerFactory.getInstance(XblUtils::class.java)

    private const val TOKEN_URL_STRING = "https://login.live.com/oauth20.srf"
    private const val REQUEST_URL_STRING =
        "https://login.live.com/oauth20_authorize.srf?client_id=00000000441cc96b&redirect_uri=https://login.live.com/oauth20_desktop.srf&response_type=token&display=touch&scope=service::user.auth.xboxlive.com::MBI_SSL&locale=en"
    private var TOKEN_URL: URL
    private var REQUEST_URL: URL

    init {
        try {
            TOKEN_URL = URI(TOKEN_URL_STRING).toURL()
            REQUEST_URL = URI(REQUEST_URL_STRING).toURL()
        } catch (e: MalformedURLException) {
            throw AssertionError("Unable to create XBL URLs", e)
        }
    }

    @Throws(IOException::class)
    fun test(): CompletableFuture<Any?> {
        val future: CompletableFuture<*> = CompletableFuture<Any>()
        ForkJoinPool.commonPool().execute {
            try {
                val connection = TOKEN_URL.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response: String
                BufferedReader(InputStreamReader(connection.inputStream)).use { `in` ->
                    val buffer = StringBuilder()
                    var line: String?
                    while ((`in`.readLine().also { line = it }) != null) {
                        buffer.append(line)
                    }
                    response = buffer.toString()
                }
                log.debug("RESPONSE\n{}", response)
            } catch (e: Throwable) {
                future.completeExceptionally(e)
            }
        }

        return CompletableFuture.supplyAsync { null }
    }
}
