package ch.timofey.grader.ui.screen.school_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.timofey.grader.db.domain.school.School
import ch.timofey.grader.db.domain.school.SchoolRepository
import ch.timofey.grader.ui.utils.UiEvent
import ch.timofey.grader.navigation.Screen
import ch.timofey.grader.ui.utils.getAverage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class SchoolListViewModel @Inject constructor(
    private val repository: SchoolRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(SchoolListState())
    val uiState: StateFlow<SchoolListState> = _uiState.asStateFlow()

    init {
        println("starting init")
        viewModelScope.launch {
            repository.getAllSchools().collect { schoolList ->
                _uiState.value =
                    _uiState.value.copy(schoolList = schoolList.filter { school -> !school.onDelete })
                if (schoolList.isNotEmpty()) {
                    val averageGrade = calculateAverageGrade(schoolList)
                    _uiState.value = _uiState.value.copy(averageGrade = averageGrade.toString())
                    if (_uiState.value.averageGrade.toDouble() == 0.0) {
                        _uiState.value = _uiState.value.copy(averageGradeIsZero = true)
                    } else {
                        _uiState.value = _uiState.value.copy(averageGradeIsZero = false)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(averageGradeIsZero = true)
                }
            }
        }
    }

    fun onEvent(event: SchoolListEvent) {
        when (event) {
            is SchoolListEvent.OnCreateSchool -> {
                println("OnCreateSchools")
                viewModelScope.launch {
                    println("tried to delete in onCreateSchool")
                   deleteSchoolItems()
                }
                sendUiEvent(UiEvent.Navigate(Screen.CreateSchoolScreen.route))
            }

            is SchoolListEvent.OnDeleteItems -> {
                println("OnDeleteItems")
                viewModelScope.launch {
                    println("tried to delete in OnDeleteItems")
                    //deleteSchoolItems()
                }
                sendUiEvent(UiEvent.Navigate(event.route))
            }

            is SchoolListEvent.OnCheckChange -> {
                println("OnCheckChange")
                //viewModelScope.launch {
                    repository.updateIsSelectedSchool(id = event.id, value = event.value)
                //}
            }

            is SchoolListEvent.OnSwipeDelete -> {
                println("OnSwipeDelete")
                //viewModelScope.launch {
                    repository.updateOnDeleteSchool(event.id, true)
                //}
                viewModelScope.launch {
                    repository.getAllSchools().collect{ schoolList ->
                        _uiState.value =
                            _uiState.value.copy(schoolList = schoolList.filter { school -> !school.onDelete })
                    }
                }
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        "School Deleted was deleted", true, "Undo"
                    )
                )
            }

            is SchoolListEvent.OnUndoDeleteClick -> {
                println("OnUdoDeleteClick")
                //viewModelScope.launch {
                    repository.updateOnDeleteSchool(event.id, false)
                //}
            }
        }
    }

    private fun calculateAverageGrade(list: List<School>): Double {
        val validExams = list.map { it }.filter { it.isSelected }
        val gradeList = validExams.map { it.grade }
        return getAverage(grades = gradeList).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
            .toDouble()
    }

    private suspend fun deleteSchoolItems(){
        println("Inside deleteSchoolItems")
        val schoolList = repository.getAllSchools()
        schoolList.collect { list ->
            list.filter { school -> school.onDelete }.forEach { school ->
                repository.deleteSchool(school)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}