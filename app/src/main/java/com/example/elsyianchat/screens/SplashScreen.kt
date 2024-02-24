package com.example.elsyianchat.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.commandiron.compose_loading.ChasingTwoDots
import com.example.elsyianchat.R
import com.example.elsyianchat.appComponent.LoaderComponent
import com.example.elsyianchat.ui.theme.inter
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1.4f, animationSpec = tween(1600, easing = FastOutSlowInEasing))
        scale.animateTo(0.9f, animationSpec = tween(1600, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(key1 = true) {
        delay(3200)

        val navOptions = NavOptions.Builder()
            .setPopUpTo(Screens.SplashScreen.route, inclusive = true)
            .build()
        navController.navigate(Screens.ChoiceScreen.route, navOptions)


    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LoaderComponent(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp * 0.5f),
            rawRes = R.raw.splash
        )

        Text(
            text = "Elsyian AI",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = inter,
            modifier = Modifier
                .wrapContentSize()
                .offset(y = (-90).dp)
                .scale(scale.value),
            fontSize = 30.sp
        )

        ChasingTwoDots(color = MaterialTheme.colorScheme.onSecondary)


    }


}