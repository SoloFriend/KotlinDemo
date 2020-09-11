package com.syt.gallery.ds

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.syt.gallery.bean.Hit

class PixabayDataSourceFactory(private val context: Context) : DataSource.Factory<Int, Hit>() {

    private val _dataSourceLiveData = MutableLiveData<PixabayDataSource>()
    val dataSourceLiveData: LiveData<PixabayDataSource> = _dataSourceLiveData

    override fun create(): DataSource<Int, Hit> {
        return PixabayDataSource(context).also {
            _dataSourceLiveData.postValue(it)
        }
    }
}