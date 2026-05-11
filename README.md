# AdaptiveNavBar

A Compose Multiplatform library that renders a platform-native navigation bar — Material 3 `NavigationBar` on Android, and a glassy floating bar with an optional FAB on iOS.

---

## Installation

```kotlin
implementation("io.github.narendraanjana09:adaptive-nav-bar:1.0.0")
```

---

## Usage

```kotlin
val navItems = remember {
    listOf(
        NavigationItem(
            title = "Overview",
            icon = Res.drawable.ic_overview,              // Compose resource — Android
            selectedIcon = Res.drawable.ic_overview_fill,
            systemIcon = "rectangle.grid.3x1",            // SF Symbol OR Xcode asset name — iOS
            selectedSystemIcon = "rectangle.grid.3x1.fill",
        ),
        NavigationItem(
            title = "Transactions",
            icon = Res.drawable.ic_clock,
            selectedIcon = Res.drawable.ic_clock_fill,
            systemIcon = "clock",
            selectedSystemIcon = "clock.fill",
        )
    )
}

val iosFab = IosFabItem(
    icon = Res.drawable.ic_add,
    systemIcon = "plus",                                  // SF Symbol OR Xcode asset name — iOS
    contentColor = MaterialTheme.colorScheme.onPrimary,
    containerColor = MaterialTheme.colorScheme.primary,
    showLabel = false
)

Scaffold(
    // Android FAB — use Scaffold's floatingActionButton slot
    // On iOS, FAB is handled inside AdaptiveNavigationBar via iosFab
    floatingActionButton = {
        if (getPlatform() == Platform.Android) {
            ExtendedFloatingActionButton(
                onClick = { },
                icon = { Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null) },
                text = { Text("Add") },
                expanded = false
            )
        }
    },
    bottomBar = {
        AdaptiveNavigationBar(
            items = navItems,
            iosFab = iosFab,
            selectedIndex = selectedIndex,
            onItemSelected = { index -> /* handle tab switch */ },
            onIosFabClick = { /* handle FAB tap — iOS only */ }
        )
    }
)
```

> **FAB behaviour by platform**
> - **Android** — use Scaffold's `floatingActionButton` slot with a `getPlatform() == Platform.Android` guard. `AdaptiveNavigationBar` renders a standard M3 `NavigationBar` with no FAB.
> - **iOS** — pass an `IosFabItem` to `iosFab`. The glassy navbar renders the FAB inline. Do not add a separate FAB on iOS.

---

## API

### `NavigationItem`

| Parameter | Type | Default | Description |
|---|---|---|---|
| `title` | `String` | — | Label shown under the icon |
| `icon` | `DrawableResource` | — | Unselected icon — used on Android |
| `selectedIcon` | `DrawableResource?` | `null` | Selected icon — used on Android |
| `systemIcon` | `String` | — | SF Symbol name **or** Xcode asset catalog name — used on iOS |
| `selectedSystemIcon` | `String?` | `null` | Selected SF Symbol name **or** Xcode asset catalog name — used on iOS |
| `showLabel` | `Boolean` | `true` | Whether to show the title label |
| `badge` | `String?` | `null` | Badge text (e.g. `"3"` or `"99+"`) |
| `showBadgeDot` | `Boolean` | `false` | Show a dot badge — Android only, use `.badge` SF Symbol variant on iOS |
| `enabled` | `Boolean` | `true` | Whether the item is interactive |
| `contentDescription` | `String?` | `null` | Accessibility description |

### `IosFabItem`

| Parameter | Type | Default | Description |
|---|---|---|---|
| `title` | `String` | `""` | FAB label |
| `showLabel` | `Boolean` | `false` | Whether to show the label |
| `icon` | `DrawableResource` | — | FAB icon — used on Android |
| `systemIcon` | `String` | — | SF Symbol name **or** Xcode asset catalog name — used on iOS |
| `contentColor` | `Color` | — | Icon tint color |
| `containerColor` | `Color` | — | FAB background color |
| `contentDescription` | `String?` | `null` | Accessibility description |

### `AdaptiveNavigationBar`

| Parameter | Type | Description |
|---|---|---|
| `items` | `List<NavigationItem>` | Navigation destinations |
| `iosFab` | `IosFabItem?` | FAB shown inside the navbar — iOS only |
| `selectedIndex` | `Int` | Currently active tab index |
| `onItemSelected` | `(Int) -> Unit` | Called when a tab is tapped |
| `onIosFabClick` | `() -> Unit` | Called when the iOS FAB is tapped |
| `colors` | `AdaptiveNavigationBarColors` | Color overrides — see `AdaptiveNavigationBarDefaults.colors()` |

### `AdaptiveNavigationBarDefaults.colors()`

All colors default to your active MaterialTheme. Override any of them:

```kotlin
AdaptiveNavigationBar(
    colors = AdaptiveNavigationBarDefaults.colors(
        containerColor = Color.Transparent,
        selectedIconColor = Color.Red,
    ),
    ...
)
```

| Parameter | Default |
|---|---|
| `containerColor` | `MaterialTheme.colorScheme.surfaceContainer` |
| `indicatorColor` | `MaterialTheme.colorScheme.primaryContainer` |
| `selectedIconColor` | `MaterialTheme.colorScheme.primary` |
| `selectedTextColor` | `MaterialTheme.colorScheme.primary` |
| `unselectedIconColor` | `MaterialTheme.colorScheme.onSurfaceVariant` |
| `unselectedTextColor` | `MaterialTheme.colorScheme.onSurfaceVariant` |
| `badgeContainerColor` | `MaterialTheme.colorScheme.error` |
| `badgeContentColor` | `MaterialTheme.colorScheme.onError` |

---

## Platform behaviour

| | Android | iOS |
|---|---|---|
| Nav bar | Material 3 `NavigationBar` | Glassy floating bar |
| Icons | Compose `DrawableResource` | SF Symbol name or Xcode asset catalog name |
| FAB | Scaffold's `floatingActionButton` slot | `IosFabItem` inside `AdaptiveNavigationBar` |
| Badge dot | `showBadgeDot = true` | Use `.badge` SF Symbol variant |

---

## License

```
Copyright 2026 Narendra Singh Anjana

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```