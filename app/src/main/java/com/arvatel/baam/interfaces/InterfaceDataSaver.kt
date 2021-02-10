package com.arvatel.baam.interfaces

interface InterfaceDataSaver{
    fun getCookie() : String?
    fun setCookie(c: String?)
    fun getSession() : String?
    fun setSession(s: String?)
    fun getSecretCode() : String?
    fun setSecretCode(sc: String?)
}