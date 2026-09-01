package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ImperialGoldTertiary
import com.example.ui.theme.ImperialRedPrimary
import com.example.ui.theme.JadeBambooSecondary
import com.example.ui.theme.ZimbabweEcoCashBlue

enum class PaymentMethodOption {
  ECOCASH_ZIMBABWE,
  ONEMONEY_ZIMBABWE,
  GOOGLE_PLAY_BILLING,
  CREDIT_CARD
}

@Composable
fun SubscriptionDialog(
  isProUnlocked: Boolean,
  onTogglePro: (Boolean) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedPayment by remember { mutableStateOf(PaymentMethodOption.ECOCASH_ZIMBABWE) }
  var zimPhoneNumber by remember { mutableStateOf("+263 77 ") }
  var isProcessing by remember { mutableStateOf(false) }
  var paymentSuccessMessage by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("subscription_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ImperialGoldTertiary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Star, contentDescription = null, tint = ImperialGoldTertiary)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "HanyuMate Pro VIP",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Unlimited AI Tutoring & Mock Exams",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feature Comparison Card
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            val features = listOf(
              "Unlimited AI Chinese Tutor Chat (24/7)",
              "Full HSK 1–9 Curriculum & Stroke Logic",
              "Spoken Chinese Audio Recasting & Tone Drills",
              "Official HSK Mock Exam Simulator with Scoring",
              "Offline SRS Vocab Bank Sync"
            )
            features.forEach { feat ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Icon(
                  Icons.Default.Check,
                  contentDescription = null,
                  tint = JadeBambooSecondary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = feat, style = MaterialTheme.typography.bodySmall)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Regional Payment Selector
        Text(
          text = "CHOOSE PAYMENT METHOD",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        val options = listOf(
          Pair(PaymentMethodOption.ECOCASH_ZIMBABWE, "EcoCash (Zimbabwe Mobile Money · \$4.99/mo)"),
          Pair(PaymentMethodOption.ONEMONEY_ZIMBABWE, "OneMoney (NetOne Zimbabwe · \$4.99/mo)"),
          Pair(PaymentMethodOption.GOOGLE_PLAY_BILLING, "Google Play In-App Billing (\$4.99/mo)"),
          Pair(PaymentMethodOption.CREDIT_CARD, "International Card / Visa")
        )

        options.forEach { (option, label) ->
          val isSelected = selectedPayment == option
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) ImperialRedPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ImperialRedPrimary) else null,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable { selectedPayment = option }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(12.dp)
            ) {
              Icon(
                imageVector = if (option == PaymentMethodOption.ECOCASH_ZIMBABWE || option == PaymentMethodOption.ONEMONEY_ZIMBABWE) Icons.Default.PhoneAndroid else Icons.Default.CreditCard,
                contentDescription = null,
                tint = if (isSelected) ImperialRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        if (selectedPayment == PaymentMethodOption.ECOCASH_ZIMBABWE || selectedPayment == PaymentMethodOption.ONEMONEY_ZIMBABWE) {
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = zimPhoneNumber,
            onValueChange = { zimPhoneNumber = it },
            label = { Text("Econet / NetOne Mobile Number") },
            placeholder = { Text("+263 77 123 4567") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (paymentSuccessMessage != null) {
          Text(
            text = paymentSuccessMessage!!,
            color = JadeBambooSecondary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
          onClick = {
            isProcessing = true
            onTogglePro(true)
            paymentSuccessMessage = "🎉 Payment Successful! HanyuMate VIP is now unlocked."
            isProcessing = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ImperialRedPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(if (isProUnlocked) "Renew / Manage VIP Plan" else "Unlock VIP (\$4.99 / month)")
        }
      }
    }
  }
}
