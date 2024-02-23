package com.example.elsyianchat.appComponent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.elsyianchat.R

@Suppress("DEPRECATION")
@Composable
fun LoaderComponent(modifier: Modifier,rawRes :Int ) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawRes)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1.2f,
        restartOnPlay = false
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        renderMode = RenderMode.HARDWARE,
        modifier = modifier
    )
}