package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface AsteroidDao {
	@Query("select * from databaseasteroid where closeApproachDate = :key order by closeApproachDate desc")
	fun getAsteroidsByDate(key: String): LiveData<List<DatabaseAsteroid>>

	@Query("select * from databaseasteroid where closeApproachDate between :key1 and :key2 order by closeApproachDate desc")
	fun getAsteroidsByDateRange(key1: String, key2: String): LiveData<List<DatabaseAsteroid>>

	@Query("select * from databaseasteroid  order by closeApproachDate desc")
	fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
	abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
	synchronized(AsteroidsDatabase::class.java){
		if (!::INSTANCE.isInitialized) {
			INSTANCE = Room.databaseBuilder(context.applicationContext,
				AsteroidsDatabase::class.java,
				"asteroids").build()
		}}
	return INSTANCE
}