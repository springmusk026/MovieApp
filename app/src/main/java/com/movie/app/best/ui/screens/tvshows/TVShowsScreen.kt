package com.movie.app.best.ui.screens.tvshows

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.ui.components.ErrorView
import androidx.navigation.NavController
import com.movie.app.best.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TVShowsScreen(
    onTVShowClick: (TVShow) -> Unit,
    navController: NavController,
    viewModel: TVShowsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Play handler for all TV shows
    val onPlayClick: (TVShow) -> Unit = { tvShow ->
        navController.navigate(
            Screen.VideoPlayer.createContentRoute(
                mediaType = "tv",
                mediaId = tvShow.id.toString(),
                title = tvShow.name
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Featured TV Show banner
        if (uiState.popularTVShows.isNotEmpty()) {
            FeaturedTVShow(
                tvShow = uiState.popularTVShows.first(),
                onTVShowClick = onTVShowClick,
                onPlayClick = onPlayClick
            )
        } else if (uiState.isPopularLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        
        // Main Screen sections
        Text(
            text = "TV Shows",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        
        // Genre categories
        GenreRow()
        
        // Popular TV Shows
        SectionTitle(title = "Popular on Netflix")
        TVShowRow(
            tvShows = uiState.popularTVShows,
            isLoading = uiState.isPopularLoading,
            error = uiState.popularError,
            onTVShowClick = onTVShowClick,
            onRetry = viewModel::loadPopularTVShows,
            useWideCard = true,
        )
        
        // Top Rated TV Shows
        SectionTitle(title = "Top Rated")
        TVShowRow(
            tvShows = uiState.topRatedTVShows,
            isLoading = uiState.isTopRatedLoading,
            error = uiState.topRatedError,
            onTVShowClick = onTVShowClick,
            onRetry = viewModel::loadTopRatedTVShows,
        )
        
        // On The Air TV Shows
        SectionTitle(title = "New Episodes")
        TVShowRow(
            tvShows = uiState.onTheAirTVShows,
            isLoading = uiState.isOnTheAirLoading,
            error = uiState.onTheAirError,
            onTVShowClick = onTVShowClick,
            onRetry = viewModel::loadOnTheAirTVShows,
        )
        
        Spacer(modifier = Modifier.height(80.dp)) // Space for bottom nav
    }
}

@Composable
fun GenreRow() {
    val genres = listOf("All", "Drama", "Comedy", "Action", "Crime", "Sci-Fi", "Reality", "Documentary")
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genres) { genre ->
            GenreChip(genre = genre)
        }
    }
}

@Composable
fun GenreChip(genre: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = genre,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
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
fun FeaturedTVShow(
    tvShow: TVShow,
    onTVShowClick: (TVShow) -> Unit,
    onPlayClick: (TVShow) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Banner image
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${tvShow.backdrop_path}",
            contentDescription = tvShow.name,
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
                text = tvShow.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onPlayClick(tvShow) },
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
                    onClick = { onTVShowClick(tvShow) },
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
fun TVShowRow(
    tvShows: List<TVShow>,
    isLoading: Boolean,
    error: String?,
    onTVShowClick: (TVShow) -> Unit,
    onRetry: () -> Unit,
    useWideCard: Boolean = false,
) {
    when {
        isLoading && tvShows.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        error != null && tvShows.isEmpty() -> {
            ErrorView(
                error = error,
                onRetry = onRetry,
                modifier = Modifier.padding(16.dp)
            )
        }
        tvShows.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No TV shows found",
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
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
                    NetflixTVShowCard(
                        tvShow = tvShow,
                        onTVShowClick = onTVShowClick,
                        useWideCard = useWideCard,
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
fun NetflixTVShowCard(
    tvShow: TVShow,
    onTVShowClick: (TVShow) -> Unit,
    useWideCard: Boolean = false,
) {
    Card(
        onClick = { onTVShowClick(tvShow) },
        modifier = Modifier
            .width(if (useWideCard) 280.dp else 160.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box {
            AsyncImage(
                model = if (useWideCard) {
                    "https://image.tmdb.org/t/p/w500${tvShow.backdrop_path ?: tvShow.poster_path}"
                } else {
                    "https://image.tmdb.org/t/p/w342${tvShow.poster_path}"
                },
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (useWideCard) 16f/9f else 2f/3f)
            )

        }
    }
} 