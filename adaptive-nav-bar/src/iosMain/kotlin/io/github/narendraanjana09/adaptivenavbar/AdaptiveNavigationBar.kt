package io.github.narendraanjana09.adaptivenavbar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import kotlinx.cinterop.useContents
import org.jetbrains.compose.resources.painterResource
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSProcessInfo
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.kCACornerCurveContinuous
import platform.UIKit.*
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject

@OptIn(ExperimentalForeignApi::class)
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
    val isiOS26OrAbove =
        NSProcessInfo.processInfo
            .operatingSystemVersion
            .useContents {
                majorVersion >= 26
            }

    if (isiOS26OrAbove) {
        IosNavBarVersion26(
            items = items,
            iosFab = iosFab,
            selectedIndex = selectedIndex,
            colors = colors,
            onItemSelected = onItemSelected,
            onIosFabClick = {
                onIosFabClick()
                println("On Fab Click")
            }
        )
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            iosFab?.let { fabItem ->
                ExtendedFloatingActionButton(
                    modifier = Modifier.animateContentSize()
                        .padding(end = 32.dp, bottom = 24.dp),
                    onClick = onIosFabClick,
                    icon = {
                        Icon(
                            painter = painterResource(fabItem.icon),
                            contentDescription = fabItem.contentDescription,
                        )
                    },
                    text = {
                        Text(text = fabItem.title)
                    },
                    expanded = fabItem.showLabel,
                    containerColor = fabItem.containerColor,
                    contentColor = fabItem.contentColor,
                )
            }
            ComposeNavigationBar(
                windowInsets = windowInsets,
                colors = colors,
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        }
    }
}

@OptIn(
    ExperimentalForeignApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun IosNavBarVersion26(
    items: List<NavigationItem>,
    iosFab: IosFabItem?,
    selectedIndex: Int,
    colors: AdaptiveNavigationBarColors,
    onItemSelected: (Int) -> Unit,
    onIosFabClick: () -> Unit
) {
    val screenWidth =
        UIScreen.mainScreen.bounds.useContents { size.width }

    val tabBarHeight = 68.0
    val fabSize = 64.0
    val endPadding = 20.0
    val fabTabGap = 8.0
    val topPadding = 16.0
    val bottomPadding = 20.0

    val normalRootHeight = tabBarHeight + (bottomPadding * 2)

    val fabAboveRootHeight =
        topPadding + fabSize + fabTabGap + tabBarHeight + bottomPadding

    val fabAbove = iosFab != null && items.size > 3
    val rootHeightPx = if (fabAbove) fabAboveRootHeight else normalRootHeight
    val rootHeightDp = rootHeightPx.dp

    UIKitView(
        modifier = Modifier
            .fillMaxWidth()
            .height(rootHeightDp),

        properties = UIKitInteropProperties(
            placedAsOverlay = true,
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        ),

        factory = {
            val rootView = UIView().apply {
                backgroundColor = UIColor.clearColor
                clipsToBounds = false
                layer.masksToBounds = false
            }

            val tabBarY = rootHeightPx - bottomPadding - tabBarHeight
            val fabY = if (fabAbove) tabBarY - fabTabGap - fabSize else tabBarY - 2
            val fabX = screenWidth - endPadding - fabSize

            val tabItemWidth = when {
                items.size > 3 -> 106.0
                else -> {
                    val maxWidth = 140.0
                    val minWidth = 120.0
                    val step = (maxWidth - minWidth) / (3 - 1).toDouble()
                    maxWidth - ((items.size - 1) * step)
                }
            }

            val tabBar = UITabBar().apply {

                translucent = true
                opaque = false
                backgroundImage = UIImage()
                shadowImage = UIImage()
                backgroundColor = UIColor.clearColor
                clipsToBounds = false
                layer.masksToBounds = false
                layer.cornerRadius = 30.0
                layer.cornerCurve = kCACornerCurveContinuous

                val appearance = UITabBarAppearance().apply {

                    configureWithTransparentBackground()

                    backgroundEffect =
                        UIBlurEffect.effectWithStyle(
                            UIBlurEffectStyle.UIBlurEffectStyleSystemUltraThinMaterial
                        )

                    backgroundColor = colors.containerColor
                        .toUIColor()
                        .colorWithAlphaComponent(0.72)

                    shadowColor = UIColor.clearColor

                    stackedLayoutAppearance.selected.titleTextAttributes =
                        mapOf(
                            NSForegroundColorAttributeName to colors.selectedTextColor.toUIColor()
                        )

                    stackedLayoutAppearance.selected.iconColor =
                        colors.selectedIconColor.toUIColor()

                    stackedLayoutAppearance.normal.iconColor =
                        colors.unselectedIconColor.toUIColor()

                    stackedLayoutAppearance.selected.badgeBackgroundColor =
                        colors.badgeContainerColor.toUIColor()

                    itemPositioning = UITabBarItemPositioning.UITabBarItemPositioningCentered
                }

                standardAppearance = appearance
                scrollEdgeAppearance = appearance

                val nativeItems = items.mapIndexed { index, item ->

                    val normalImage =
                        (
                                UIImage.systemImageNamed(item.systemIcon)
                                    ?: UIImage.imageNamed(item.systemIcon)
                                )?.imageWithRenderingMode(
                                UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate
                            )

                    val selectedImage =
                        (
                                item.selectedSystemIcon?.let {
                                    UIImage.systemImageNamed(it) ?: UIImage.imageNamed(it)
                                } ?: normalImage
                                )?.imageWithRenderingMode(
                                UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate
                            )

                    UITabBarItem(
                        title = if (item.showLabel) item.title else null,
                        image = normalImage,
                        selectedImage = selectedImage,
                    ).apply {
                        tag = index.toLong()
                        itemWidth = tabItemWidth
                        enabled = item.enabled
                        accessibilityLabel = item.contentDescription ?: item.title
                        badgeValue = item.badge
                        badgeColor = colors.badgeContainerColor.toUIColor()
                        setBadgeTextAttributes(
                            mapOf(
                                NSForegroundColorAttributeName to
                                        colors.badgeContentColor.toUIColor()
                            ),
                            UIControlStateNormal
                        )
                    }
                }

                setItems(nativeItems, animated = false)
                selectedItem = nativeItems.getOrNull(selectedIndex)
            }

            tabBar.layoutMargins = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)

            rootView.setFrame(CGRectMake(0.0, 0.0, screenWidth, rootHeightPx))

            if (iosFab != null && !fabAbove) {
                val maxTabBarWidth = fabX - endPadding
                val tabBarWidth = (items.size * tabItemWidth)
                    .coerceAtMost(maxTabBarWidth)
                tabBar.setFrame(
                    CGRectMake(
                        0.0,
                        tabBarY,
                        tabBarWidth,
                        tabBarHeight
                    )
                )
            } else {
                tabBar.setFrame(
                    CGRectMake(
                        0.0,
                        tabBarY,
                        screenWidth,
                        tabBarHeight
                    )
                )
            }


            val delegate = TabBarDelegate(onItemSelected)
            tabBar.delegate = delegate
            objc_setAssociatedObject(
                rootView,
                "tabBarDelegate".cstr,
                delegate,
                OBJC_ASSOCIATION_RETAIN_NONATOMIC
            )
            rootView.addSubview(tabBar)

            iosFab?.let {

                // ── Glass container so tab bar + FAB blend at their edges ──────────
                val glassContainer = UIVisualEffectView(
                    effect = UIGlassContainerEffect()
                ).apply {
                    backgroundColor = UIColor.clearColor
                    clipsToBounds = false
                    layer.masksToBounds = false
                    setFrame(CGRectMake(fabX, fabY, fabSize, fabSize))
                    userInteractionEnabled = true
                }
                rootView.addSubview(glassContainer)
                val glassEffect = UIGlassEffect().apply {
                    tintColor = iosFab.containerColor.toUIColor().colorWithAlphaComponent(0.72)
                    interactive = true
                }

                val fabEffectView = UIVisualEffectView(effect = glassEffect).apply {
                    setFrame(CGRectMake(0.0, 0.0, fabSize, fabSize))  // relative to glassContainer
                    layer.cornerRadius = fabSize / 2.0
                    layer.cornerCurve = kCACornerCurveContinuous
                    clipsToBounds = true
                }

                val fabIcon =
                    (
                            UIImage.systemImageNamed(iosFab.systemIcon)
                                ?: UIImage.imageNamed(iosFab.systemIcon)
                            )?.imageWithRenderingMode(
                            UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate
                        )

                val iconView = UIImageView(image = fabIcon).apply {
                    tintColor = iosFab.contentColor.toUIColor()
                    contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
                    val iconSize = 24.0
                    val inset = (fabSize - iconSize) / 2.0
                    setFrame(CGRectMake(inset, inset, iconSize, iconSize))
                }
                fabEffectView.contentView.addSubview(iconView)

                val tapButton = UIButton.buttonWithType(UIButtonTypeSystem) as UIButton
                tapButton.setFrame(CGRectMake(0.0, 0.0, fabSize, fabSize))
                tapButton.backgroundColor = UIColor.clearColor
                tapButton.accessibilityLabel = iosFab.contentDescription

                val fabHandler = FabHandler(onIosFabClick)
                tapButton.addTarget(
                    fabHandler,
                    NSSelectorFromString("onFabTap"),
                    UIControlEventTouchUpInside
                )
                objc_setAssociatedObject(
                    rootView,
                    "fabHandler".cstr,
                    fabHandler,
                    OBJC_ASSOCIATION_RETAIN_NONATOMIC
                )

                fabEffectView.contentView.addSubview(tapButton)
                glassContainer.contentView.addSubview(fabEffectView)
            }
            rootView
        },

        update = { rootView ->
            val tabBar =
                rootView.subviews.firstOrNull { it is UITabBar } as? UITabBar
                    ?: return@UIKitView

            val current = tabBar.items?.getOrNull(selectedIndex)
            if (tabBar.selectedItem != current) {
                tabBar.selectedItem = current as UITabBarItem?
            }
        }
    )
}

private class FabHandler(val onClick: () -> Unit) : NSObject() {
    @ObjCAction
    fun onFabTap() {
        onClick()
    }
}

fun Color.toUIColor(): UIColor =
    UIColor(
        red = red.toDouble(),
        green = green.toDouble(),
        blue = blue.toDouble(),
        alpha = alpha.toDouble()
    )

private class TabBarDelegate(
    val onItemSelected: (Int) -> Unit
) : NSObject(), UITabBarDelegateProtocol {

    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}