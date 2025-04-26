package com.movie.app.best.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashScreenFinish: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "Splash Alpha Animation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        onSplashScreenFinish()
    }

    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        // Branded content with movie/video theme
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFFE50914))
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "App Logo",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
        
        Text(
            text = "MOVIE APP",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(alpha)
                .padding(bottom = 50.dp)
        )
    }
} 