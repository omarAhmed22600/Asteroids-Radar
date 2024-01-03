package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.getPictureOfDay
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseInstance = AsteroidsDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidRepository(databaseInstance)

    private val _navigateToDetailFragment = MutableLiveData<Boolean>()
    val navigateToDetailFragment: LiveData<Boolean>
        get() = _navigateToDetailFragment

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int>
        get() = _selectedFilter

    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid


    private var _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        _selectedFilter.value = 1
        getAsteroids()
        _navigateToDetailFragment.value = false
        viewModelScope.launch {
            try {
                asteroidRepository.refreshFeeds()
                refreshPictureOfDay()
            } catch (e: Exception) {
                Log.i("ViewModel", e.toString())
            }
        }
    }

    private suspend fun refreshPictureOfDay() {
        _pictureOfDay.value = getPictureOfDay()
    }

    fun getAsteroids() {
        Log.i("ViewModel", "getAsteroids()")
        getAsteroidsAccordingToFilter(_selectedFilter.value!!)

    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        Log.i("ViewModel", "ViewModel :" + (asteroid == null).toString())
        _selectedAsteroid.value = asteroid
        _navigateToDetailFragment.value = true
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = false
    }

    fun onShowWeekSelected() {
        _selectedFilter.value = 1
    }

    fun onShowTodaySelected() {
        _selectedFilter.value = 2
    }

    fun onShowSavedSelected() {
        _selectedFilter.value = 3
    }

    private fun getAsteroidsAccordingToFilter(selection: Int) {
        when (selection) {
            1 -> viewModelScope.launch {
                databaseInstance.asteroidsDatabaseDao.getAsteroidsByCloseApproachDate(
                    getToday(),
                    getSeventhDay()
                )
                    .collect { asteroids ->
                        _asteroidsList.value = asteroids
                        Log.i("getAsteroids()", "Done!!" + asteroids.isEmpty().toString())
                    }
            }
            2 -> viewModelScope.launch {
                databaseInstance.asteroidsDatabaseDao.getAsteroidsByCloseApproachDate(
                    getToday(),
                    getToday()
                )
                    .collect { asteroids ->
                        _asteroidsList.value = asteroids
                        Log.i("getAsteroids()", "Done!!" + asteroids.isEmpty().toString())
                    }
            }
            else -> {
                viewModelScope.launch {
                    databaseInstance.asteroidsDatabaseDao.getAllAsteroids().collect { asteroids ->
                        _asteroidsList.value = asteroids
                        Log.i("getAsteroids()", "Done!!" + asteroids.isEmpty().toString())
                    }
                }
            }
        }
    }


}