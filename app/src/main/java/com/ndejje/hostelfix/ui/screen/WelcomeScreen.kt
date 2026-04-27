package com.ndejje.hostelfix.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ndejje.hostelfix.R
import kotlinx.coroutines.delay

/**
 * The initial landing page of the application with a full-screen animated background.
 * Features a cross-fading slideshow of hostel images covering the entire background
 * with text and buttons floating on top.
 */
@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    // Animation state for screen entry
    var visible by remember { mutableStateOf(false) }
    
    // State for the current image index in the slideshow
    var currentImageIndex by remember { mutableIntStateOf(0) }

    val images = listOf(
        R.drawable.hostel,
        R.drawable.taylor,
        R.drawable.victoria
    )

    // Slideshow logic: Change image every 5 seconds
    LaunchedEffect(Unit) {
        visible = true
        while (true) {
            delay(5000)
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- LAYER 1: Full-Screen Animated Background ---
        val infiniteTransition = rememberInfiniteTransition(label = "scaleTransition")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 6000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "zoomAnimation"
        )

        Crossfade(
            targetState = images[currentImageIndex],
            animationSpec = tween(2000),
            label = "imageCrossfade",
            modifier = Modifier.fillMaxSize()
        ) { imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                contentScale = ContentScale.Crop
            )
        }

        // --- LAYER 2: Gradient Overlay for Text Readability ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // --- LAYER 3: Interactive Content (Text and Floating Button) ---
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1500)) + slideInVertically(initialOffsetY = { it / 4 }),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                // App Title
                Text(
                    text = stringResource(R.string.welcome_title),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Green,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                // Headline
                Text(
                    text = stringResource(R.string.welcome_headline),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                // Description
                Text(
                    text = stringResource(R.string.welcome_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Cyan.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

                // Floating "Get Started" Button
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.get_started),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
            }
        }
    }
}
