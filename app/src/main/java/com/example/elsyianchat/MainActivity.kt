package com.example.elsyianchat

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.elsyianchat.navigation.MainNavHost
import com.example.elsyianchat.ui.theme.ElsyianChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,   Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,   Color.TRANSPARENT,
            )
        )
        setContent {
            val navHost = rememberNavController()
            ElsyianChatTheme {
                MainNavHost(navHost = navHost)
            }
        }
    }
}
