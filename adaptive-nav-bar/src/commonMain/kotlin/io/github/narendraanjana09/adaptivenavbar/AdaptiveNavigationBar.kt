package io.github.narendraanjana09.adaptivenavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.runtime.Composable

/**
 * @param items List of navigation items displayed in the navigation bar.
 * @param iosFab Optional floating action button item shown only on iOS for android use native with platform conditions.
 * @param selectedIndex Currently selected navigation item index.
 * @param windowInsets Insets applied to the navigation bar.
 * @param colors Colors used for icons, labels, backgrounds, and selection states.
 * @param onItemSelected Callback triggered when a navigation item is selected.
 * @param onIosFabClick Callback triggered when the iOS floating action button is clicked.

 */
@Composable
expect fun AdaptiveNavigationBar(
    items: List<NavigationItem>,
    iosFab: IosFabItem? = null,
    selectedIndex: Int,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    colors: AdaptiveNavigationBarColors = AdaptiveNavigationBarDefaults.colors(),
    onItemSelected: (Int) -> Unit,
    onIosFabClick: () -> Unit = {}
)