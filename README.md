### Steam Companion (Kotlin Multiplatform + Compose Multiplatform)

This repository contains a minimal Kotlin Multiplatform app targeting Android and iOS using Compose Multiplatform. It integrates the real Steam Web API with API-key login and implements the core flows requested:

- API key login + SteamID64 or vanity URL input (vanity is resolved to SteamID64)
- Home screen with bottom navigation (Home, Profile)
- Profile screen: shows your Steam avatar, display name, Steam level, and the 3 most recently played games
- Tap a game to view its achievements list and the overall completion percentage
- Architecture: three layers (data, domain, presentation)

Future features (scaffolding to be added later): market browsing, wishlists, notifications, friends tab, standard Steam login.

Requirements
- JDK 17+
- Android Studio Ladybug+ or IntelliJ IDEA with KMP + Compose support
- Xcode 15+ for iOS
- A Steam Web API key: https://steamcommunity.com/dev/apikey

Run on Android
1. Open the project in Android Studio.
2. Create a Run configuration for the `composeApp` Android application module.
3. Run on a device/emulator. Enter your API key and your SteamID64 or vanity name, then Login.

Run on iOS
1. Open the project in Android Studio or IntelliJ IDEA.
2. From Gradle tool window, sync the project. Use the KMP iOS run configuration (Compose Multiplatform automatically creates a sample iOS app). Alternatively, integrate this UIViewController (`MainViewController`) in a simple SwiftUI wrapper in an Xcode project.

Notes
- Only API-key login is implemented right now, per request. Public-profile login without a key and market features are TODO.
- The design loosely follows the provided Dribbble mock with rounded cards and animated size changes. You can extend styles to fully match the mock.
- Images: for simplicity, this minimal version uses placeholder boxes with a `shimmer()` Modifier. You may hook in an image loader like Kamel or Coil (Android) for real images using `steamCdnHeaderImage`/`steamCdnIcon` helpers.

Project structure
- composeApp/src/commonMain
  - data/steam: SteamApi (Ktor client), repository
  - domain: models and use-cases
  - presentation/navigation: simple navigation (RootNav + Screen sealed interface)
  - presentation/ui/components: reusable UI components (Avatar, GameRow, shimmer)
  - presentation/ui/feature: feature screens split by feature (auth/LoginScreen, main/MainShell, home/HomeScreen, profile/ProfileScreen, achievements/AchievementsScreen)
- androidMain: Android manifest and MainActivity
- iosMain: `MainViewController` for iOS Compose embedding

Disclaimer
This project uses the Steam Web API. Respect Valve's terms of use. Some endpoints may require the user profile to be public to retrieve data like owned games or achievements.

### Steam Companion (Kotlin Multiplatform + Compose Multiplatform)

This repository contains a minimal Kotlin Multiplatform app targeting Android and iOS using Compose Multiplatform. It integrates the real Steam Web API with API-key login and implements the core flows requested:

- API key login + SteamID64 or vanity URL input (vanity is resolved to SteamID64)
- Home screen with bottom navigation (Home, Profile)
- Profile screen: shows your Steam avatar, display name, Steam level, and the 3 most recently played games
- Tap a game to view its achievements list and the overall completion percentage
- Architecture: three layers (data, domain, presentation)

Future features (scaffolding to be added later): market browsing, wishlists, notifications, friends tab, standard Steam login.

Requirements
- JDK 17+
- Android Studio Ladybug+ or IntelliJ IDEA with KMP + Compose support
- Xcode 15+ for iOS
- Android SDK installed and configured (see Setup: Android SDK below)
- A Steam Web API key: https://steamcommunity.com/dev/apikey

Setup: Android SDK
If you see an error like "SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file", configure your Android SDK path:

- Option A: local.properties (recommended)
  1) Copy local.properties.example to local.properties at the project root.
  2) Edit local.properties and set sdk.dir to your SDK path.
     Typical locations:
     - macOS: /Users/<your-username>/Library/Android/sdk
     - Linux: /home/<your-username>/Android/Sdk
     - Windows: C:\\Users\\<your-username>\\AppData\\Local\\Android\\Sdk

- Option B: Environment variable
  Set ANDROID_HOME or ANDROID_SDK_ROOT to your SDK path and restart the IDE.

Run on Android
1. Ensure the Android SDK is configured (see above). If needed, create local.properties with the correct sdk.dir.
2. Open the project in Android Studio.
3. Create a Run configuration for the `composeApp` Android application module.
4. Run on a device/emulator. Enter your API key and your SteamID64 or vanity name, then Login.

Run on iOS
1. Open the project in Android Studio or IntelliJ IDEA.
2. From Gradle tool window, sync the project. Use the KMP iOS run configuration (Compose Multiplatform automatically creates a sample iOS app). Alternatively, integrate this UIViewController (`MainViewController`) in a simple SwiftUI wrapper in an Xcode project.

Notes
- Only API-key login is implemented right now, per request. Public-profile login without a key and market features are TODO.
- The design loosely follows the provided Dribbble mock with rounded cards and animated size changes. You can extend styles to fully match the mock.
- Images: for simplicity, this minimal version uses placeholder boxes with a `shimmer()` Modifier. You may hook in an image loader like Kamel or Coil (Android) for real images using `steamCdnHeaderImage`/`steamCdnIcon` helpers.

Project structure
- composeApp/src/commonMain
  - data/steam: SteamApi (Ktor client), repository
  - domain: models and use-cases
  - presentation/navigation: simple navigation (RootNav + Screen sealed interface)
  - presentation/ui/components: reusable UI components (Avatar, GameRow, shimmer)
  - presentation/ui/feature: feature screens split by feature (auth/LoginScreen, main/MainShell, home/HomeScreen, profile/ProfileScreen, achievements/AchievementsScreen)
- androidMain: Android manifest and MainActivity
- iosMain: `MainViewController` for iOS Compose embedding

Disclaimer
This project uses the Steam Web API. Respect Valve's terms of use. Some endpoints may require the user profile to be public to retrieve data like owned games or achievements.
