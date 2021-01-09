package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

	private val _asteroids = MutableLiveData<List<Asteroid>>()

	val asteroids: LiveData<List<Asteroid>>
		get() = _asteroids

	private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
	val navigateToSelectedAsteroid: LiveData<Asteroid>
		get() = _navigateToSelectedAsteroid

	init {
		getAsteroidsForToday()
	}

	private fun getAsteroidsForToday() {
		viewModelScope.launch {
			try {
				val calendar = Calendar.getInstance()
				val currentTime = calendar.time
				val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
				calendar.add(Calendar.DAY_OF_YEAR, 1)
				val tomorrowTime = calendar.time

				Network.asteroids.getAsteroids(
					"vkYXEwFM6pn1RCHeRLjnangdwDt2rY5XxqauSJVg",
					"2021-01-08",
					"2021-01-09"
				).enqueue(object : Callback<String> {
					override fun onFailure(call: Call<String>, t: Throwable) {
						_asteroids.value = ArrayList()
					}

					override fun onResponse(call: Call<String>, response: Response<String>) {
						val jsonObject = JSONObject(response.body())
						_asteroids.value = parseAsteroidsJsonResult(jsonObject)
					}
				})

				//_status.value = MarsApiStatus.DONE
			} catch (e: Exception) {
				//_status.value = MarsApiStatus.ERROR
				_asteroids.value = ArrayList()
			}
		}
	}

	fun displayAsteroidDetails(asteroid: Asteroid) {
		_navigateToSelectedAsteroid.value = asteroid
	}

	fun displayAsteroidDetailsComplete() {
		_navigateToSelectedAsteroid.value = null
	}

}