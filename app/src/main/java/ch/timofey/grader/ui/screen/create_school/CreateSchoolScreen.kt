package ch.timofey.grader.ui.screen.create_school

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ch.timofey.grader.ui.theme.GraderTheme
import ch.timofey.grader.ui.components.AppBar
import ch.timofey.grader.ui.utils.UiEvent
import ch.timofey.grader.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CreateSchoolScreen(
    state: CreateSchoolState,
    onEvent: (CreateSchoolEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    onPopBackStack: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack()
                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = event.withDismissAction,
                        duration = SnackbarDuration.Short
                    )
                }

                else -> Unit
            }
        }
    }

    Scaffold(topBar = {
        AppBar(
            onNavigationIconClick = { onEvent(CreateSchoolEvent.OnReturnBack) },
            icon = Icons.Default.ArrowBack,
            contentDescription = "Go back to School Screen"
        )
    }, snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(it)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create School", style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(value = state.name,
                label = {
                    Text(text = buildAnnotatedString {
                        append("School name ")
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }
                    })
                },
                onValueChange = { name ->
                    onEvent(CreateSchoolEvent.OnNameChange(name))
                },
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.small)
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                isError = !state.validName,
                supportingText = {
                    if (!state.validName) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.nameErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                minLines = 2
            )
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.large)
                .fillMaxWidth(),
                value = state.address,
                label = {
                    Text(text = buildAnnotatedString {
                        append("Address ")
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }
                    })
                },
                isError = !state.validAddress,
                supportingText = {
                    if (!state.validAddress) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.addressErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { address -> onEvent(CreateSchoolEvent.OnAddressChange(address)) },
                minLines = 3
            )
            OutlinedTextField(value = state.zip,
                label = {
                    Text(text = buildAnnotatedString {
                        append("Zip ")
                        withStyle(
                            SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }
                    })
                },
                isError = !state.validZip,
                supportingText = {
                    if (!state.validZip) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.zipErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { zip -> onEvent(CreateSchoolEvent.OnZipChange(zip)) },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(value = state.city,
                label = {
                    Text(text = buildAnnotatedString {
                        append("City ")
                        withStyle(
                            SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }
                    })
                },
                isError = !state.validCity,
                supportingText = {
                    if (!state.validCity) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.cityErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { city -> onEvent(CreateSchoolEvent.OnCityChange(city)) },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.description,
                label = {
                    Text(
                        text = "School Description"
                    )
                },
                onValueChange = { description ->
                    onEvent(
                        CreateSchoolEvent.OnDescriptionChange(
                            description
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                minLines = 6,
            )
            Button(
                onClick = { onEvent(CreateSchoolEvent.OnCreateSchool) },
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = "Create")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateSchoolScreen() {
    GraderTheme {
        CreateSchoolScreen(state = CreateSchoolState(
            name = "Technische Berufsschule Zürich",
            description = "Das ist eine Berufsschule\n asfaf"
        ),
            onEvent = {},
            uiEvent = emptyFlow(),
            onPopBackStack = {},
            snackBarHostState = remember { SnackbarHostState() })
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun PreviewDarkModeCreateSchoolScreen() {
    GraderTheme {
        CreateSchoolScreen(state = CreateSchoolState(
            validName = false, nameErrorMessage = "This is an Error Message"
        ),
            onEvent = {},
            uiEvent = emptyFlow(),
            onPopBackStack = {},
            snackBarHostState = remember { SnackbarHostState() })
    }
}