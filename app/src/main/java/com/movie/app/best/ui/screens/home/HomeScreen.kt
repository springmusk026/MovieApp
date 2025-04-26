package com.movie.app.best.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.movie.app.best.data.model.Movie
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.ui.components.ErrorView
import com.movie.app.best.ui.components.MovieCard
import com.movie.app.best.ui.components.TVShowCard
import com.movie.app.best.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onTVShowClick: (TVShow) -> Unit,
    onTVShowsClick: () -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero carousel section
            HeroCarousel(
                featuredItems = uiState.featuredItems,
                onMovieClick = onMovieClick,
                onTVShowClick = onTVShowClick,
                onPlayMovie = { movie ->
                    // Direct content playback
                    navController.navigate(
                        Screen.VideoPlayer.createContentRoute(
                            mediaType = "movie",
                            mediaId = movie.id.toString(),
                            title = movie.title
                        )
                    )
                },
                onPlayTVShow = { tvShow ->
                    // Direct content playback
                    navController.navigate(
                        Screen.VideoPlayer.createContentRoute(
                            mediaType = "tv",
                            mediaId = tvShow.id.toString(),
                            title = tvShow.name
                        )
                    )
                }
            )
            
            // Netflix Originals (Trending Movies) - Larger posters
            SectionTitle(title = "Netflix Originals")
            MovieRow(
                movies = uiState.trendingMovies,
                isLoading = uiState.isTrendingMoviesLoading,
                error = uiState.trendingMoviesError,
                onMovieClick = onMovieClick,
                onRetry = viewModel::loadTrendingMovies,
                isPoster = true
            )
            
            // Trending TV Shows
            SectionTitle(title = "Trending TV Shows")
            TVShowRow(
                tvShows = uiState.trendingTVShows,
                isLoading = uiState.isTrendingTVShowsLoading,
                error = uiState.trendingTVShowsError,
                onTVShowClick = onTVShowClick,
                onRetry = viewModel::loadTrendingTVShows
            )
            
            // Popular Movies
            SectionTitle(title = "Popular Movies")
            MovieRow(
                movies = uiState.popularMovies,
                isLoading = uiState.isPopularLoading,
                error = uiState.popularError,
                onMovieClick = onMovieClick,
                onRetry = viewModel::loadPopularMovies
            )
            
            // Popular TV Shows
            SectionTitle(title = "Popular TV Shows")
            TVShowRow(
                tvShows = uiState.popularTVShows,
                isLoading = uiState.isPopularTVShowsLoading,
                error = uiState.popularTVShowsError,
                onTVShowClick = onTVShowClick,
                onRetry = viewModel::loadPopularTVShows
            )
            
            // Top Rated Movies
            SectionTitle(title = "Top Rated Movies")
            MovieRow(
                movies = uiState.topRatedMovies,
                isLoading = uiState.isTopRatedLoading,
                error = uiState.topRatedError,
                onMovieClick = onMovieClick,
                onRetry = viewModel::loadTopRatedMovies
            )
            
            // Top Rated TV Shows
            SectionTitle(title = "Top Rated TV Shows")
            TVShowRow(
                tvShows = uiState.topRatedTVShows,
                isLoading = uiState.isTopRatedTVShowsLoading,
                error = uiState.topRatedTVShowsError,
                onTVShowClick = onTVShowClick,
                onRetry = viewModel::loadTopRatedTVShows
            )
            
            // Upcoming Movies
            SectionTitle(title = "Coming Soon")
            MovieRow(
                movies = uiState.upcomingMovies,
                isLoading = uiState.isUpcomingLoading,
                error = uiState.upcomingError,
                onMovieClick = onMovieClick,
                onRetry = viewModel::loadUpcomingMovies
            )
            
            // On The Air TV Shows
            SectionTitle(title = "On The Air")
            TVShowRow(
                tvShows = uiState.onTheAirTVShows,
                isLoading = uiState.isOnTheAirTVShowsLoading,
                error = uiState.onTheAirTVShowsError,
                onTVShowClick = onTVShowClick,
                onRetry = viewModel::loadOnTheAirTVShows
            )
            
            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom nav
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel(
    featuredItems: List<FeaturedItem>,
    onMovieClick: (Movie) -> Unit,
    onTVShowClick: (TVShow) -> Unit,
    onPlayMovie: (Movie) -> Unit,
    onPlayTVShow: (TVShow) -> Unit
) {
    if (featuredItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
        return
    }
    
    val pagerState = rememberPagerState(pageCount = { featuredItems.size })
    val scope = rememberCoroutineScope()
    
    // Auto-scroll effect with better control
    LaunchedEffect(Unit) {
        while (true) {
            delay(6.seconds)
            val nextPage = if (pagerState.currentPage < featuredItems.lastIndex) {
                pagerState.currentPage + 1
            } else {
                0
            }
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            )
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) { page ->
            val featured = featuredItems[page]
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Apply scaling effect for a more dynamic feel
                        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                        
                        // Scale items that are not focused
                        lerp(
                            start = 0.95f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        
                        // Apply a slight alpha fade to items not focused
                        alpha = lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 0.5f) * 2
                        )
                    }
            ) {
                when (featured) {
                    is FeaturedItem.Movie -> {
                        EnhancedFeaturedMovie(
                            movie = featured.movie,
                            onMovieClick = onMovieClick,
                            onPlayClick = onPlayMovie
                        )
                    }
                    
                    is FeaturedItem.TVShow -> {
                        EnhancedFeaturedTVShow(
                            tvShow = featured.tvShow,
                            onTVShowClick = onTVShowClick,
                            onPlayClick = onPlayTVShow
                        )
                    }
                }
            }
        }
        
        // Improved page indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(featuredItems.size) { index ->
                val isSelected = pagerState.currentPage == index
                
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(width = if (isSelected) 24.dp else 8.dp, height = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected) Color.Red else Color.Gray.copy(alpha = 0.7f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                                    )
                                )
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun EnhancedFeaturedMovie(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    onPlayClick: (Movie) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        // Banner image
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${movie.backdrop_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Enhanced gradient overlay with multiple layers
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f),
                        ),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        )
        
        // Top rating badge
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${movie.vote_average}/10",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Content (title, buttons) with improved layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title with better styling
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Year and genres info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Year
                    if (!movie.release_date.isNullOrEmpty()) {
                        Text(
                            text = movie.release_date.split("-")[0],
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = " • ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Rating as text
                    Text(
                        text = "Rating: ${movie.vote_average}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Play and Info buttons with improved styling
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onPlayClick(movie) },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Play",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { onMovieClick(movie) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "More Info"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Info",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedFeaturedTVShow(
    tvShow: TVShow,
    onTVShowClick: (TVShow) -> Unit,
    onPlayClick: (TVShow) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        // Banner image
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${tvShow.backdrop_path}",
            contentDescription = tvShow.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Enhanced gradient overlay with multiple layers
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f),
                        ),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        )
        
        // TV Show badge with improved styling
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "TV Series",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Content (title, buttons) with improved layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title with better styling
                Text(
                    text = tvShow.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Year and genres info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Year
                    if (!tvShow.first_air_date.isNullOrEmpty()) {
                        Text(
                            text = tvShow.first_air_date.split("-")[0],
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = " • ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Rating as text
                    Text(
                        text = "Rating: ${tvShow.vote_average}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Play and Info buttons with improved styling
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onPlayClick(tvShow) },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Play",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { onTVShowClick(tvShow) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "More Info"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Info",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
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
fun MovieRow(
    movies: List<Movie>,
    isLoading: Boolean,
    error: String?,
    onMovieClick: (Movie) -> Unit,
    onRetry: () -> Unit,
    isPoster: Boolean = false
) {
    when {
        isLoading && movies.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
        error != null && movies.isEmpty() -> {
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
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No movies available",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        else -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies) { movie ->
                    NetflixStyleMovieItem(
                        movie = movie,
                        onClick = { onMovieClick(movie) },
                        modifier = Modifier.width(if (isPoster) 130.dp else 250.dp),
                        isPoster = isPoster
                    )
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
    onRetry: () -> Unit
) {
    when {
        isLoading && tvShows.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
        error != null && tvShows.isEmpty() -> {
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
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No TV shows available",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        else -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tvShows) { tvShow ->
                    NetflixStyleTVShowItem(
                        tvShow = tvShow,
                        onClick = { onTVShowClick(tvShow) },
                        modifier = Modifier.width(250.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NetflixStyleMovieItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPoster: Boolean = false
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp)
    ) {
        Box {
            AsyncImage(
                model = if (isPoster) {
                    "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                } else {
                    "https://image.tmdb.org/t/p/w500${movie.backdrop_path ?: movie.poster_path}"
                },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (isPoster) 2/3f else 16/9f)
            )
            
            if (!isPoster) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 50f
                            )
                        )
                )
                
                Text(
                    text = movie.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun NetflixStyleTVShowItem(
    tvShow: TVShow,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp)
    ) {
        Box {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvShow.backdrop_path ?: tvShow.poster_path}",
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16/9f)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 50f
                        )
                    )
            )
            
            Text(
                text = tvShow.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}