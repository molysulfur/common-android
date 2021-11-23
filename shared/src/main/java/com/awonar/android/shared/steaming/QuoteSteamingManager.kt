package com.awonar.android.shared.steaming

import com.awonar.android.model.market.Quote
import com.awonar.android.shared.api.NetworkClient
import com.awonar.android.shared.db.hawk.AccessTokenManager
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface QuoteSteamingListener {
    fun marketStatusCallback(event: String, data: Any)
    fun marketQuoteCallback(event: String, data: Array<Quote>)
}

object QuoteSteamingEvent {
    const val subscribe = "subscribe"
    const val unsubscribe = "unsubscribe"
}

@Singleton
class QuoteSteamingManager @Inject constructor(
    private val accessTokenManager: AccessTokenManager,
    private val networkClient: NetworkClient
) :
    WebSocketListener() {

    companion object {
        private val QUOTESTEAMING_CLOSE_CODE = 10000
        private val QUOTESTEAMING_CLOSE_MESSAGE = "Close quote steaming"
        private val EVENT_KEY = "event"
        private val DATA_KEY = "data"
    }

    private val url: String = "wss://streamer.awonar.com/api/v1/streamer?id=${UUID.randomUUID()}"

    private var client: OkHttpClient? = null
    private var webSocket: WebSocket? = null
    private var listener: QuoteSteamingListener? = null

    init {
        client = networkClient.getClient().newBuilder().build()
        val request: Request = Request.Builder().url(url).build()
        webSocket = client?.newWebSocket(request = request, this)
    }


    fun send(event: String, data: String) {
        val request =
            "{\"event\":\"${event}\",\"data\":\"${data}\",\"id\":\"${accessTokenManager.getAccessToken()}\"}"
        webSocket?.send(request)
    }

    fun setListener(listener: QuoteSteamingListener) {
        this.listener = listener
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
    }


    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        prase("$bytes")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        prase(text)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    private fun prase(message: String) {
//        Timber.d(message)
        val obj = JSONObject(message)
        val event = obj.get(EVENT_KEY).toString()
        val data = obj.get(DATA_KEY).toString()
        if (event == "quote") {
            try {
                val quotes: Array<Quote> = Gson().fromJson(
                    data,
                    Array<Quote>::class.java
                )
                listener?.marketQuoteCallback(event = event, data = quotes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}