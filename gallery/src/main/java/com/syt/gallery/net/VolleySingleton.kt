package com.syt.gallery.net

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.syt.gallery.SingletonHolder

class VolleySingleton private constructor(context: Context) {
//    companion object {
//        private var INSTANCE: VolleySingleton? = null
//        fun getInstance(context: Context) =
//            INSTANCE ?: synchronized(this) {
//                VolleySingleton(context).also { INSTANCE = it }
//            }
//    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    companion object : SingletonHolder<VolleySingleton,Context>(::VolleySingleton)
}