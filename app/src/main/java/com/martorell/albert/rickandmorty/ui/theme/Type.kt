package com.martorell.albert.rickandmorty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.martorell.albert.rickandmorty.R




/**
 * Grandstander font downloaded from https://fonts.google.com/
 */
val GrandStander = FontFamily(

    Font(R.font.grandstander_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.grandstander_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.grandstander_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.grandstander_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.grandstander_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.grandstander_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.grandstander_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.grandstander_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.grandstander_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.grandstander_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.grandstander_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.grandstander_medium, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.grandstander_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.grandstander_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.grandstander_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.grandstander_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.grandstander_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.grandstander_thin_italic, FontWeight.Thin, FontStyle.Italic),

    )

/**
 * Extensions function that applies a new font fomily to the project
 */
fun Typography.defaultFontFamily(fontFamily: FontFamily): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = this.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = this.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = this.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = this.labelSmall.copy(fontFamily = fontFamily)
    )
}

// Set of Material typography styles to start with
val Typography = Typography().defaultFontFamily(fontFamily = GrandStander)