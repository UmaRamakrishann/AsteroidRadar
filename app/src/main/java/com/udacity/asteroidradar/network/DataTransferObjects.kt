package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
	val id: Long,
	val name: String,
	val closeApproachDate: String,
	val absolute_magnitude_h: Double,
	val estimated_diameter_max: Double,
	val relative_velocity: Double,
	val astronomical: Double,
	val is_potentially_hazardous_asteroid: Boolean
)

/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
	return asteroids.map {
		Asteroid(
			id = it.id,
			codename = it.name,
			closeApproachDate = it.closeApproachDate,
			absoluteMagnitude = it.absolute_magnitude_h,
			estimatedDiameter = it.estimated_diameter_max,
			relativeVelocity = it.relative_velocity,
			distanceFromEarth = it.astronomical,
			isPotentiallyHazardous = it.is_potentially_hazardous_asteroid
		)
	}
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
	return asteroids.map {
		DatabaseAsteroid(
			id = it.id,
			codename = it.name,
			closeApproachDate = it.closeApproachDate,
			absoluteMagnitude = it.absolute_magnitude_h,
			estimatedDiameter = it.estimated_diameter_max,
			relativeVelocity = it.relative_velocity,
			distanceFromEarth = it.astronomical,
			isPotentiallyHazardous = it.is_potentially_hazardous_asteroid
		)
	}.toTypedArray()
}

