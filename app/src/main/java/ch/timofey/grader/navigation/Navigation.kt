package ch.timofey.grader.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.timofey.grader.ui.screen.create_school.CreateSchoolScreen
import ch.timofey.grader.ui.screen.settings.SettingsScreen
import ch.timofey.grader.ui.screen.create_school.CreateSchoolViewModel
import ch.timofey.grader.ui.screen.school_list.SchoolListScreen
import ch.timofey.grader.ui.screen.school_list.SchoolListViewModel
import ch.timofey.grader.ui.screen.settings.SettingsViewModel
import ch.timofey.grader.ui.screen.share.ShareScreen
import ch.timofey.grader.ui.screen.share.ShareViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val viewModel = hiltViewModel<SchoolListViewModel>()
            val state by viewModel.uiState.collectAsState()
            SchoolListScreen(
                drawerState = drawerState,
                onEvent = viewModel::onEvent,
                state = state,
                uiEvent = viewModel.uiEvent,
                onNavigate = {
                    navController.navigate(it.route)
                },
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = Screen.CreateSchoolScreen.route) {
            val viewModel = hiltViewModel<CreateSchoolViewModel>()
            val state by viewModel.uiState.collectAsState()
            CreateSchoolScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
                onPopBackStack = { navController.popBackStack() },
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = Screen.SettingsScreen.route){
            val viewModel = hiltViewModel<SettingsViewModel>()
            val state by viewModel.uiState.collectAsState()
            SettingsScreen(
                drawerState = drawerState,
                state = state,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(route = Screen.ShareScreen.route){
            val viewModel = hiltViewModel<ShareViewModel>()
            ShareScreen(
                drawerState = drawerState,
                uiEvent = viewModel.uiEvent,
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
    }
}