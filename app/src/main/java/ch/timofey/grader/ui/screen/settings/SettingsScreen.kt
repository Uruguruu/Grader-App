package ch.timofey.grader.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ch.timofey.grader.navigation.Screen
import ch.timofey.grader.ui.components.AppBar
import ch.timofey.grader.ui.components.Greeting
import ch.timofey.grader.ui.components.NavigationDrawer
import ch.timofey.grader.ui.utils.UiEvent
import ch.timofey.grader.ui.utils.NavigationDrawerItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    drawerState: DrawerState,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    onNavigate: (UiEvent.Navigate) -> Unit
){
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                else -> Unit
            }
        }
    }
    NavigationDrawer(
        drawerState = drawerState,
        currentScreen = Screen.SettingsScreen,
        items = NavigationDrawerItems.getNavigationDrawerItems(),
        onItemClick = { menuItem ->
            println("Clicked on ${menuItem.title}")
            if (menuItem.onNavigate != Screen.SettingsScreen.route){
                onNavigate(UiEvent.Navigate(menuItem.onNavigate))
            }
            scope.launch{
                drawerState.close()
            }
        }
    ) {
        Scaffold (
            topBar = {
                AppBar(
                    onNavigationIconClick = { scope.launch { drawerState.open() } },
                    icon = Icons.Default.Menu,
                    contentDescription = "Toggle Drawer"
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Greeting(name = "Settings Screen")
            }
        }
    }
}