package com.technosudo.gymfollower.ui.screens.createexercise

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technosudo.gymfollower.R
import com.technosudo.gymfollower.domain.entity.ExerciseEntity
import com.technosudo.gymfollower.domain.entity.ProgressEntity
import com.technosudo.gymfollower.domain.repository.ExerciseRepository
import com.technosudo.gymfollower.domain.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CreateExerciseViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateExerciseUiState.default())
    val uiState = _uiState.asStateFlow()

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputData = it.inputData.copy(text = text))
        }
    }
    fun increaseWeight() {
        _uiState.update {
            it.copy(weight = it.weight + 5)
        }
    }
    fun decreaseWeight() {
        if(_uiState.value.weight >= 5)
            _uiState.update {
                it.copy(weight = it.weight - 5)
            }
    }
    fun onConfirm(navigation: CreateExerciseNavigation) {
        viewModelScope.launch {
            var result = 0L
            val lastPosition = exerciseRepository
                .getCurrentLastPosition()
                .firstOrNull()
                ?: -1
            withContext(Dispatchers.IO) {
                result = exerciseRepository.insertExercise(ExerciseEntity(
                    name = uiState.value.inputData.text,
                    weight = uiState.value.weight,
                    icon = uiState.value.icon,
                    position = lastPosition + 1
                ))
                if(result > 0) {
                    progressRepository.upsertProgress(ProgressEntity(
                        exerciseId = result.toInt(),
                        dateEpochDay = LocalDate.now().toEpochDay(),
                        weight = uiState.value.weight
                    ))
                    withContext(Dispatchers.Main) {
                        navigation.goBack()
                    }
                }
            }
        }
    }
    fun onCancel(navigation: CreateExerciseNavigation) {
        viewModelScope.launch {
            navigation.goBack()
        }
    }
}