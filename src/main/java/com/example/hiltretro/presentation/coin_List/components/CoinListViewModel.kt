package com.example.hiltretro.presentation.coin_List.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltretro.common.Resource
import com.example.hiltretro.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class CoinListViewModel @Inject constructor(

    private val getCoinsUseCase: GetCoinsUseCase
): ViewModel(){

    private val coinListState = mutableStateOf(CoinListState())
    val state : State<CoinListState> = coinListState

    init {
        getCoins()
    }
    private fun getCoins(){
        getCoinsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    coinListState.value = CoinListState(coins = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    coinListState.value = CoinListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    coinListState.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}