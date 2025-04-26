package com.movie.app.best.ui.screens.trending

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.ui.components.ErrorView
import com.movie.app.best.ui.components.MovieCard
import com.movie.app.best.ui.components.TVShowCard
import com.movie.app.best.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    onMovieClick: (Movie) -> Unit,
    onTVShowClick: (TVShow) -> Unit,
    navController: NavController,
    viewModel: TrendingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Today", "This Week")
    
    // Play handlers
    val onMoviePlayClick: (Movie) -> Unit = { movie ->
        navController.navigate(
            Screen.VideoPlayer.createContentRoute(
                mediaType = "movie",
                mediaId = movie.id.toString(),
                title = movie.title
            )
        )
    }
    
    val onTVShowPlayClick: (TVShow) -> Unit = { tvShow ->
        navController.navigate(
            Screen.VideoPlayer.createContentRoute(
                mediaType = "tv",
                mediaId = tvShow.id.toString(),
                title = tvShow.name
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Trending", 
                        color = Color.White, 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Tab selection
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Black,
                contentColor = Color.Red
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { 
                            Text(
                                title,
                                color = if (selectedTabIndex == index) Color.White else Color.Gray
                            ) 
                        },
                        selected = selectedTabIndex == index,
                        onClick = { 
                            selectedTabIndex = index
                            if (index == 0) {
                                viewModel.loadTrendingToday()
                            } else {
                                viewModel.loadTrendingThisWeek()
                            }
                        }
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black)
            ) {
                // Featured Content Banner
                FeaturedContent(
                    movies = if (selectedTabIndex == 0) uiState.trendingMoviesToday else uiState.trendingMoviesWeek,
                    onMovieClick = onMovieClick,
                    onPlayClick = onMoviePlayClick
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Trending Movies
                TrendingMoviesSection(
                    title = "Trending Movies",
                    movies = if (selectedTabIndex == 0) uiState.trendingMoviesToday else uiState.trendingMoviesWeek,
                    isLoading = if (selectedTabIndex == 0) uiState.isTrendingMoviesTodayLoading else uiState.isTrendingMoviesWeekLoading,
                    error = if (selectedTabIndex == 0) uiState.trendingMoviesTodayError else uiState.trendingMoviesWeekError,
                    onMovieClick = onMovieClick,
                    onRetry = { 
                        if (selectedTabIndex == 0) viewModel.loadTrendingMoviesToday() 
                        else viewModel.loadTrendingMoviesThisWeek() 
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Trending TV Shows
                TrendingTVShowsSection(
                    title = "Trending TV Shows",
                    tvShows = if (selectedTabIndex == 0) uiState.trendingTVShowsToday else uiState.trendingTVShowsWeek,
                    isLoading = if (selectedTabIndex == 0) uiState.isTrendingTVShowsTodayLoading else uiState.isTrendingTVShowsWeekLoading,
                    error = if (selectedTabIndex == 0) uiState.trendingTVShowsTodayError else uiState.trendingTVShowsWeekError,
                    onTVShowClick = onTVShowClick,
                    onPlayClick = onTVShowPlayClick,
                    onRetry = { 
                        if (selectedTabIndex == 0) viewModel.loadTrendingTVShowsToday() 
                        else viewModel.loadTrendingTVShowsThisWeek() 
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FeaturedContent(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onPlayClick: (Movie) -> Unit
) {
    if (movies.isNotEmpty()) {
        val featuredMovie = movies.first()
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            // Featured movie backdrop
            AsyncImage(
                model = "https://image.tmdb.org/t/p/original${featuredMovie.backdrop_path ?: featuredMovie.poster_path}",
                contentDescription = featuredMovie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 0f,
                            endY = 900f
                        )
                    )
            )
            
            // Content info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = featuredMovie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 28.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = featuredMovie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onPlayClick(featuredMovie) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Play",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { onMovieClick(featuredMovie) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "More Info",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "More Info",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingMoviesSection(
    title: String,
    movies: List<Movie>,
    isLoading: Boolean,
    error: String?,
    onMovieClick: (Movie) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 20.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            error != null -> {
                ErrorView(
                    error = error,
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            movies.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No trending movies found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(movies) { movie ->
                        NetflixStyleMovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie) },
                            modifier = Modifier.width(130.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingTVShowsSection(
    title: String,
    tvShows: List<TVShow>,
    isLoading: Boolean,
    error: String?,
    onTVShowClick: (TVShow) -> Unit,
    onPlayClick: (TVShow) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 20.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            error != null -> {
                ErrorView(
                    error = error,
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            tvShows.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No trending TV shows found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tvShows) { tvShow ->
                        NetflixStyleTVShowItem(
                            tvShow = tvShow,
                            onClick = { onTVShowClick(tvShow) },
                            modifier = Modifier.width(130.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NetflixStyleMovieItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .aspectRatio(2/3f)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
        )

    }
}

@Composable
fun NetflixStyleTVShowItem(
    tvShow: TVShow,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .aspectRatio(2/3f)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${tvShow.poster_path}",
            contentDescription = tvShow.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
        )

    }
} 