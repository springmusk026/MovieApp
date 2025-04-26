package com.movie.app.best.ui.screens.movies

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.movie.app.best.data.model.Movie
import com.movie.app.best.ui.components.ErrorView
import com.movie.app.best.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    onMovieClick: (Movie) -> Unit,
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Play handler for all movies
    val onPlayClick: (Movie) -> Unit = { movie ->
        navController.navigate(
            Screen.VideoPlayer.createContentRoute(
                mediaType = "movie",
                mediaId = movie.id.toString(),
                title = movie.title
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Featured movie section
        if (uiState.popularMovies.isNotEmpty()) {
            FeaturedMovie(
                movie = uiState.popularMovies.first(),
                onMovieClick = onMovieClick,
                onPlayClick = onPlayClick
            )
        }
        
        // Genre Header
        Text(
            text = "Movies",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        
        // Categories row
        CategoryRow()
        
        // Popular Movies
        SectionTitle(title = "Popular")
        MovieRow(
            movies = uiState.popularMovies,
            isLoading = uiState.isPopularMoviesLoading,
            error = uiState.popularMoviesError,
            onMovieClick = onMovieClick,
            onRetry = viewModel::loadPopularMovies,
            useLargeCard = true,
        )
        
        // Top Rated Movies
        SectionTitle(title = "Top Rated")
        MovieRow(
            movies = uiState.topRatedMovies,
            isLoading = uiState.isTopRatedMoviesLoading,
            error = uiState.topRatedMoviesError,
            onMovieClick = onMovieClick,
            onRetry = viewModel::loadTopRatedMovies,
        )
        
        // Upcoming Movies
        SectionTitle(title = "Coming Soon")
        MovieRow(
            movies = uiState.upcomingMovies,
            isLoading = uiState.isUpcomingMoviesLoading,
            error = uiState.upcomingMoviesError,
            onMovieClick = onMovieClick,
            onRetry = viewModel::loadUpcomingMovies,
        )
        
        // Now Playing Movies
        SectionTitle(title = "Now Playing")
        MovieRow(
            movies = uiState.nowPlayingMovies,
            isLoading = uiState.isNowPlayingMoviesLoading,
            error = uiState.nowPlayingMoviesError,
            onMovieClick = onMovieClick,
            onRetry = viewModel::loadNowPlayingMovies,
        )
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun CategoryRow() {
    val categories = listOf("All", "Action", "Comedy", "Horror", "Drama", "Sci-Fi", "Fantasy")
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(category = category)
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
fun FeaturedMovie(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    onPlayClick: (Movie) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Banner image
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${movie.backdrop_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 300f
                    )
                )
        )
        
        // Content (title, buttons)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Play and Info buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onPlayClick(movie) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "Play")
                }
                
                OutlinedButton(
                    onClick = { onMovieClick(movie) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "More Info",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "More Info")
                }
            }
        }
    }
}

@Composable
fun MovieRow(
    movies: List<Movie>,
    isLoading: Boolean,
    error: String?,
    onMovieClick: (Movie) -> Unit,
    onRetry: () -> Unit,
    useLargeCard: Boolean = false,
) {
    when {
        isLoading && movies.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        error != null && movies.isEmpty() -> {
            ErrorView(
                error = error,
                onRetry = onRetry,
                modifier = Modifier.padding(16.dp)
            )
        }
        movies.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No movies found",
                    color = Color.White.copy(alpha = 0.7f)
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
                    MovieCard(
                        movie = movie,
                        onMovieClick = onMovieClick,
                        useLargeCard = useLargeCard,
                    )
                }
            }
            
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    useLargeCard: Boolean = false,
) {
    Card(
        onClick = { onMovieClick(movie) },
        modifier = Modifier
            .width(if (useLargeCard) 280.dp else 160.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box {
            AsyncImage(
                model = if (useLargeCard) {
                    "https://image.tmdb.org/t/p/w500${movie.backdrop_path ?: movie.poster_path}"
                } else {
                    "https://image.tmdb.org/t/p/w342${movie.poster_path}"
                },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (useLargeCard) 16f/9f else 2f/3f)
            )

        }
    }
} 