package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.AsteroidDisplayFilter
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaImage
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

	private val database = getDatabase(application)
	private val asteroidsRepository = AsteroidsRepository(database)
	private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
	private val startDate = getToday()
	private val endDate = getEndDate()

	var asteroids = asteroidsRepository.getTodayAsteroids()

	val navigateToSelectedAsteroid: LiveData<Asteroid>
		get() = _navigateToSelectedAsteroid

	private val _pictureOfDay = MutableLiveData<PictureOfDay>()
	val pictureOfDay: LiveData<PictureOfDay>
		get() = _pictureOfDay

	init {
		val apiKey = "vkYXEwFM6pn1RCHeRLjnangdwDt2rY5XxqauSJVg"
		viewModelScope.launch { asteroidsRepository.refreshAsteroids(apiKey, startDate, endDate) }
		asteroids = asteroidsRepository.getTodayAsteroids()
		getPictureOfDay(apiKey)
	}

	fun displayAsteroidDetails(asteroid: Asteroid) {
		_navigateToSelectedAsteroid.value = asteroid
	}

	fun displayAsteroidDetailsComplete() {
		_navigateToSelectedAsteroid.value = null
	}

	private fun getPictureOfDay(apiKey: String) {
		viewModelScope.launch {
			try {
				_pictureOfDay.value = NasaImage.pictureOfDay.getPictureOfDay(apiKey)
			} catch (e: Exception) {
				Timber.e("No Internet")
			}
		}
	}

	fun updateFilter(filter: AsteroidDisplayFilter) {
		when (filter) {
			AsteroidDisplayFilter.SHOW_TODAY -> asteroids = asteroidsRepository.getTodayAsteroids()
			AsteroidDisplayFilter.SHOW_WEEKLY -> asteroids = asteroidsRepository.getWeeklyAsteroids()
			else -> asteroids = asteroidsRepository.getAllAsteroids()
		}
		Timber.e("Asteroids Size" + asteroids.value?.size.toString())
	}

}