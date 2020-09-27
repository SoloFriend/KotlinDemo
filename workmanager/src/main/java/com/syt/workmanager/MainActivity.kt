package com.syt.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

const val KEY_INPUT = "input_key"
const val KEY_OUTPUT = "output_key"
const val WORK_NAME_ONE = "work_one"
const val WORK_NAME_TWO = "work_two"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest1 = createWorkRequest(WORK_NAME_ONE)
        val workRequest2 = createWorkRequest(WORK_NAME_TWO)
        button.setOnClickListener {
            workManager.beginWith(workRequest1).then(workRequest2).enqueue()
        }

        workManager.getWorkInfoByIdLiveData(workRequest1.id).observe(this) {
            Log.w("WorkActivity", "onCreate: " + it.state.toString())
            if (it.state == WorkInfo.State.SUCCEEDED) {
                textView.text = it.outputData.getString(KEY_OUTPUT).plus(it.state.toString())
            }
        }
        workManager.getWorkInfoByIdLiveData(workRequest2.id).observe(this) {
            Log.w("WorkActivity", "onCreate: " + it.state.toString())
            if (it.state == WorkInfo.State.SUCCEEDED) {
                textView.text = it.outputData.getString(KEY_OUTPUT).plus(it.state.toString())
            }
        }
    }

    private fun createWorkRequest(name: String): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        return OneTimeWorkRequestBuilder<TestWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(KEY_INPUT to name))
            .build()
    }
}
