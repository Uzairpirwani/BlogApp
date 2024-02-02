package com.example.blogapplication.network

import com.example.blogapplication.utils.Configuration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkClient {
    companion object {
        private var retrofit: Retrofit? = null

        fun getNetworkClient(): Retrofit {
            if (retrofit == null) {
                val trustManager: X509TrustManager = object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }

                // Create an SSLContext with the custom TrustManager
                val sslContext: SSLContext = SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())

                // Create an OkHttpClient with the custom SSLContext
                val client = OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory, trustManager)
                    .hostnameVerifier { _, _ -> true } // Disables hostname verification
                    .build()

                // Create the Retrofit instance
                retrofit = Retrofit.Builder()
                    .baseUrl(Configuration.Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }

            return retrofit!!
        }
    }
}
