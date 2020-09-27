package com.syt.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class TestWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG = "TestWorker"
    override fun doWork(): Result {
        val name = inputData.getString(KEY_INPUT)
        Log.w(TAG, "doWork: $name started")
        Thread.sleep(3000)
        Log.w(TAG, "doWork: $name finished")
        return Result.success(workDataOf(KEY_OUTPUT to name))
    }
}