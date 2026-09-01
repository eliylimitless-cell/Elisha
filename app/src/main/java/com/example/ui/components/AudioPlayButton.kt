package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ImperialRedPrimary

@Composable
fun AudioPlayButton(
  isPlaying: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  tintColor: Color = ImperialRedPrimary
) {
  val infiniteTransition = rememberInfiniteTransition(label = "audio_ripple")
  val scale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isPlaying) 1.25f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .size(size)
      .testTag("audio_play_button")
  ) {
    if (isPlaying) {
      Box(
        modifier = Modifier
          .size(size * 0.9f)
          .scale(scale)
          .clip(CircleShape)
          .background(tintColor.copy(alpha = 0.2f))
      )
    }

    IconButton(
      onClick = onClick,
      modifier = Modifier
        .size(size)
        .clip(CircleShape)
    ) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
        contentDescription = if (isPlaying) "Stop playing audio" else "Play Chinese pronunciation audio",
        tint = tintColor
      )
    }
  }
}
