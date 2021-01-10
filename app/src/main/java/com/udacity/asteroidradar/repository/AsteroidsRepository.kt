package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.getDatePastSevenDays
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidRadar
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidsRepository(private val database: AsteroidsDatabase) {

	 fun getTodayAsteroids(): LiveData<List<Asteroid>> {
		return Transformations.map(
			database.asteroidDao.getAsteroidsByDate(
				getToday()
			)
		) {
			it.asDomainModel()
		}
	}
		fun getWeeklyAsteroids(): LiveData<List<Asteroid>> {
			return Transformations.map(
				database.asteroidDao.getAsteroidsByDateRange(
					getDatePastSevenDays(),
					getToday()
				)
			) {
				it.asDomainModel()
			}
		}

	 fun getAllAsteroids(): LiveData<List<Asteroid>> {
		return Transformations.map(
			database.asteroidDao.getAsteroids()
		) {
			it.asDomainModel()
		}
	}

		suspend fun refreshAsteroids(apiKey: String, startDate: String, endDate: String) {
			withContext(Dispatchers.IO) {
				try {
					val response = AsteroidRadar.asteroids.getAsteroids(apiKey, startDate, endDate)
					val jsonResponse = JSONObject(response)
					val asteroidList = parseAsteroidsJsonResult(jsonResponse)
					database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
				}catch (e: Exception) {
					Timber.e("No Internet")
				}
			}
		}
	}