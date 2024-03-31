/* Assignment 5 Demo

FlightSearchViewModel.kt

Abhram Medina / medinaab@oregonstate.edu
CS 492/ OSU

* */

package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightRepository
import com.example.flightsearch.data.IataAndName

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FlightSearchUIState(
    val userInput: String = "",
    val selectedAirport: IataAndName = IataAndName(iataCode = "", name =  ""),
    val isAirportSelected: Boolean = false,
    val flightSavedStates: MutableMap<Favorite, Boolean> = mutableMapOf(),
    val isDeleteDialogVisible: Boolean = false
)

@OptIn(FlowPreview::class)
class FlightSearchViewModel(
    private val flightRepository: FlightRepository,

): ViewModel() {

    private val _uiState = MutableStateFlow(
        FlightSearchUIState()
    )

    val uiState: StateFlow<FlightSearchUIState> = _uiState

    init {
        viewModelScope.launch {

        }
    }


    fun updateUserInput(input: String) {
        _uiState.update {
            it.copy(
                userInput = input,
                isAirportSelected = false
            )
        }
    }


    fun updateSelectedAirport(updatedSelectedAirport: IataAndName) {
        _uiState.update {
            it.copy(
                selectedAirport = updatedSelectedAirport,
                isAirportSelected = true
            )
        }
    }


    fun retrieveAutocompleteSuggestions(): Flow<List<IataAndName>> {
        return if (_uiState.value.userInput.isNotBlank())
            flightRepository.getAutocompleteSuggestions(_uiState.value.userInput.trim()).debounce(500L)
        else
            emptyFlow()
    }


    fun retrievePossibleFlights(selectedAirport: IataAndName): Flow<List<IataAndName>> =
        flightRepository.getPossibleFlights(selectedAirport.iataCode, selectedAirport.name)


    private fun updateFlightSavedState(favorite: Favorite, newState: Boolean) {
        _uiState.update {
            it.copy(
                flightSavedStates = _uiState.value.flightSavedStates.toMutableMap().apply {
                    this[favorite] = newState
                }
            )
        }
    }


    fun insertItem(favorite: Favorite) {
        updateFlightSavedState(favorite, true)

        viewModelScope.launch {
            flightRepository.insertFavoriteItem(favorite)
        }
    }


    fun deleteItem(favorite: Favorite) {
        if (_uiState.value.flightSavedStates[favorite] == true)
            updateFlightSavedState(favorite, false)

        viewModelScope.launch {
            flightRepository.deleteFavorite(favorite.departureCode, favorite.destinationCode)
        }
    }


    fun isFlightSaved(favorite: Favorite): Boolean {
        return _uiState.value.flightSavedStates[favorite] == true
    }


    fun getAllFavorites(): Flow<List<Favorite>> =
        flightRepository.getAllFavorites()




    fun syncFavoritesWithFlights(favorites: List<Favorite>, selectedAirport: IataAndName, destinationAirports: List<IataAndName>) {
        for (favorite in favorites)
            for (destinationAirport in destinationAirports) {
                if (favorite.departureCode == selectedAirport.iataCode && favorite.destinationCode == destinationAirport.iataCode)
                    updateFlightSavedState(Favorite(departureCode = selectedAirport.iataCode, destinationCode = destinationAirport.iataCode), true)
            }
    }
}