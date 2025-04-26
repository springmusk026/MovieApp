package com.movie.app.best.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Movies : BottomNavItem("movies", "Movies", Icons.Default.Movie)
    object TVShows : BottomNavItem(Screen.TVShows.route, "TV Shows", Icons.Default.Tv)
    object Trending : BottomNavItem("trending", "Trending", Icons.Default.Whatshot)
    object Search : BottomNavItem(Screen.Search.route, "Search", Icons.Default.Search)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Movies,
        BottomNavItem.TVShows,
        BottomNavItem.Trending,
        BottomNavItem.Search
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Don't show bottom nav on detail screens
    val isDetailScreen = currentRoute?.startsWith("movie/") == true || 
                         currentRoute?.startsWith("tv-show/") == true ||
                         currentRoute?.startsWith("videoPlayer/") == true
    
    if (!isDetailScreen) {
        NavigationBar(
            modifier = modifier
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    selected = selected,
                    onClick = {
                        // Avoid renavigating to the same destination
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
} 