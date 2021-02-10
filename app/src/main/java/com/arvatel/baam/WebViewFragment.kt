package com.arvatel.baam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arvatel.baam.interfaces.InterfaceAcceptRedirect
import com.arvatel.baam.interfaces.InterfaceDataSaver
import kotlinx.android.synthetic.main.fragment_web_view.view.*


class WebViewFragment : Fragment() {

    val mainUrl : String = "https://baam.duckdns.org/"
    var acceptRedirect : Boolean = true
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
                if (!(activity as InterfaceAcceptRedirect).getAccept() && url == mainUrl){
                    goToScan()
                }
                (activity as InterfaceAcceptRedirect).setAccept(false)
                return false //Allow WebView to load url
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                if (view != null && url == mainUrl) {
                    goToScan()
                }
                (activity as InterfaceAcceptRedirect).setAccept(false)
                super.onPageFinished(view, url)
            }
        }
        myWebView.settings.javaScriptEnabled = true

        return view
    }

    fun goToScan(){
        val cookieManager : CookieManager = CookieManager.getInstance()
        cookieManager.acceptCookie()
        var cookie : String? = null

        if (cookieManager.hasCookies())
            cookie = cookieManager.getCookie(mainUrl)

//        if ((activity as InterfaceDataSaver).getSecretCode() == null) {
//            cookie = "cookie :)"
//        }

        (activity as InterfaceDataSaver).setCookie(cookie)

        val navHostFragment = (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment

        navHostFragment.navController.navigate(R.id.action_webViewFragment_to_QRScanner)
    }
}