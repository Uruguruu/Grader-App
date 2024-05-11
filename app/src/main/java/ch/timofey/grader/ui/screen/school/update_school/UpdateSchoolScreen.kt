package ch.timofey.grader.ui.screen.school.update_school

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
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
import ch.timofey.grader.R
import ch.timofey.grader.ui.components.organisms.AppBar
import ch.timofey.grader.ui.theme.GraderTheme
import ch.timofey.grader.ui.theme.spacing
import ch.timofey.grader.utils.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun UpdateSchoolScreen(
    state: UpdateSchoolState,
    onEvent: (UpdateSchoolEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    onPopBackStack: (SnackbarVisuals?) -> Unit,
    snackBarHostState: SnackbarHostState
){
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(null)
                }

                is UiEvent.PopBackStackAndShowSnackBar -> {
                    onPopBackStack(event.snackbarVisuals)
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
            onNavigationIconClick = { onEvent(UpdateSchoolEvent.OnReturnBack) },
            actionIcon = Icons.AutoMirrored.Filled.ArrowBack,
            actionContentDescription = R.string.go_back_to_school_screen.toString()
        )
    }, //snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(it)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = R.string.update_school.toString(), style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(value = state.name,
                label = {
                    Text(text = buildAnnotatedString {
                        append(R.string.school_name.toString())
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append(R.string.required.toString())
                        }
                    })
                },
                onValueChange = { name ->
                    onEvent(UpdateSchoolEvent.OnNameChange(name))
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
                        append(R.string.address.toString())
                        /*withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic, fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }*/
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
                onValueChange = { address -> onEvent(UpdateSchoolEvent.OnAddressChange(address)) },
                minLines = 3
            )
            OutlinedTextField(value = state.zip,
                label = {
                    Text(text = buildAnnotatedString {
                        append(R.string.zip.toString())
                        /*withStyle(
                            SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontSize = 8.sp
                            )
                        ) {
                            append("(Required)")
                        }*/
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
                onValueChange = { zip -> onEvent(UpdateSchoolEvent.OnZipChange(zip)) },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(value = state.city,
                label = {
                    Text(text = buildAnnotatedString {
                        append(R.string.city.toString())
                        /*if (state.city.isBlank()){
                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic, fontSize = 8.sp
                                )
                            ) {
                                append("(Required)")
                            }
                        }*/
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
                onValueChange = { city -> onEvent(UpdateSchoolEvent.OnCityChange(city)) },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.description,
                label = {
                    Text(
                        text = R.string.school_description.toString()
                    )
                },
                onValueChange = { description ->
                    onEvent(
                        UpdateSchoolEvent.OnDescriptionChange(
                            description
                        )
                    )
                },
                isError = !state.validDescription,
                supportingText = {
                    if (!state.validDescription) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.descriptionErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                minLines = 6,
            )
            Button(
                onClick = { onEvent(UpdateSchoolEvent.OnUpdateSchool) },
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = R.string.update.toString())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateSchoolScreen() {
    GraderTheme {
        UpdateSchoolScreen(state = UpdateSchoolState(
            name = "Technische Berufsschule Zürich",
            description = "Das ist eine Berufsschule\n asfaf",
            city = "kasjdxnb"
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
        UpdateSchoolScreen(state = UpdateSchoolState(
            validName = false, nameErrorMessage = "This is an Error Message"
        ),
            onEvent = {},
            uiEvent = emptyFlow(),
            onPopBackStack = {},
            snackBarHostState = remember { SnackbarHostState() })
    }
}