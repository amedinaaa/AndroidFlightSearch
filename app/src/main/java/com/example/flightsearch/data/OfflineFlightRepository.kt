/* Assignment 5 Demo

OfflineFlightRepository.kt

Abhram Medina / medinaab@oregonstate.edu
CS 492/ OSU

* */
package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightRepository(private val airportDao: FlightDao): FlightRepository {
    override fun getAutocompleteSuggestions(input: String): Flow<List<IataAndName>> =
        airportDao.getAutocompleteSuggestions(input)

    override fun getPossibleFlights(name: String, iataCode: String): Flow<List<IataAndName>> =
        airportDao.getPossibleFlights(name, iataCode)

    override suspend fun insertFavoriteItem(favorite: Favorite) =
        airportDao.insertFavorite(favorite)

    override suspend fun deleteFavorite(departureCode: String, destinationCode: String) =
        airportDao.deleteFavorite(departureCode, destinationCode)

    override suspend fun deleteAllFavorites() = airportDao.deleteAllFavorites()

    override fun getAllFavorites(): Flow<List<Favorite>> =
        airportDao.retrieveAllFavorites()
}