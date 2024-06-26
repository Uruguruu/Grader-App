package ch.timofey.grader.ui.screen.settings

import ch.timofey.grader.db.AppTheme
import ch.timofey.grader.type.AppLanguage
import ch.timofey.grader.type.DateFormatting
import java.io.InputStream
import java.io.OutputStream

sealed class SettingsEvent {
    data class OnThemeChange(val theme: AppTheme) : SettingsEvent()
    data class OnMinimumGradeChange(val grade: String) : SettingsEvent()
    data class OnCalculatePointsChange(val value: Boolean) : SettingsEvent()
    data class OnDoublePointsChange(val value: Boolean) : SettingsEvent()
    data class OnEnableSwipeToDeleteChange(val value: Boolean) : SettingsEvent()
    data class OnShowNavigationIconsChange(val value: Boolean) : SettingsEvent()
    data class OnGradeColorChange(val value: Boolean) : SettingsEvent()
    data class OnLoadBackupFile(val file: InputStream) : SettingsEvent()
    data class OnLanguageChange(val language: AppLanguage) : SettingsEvent()
    data class OnDateFormatChange(val format: DateFormatting) : SettingsEvent()
    data class OnSwapNavigationChange(val value: Boolean) : SettingsEvent()
    data class OnCreateBackupFile(val file: OutputStream, val fileLocation: String) : SettingsEvent()
    data object OnDeleteDatabaseButtonClick : SettingsEvent()
    data object OnDismissDeleteData : SettingsEvent()
    data object OnConfirmDeleteData : SettingsEvent()
}