package com.syt.gallery.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.syt.gallery.bean.Hit
import com.syt.gallery.bean.Pixabay
import com.syt.gallery.net.VolleySingleton

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<Hit>>()
    val photoListLive: LiveData<List<Hit>>
        get() = _photoListLive


    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
            },
            Response.ErrorListener {
                Log.e("GalleryVM", it.toString())
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private val keyWords = arrayListOf(
        "dog",
        "cat",
        "car",
        "beauty",
        "gun",
        "moto",
        "phone",
        "computer",
        "animal",
        "flower",
        "movie",
        "bike",
        "music",
        "girl",
        "sexy",
        "death"
    )

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=18114850-972781ef8be2db7220e906302&q=${keyWords.random()}&per_page=100"
    }
}