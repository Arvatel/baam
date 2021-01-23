package com.arvatel.baam

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

interface InterfaceCookieSaver{
    fun getCookie() : String?
    fun setCookie(c : String?)
}

interface InterfaceQRSaver{
    fun setSession(s : String?)
    fun setSecretCode(s : String?)
    fun getSession() : String?
    fun getSecretCode() : String?
}

class MainActivity : AppCompatActivity(), InterfaceCookieSaver, InterfaceQRSaver {

    private var cookie : String? = null
    private var session : String? = null
    private var secretCode : String? = null

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

    override fun setCookie(c : String?) {
        cookie = c
    }

    override fun setSession(s: String?) {
        session = s
    }

    override fun setSecretCode(s: String?) {
        secretCode = s
    }

    override fun getSession(): String? {
        return session
    }

    override fun getSecretCode(): String? {
        return secretCode
    }
}
