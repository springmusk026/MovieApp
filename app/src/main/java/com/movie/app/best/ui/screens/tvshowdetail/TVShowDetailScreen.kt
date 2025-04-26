package com.movie.app.best.ui.screens.tvshowdetail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.movie.app.best.data.model.Cast
import com.movie.app.best.data.model.Episode
import com.movie.app.best.data.model.Season
import com.movie.app.best.data.model.TVShow
import com.movie.app.best.data.model.Video
import com.movie.app.best.ui.components.ErrorView
import com.movie.app.best.ui.components.RatingBadge

@Composable
fun TVShowDetailScreen(
    tvShowId: Int,
    onBackClick: () -> Unit,
    onTVShowClick: (Int) -> Unit,
    onVideoClick: (tvShowId: String, tvShowName: String, videoKey: String) -> Unit,
    viewModel: TVShowDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // State for selected season
    var selectedSeason by remember { mutableStateOf<Season?>(null) }
    var showSeasonDropdown by remember { mutableStateOf(false) }
    
    // Initialize selected season if not set and seasons available
    if (selectedSeason == null && uiState.tvShow?.seasons?.isNotEmpty() == true) {
        // Default to first regular season (usually season 1, skipping specials)
        selectedSeason = uiState.tvShow?.seasons?.firstOrNull { it.season_number > 0 }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading && uiState.tvShow == null -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            uiState.error != null && uiState.tvShow == null -> {
                ErrorView(
                    error = uiState.error ?: "Failed to load TV show details",
                    onRetry = viewModel::loadTVShowDetails
                )
            }
            uiState.tvShow != null -> {
                val tvShow = uiState.tvShow!!
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Banner with Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(550.dp)
                    ) {
                        // Backdrop Image
                        AsyncImage(
                            model = if (tvShow.backdrop_path != null) {
                                "https://image.tmdb.org/t/p/original${tvShow.backdrop_path}"
                            } else {
                                "https://image.tmdb.org/t/p/original${tvShow.poster_path}"
                            },
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
                                            Color.Black.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 0f,
                                        endY = 1000f
                                    )
                                )
                        )
                        
                        // Back button
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        
                        // Content at the bottom of hero
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            // Title
                            Text(
                                text = tvShow.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Metadata
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Year
                                if (tvShow.first_air_date?.isNotEmpty() == true) {
                                    Text(
                                        text = tvShow.first_air_date.split("-")[0],
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                    
                                    Text(
                                        text = " • ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                                
                                // Rating
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFC107)
                                )
                                
                                Spacer(modifier = Modifier.width(4.dp))
                                
                                Text(
                                    text = String.format("%.1f", tvShow.vote_average),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                
                                Text(
                                    text = " • ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                
                                // Number of seasons
                                Text(
                                    text = "${tvShow.number_of_seasons} season${if (tvShow.number_of_seasons != 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )

                                Text(
                                    text = " • ${tvShow.status}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Play First Episode Button
                                Button(
                                    onClick = { 
                                        // Navigate to direct content playback, passing empty videoKey for direct playback
                                        // Use selected season or default to season 1, episode 1
                                        val seasonNumber = selectedSeason?.season_number ?: 1
                                        onVideoClick(tvShowId.toString(), tvShow.name, "")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Play")
                                }
                                
                                // Play Trailer Button (if available)
                                if (uiState.videos.any { it.type == "Trailer" && it.site == "YouTube" }) {
                                    OutlinedButton(
                                        onClick = { 
                                            // Find the first trailer
                                            val trailer = uiState.videos.firstOrNull { 
                                                it.type == "Trailer" && it.site == "YouTube" 
                                            }
                                            if (trailer != null) {
                                                onVideoClick(tvShowId.toString(), tvShow.name, trailer.key)
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Trailer")
                                    }
                                } else {
                                    // My List Button as fallback if no trailer available
                                    OutlinedButton(
                                        onClick = { /* Add to list functionality */ },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "My List")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Show description
                    if (!tvShow.overview.isNullOrEmpty()) {
                        Text(
                            text = tvShow.overview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    
                    // Episodes section
                    if (uiState.tvShow != null && uiState.tvShow!!.seasons.isNotEmpty()) {
                        // Season selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Episodes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Season dropdown selector
                            Box {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.DarkGray.copy(alpha = 0.3f))
                                        .clickable { showSeasonDropdown = true }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedSeason?.name ?: "Select Season",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Season",
                                        tint = Color.White
                                    )
                                }
                                
                                DropdownMenu(
                                    expanded = showSeasonDropdown,
                                    onDismissRequest = { showSeasonDropdown = false }
                                ) {
                                    tvShow.seasons.forEach { season ->
                                        // Skip season 0 (specials)
                                        if (season.season_number > 0) {
                                            DropdownMenuItem(
                                                text = { Text(season.name) },
                                                onClick = { 
                                                    selectedSeason = season
                                                    showSeasonDropdown = false
                                                    viewModel.loadSeasonEpisodes(tvShowId, season.season_number)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Episode list for selected season
                        if (selectedSeason != null) {
                            // Trigger loading episodes when season changes
                            if (uiState.episodes.isEmpty() && !uiState.isEpisodesLoading) {
                                viewModel.loadSeasonEpisodes(tvShowId, selectedSeason!!.season_number)
                            }
                            
                            // Episode list with loading state
                            when {
                                uiState.isEpisodesLoading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                uiState.episodesError != null -> {
                                    ErrorView(
                                        error = uiState.episodesError ?: "Failed to load episodes",
                                        onRetry = { viewModel.loadSeasonEpisodes(tvShowId, selectedSeason!!.season_number) },
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                uiState.episodes.isEmpty() -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No episodes found for this season",
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                else -> {
                                    // Display episodes
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        uiState.episodes.forEach { episode ->
                                            EpisodeItem(
                                                episode = episode,
                                                onPlayClick = {
                                                    // Navigate to video player with season and episode info
                                                    onVideoClick(
                                                        tvShowId.toString(),
                                                        "${tvShow.name} - S${selectedSeason!!.season_number}:E${episode.episode_number} - ${episode.name}",
                                                        ""
                                                    )
                                                }
                                            )
                                            Divider(
                                                modifier = Modifier.padding(horizontal = 16.dp),
                                                color = Color.DarkGray.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Seasons section (horizontal list for quick access to specific seasons)
                    if (uiState.tvShow != null) {
                        Text(
                            text = "Seasons",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        )
                        
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.tvShow!!.seasons) { season ->
                                // Skip season 0 (often specials)
                                if (season.season_number > 0) {
                                    SeasonItem(
                                        season = season,
                                        isSelected = selectedSeason?.season_number == season.season_number,
                                        onClick = {
                                            selectedSeason = season
                                            viewModel.loadSeasonEpisodes(tvShowId, season.season_number)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Cast section if available
                    if (uiState.credits != null) {
                        Text(
                            text = "Cast",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        )
                        
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.credits?.cast ?: emptyList()) { cast ->
                                CastItem(cast = cast)
                            }
                        }
                    }
                    
                    // Trailers section if available
                    if (uiState.videos.isNotEmpty()) {
                        Text(
                            text = "Trailers & More",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        )
                        
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(uiState.videos.filter { it.site == "YouTube" }) { video ->
                                VideoItem(
                                    video = video,
                                    onClick = {
                                        onVideoClick(tvShowId.toString(), tvShow.name, video.key)
                                    }
                                )
                            }
                        }
                    }
                    
                    // Similar TV Shows section if available
                    if (uiState.similarTVShows.isNotEmpty()) {
                        Text(
                            text = "More Like This",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        )
                        
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.similarTVShows) { similarTVShow ->
                                SimilarTVShowItem(
                                    tvShow = similarTVShow,
                                    onClick = { onTVShowClick(similarTVShow.id) }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: Episode,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Episode thumbnail
        Box(
            modifier = Modifier
                .width(160.dp)
                .aspectRatio(16f/9f)
        ) {
            Card(
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = if (episode.still_path != null) {
                        "https://image.tmdb.org/t/p/w300${episode.still_path}"
                    } else {
                        null
                    },
                    contentDescription = episode.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Play overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(onClick = onPlayClick),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        // Episode details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Episode number and title
            Text(
                text = "${episode.episode_number}. ${episode.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            // Episode runtime
            if (episode.runtime != null && episode.runtime > 0) {
                Text(
                    text = "${episode.runtime} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Episode overview
            Text(
                text = episode.overview ?: "No description available",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CastItem(cast: Cast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(80.dp)
        ) {
            SubcomposeAsyncImage(
                model = if (cast.profile_path != null) {
                    "https://image.tmdb.org/t/p/w185${cast.profile_path}"
                } else {
                    null
                },
                contentDescription = cast.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                },
                error = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = cast.name.firstOrNull()?.toString() ?: "?",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = cast.name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Text(
            text = cast.character,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SeasonItem(
    season: Season,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.width(130.dp)
    ) {
        Box {
            Card(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(),
                border = if (isSelected) {
                    androidx.compose.foundation.BorderStroke(
                        2.dp, MaterialTheme.colorScheme.primary
                    )
                } else null
            ) {
                AsyncImage(
                    model = if (season.poster_path != null) {
                        "https://image.tmdb.org/t/p/w342${season.poster_path}"
                    } else {
                        null
                    },
                    contentDescription = season.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f/3f)
                )
            }
            
            // Play button overlay if onClick is provided
            if (onClick != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f/3f)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(onClick = onClick),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            // Selected indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = season.name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Text(
            text = "${season.episode_count} episodes",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoItem(
    video: Video,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .aspectRatio(16f/9f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://img.youtube.com/vi/${video.key}/hqdefault.jpg",
                contentDescription = video.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Play icon overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                // Trailer indicator if the video is a trailer
                if (video.type.equals("Trailer", ignoreCase = true)) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "TRAILER",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White
                        )
                    }
                }
                
                // Play button
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )
                }
            }
            
            // Video title
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimilarTVShowItem(
    tvShow: TVShow,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .aspectRatio(2f/3f),
        shape = RoundedCornerShape(4.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w342${tvShow.poster_path}",
            contentDescription = tvShow.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
} 