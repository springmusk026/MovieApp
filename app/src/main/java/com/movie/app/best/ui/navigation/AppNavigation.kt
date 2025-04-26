package com.movie.app.best.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.movie.app.best.ui.screens.home.HomeScreen
import com.movie.app.best.ui.screens.moviedetail.MovieDetailScreen
import com.movie.app.best.ui.screens.search.SearchScreen
import com.movie.app.best.ui.screens.tvshowdetail.TVShowDetailScreen
import com.movie.app.best.ui.screens.tvshows.TVShowsScreen
import com.movie.app.best.ui.screens.player.VideoPlayerScreen
import com.movie.app.best.ui.screens.movies.MoviesScreen
import com.movie.app.best.ui.screens.trending.TrendingScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Movies : Screen("movies")
    object Trending : Screen("trending")
    object MovieDetail : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int) = "movie/$movieId"
    }
    object Search : Screen("search")
    object TVShows : Screen("tv-shows")
    object TVShowDetail : Screen("tv-show/{tvShowId}") {
        fun createRoute(tvShowId: Int) = "tv-show/$tvShowId"
    }
    object VideoPlayer : Screen("videoPlayer/{mediaType}/{mediaId}/{title}/{videoKey}") {
        fun createRoute(mediaType: String, mediaId: String, title: String, videoKey: String): String {
            val encodedTitle = Uri.encode(title)
            return "videoPlayer/$mediaType/$mediaId/$encodedTitle/$videoKey"
        }
        
        // New function for direct content playback (no trailer)
        fun createContentRoute(mediaType: String, mediaId: String, title: String): String {
            val encodedTitle = Uri.encode(title)
            return "videoPlayer/$mediaType/$mediaId/$encodedTitle/"
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                },
                onTVShowClick = { tvShow ->
                    navController.navigate(Screen.TVShowDetail.createRoute(tvShow.id))
                },
                onTVShowsClick = {
                    navController.navigate(Screen.TVShows.route)
                },
                navController = navController
            )
        }
        
        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                },
                onMovieClick = { selectedMovieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(selectedMovieId)) {
                        // Pop current detail to avoid stacking details
                        popUpTo(Screen.MovieDetail.route) {
                            inclusive = true
                        }
                    }
                },
                onVideoClick = { id, title, videoKey ->
                    navController.navigate(
                        Screen.VideoPlayer.createRoute(
                            mediaType = "movie",
                            mediaId = id.toString(),
                            title = title,
                            videoKey = videoKey
                        )
                    )
                }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }
        
        composable(Screen.TVShows.route) {
            TVShowsScreen(
                onTVShowClick = { tvShow ->
                    navController.navigate(Screen.TVShowDetail.createRoute(tvShow.id))
                },
                navController = navController
            )
        }
        
        composable(
            route = Screen.TVShowDetail.route,
            arguments = listOf(
                navArgument("tvShowId") { type = NavType.StringType }
            )
        ) {
            it.arguments?.getString("tvShowId")?.let { tvShowId ->
                TVShowDetailScreen(
                    tvShowId = tvShowId.toInt(),
                    onBackClick = { navController.popBackStack() },
                    onTVShowClick = { similarTVShowId ->
                        navController.navigate(Screen.TVShowDetail.createRoute(similarTVShowId.toInt())) {
                            // Avoid building up a stack of the same screen
                            popUpTo(Screen.TVShowDetail.route) {
                                inclusive = true
                            }
                        }
                    },
                    onVideoClick = { id, title, videoKey ->
                        navController.navigate(
                            Screen.VideoPlayer.createRoute(
                                mediaType = "tv",
                                mediaId = id,
                                title = title,
                                videoKey = videoKey
                            )
                        )
                    }
                )
            }
        }
        
        // Video Player screen
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("mediaType") { type = NavType.StringType },
                navArgument("mediaId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("videoKey") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mediaType = backStackEntry.arguments?.getString("mediaType") ?: ""
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val videoKey = backStackEntry.arguments?.getString("videoKey") ?: ""
            
            VideoPlayerScreen(
                onBackClick = { navController.popBackStack() },
                title = title,
                mediaId = mediaId,
                mediaType = mediaType,
                videoKey = videoKey
            )
        }
        
        composable(Screen.Movies.route) {
            MoviesScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                },
                navController = navController
            )
        }
        
        composable(Screen.Trending.route) {
            TrendingScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                },
                onTVShowClick = { tvShow ->
                    navController.navigate(Screen.TVShowDetail.createRoute(tvShow.id))
                },
                navController = navController
            )
        }
    }
} 