package com.mobapptuts.kotlinwebview

import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.browser_toolbar.*

class MainActivity : AppCompatActivity(), HistoryDialogFragment.WebHistory {

    lateinit var fullscreenView: View
    override fun getWebView() = webview

/*
    override fun webpageSelected(webTitle: String) {
        val webHistory = webview.copyBackForwardList()
        for (i in 0 until webHistory.size) {
            if (webHistory.getItemAtIndex(i).title.equals(webTitle)) {
                webview.goBackOrForward(i - webHistory.currentIndex)
                break
            }
        }
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uriText.setOnEditorActionListener { textView, i, keyEvent ->
            if(i.equals(EditorInfo.IME_ACTION_SEND)) {
                loadWebpage()
                true
            } else false
        }
        backButton.setOnClickListener {
            if(webview.canGoBack()) webview.goBack()
        }
        forwardButton.setOnClickListener {
            if(webview.canGoForward()) webview.goForward()
        }
        backButton.setOnLongClickListener {
            getHistoryDialog(true)
            true
        }
        forwardButton.setOnLongClickListener {
            getHistoryDialog(false)
            true
        }
    }

    fun getHistoryDialog(backAdapter: Boolean) {
        val historyDialogFragment = HistoryDialogFragment()
        val bundle = Bundle()
        bundle.putBoolean(historyDialogFragment.selectBackAdapter, backAdapter)
        historyDialogFragment.arguments = bundle
        historyDialogFragment.show(supportFragmentManager, "HistoryDialog")
    }

    @Throws(UnsupportedOperationException::class)
    fun buildUri(authority: String) : Uri {
        val builder = Uri.Builder()
        builder.scheme("https")
                .authority(authority)
        return builder.build()
    }

    fun loadWebpage() {

        // Enable javascript
        webview.settings.javaScriptEnabled = true
        pageLoadStatus()
        updateProgress()
        try {
            val uri = buildUri(uriText.text.toString())
            webview.loadUrl(uri.toString())
        } catch(e: UnsupportedOperationException) {
            e.printStackTrace()
        }
    }

    fun updateProgress() {
        webview.webChromeClient = object: WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                pageLoadProgressBar.progress = newProgress
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)

                if (view is FrameLayout) {
                    fullscreenView = view
                    fullscreenContainer.addView(fullscreenView)
                    fullscreenContainer.visibility = View.VISIBLE
                    mainContainer.visibility = View.GONE
                }
            }

            override fun onHideCustomView() {
                super.onHideCustomView()

                fullscreenContainer.removeView(fullscreenView)
                fullscreenContainer.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE
            }
        }
    }
    fun pageLoadStatus() {
        webview.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                pageLoadProgressBar.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                pageLoadProgressBar.visibility = View.VISIBLE
                pageLoadProgressBar.progress = 0
            }
        }
    }
}
