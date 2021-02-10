package com.arvatel.baam.network

import com.arvatel.baam.InterfaceResponseCallback
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaamApiController (var callback: InterfaceResponseCallback) {

    private lateinit var baamAPI: BaamAPI
    private lateinit var retrofit: Retrofit
    private lateinit var okClient: OkHttpClient
    private lateinit var logging: HttpLoggingInterceptor
    private lateinit var gson: Gson

    fun start() {
        gson = GsonBuilder()
                .setLenient()
                .create()
        logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        baamAPI = this.retrofit.create(BaamAPI::class.java)
    }

    companion object{
        const val BASE_URL = "https://baam.duckdns.org"
        const val CODE_REDIRECT = 300
        const val CODE_OKAY = 200
        const val CODE_ERROR = 400
    }

    fun submitChallenge(cookie: String?, session: String?, secretCode: String?) {
        val call = baamAPI.submitChallenge(cookie, session, secretCode)

        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.code() in 300..399) {
                    callback.responseCallBack(CODE_REDIRECT)
                    println("Redirect")
                }
                if (response.isSuccessful) {
                    var ur = response.raw().request.url.toString()
                    ur = ur.substringAfter("//")
                    ur = ur.substringBefore("/")

                    if (ur == BASE_URL) {
                        callback.responseCallBack(CODE_OKAY)
                        println("Success")
                    }
                    else{
                        callback.responseCallBack(CODE_REDIRECT)
                    }
                } else {
                    callback.responseCallBack(CODE_ERROR)
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                callback.responseCallBack(CODE_ERROR)
                t.printStackTrace()
            }
        })
    }
}