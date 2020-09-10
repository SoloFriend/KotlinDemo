package com.syt.gallery.ds

import android.content.Context
import androidx.paging.DataSource
import com.syt.gallery.bean.Hit

class PixabayDataSourceFactory(private val context: Context) : DataSource.Factory<Int, Hit>() {
    override fun create(): DataSource<Int, Hit> {
        return PixabayDataSource(context)
    }
}