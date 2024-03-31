/* Assignment 5 Demo

FlightApplication.kt

Abhram Medina / medinaab@oregonstate.edu
CS 492/ OSU

* */

package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.AppContainer
import com.example.flightsearch.data.AppDataContainer




class FlightApplication: Application() {
    lateinit var container: AppContainer


    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

    }
}