# Color Time Pass

[![Minimum API Level](https://img.shields.io/badge/Min%20API%20Level-14-green)](app/build.gradle.kts)
[![Target API Level](https://img.shields.io/badge/Target%20API%20Level-35-orange)](app/build.gradle.kts)
[![Releases](https://img.shields.io/github/release/Appliberated/ColorTimePass-Android.svg)](https://github.com/Appliberated/ColorTimePass-Android/releases/latest)
[![GitHub repo size](https://img.shields.io/github/repo-size/Appliberated/ColorTimePass-Android)](https://github.com/Appliberated/ColorTimePass-Android)
[![License](https://img.shields.io/npm/l/express.svg)](LICENSE)
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Appliberated/ColorTimePass-Android/total)](https://github.com/Appliberated/ColorTimePass-Android/releases)

## Download

You can download the latest version from the [GitHub Releases](https://github.com/Appliberated/ColorTimePass-Android/releases/latest) page. This app is not available on the Google Play Store. You will download an `.apk` file that can be installed directly on your Android device.

<a href="https://github.com/Appliberated/ColorTimePass-Android/releases/latest">
  <img height="24px" alt="Static Badge" src="https://img.shields.io/badge/Download-from_GitHub_Releases-brightgreen?style=plastic&logo=github&color=%23181717">
</a>

## Building from Source

If you'd like to build the app yourself:

1.  Clone the repository: `git clone https://github.com/Appliberated/ColorTimePass-Android.git`

2.  Open the project in Android Studio.

3.  Let Gradle sync the project.

4.  Click `Run 'app'`.

The project is configured to work with a minimum API level of 14 (Android 4 Ice Cream Sandwich).

## Technical Notes

This project is intentionally kept simple as a learning example. It is written in [Java](app/src/main/java/com/appliberated/colortimepass/ColorTimePassActivity.java) and uses the classic, plain Android SDK (`android.app.Activity`, `android.widget.TextView`, etc.) without any modern `androidx` libraries or Jetpack Compose. In fact, the `dependencies` section in the [build file](app/build.gradle.kts) is empty.

This direct-to-the-SDK approach is the main reason the final `.apk` is only 93 KB. While this is not the recommended architecture for complex, modern applications, it serves as a clear example of a minimal, dependency-free Android app. The original code was written in 2016 and has been maintained to run on the latest Android versions while preserving this original, lightweight approach.

## Contributing

Thank you for contributing! Color Time Pass is designed to be as simple as possible, but you can still help with:

* testing and reporting bugs
* creating a nice Android app icon to replace the [current one](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

## Support my work

Color Time Pass is a free app, and I enjoy creating and sharing learning projects like this, along with other useful [free apps](https://www.appliberated.com/). If you'd like to support my work, consider checking out the following Pro applications:

<a href="https://www.tecdrop.com/apps/pro/"><img width="300" src="repo-assets/tecdrop-pro-apps.png" alt="Tecdrop Pro Apps" /></a>

[Pitch Black Wallpaper Pro](https://www.tecdrop.com/pitchblackwallpaperpro/) | [RGB Color Wallpaper Pro](https://www.tecdrop.com/rgbcolorwallpaperpro/) |
| :--- | :--- |
| <li>Pitch black & dark wallpapers</li><li>AI-generated wallpapers</li><li>Enhances battery life</li><li>Reduces eyestrain</li> | <li>Set any color as wallpaper</li><li>Endless color options, built-in & custom</li><li>Reduce visual clutter</li><li>Enhanced device aesthetics</li> |

## License

Color Time Pass is released under the [MIT License](LICENSE).
