package com.arvatel.baam

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


interface InterfaceDataSaver{
    fun getCookie() : String?
    fun setCookie(c: String?)
    fun getSession() : String?
    fun setSession(s: String?)
    fun getSecretCode() : String?
    fun setSecretCode(sc: String?)
}

interface InterfaceSendRequest{
    fun sendRequest()
}

class MainActivity : AppCompatActivity(), InterfaceDataSaver{

    private var cookie: String? = null
    private var secretCode: String? = null
    private var session: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissionList, MY_PERMISSION_REQUEST_CODE)
    }

    companion object{
        private val permissionList = arrayOf(Manifest.permission.CAMERA)
        private const val MY_PERMISSION_REQUEST_CODE = 1001
    }

    override fun getCookie() : String? {
    return cookie
}

    override fun setCookie(c: String?) {
        if (c != null) {
            cookie = c
        }
    }

    override fun setSession(s: String?) {
        if (s != null) {
            session = s
        }
    }
    override fun getSession(): String? {
        return session
    }

    override fun setSecretCode(sc: String?) {
        if (sc != null) {
            secretCode = sc
        }
    }

    override fun getSecretCode(): String? {
        return secretCode
    }

}


