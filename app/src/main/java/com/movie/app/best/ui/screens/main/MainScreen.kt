package com.movie.app.best.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.movie.app.best.ui.navigation.AppNavigation
import com.movie.app.best.ui.navigation.BottomNavigationBar
import com.movie.app.best.ui.navigation.Screen
import com.movie.app.best.ui.screens.splash.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    // State to track whether splash screen is finished
    var splashScreenFinished by remember { mutableStateOf(false) }
    
    if (!splashScreenFinished) {
        // Show splash screen
        SplashScreen(onSplashScreenFinish = { splashScreenFinished = true })
    } else {
        // Show main content after splash screen
        MainContent()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val shouldShowBottomBar = when {
        currentRoute == Screen.Home.route -> true
        currentRoute == Screen.Movies.route -> true
        currentRoute == Screen.TVShows.route -> true
        currentRoute == Screen.Trending.route -> true
        currentRoute == Screen.Search.route -> true
        else -> false
    }
    
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            //modifier = Modifier.padding(innerPadding)
        )
    }
}