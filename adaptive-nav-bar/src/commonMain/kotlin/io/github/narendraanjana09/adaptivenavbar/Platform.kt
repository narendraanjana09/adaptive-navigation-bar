// commonMain

package io.github.narendraanjana09.adaptivenavbar

/**
 * Represents the current running platform.
 */
enum class Platform {
    Android,
    IOS
}

/**
 * Returns the current platform at runtime.
 */
expect fun getPlatform(): Platform