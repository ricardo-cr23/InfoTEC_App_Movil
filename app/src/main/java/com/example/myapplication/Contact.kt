package com.example.myapplication

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class Contact : Fragment() {

    private lateinit var downloadListener: DownloadListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_contact, container, false)

        createDownloadListener()
        val mWebView = view.findViewById<View>(R.id.webView) as WebView
        mWebView.loadUrl("http://info-ic.tec.siua.ac.cr/index.php/contactos/")

        val webSetting = mWebView.settings
        webSetting.javaScriptEnabled = true
        mWebView.webViewClient = WebViewClient()

        mWebView.setDownloadListener(downloadListener)

        mWebView.canGoBack()
        mWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK

                && event.action == MotionEvent.ACTION_UP
                && mWebView.canGoBack()){
                mWebView.goBack()
                return@OnKeyListener true
            }
            false
        })

        return view
    }

    private fun createDownloadListener()
    {
        downloadListener = DownloadListener { url, userAgent, contentDescription, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val fileName = URLUtil.guessFileName(url, contentDescription, mimetype)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
    }

}