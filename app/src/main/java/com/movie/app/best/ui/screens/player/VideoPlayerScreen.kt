package com.movie.app.best.ui.screens.player

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.util.Rational
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.movie.app.best.BuildConfig
import com.movie.app.best.data.model.Season
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VideoPlayerScreen(
    onBackClick: () -> Unit,
    title: String,
    mediaId: String,
    mediaType: String,
    videoKey: String = "",
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = context as? Activity

    // Control UI visibility
    var isControlsVisible by remember { mutableStateOf(true) }
    
    // Determine if we're in trailer mode (true if videoKey was provided, false otherwise)
    val isTrailerMode = videoKey.isNotEmpty()
    
    // Auto-hide controls after delay
    LaunchedEffect(isControlsVisible) {
        if (isControlsVisible) {
            delay(3000)
            isControlsVisible = false
        }
    }
    
    // Force landscape mode
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        hideSystemUI(activity)
    }
    
    // Restore portrait mode on exit
    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            showSystemUI(activity)
        }
    }

    // Combine videoKey from navigation and viewModel
    val effectiveVideoKey = if (videoKey.isNotEmpty()) videoKey else uiState.videoKey

    // Handle loading videos if videoKey wasn't provided
    LaunchedEffect(mediaId, mediaType) {
        if (videoKey.isEmpty()) {
            viewModel.initializeWithMedia(mediaId, mediaType)
            viewModel.loadVideoTrailer(mediaId, mediaType)
        }
    }

    // Custom back handler to ensure portrait mode is restored
    BackHandler {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        showSystemUI(activity)
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { isControlsVisible = !isControlsVisible }
    ) {
        // Video Player
        VideoPlayer(
            mediaId = mediaId,
            mediaType = mediaType,
            season = uiState.selectedSeason,
            episode = uiState.selectedEpisode,
            videoKey = effectiveVideoKey,
            isTrailer = isTrailerMode,
            modifier = Modifier.fillMaxSize()
        )

        // Controls overlay (visible only when isControlsVisible is true)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                // Top bar with title and back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            showSystemUI(activity)
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = title + if (isTrailerMode) " (Trailer)" else "",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // PiP button
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        IconButton(onClick = {
                            enterPipMode(activity)
                        }) {
                            Icon(
                                imageVector = Icons.Default.PictureInPicture,
                                contentDescription = "Picture in Picture",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayer(
    mediaId: String,
    mediaType: String,
    season: Int = 1,
    episode: Int = 1,
    videoKey: String = "",
    isTrailer: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    
    // Check if it's a trailer (which works in WebView) or regular content
    if (isTrailer && videoKey.isNotEmpty()) {
        // For YouTube trailers - use WebView
        val youtubeUrl = "https://www.youtube.com/embed/$videoKey?autoplay=1&controls=1&showinfo=0&rel=0"
        Log.d("VideoPlayer", "Loading YouTube trailer: $youtubeUrl")
        
        // Simple WebView for YouTube trailers
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                    }
                    
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            isLoading = true
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                        }
                    }
                    
                    loadUrl(youtubeUrl)
                }
            },
            update = { webView ->
                val updatedUrl = "https://www.youtube.com/embed/$videoKey?autoplay=1&controls=1&showinfo=0&rel=0"
                if (webView.url != updatedUrl) {
                    webView.loadUrl(updatedUrl)
                }
            },
            modifier = modifier
        )
    } else {
        // For movies and TV shows - open in external browser
        val websiteBaseUrl = BuildConfig.BASE_URL
        val playerUrl = "$websiteBaseUrl/player?mediaType=$mediaType&mediaId=$mediaId&season=$season&episode=$episode" +
            (if (videoKey.isNotEmpty()) "&videoKey=$videoKey" else "") +
            "&isTrailer=$isTrailer"
        
        Log.d("VideoPlayer", "Opening player page in browser: $playerUrl")
        
        // Show message to open in browser
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Video player is opening in your browser",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        // Open the URL in the browser
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(playerUrl))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Open in Browser")
                }
            }
        }
        
        // Auto-open in browser when component is first displayed
        LaunchedEffect(mediaId, mediaType, season, episode) {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(playerUrl))
            context.startActivity(intent)
            isLoading = false
        }
    }
    
    // Loading indicator
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
    }
}

// Helper function to hide the system UI for immersive fullscreen
private fun hideSystemUI(activity: Activity?) {
    activity?.let {
        WindowCompat.setDecorFitsSystemWindows(it.window, false)
        WindowInsetsControllerCompat(it.window, it.window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = 
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

// Helper function to show the system UI when exiting fullscreen
private fun showSystemUI(activity: Activity?) {
    activity?.let {
        WindowCompat.setDecorFitsSystemWindows(it.window, true)
        WindowInsetsControllerCompat(it.window, it.window.decorView).show(
            WindowInsetsCompat.Type.systemBars()
        )
    }
}

// Enter Picture-in-Picture mode
@Suppress("DEPRECATION")
private fun enterPipMode(activity: Activity?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        activity?.let {
            val aspectRatio = Rational(16, 9)
            val pipBuilder = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
            
            it.enterPictureInPictureMode(pipBuilder.build())
        }
    }
} 