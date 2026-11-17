package com.example.ght.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

private const val ESCALA_FONTE = 1.25f

private val DefaultTypography = Typography()

private fun TextStyle.comFonteGeral() = copy(
    fontFamily = FonteGeral,
    fontSize = fontSize * ESCALA_FONTE,
    lineHeight = lineHeight * ESCALA_FONTE
)

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.comFonteGeral(),
    displayMedium = DefaultTypography.displayMedium.comFonteGeral(),
    displaySmall = DefaultTypography.displaySmall.comFonteGeral(),
    headlineLarge = DefaultTypography.headlineLarge.comFonteGeral(),
    headlineMedium = DefaultTypography.headlineMedium.comFonteGeral(),
    headlineSmall = DefaultTypography.headlineSmall.comFonteGeral(),
    titleLarge = DefaultTypography.titleLarge.comFonteGeral(),
    titleMedium = DefaultTypography.titleMedium.comFonteGeral(),
    titleSmall = DefaultTypography.titleSmall.comFonteGeral(),
    bodyLarge = DefaultTypography.bodyLarge.comFonteGeral(),
    bodyMedium = DefaultTypography.bodyMedium.comFonteGeral(),
    bodySmall = DefaultTypography.bodySmall.comFonteGeral(),
    labelLarge = DefaultTypography.labelLarge.comFonteGeral(),
    labelMedium = DefaultTypography.labelMedium.comFonteGeral(),
    labelSmall = DefaultTypography.labelSmall.comFonteGeral()
)
