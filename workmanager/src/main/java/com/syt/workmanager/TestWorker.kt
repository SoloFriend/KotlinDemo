package com.syt.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class TestWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG ="TestWorker"
    override fun doWork(): Result {
        Log.w(TAG, "doWork: started" )
        Thread.sleep(3000)
        Log.w(TAG, "doWork: finished" )
        return Result.success()
    }
}