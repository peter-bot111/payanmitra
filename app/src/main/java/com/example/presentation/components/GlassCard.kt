package com.example.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.theme.GlassCardBackground
import com.example.presentation.theme.GlassCardBorder

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 1.5.dp,
    elevation: Dp = 8.dp,
    backgroundColor: Color = GlassCardBackground,
    borderColor: Color = GlassCardBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    ambientColor = Color(0x1A1A56DB),
                    spotColor = Color(0x331A56DB)
                ),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(backgroundColor)
                    .padding(16.dp),
                content = content
            )
        }
    } else {
        Surface(
            modifier = modifier
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    ambientColor = Color(0x1A1A56DB),
                    spotColor = Color(0x331A56DB)
                ),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(backgroundColor)
                    .padding(16.dp),
                content = content
            )
        }
    }
}
