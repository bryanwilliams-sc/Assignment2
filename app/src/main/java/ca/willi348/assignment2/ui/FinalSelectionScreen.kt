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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ca.willi348.assignment2.data.OrderUiState
import ca.willi348.assignment2.ui.theme.SelectionTheme

@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tryAgain = stringResource(R.string.try_again)
    val selectedItem = stringResource(orderUiState.selectedOption)

    val orderSummary = stringResource(
        R.string.select_details,
        selectedItem
    )

    val iWon = stringResource(
        R.string.i_won,
        selectedItem
    )


    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.mipmap.party),
                contentDescription = null,
                modifier = Modifier
                    .scale(2.0F)
                    .padding(dimensionResource(R.dimen.padding_large))
            )
            Text(
                text = orderSummary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "You could've won:")

            Column {
                orderUiState.options.forEach { item ->
                    if (stringResource(item) != selectedItem) {
                        HorizontalDivider(thickness = dimensionResource(R.dimen.thickness_divider))
                        Row() {
                            Text(stringResource(item))
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Row(

            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(text = stringResource(R.string.try_again) )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        onSendButtonClicked(tryAgain, iWon)
                    }
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    SelectionTheme {
        OrderSummaryScreen(
            orderUiState = OrderUiState(arrayOf(), 0),
            onSendButtonClicked = { subject: String, summary: String -> },
            onCancelButtonClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}
