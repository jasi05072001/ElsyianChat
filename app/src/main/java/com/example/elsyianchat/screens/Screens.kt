package com.example.elsyianchat.screens

sealed class Screens (val route: String) {
    data object ChatScreen: Screens("chat_screen")
    data object SplashScreen: Screens("splash_screen")
}