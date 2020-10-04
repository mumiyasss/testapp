package com.example.android.myapplication

import SearchYelpQuery.Business
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.md.matur.feature.profile.notfications.MyDataSource


class MainViewModel : ViewModel() {

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setEnablePlaceholders(false)
        .build()

    fun buildPagedList(latitude: Double, longitude: Double): LiveData<PagedList<Business>> {
        val dataSourceFactory = object : DataSource.Factory<Int, Business>() {
            override fun create(): DataSource<Int, Business> {
                return MyDataSource(
                    latitude, longitude
                )
            }
        }
        return LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }
}