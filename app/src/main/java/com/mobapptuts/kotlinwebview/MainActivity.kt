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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.browser_toolbar.*

class MainActivity : AppCompatActivity() {

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
            getHistoryDialog(getBackHistory())
            true
        }
        forwardButton.setOnLongClickListener {
            getHistoryDialog(getForwardHistory())
            true
        }
    }

    fun getHistoryDialog(historyList: ArrayList<String>) {
        val historyDialogFragment = HistoryDialogFragment()
        val bundle = Bundle()
        bundle.putStringArrayList(historyDialogFragment.history, historyList)
        historyDialogFragment.arguments = bundle
        historyDialogFragment.show(supportFragmentManager, "HistoryDialog")
    }

    fun getBackHistory() : ArrayList<String> {
        val webBackHistory = webview.copyBackForwardList()
        val historyList = ArrayList<String>()

        for (i in 0 until webBackHistory.currentIndex)
            historyList.add(webBackHistory.getItemAtIndex(i).title)

        historyList.reverse()
        return historyList
    }

    fun getForwardHistory() : ArrayList<String> {
        val webForwardHistory = webview.copyBackForwardList()
        val historyList = ArrayList<String>()

        for (i in 0 until webForwardHistory.size - webForwardHistory.currentIndex -1)
            historyList.add(webForwardHistory.getItemAtIndex(
                    webForwardHistory.currentIndex + i + 1
            ).title)
        return historyList
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
