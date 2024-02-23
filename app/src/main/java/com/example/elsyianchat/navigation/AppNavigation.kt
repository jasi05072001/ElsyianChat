package com.example.elsyianchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.elsyianchat.screens.ChatScreen
import com.example.elsyianchat.screens.Screens
import com.example.elsyianchat.screens.SplashScreen

@Composable
fun MainNavHost(navHost: NavHostController) {

    NavHost(
        navController = navHost,
        startDestination = Screens.SplashScreen.route
    ) {

        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navHost)
        }

        composable(Screens.ChatScreen.route) {
            ChatScreen(navController = navHost)
        }
    }

}