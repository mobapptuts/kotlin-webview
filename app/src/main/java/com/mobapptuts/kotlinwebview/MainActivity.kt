package com.mobapptuts.kotlinwebview

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
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
    }

    @Throws(UnsupportedOperationException::class)
    fun buildUri(authority: String) : Uri {
        val builder = Uri.Builder()
        builder.scheme("https")
                .authority(authority)
        return builder.build()
    }

    fun loadWebpage() {
        webview.loadUrl("")

        // Enable javascript
        webview.settings.javaScriptEnabled = true
        // Keep browsing links inside webview
        webview.webViewClient = WebViewClient()
        try {
            val uri = buildUri(uriText.text.toString())
            webview.loadUrl(uri.toString())
        } catch(e: UnsupportedOperationException) {
            e.printStackTrace()
        }
    }
}
