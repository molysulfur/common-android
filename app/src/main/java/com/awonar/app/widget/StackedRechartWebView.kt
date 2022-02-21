package com.awonar.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import com.awonar.android.shared.utils.JsonUtil
import kotlinx.parcelize.Parcelize
import java.util.*


@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
class StackedRechartWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : WebView(context, attrs) {
    private var listener: IStackedRechartListener? = null


    fun setListener(listener: IStackedRechartListener) {
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
        loadUrl("file:///android_asset/stacked-rechart.html")
    }

    fun setData(data: List<StackedRechartEntity>) {
        evaluateJavascript("window.setData(`${JsonUtil.gsonToJson(data)}`)") {

        }
    }

    interface IStackedRechartListener {
        fun chartAlready()
    }

    @Parcelize
    data class StackedRechartEntity(
        val label: String?,
        val avg: Float,
        val max: Float,
        val year: Int,
    ) : Parcelable
}