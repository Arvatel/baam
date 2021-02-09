package com.arvatel.baam

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_web_view.view.*


class WebViewFragment : Fragment() {

    val mainUrl : String = "https://baam.duckdns.org/"
    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)

        mainView = view
        val myWebView : WebView = view.webView
        myWebView.loadUrl(mainUrl)

        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url == mainUrl){
                    goToScan()
                }
//                Toast.makeText((activity as Activity), "Trying to redirect {$url}", Toast.LENGTH_LONG).show()
//                Log.d("My Webview", url)

                return false //Allow WebView to load url
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                if (view != null) {
                    goToScan()
                }

                super.onPageFinished(view, url)
            }
        }
        myWebView.settings.javaScriptEnabled = true

//        view.button.setOnClickListener {
//            Navigation.findNavController(mainView).navigate(R.id.action_webViewFragment_to_QRScanner)
//        }

        return view
    }

    fun goToScan(){
        val cookieManager : CookieManager = CookieManager.getInstance()
        cookieManager.acceptCookie()
        var cookie : String? = null

        if (cookieManager.hasCookies())
            cookie = cookieManager.getCookie(mainUrl)

        (activity as InterfaceDataSaver).setCookie(cookie)
//        Toast.makeText((activity as Activity), "Cookie: {$cookie}", Toast.LENGTH_LONG).show()
        Navigation.findNavController(mainView).navigate(R.id.action_webViewFragment_to_QRScanner)
    }


}