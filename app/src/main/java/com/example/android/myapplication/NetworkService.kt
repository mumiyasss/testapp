package com.example.android.myapplication

import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


class NetworkService {

    fun getApolloClient(): ApolloClient {
        val okHttp = OkHttpClient
            .Builder()
            .build()

        return ApolloClient.builder()
            .serverUrl("https://api.yelp.com/v3/graphql")
            .okHttpClient(okHttp)
            .build()
    }

    fun getApolloClientWithTokenInterceptor(token: String): ApolloClient {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()

                val builder: Request.Builder = original
                    .newBuilder()
                    .method(original.method, original.body)

                builder.header("Authorization", "Bearer $token")
                return@Interceptor chain.proceed(builder.build())
            })
            .build()

        return ApolloClient.builder()
            .serverUrl("https://api.yelp.com/v3/graphql")
            .okHttpClient(httpClient)
            .build()
    }

    companion object {
        private var mInstance: NetworkService? = null

        fun getInstance(): NetworkService? {
            if (mInstance == null) {
                mInstance = NetworkService()
            }
            return mInstance
        }
    }
}