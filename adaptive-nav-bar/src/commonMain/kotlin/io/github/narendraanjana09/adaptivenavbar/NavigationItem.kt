package io.github.narendraanjana09.adaptivenavbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource

data class NavigationItem(
    val title: String,
    val icon: DrawableResource,
    val systemIcon: String,
    val selectedIcon: DrawableResource? = null,
    val selectedSystemIcon: String? = null,
    val showLabel: Boolean = true,
    val badge: String? = null,

    /**
     * Only for Android Implementation for IOS use .badge SF Symbol
     */
    val showBadgeDot: Boolean = false,
    val enabled: Boolean = true,
    val contentDescription: String? = null,
)


data class IosFabItem(
    val title: String = "",
    val showLabel: Boolean = false,
    val icon: DrawableResource,
    val systemIcon: String,
    val contentColor: Color,
    val containerColor: Color,
    val contentDescription: String? = null,
)

data class AdaptiveNavigationBarColors(
    val containerColor: Color,
    val indicatorColor: Color,
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
    val badgeContainerColor: Color,
    val badgeContentColor: Color,
)

object AdaptiveNavigationBarDefaults {
    @Composable
    fun colors(
        containerColor: Color =
            MaterialTheme.colorScheme.surfaceContainer,

        indicatorColor: Color =
            MaterialTheme.colorScheme.primaryContainer,

        selectedIconColor: Color =
            MaterialTheme.colorScheme.primary,

        selectedTextColor: Color =
            MaterialTheme.colorScheme.primary,

        unselectedIconColor: Color =
            MaterialTheme.colorScheme.onSurfaceVariant,

        unselectedTextColor: Color =
            MaterialTheme.colorScheme.onSurfaceVariant,

        badgeContainerColor: Color =
            MaterialTheme.colorScheme.error,

        badgeContentColor: Color =
            MaterialTheme.colorScheme.onError,
    ) = AdaptiveNavigationBarColors(
        containerColor = containerColor,
        indicatorColor = indicatorColor,
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
        badgeContainerColor = badgeContainerColor,
        badgeContentColor = badgeContentColor
    )
}