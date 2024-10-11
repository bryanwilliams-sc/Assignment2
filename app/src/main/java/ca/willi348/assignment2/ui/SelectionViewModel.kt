/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.willi348.assignment2.ui

import androidx.lifecycle.ViewModel
import ca.willi348.assignment2.data.DataSource
import ca.willi348.assignment2.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random



private const val AMOUNT_OF_CASES = 5;


class SelectionViewModel : ViewModel() {


    var chosenCases: Array<Int> = Array(AMOUNT_OF_CASES){0}
    private val _uiState = MutableStateFlow(OrderUiState(options = chosenCases))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()


    fun setChoice(numberSelected: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedOption = numberSelected
            )
        }
    }

    fun chooseCases(random: Random){
        chosenCases = Array(AMOUNT_OF_CASES){0}
        for(i in 0..<AMOUNT_OF_CASES){
            var exist = true
            while (exist){
                exist = false
                var rand = DataSource.options[random.nextInt(DataSource.options.size)]
                for(j in 0 .. i){
                    if(rand == chosenCases[j]){
                        exist = true
                        break

                    }
                }
                if (!exist){
                    chosenCases[i] = rand
                }

            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                options = chosenCases
            )
        }
    }

}
