package io.github.codefarmer.adaptivenavigationbar

import adaptive_navigation_bar.composeapp.generated.resources.Res
import adaptive_navigation_bar.composeapp.generated.resources.ic_add
import adaptive_navigation_bar.composeapp.generated.resources.ic_rectangle_grid_3x1
import adaptive_navigation_bar.composeapp.generated.resources.ic_rectangle_grid_3x1_fill
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.narendraanjana09.adaptivenavbar.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.painterResource

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    @SerialName("Overview")
    data object Overview : Screen

    @Serializable
    @SerialName("Transactions")
    data object Transactions : Screen
}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Overview::class, Screen.Overview.serializer())
            subclass(Screen.Transactions::class, Screen.Transactions.serializer())
        }
    }
}

private val allNavItems = listOf(
    Triple("Overview", "rectangle.grid.3x1", "rectangle.grid.3x1.fill"),
    Triple("Transactions", "clock", "clock.fill"),
    Triple("Overview", "rectangle.grid.3x1", "rectangle.grid.3x1.fill"),
//    Triple("Transactions", "clock", "clock.fill"),
//    Triple("Overview", "rectangle.grid.3x1", "rectangle.grid.3x1.fill"),
)

@Composable
fun ScaffoldSample() {
    val backStack = rememberNavBackStack(
        configuration = config,
        elements = arrayOf(Screen.Overview)
    )


    val navItems = remember {
        allNavItems.map { (title, icon, selectedIcon) ->
            NavigationItem(
                title = title,
                icon = Res.drawable.ic_rectangle_grid_3x1,
                selectedIcon = Res.drawable.ic_rectangle_grid_3x1_fill,
                systemIcon = icon,
                selectedSystemIcon = selectedIcon,
            )
        }
    }

    val iosFab = IosFabItem(
        icon = Res.drawable.ic_add,
        systemIcon = "plus",
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary,
        showLabel = false
    )

    Scaffold(
        floatingActionButton = {
            if (getPlatform() == Platform.Android) {
                ExtendedFloatingActionButton(
                    onClick = { },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription = null,
                        )
                    },
                    text = { Text(text = "Add Title") },
                    expanded = false
                )
            }
        },
        bottomBar = {
            AdaptiveNavigationBar(
                items = navItems,
                iosFab = iosFab,
                selectedIndex = getScreenIndex(backStack.last() as Screen),
                onItemSelected = { index ->
                    val newScreen = when (index) {
                        0 -> Screen.Overview
                        else -> Screen.Transactions
                    }
                    if (backStack.last() != newScreen) {
                        if (backStack.contains(newScreen)) backStack.remove(newScreen)
                        backStack.add(newScreen)
                    }
                },
                onIosFabClick = { },
                colors = AdaptiveNavigationBarDefaults.colors(
                    selectedTextColor = Color.White
                )
            )
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                entry<Screen.Overview> {
                    OverviewScreen()
                }
                entry<Screen.Transactions> {
                    HistoryScreen()
                }
            },
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            popTransitionSpec = { fadeIn() togetherWith fadeOut() },
            predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() }
        )
    }
}

private fun getScreenIndex(currentScreen: Screen): Int = when (currentScreen) {
    is Screen.Overview -> 0
    is Screen.Transactions -> 1
}

@Composable
fun BaseDashboardScreen(
    title: String
) {
    val randomColors = remember {
        List(30) {
            Color(
                red = (0..255).random() / 255f,
                green = (0..255).random() / 255f,
                blue = (0..255).random() / 255f,
                alpha = 1f
            )
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
            bottom = WindowInsets.navigationBars.asPaddingValues()
                .calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        ),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
            }
        }

        itemsIndexed((1..50).toList()) { index, _ ->
            val height = remember(index) { (120..300).random().dp }
            val color = randomColors[index % randomColors.size]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color)
            )
        }
    }
}

@Composable
fun OverviewScreen() {
    BaseDashboardScreen(
        title = "Overview Dashboard"
    )
}

@Composable
fun HistoryScreen(
) {
    BaseDashboardScreen(
        title = "Transactions Dashboard"
    )
}