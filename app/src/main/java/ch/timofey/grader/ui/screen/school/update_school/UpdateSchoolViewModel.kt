package ch.timofey.grader.ui.screen.school.update_school

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.timofey.grader.GraderApp
import ch.timofey.grader.db.domain.school.SchoolRepository
import ch.timofey.grader.ui.utils.UiEvent
import ch.timofey.grader.validation.ValidateSchool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UpdateSchoolViewModel @Inject constructor(
    private val repository: SchoolRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val schoolId: String = savedStateHandle.get<String>("id").orEmpty()

    private val _uiState = MutableStateFlow(UpdateSchoolState())
    val uiState: StateFlow<UpdateSchoolState> = _uiState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getSchoolById(UUID.fromString(schoolId)).apply {
                this?.let { school ->
                    _uiState.value = _uiState.value.copy(
                        name = school.name,
                        description = school.description ?: "",
                        address = school.address,
                        zip = school.zipCode,
                        city = school.city,
                        currentSchool = school
                    )
                } ?: run {
                    sendUiEvents(
                        UiEvent.PopBackStack,
                        UiEvent.ShowSnackBar(
                            message = "School was unable to be found",
                            withDismissAction = true
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: UpdateSchoolEvent) {
        when (event) {
            is UpdateSchoolEvent.OnReturnBack -> {
                sendUiEvent(UiEvent.PopBackStack)
            }

            is UpdateSchoolEvent.OnNameChange -> {
                val result = ValidateSchool.name(event.name)
                _uiState.value = _uiState.value.copy(
                    name = event.name,
                    validName = result.isValid,
                    nameErrorMessage = result.errorMessage
                )
            }

            is UpdateSchoolEvent.OnDescriptionChange -> {
                val result = ValidateSchool.description(event.description)
                _uiState.value = _uiState.value.copy(
                    description = event.description,
                    validDescription = result.isValid,
                    descriptionErrorMessage = result.errorMessage
                )
            }

            is UpdateSchoolEvent.OnAddressChange -> {
                val result = ValidateSchool.address(event.address)
                _uiState.value = _uiState.value.copy(
                    address = event.address,
                    validAddress = result.isValid,
                    addressErrorMessage = result.errorMessage
                )
            }

            is UpdateSchoolEvent.OnCityChange -> {
                val result = ValidateSchool.city(event.city)
                _uiState.value = _uiState.value.copy(
                    city = event.city,
                    validCity = result.isValid,
                    cityErrorMessage = result.errorMessage
                )
            }

            is UpdateSchoolEvent.OnZipChange -> {
                val result = ValidateSchool.zip(event.zip)
                _uiState.value = _uiState.value.copy(
                    city = event.zip,
                    validCity = result.isValid,
                    cityErrorMessage = result.errorMessage
                )
            }

            is UpdateSchoolEvent.OnUpdateSchool -> {
                if (ValidateSchool.validateAll(_uiState.value)) {
                    val updatedSchool = _uiState.value.currentSchool!!.copy(
                        name = _uiState.value.name,
                        description = _uiState.value.description,
                        address = _uiState.value.address,
                        zipCode = _uiState.value.zip,
                        city = _uiState.value.city
                    )

                    viewModelScope.launch {
                        repository.updateSchool(updatedSchool)
                    }
                    sendUiEvent(UiEvent.PopBackStack)
                    Toast.makeText(GraderApp.getContext(), "School Updated", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        GraderApp.getContext(),
                        "School was unable to be Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    private fun sendUiEvents(vararg events: UiEvent) {
        viewModelScope.launch {
            for (event in events) {
                _uiEvent.send(event)
            }
        }
    }
}