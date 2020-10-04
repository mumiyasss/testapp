package com.md.matur.feature.profile.notfications

import SearchYelpQuery.Business
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloCall.*
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.android.myapplication.NetworkService
import com.example.android.myapplication.token


class MyDataSource(private val latitude: Double, private val longitude: Double) :
    PageKeyedDataSource<Int, Business>() {


    private val pageSize: Int = 15

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Business>
    ) {
        val myQuery = SearchYelpQuery.builder()
            .term("restaurants")
            .sort_by("review_count")
            .latitude(latitude)
            .longitude(longitude)
            .radius(10000.toDouble())
            .build()

        val client = NetworkService.getInstance()?.getApolloClientWithTokenInterceptor(token)

        client
            ?.query(myQuery)
            ?.enqueue(object : Callback<SearchYelpQuery.Data>() {
                override fun onResponse(response: Response<SearchYelpQuery.Data>) {
                    if (!response.hasErrors()) {
                        response.data?.search()?.business()?.let {
                            callback.onResult(
                                it, null,
                                ((response.data?.search()?.total() ?: pageSize) / pageSize) + 1
                            )
                        }
                    }
                }

                override fun onFailure(e: ApolloException) {
                }
            })

    }


    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, Business>
    ) {
        val myQuery = SearchYelpQuery.builder()
            .term("restaurants")
            .sort_by("review_count")
            .offset(pageSize * (params.key))
            .latitude(latitude)
            .longitude(longitude)
            .radius(10000.toDouble())
            .build()

        val client = NetworkService.getInstance()?.getApolloClientWithTokenInterceptor(token)

        client
            ?.query(myQuery)
            ?.enqueue(object : Callback<SearchYelpQuery.Data>() {
                override fun onResponse(response: Response<SearchYelpQuery.Data>) {
                    if (!response.hasErrors()) {
                        response.data?.search()?.business()?.let {
                            callback.onResult(
                                it,
                                ((response.data?.search()?.total() ?: pageSize) / pageSize) + 1
                            )
                        }
                    }
                }

                override fun onFailure(e: ApolloException) {
                }
            })

    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, Business>
    ) {
        val myQuery = SearchYelpQuery.builder()
            .term("restaurants")
            .sort_by("review_count")
            .offset(pageSize * (params.key - 1))
            .latitude(latitude)
            .longitude(longitude)
            .radius(10000.toDouble())
            .build()

        val client = NetworkService.getInstance()?.getApolloClientWithTokenInterceptor(token)

        client
            ?.query(myQuery)
            ?.enqueue(object : Callback<SearchYelpQuery.Data>() {
                override fun onResponse(response: Response<SearchYelpQuery.Data>) {
                    if (!response.hasErrors()) {
                        response.data?.search()?.business()?.let {
                            callback.onResult(
                                it,
                                ((response.data?.search()?.total() ?: pageSize) / pageSize) - 1
                            )
                        }
                    }
                }

                override fun onFailure(e: ApolloException) {
                }
            })

    }

}