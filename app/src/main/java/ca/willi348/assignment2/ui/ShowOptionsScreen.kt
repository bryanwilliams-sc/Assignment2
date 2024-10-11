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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ca.willi348.assignment2.R
import ca.willi348.assignment2.ui.theme.SelectionTheme

@Composable
fun ShowOptionsScreen(
    options: Array<Int>,
    onSelectionChanged: (Int) -> Unit = {},
    onNextButtonClicked: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    previouslySelected: Int,

) {
    var selectedValue by rememberSaveable { mutableStateOf(0) }
    var unselected: Int = 0

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.mipmap.peeking),
            contentDescription = null,
            modifier = Modifier
                .scale(2.0F)
                .padding(dimensionResource(R.dimen.padding_large))
        )
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) {
            options.forEach { item ->
                if (item != previouslySelected) {
                    unselected = item
                }

            }
            Text(
                text = stringResource(R.string.change_or_keep),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )
                SelectBoxButton(
                    labelResourceId = R.string.your_choice,
                    itemContained = previouslySelected,
                    onClick = { onNextButtonClicked(previouslySelected) },
                    modifier = Modifier.fillMaxWidth()
                )
                SelectBoxButton(
                    labelResourceId = unselected,
                    itemContained = unselected,
                    onClick = { onNextButtonClicked(unselected) },
                    modifier = Modifier.fillMaxWidth()
                )
        }

    }

}



@Preview
@Composable
fun SelectOptionPreview() {
    SelectionTheme {
        ShowOptionsScreen(
            options = arrayOf(),
            modifier = Modifier.fillMaxHeight(),
            previouslySelected = 0
        )
    }
}
