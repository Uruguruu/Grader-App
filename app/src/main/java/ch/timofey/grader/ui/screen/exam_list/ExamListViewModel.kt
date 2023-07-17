package ch.timofey.grader.ui.screen.exam_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.timofey.grader.db.domain.exam.Exam
import ch.timofey.grader.db.domain.exam.ExamRepository
import ch.timofey.grader.navigation.Screen
import ch.timofey.grader.ui.utils.UiEvent
import ch.timofey.grader.ui.utils.getAverage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExamListViewModel @Inject constructor(
    private val repository: ExamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val moduleId = savedStateHandle.get<String>("id").orEmpty()

    private val _uiState = MutableStateFlow(ExamListState())
    val uiState: StateFlow<ExamListState> = _uiState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getAllExams().collect { examList ->
                _uiState.value = _uiState.value.copy(exams = examList)
                if (examList.isNotEmpty()) {
                    val averageGrade = calculateAverageGrade(examList)
                    repository.updateModuleGradeById(UUID.fromString(moduleId), averageGrade)
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

    fun onEvent(event: ExamListEvent) {
        when (event) {
            is ExamListEvent.OnBackButtonClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }

            is ExamListEvent.OnFABClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.CreateExamScreen.withArgs(moduleId)))
            }

            is ExamListEvent.OnCheckChange -> {
                viewModelScope.launch {
                    repository.updateIsSelectedExam(event.id, event.value)
                }
            }

            is ExamListEvent.OnSwipeDelete -> {
                viewModelScope.launch {
                    repository.deleteExam(event.exam)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun calculateAverageGrade(list: List<Exam>): Double {
        val validExams = list.map { it }.filter { it.isSelected }
        val weightList = validExams.map { it.weight }
        val gradeList = validExams.map { it.grade }
        return getAverage(grades = gradeList, weights = weightList).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}