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
package ca.willi348.assignment2

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.willi348.assignment2.data.DataSource
import ca.willi348.assignment2.ui.OrderSummaryScreen
import ca.willi348.assignment2.ui.SelectionViewModel
import ca.willi348.assignment2.ui.ShowOptionsScreen
import ca.willi348.assignment2.ui.StartOptionsScreen
import kotlin.random.Random

enum class SelectionScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Options(title = R.string.other_options),
    Summary(title = R.string.order_summary)
}

private var random = Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionAppBar(
    currentScreen: SelectionScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onHelpButtonClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onHelpButtonClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_help_outline_24),
                    contentDescription = stringResource(R.string.menu)
                )
            }
        }
    )

}

@Composable
fun SelectionApp(
    viewModel: SelectionViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    var showAboutDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    if(viewModel.chosenCases[0] == 0){
        viewModel.chooseCases(random)
    }
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = SelectionScreen.valueOf(
        backStackEntry?.destination?.route ?: SelectionScreen.Start.name
    )

    Scaffold(
        topBar = {
            SelectionAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { cancelOrderAndNavigateToStart(viewModel, navController) },
                onHelpButtonClick = { showAboutDialog = true }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        if (showAboutDialog) {
            AssignmentAbout(onDismissRequest = { showAboutDialog = false })
        }
        NavHost(
            navController = navController,
            startDestination = SelectionScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = SelectionScreen.Start.name) {
                StartOptionsScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setChoice(it)
                        navController.navigate(SelectionScreen.Options.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    viewModel.chosenCases
                )
            }
            composable(route = SelectionScreen.Options.name) {
                val context = LocalContext.current
                ShowOptionsScreen(
                    onNextButtonClicked = {
                        viewModel.setChoice(it)
                        navController.navigate(SelectionScreen.Summary.name)
                                          },
                    options = viewModel.chosenCases,
                    modifier = Modifier.fillMaxHeight(),
                    previouslySelected = uiState.selectedOption
                )
            }
            composable(route = SelectionScreen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: SelectionViewModel,
    navController: NavHostController
) {
    viewModel.chooseCases(random)
    navController.popBackStack(SelectionScreen.Start.name, inclusive = false)
}

private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.i_won)
        )
    )
}

@Composable
private fun AssignmentAbout(onDismissRequest: () -> Unit) =
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.about_asst2)) },
        text = {
            Text(
                text = stringResource(R.string.assignment2),
                fontSize = 18.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    )
