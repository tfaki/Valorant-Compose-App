package com.canerture.valorantcompose.presentation.competitivetiers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.valorantcompose.common.Resource
import com.canerture.valorantcompose.domain.usecase.competitivetiers.GetCompetitiveTiersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CompetitiveTiersViewModel @Inject constructor(
    private val getCompetitiveTiersUseCase: GetCompetitiveTiersUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CompetitiveTiersState())
    val state: State<CompetitiveTiersState> = _state

    fun getCompetitiveTiers() {
        getCompetitiveTiersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _state.value = CompetitiveTiersState(tier = it)
                    }
                }
                is Resource.Error -> {
                    _state.value =
                        CompetitiveTiersState(
                            error = result.errorMessage ?: "Unexpected error!"
                        )
                }
                is Resource.Loading -> {
                    _state.value = CompetitiveTiersState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}