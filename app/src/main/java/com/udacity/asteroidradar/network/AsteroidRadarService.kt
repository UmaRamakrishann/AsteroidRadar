package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch the Asteroid data
 */
interface AsteroidRadarService {
	@GET("neo/rest/v1/feed")
	suspend fun getAsteroids(
		@Query("api_key") apiKey: String,
		@Query("start_date") startDate: String,
		@Query("end_date") endDate: String
	): String
}

object AsteroidRadar {

	// Configure retrofit to parse JSON and use coroutines
	private val retrofit = Retrofit.Builder()
		.baseUrl(Constants.BASE_URL)
		.addConverterFactory(ScalarsConverterFactory.create())
		.build()

	val asteroids = retrofit.create(AsteroidRadarService::class.java)
}
