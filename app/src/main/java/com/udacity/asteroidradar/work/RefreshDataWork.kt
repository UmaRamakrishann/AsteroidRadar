package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
	CoroutineWorker(appContext, params) {


	override suspend fun doWork(): Result {
		val database = getDatabase(applicationContext)
		val repository = AsteroidsRepository(database)
		return try {
			val startDate = getToday()
			val endDate = getEndDate()
			val apiKey = "vkYXEwFM6pn1RCHeRLjnangdwDt2rY5XxqauSJVg"
			repository.refreshAsteroids(apiKey, startDate, endDate)
			Result.success()
		} catch (e: HttpException) {
			Result.retry()
		}
	}

	companion object {
		const val WORK_NAME = "RefreshDataWorker"
	}
}