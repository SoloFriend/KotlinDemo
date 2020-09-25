package com.syt.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest1 = OneTimeWorkRequestBuilder<TestWorker>().build()
        button.setOnClickListener {
            workManager.enqueue(workRequest1)
        }

        workManager.getWorkInfoByIdLiveData(workRequest1.id).observe(this) {
            textView.text = it.state.toString()
        }
    }
}
