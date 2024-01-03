package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class DownloadThisWeeksData(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = AsteroidsDatabase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshFeeds()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}