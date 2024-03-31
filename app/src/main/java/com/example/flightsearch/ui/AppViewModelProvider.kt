/* Assignment 5 Demo

AppViewModelProvider.kt

Abhram Medina / medinaab@oregonstate.edu
CS 492/ OSU

* */
package com.example.flightsearch.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FlightSearchViewModel(
                flightSearchApplication().container.flightSearchRepository,

            )
        }
    }
}

fun CreationExtras.flightSearchApplication(): FlightApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication)
