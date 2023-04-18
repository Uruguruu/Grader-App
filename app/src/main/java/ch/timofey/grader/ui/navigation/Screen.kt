package ch.timofey.grader.ui.navigation

sealed class Screen(var route: String) {
    object MainScreen : Screen("main_screen")
    object SecondScreen : Screen("second_screen")
}
