package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HskAdvancedColor
import com.example.ui.theme.HskElementaryColor
import com.example.ui.theme.HskIntermediateColor

@Composable
fun HskBadge(
  level: Int,
  modifier: Modifier = Modifier,
  showLabel: Boolean = true
) {
  val (bgColor, textColor, stageText) = when (level) {
    in 1..3 -> Triple(HskElementaryColor.copy(alpha = 0.15f), HskElementaryColor, "Elementary")
    in 4..6 -> Triple(HskIntermediateColor.copy(alpha = 0.15f), HskIntermediateColor, "Intermediate")
    else -> Triple(HskAdvancedColor.copy(alpha = 0.15f), HskAdvancedColor, "Advanced")
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = if (showLabel) "HSK $level · $stageText" else "HSK $level",
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}
