package com.awonar.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import com.awonar.android.shared.utils.JsonUtil


@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
class BarRechartWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : WebView(context, attrs) {
    private var listener: IBarRechartListener? = null


    fun setListener(listener: IBarRechartListener) {
        this.listener = listener
    }

    @JavascriptInterface
    fun chartAlready() {
        kotlin.run {
            listener?.chartAlready()
        }
    }

    init {
        with(this) {
            addJavascriptInterface(this, "WEBVIEW_JS")
            with(settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                builtInZoomControls = true
                displayZoomControls = false
                javaScriptCanOpenWindowsAutomatically = true
                cacheMode = WebSettings.LOAD_NO_CACHE
                setSupportZoom(true)
                setAppCacheEnabled(false)
            }

        }
    }

    fun start() {
        loadUrl("file:///android_asset/bar-rechart.html")
    }

    fun setData(data: List<BarRechartEntity>) {
        evaluateJavascript("window.setData(`${JsonUtil.gsonToJson(data)}`)") {

        }
    }

    interface IBarRechartListener {
        fun chartAlready()
    }

    data class BarRechartEntity(
        val label: String?,
        val value: Float,
        val index: Int,
    )
}