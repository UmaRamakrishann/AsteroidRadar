package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Call
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
	): Call<String>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
	.add(KotlinJsonAdapterFactory())
	.build()

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object Network {

	// Configure retrofit to parse JSON and use coroutines
	private val retrofit = Retrofit.Builder()
		.baseUrl(Constants.BASE_URL)
		.addConverterFactory(ScalarsConverterFactory.create())
		.addCallAdapterFactory(CoroutineCallAdapterFactory())
		.build()

	val asteroids = retrofit.create(AsteroidRadarService::class.java)
}
