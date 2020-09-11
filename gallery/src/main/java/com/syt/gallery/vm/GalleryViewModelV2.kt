package com.syt.gallery.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.syt.gallery.ds.PixabayDataSourceFactory


/**
 * 首页图片列表VM
 * version 2
 */
class GalleryViewModelV2(application: Application) : AndroidViewModel(application) {

    val factory = PixabayDataSourceFactory(application)
    val pagedListLiveData = factory.toLiveData(1)
    val networkStatus =
        Transformations.switchMap(factory.dataSourceLiveData) { it.networkStatus }

    /**
     * 重新发起请求
     */
    fun resetQuery() {
        pagedListLiveData.value?.dataSource?.invalidate()
    }

    /**
     * 失败重试
     */
    fun retry() {
        factory.dataSourceLiveData.value?.retry?.invoke()
    }
}