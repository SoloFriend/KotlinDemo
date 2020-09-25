package com.syt.gallery.net

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.syt.gallery.SingletonHolder

/**
 * 单例Volley
 */
class VolleySingleton private constructor(context: Context) {

    companion object : SingletonHolder<VolleySingleton, Context>(::VolleySingleton)

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}