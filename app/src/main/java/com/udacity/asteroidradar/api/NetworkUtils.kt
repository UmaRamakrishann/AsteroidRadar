package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.AMERICA_NEW_YORK
import com.udacity.asteroidradar.network.NetworkAsteroid
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Util class for parsing and date functions related to network API
 */
fun parseAsteroidsJsonResult(jsonResult: JSONObject): NetworkAsteroidContainer {
	val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

	val asteroidList = ArrayList<NetworkAsteroid>()

	val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
	for (formattedDate in nextSevenDaysFormattedDates) {
		val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

		for (i in 0 until dateAsteroidJsonArray.length()) {
			val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
			val id = asteroidJson.getLong("id")
			val codename = asteroidJson.getString("name")
			val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
			val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
				.getJSONObject("kilometers").getDouble("estimated_diameter_max")

			val closeApproachData = asteroidJson
				.getJSONArray("close_approach_data").getJSONObject(0)
			val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
				.getDouble("kilometers_per_second")
			val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
				.getDouble("astronomical")
			val isPotentiallyHazardous = asteroidJson
				.getBoolean("is_potentially_hazardous_asteroid")

			val asteroid = NetworkAsteroid(
				id, codename, formattedDate, absoluteMagnitude,
				estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
			)
			asteroidList.add(asteroid)
		}
	}

	return NetworkAsteroidContainer(asteroidList)
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
	val formattedDateList = ArrayList<String>()

	val calendar = Calendar.getInstance()
	for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
		val currentTime = calendar.time
		val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
		formattedDateList.add(dateFormat.format(currentTime))
		calendar.add(Calendar.DAY_OF_YEAR, 1)
	}

	return formattedDateList
}

fun getToday(): String {
	val calendar =
		Calendar.getInstance(TimeZone.getTimeZone(AMERICA_NEW_YORK), Locale.US)
	val currentTime = calendar.time
	val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
	return dateFormat.format(currentTime)
}

fun getDatePastSevenDays(): String {
	val calendar =
		Calendar.getInstance(TimeZone.getTimeZone(AMERICA_NEW_YORK), Locale.US)
	calendar.add(Calendar.DAY_OF_YEAR, -7)
	val pastSevenDaysTime = calendar.time
	val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
	return dateFormat.format(pastSevenDaysTime)
}

fun getEndDate(): String {
	val calendar =
		Calendar.getInstance(TimeZone.getTimeZone(AMERICA_NEW_YORK), Locale.US)
	calendar.add(Calendar.DAY_OF_YEAR, 7)
	val endTime = calendar.time
	val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
	return dateFormat.format(endTime)
}