package com.syt.gallery.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.toLiveData
import com.syt.gallery.ds.PixabayDataSourceFactory


/**
 * 首页图片列表VM
 * version 2
 */
class GalleryViewModelV2(application: Application) : AndroidViewModel(application) {

    val pagedListLiveData = PixabayDataSourceFactory(application).toLiveData(1)

    fun resetQuery() {
        pagedListLiveData.value?.dataSource?.invalidate()
    }
}