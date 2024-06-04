package com.example.translator.navigation

sealed class Screens(val route:String) {
    data object MainScreen : Screens("main_screen")
    data object SignInScreen : Screens("sign_in_screen")
    data object SignUpScreen : Screens("sign_up_screen")
    data object FavoritesScreen : Screens("favourites_screen")
}