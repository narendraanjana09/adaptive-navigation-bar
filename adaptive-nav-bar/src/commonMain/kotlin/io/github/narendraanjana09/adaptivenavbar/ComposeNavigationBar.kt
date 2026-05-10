package io.github.narendraanjana09.adaptivenavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposeNavigationBar(
    windowInsets: WindowInsets,
    colors: AdaptiveNavigationBarColors,
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        windowInsets = windowInsets,
        containerColor = colors.containerColor,
    ) {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.selectedIconColor,
                    selectedTextColor = colors.selectedTextColor,
                    unselectedIconColor = colors.unselectedIconColor,
                    unselectedTextColor = colors.unselectedTextColor,
                    indicatorColor = colors.indicatorColor
                ),
                selected = selected,
                enabled = item.enabled,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            when {
                                item.badge != null -> {
                                    Badge(
                                        modifier = Modifier.offset(
                                            x = 8.dp,
                                            y = (-2).dp
                                        ),
                                        containerColor = colors.badgeContainerColor,
                                        contentColor = colors.badgeContentColor
                                    ) {
                                        Text(item.badge)
                                    }
                                }

                                item.showBadgeDot -> {
                                    Badge(
                                        modifier = Modifier.offset(
                                            x = 4.dp,
                                            y = (-2).dp
                                        ),
                                        containerColor = colors.badgeContainerColor,
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter =
                                painterResource(
                                    when {
                                        selected && item.selectedIcon != null ->
                                            item.selectedIcon

                                        else ->
                                            item.icon
                                    }
                                ),
                            contentDescription =
                                item.contentDescription ?: item.title
                        )
                    }
                },
                label = {
                    if (item.showLabel) {
                        Text(item.title)
                    }
                },
                alwaysShowLabel = item.showLabel
            )
        }
    }
}