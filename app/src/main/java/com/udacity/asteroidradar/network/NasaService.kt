package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {
	@GET("planetary/apod")
	suspend fun getPictureOfDay(
		@Query("api_key") apiKey: String
	): PictureOfDay
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
	.add(KotlinJsonAdapterFactory())
	.build()

object NasaImage {

	// Configure retrofit to parse JSON and use coroutines
	private val retrofit = Retrofit.Builder()
		.baseUrl(Constants.BASE_URL)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	val pictureOfDay = retrofit.create(NasaService::class.java)
}