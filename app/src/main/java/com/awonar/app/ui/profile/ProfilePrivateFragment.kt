package com.awonar.app.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentProfilePrivateBinding

class ProfilePrivateFragment : Fragment() {

    private val binding: AwonarFragmentProfilePrivateBinding by lazy {
        AwonarFragmentProfilePrivateBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    @SuppressLint("JavascriptInterface")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        with(binding.webview) {
//            addJavascriptInterface("what", "WEBVIEW_JS")
//            with(settings) {
//                javaScriptEnabled = true
//                domStorageEnabled = true
//                loadWithOverviewMode = true
//                useWideViewPort = true
//                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
//                builtInZoomControls = true
//                displayZoomControls = false
//                javaScriptCanOpenWindowsAutomatically = true
//                cacheMode = WebSettings.LOAD_NO_CACHE
//                setSupportZoom(true)
//                setAppCacheEnabled(false)
//            }
//
//            loadUrl("file:///android_asset/bar-rechart.html")
//        }
    }
}