package com.example.translator.navigation

import androidx.navigation.NavHost

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translator.uiScreens.FavoritesScreen
import com.example.translator.uiScreens.SignInScreen
import com.example.translator.uiScreens.SignUpScreen
import com.example.translator.uiScreens.TranslatorScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    NavHost(navController =navController , startDestination = Screens.SignInScreen.route){

        composable(route = Screens.MainScreen.route){
            TranslatorScreen(navController = navController)
        }

        composable(route = Screens.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }

        composable(route = Screens.SignInScreen.route){

            if (firebaseAuth.currentUser != null) {
                TranslatorScreen(navController = navController)
            }
            else {
                SignInScreen(navController = navController)
            }
        }

        composable(route = Screens.FavoritesScreen.route){
            FavoritesScreen(navController = navController)
        }

    }
}