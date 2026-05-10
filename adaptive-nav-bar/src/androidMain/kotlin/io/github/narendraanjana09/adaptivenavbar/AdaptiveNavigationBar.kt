package io.github.narendraanjana09.adaptivenavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable

@Composable
actual fun AdaptiveNavigationBar(
    items: List<NavigationItem>,
    iosFab: IosFabItem?,
    selectedIndex: Int,
    windowInsets: WindowInsets,
    colors: AdaptiveNavigationBarColors,
    onItemSelected: (Int) -> Unit,
    onIosFabClick: () -> Unit
) {
    ComposeNavigationBar(
        windowInsets = windowInsets,
        colors = colors,
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected
    )
}