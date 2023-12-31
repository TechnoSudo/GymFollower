package com.technosudo.gymfollower.ui.screens.invidualprogress

import com.technosudo.gymfollower.data.ChipData
import com.technosudo.gymfollower.data.Period
import com.technosudo.gymfollower.data.ProgressData
import com.technosudo.gymfollower.data.StatData
import java.time.LocalDate

data class IndividualProgressUiState(
    val progressData: List<ProgressData>,
    val chipData: List<ChipData>,
    val statData: List<StatData>,
    val periodType: Period
) {
    companion object {
        fun default(
            progressData: List<ProgressData> = listOf(ProgressData(
                date = LocalDate.now(),
                weight = 0.0
            )),
            chipData: List<ChipData> = emptyList(),
            statData: List<StatData> = emptyList(),
            periodType: Period = Period.Weeks
        ) = IndividualProgressUiState(
            progressData = progressData,
            chipData = chipData,
            statData = statData,
            periodType = periodType
        )
    }
}