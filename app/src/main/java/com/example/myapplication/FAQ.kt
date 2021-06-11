package com.example.myapplication

import android.app.DownloadManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment


class FAQ : Fragment() {

    private lateinit var downloadListener: DownloadListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_f_a_q, container, false)

        createDownloadListener()

        val mWebView = view.findViewById<View>(R.id.webView) as WebView

        val webSetting = mWebView.settings

        if(checkConnectivity()){
            webSetting.cacheMode = WebSettings.LOAD_DEFAULT
            mWebView.loadUrl("http://info-ic.tec.siua.ac.cr/index.php/preguntas_frecuentes/")
        } else {
            webSetting.cacheMode = WebSettings.LOAD_CACHE_ONLY
            mWebView.loadUrl("http://info-ic.tec.siua.ac.cr/index.php/preguntas_frecuentes/")
            Toast.makeText(requireActivity(), "No hay conexión a internet. Copia local cargada.",
                Toast.LENGTH_LONG).show()
        }
        webSetting.javaScriptEnabled = true
        mWebView.webViewClient = WebViewClient()

        mWebView.setDownloadListener(downloadListener)

        mWebView.canGoBack()
        mWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK

                && event.action == MotionEvent.ACTION_UP
                && mWebView.canGoBack()
            ) {
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

    private fun checkConnectivity() :Boolean{
        val cm =
            requireActivity().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        return isConnected
    }

}