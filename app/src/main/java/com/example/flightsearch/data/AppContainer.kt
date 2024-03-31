/* Assignment 5 Demo

AppContainer.kt

Abhram Medina / medinaab@oregonstate.edu
CS 492/ OSU

* */
package com.example.flightsearch.data

import android.content.Context

interface AppContainer {
    val flightSearchRepository: FlightRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val flightSearchRepository: FlightRepository by lazy {
        OfflineFlightRepository(FlightDatabase.getDatabase(context).flightDao())
    }
}