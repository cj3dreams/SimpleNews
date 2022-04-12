package com.twenty2byte.simplenews.view.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.twenty2byte.simplenews.R
import java.util.*


class WebViewFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView
    private lateinit var gotUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as MainActivity
        activity.backButton.visibility = View.VISIBLE
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_web_view, container, false)

        val activity = activity as MainActivity
        val sharedPreferencesLang = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
        if (sharedPreferencesLang!!.contains("Language")) {
            val config = resources.configuration
            val locale = Locale(sharedPreferencesLang.getString("Language","").toString())
            Locale.setDefault(locale)
            config.setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                activity.createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        progressBar = view.findViewById(R.id.webViewLoading)
        webView = view.findViewById(R.id.webView)
        if (webView.isVisible) {
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    when (progress) {
                        in 8..10 -> this@WebViewFragment.view?.let { snackBar(it, requireContext().getString(R.string.pageLoadStarted)) }
                        in 33..35 -> this@WebViewFragment.view?.let {  snackBar(it,  (requireContext().getString(R.string.loadingPage)) + "$progress%") }
                        in 55..65 -> this@WebViewFragment.view?.let {  snackBar(it,  (requireContext().getString(R.string.loadingPage)) + "$progress%") }
                        in 75..85 -> this@WebViewFragment.view?.let {  snackBar(it,  (requireContext().getString(R.string.loadingPage)) + "$progress%") }
                        in 90..100 -> this@WebViewFragment.view?.let {  snackBar(it,  (requireContext().getString(R.string.loadingPage)) + "$progress%") }
                    }
                    if (progress >= 75) progressBar.visibility = View.GONE
                }
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        webView.loadUrl(gotUrl)
        return view
    }

    companion object{
        fun getUrl(url: String): WebViewFragment{
            val fragment = WebViewFragment()
            fragment.gotUrl = url
            return fragment
        }
    }
    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (!url.isNullOrEmpty())
                view?.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, url)
        }
    }
    private fun snackBar(v: View, message: String){
        val snackBarView = Snackbar.make(v, message , Snackbar.LENGTH_SHORT)
        val view = snackBarView.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = 160
        view.layoutParams = params
        view.background = ContextCompat.getDrawable(requireContext(),R.color.black)
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        webView.isEnabled = false
        val activity = activity as MainActivity
        activity.backButton.visibility = View.GONE
    }
}
