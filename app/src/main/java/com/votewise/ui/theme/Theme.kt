package com.votewise.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.votewise.ui.theme.DarkGray
import com.votewise.ui.theme.LightGray
import com.votewise.ui.theme.NeutralWhite
import com.votewise.ui.theme.PatrioticBlue
import com.votewise.ui.theme.PatrioticRed
import com.votewise.ui.theme.Pink40
import com.votewise.ui.theme.Pink80
import com.votewise.ui.theme.TextPrimary


private val DarkColorScheme = darkColorScheme(
    primary = PatrioticBlue,
    secondary = PatrioticRed,
    tertiary = Pink80,
    background = DarkGray,
    surface = DarkGray,
    onPrimary = NeutralWhite,
    onSecondary = NeutralWhite,
    onTertiary = NeutralWhite,
    onBackground = NeutralWhite,
    onSurface = NeutralWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = PatrioticBlue,
    secondary = PatrioticRed,
    tertiary = Pink40,
    background = LightGray,
    surface = NeutralWhite,
    onPrimary = NeutralWhite,
    onSecondary = NeutralWhite,
    onTertiary = NeutralWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun VotewiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // window.statusBarColor = colorScheme.primary.toArgb() // Removed this deprecated line
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
