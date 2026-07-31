package com.example.presentation.screens.language

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.presentation.components.GlassCard
import com.example.presentation.theme.PrimaryBlue

@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel,
    onLanguageSelected: () -> Unit
) {
    val context = LocalContext.current
    val selectedLang by viewModel.selectedLanguage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedLanguage(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0E7FF), Color(0xFFF0F4FF))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    text = "🚌 PayanMitra",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.app_tagline),
                    fontSize = 16.sp,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.select_language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = stringResource(R.string.select_language_sub),
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Language Selection Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LanguageOptionCard(
                    flag = "🇬🇧",
                    name = "English",
                    subtitle = "English",
                    isSelected = selectedLang == "en",
                    onClick = { viewModel.selectLanguage("en") }
                )

                LanguageOptionCard(
                    flag = "🇮🇳",
                    name = "தமிழ்",
                    subtitle = "Tamil",
                    isSelected = selectedLang == "ta",
                    onClick = { viewModel.selectLanguage("ta") }
                )

                LanguageOptionCard(
                    flag = "🇮🇳",
                    name = "हिन्दी",
                    subtitle = "Hindi",
                    isSelected = selectedLang == "hi",
                    onClick = { viewModel.selectLanguage("hi") }
                )
            }

            // Bottom Continue Button
            Button(
                onClick = {
                    viewModel.saveAndContinue(context, onLanguageSelected)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = stringResource(R.string.continue_btn),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun LanguageOptionCard(
    flag: String,
    name: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PrimaryBlue else Color(0xCCFFFFFF)
    val borderWidth = if (isSelected) 2.5.dp else 1.5.dp

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        borderWidth = borderWidth,
        borderColor = borderColor,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = flag, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            if (isSelected) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryBlue,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
