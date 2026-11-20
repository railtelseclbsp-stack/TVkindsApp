# ABC Fun Learning – Google TV / Android TV

## Features
- A–Z learning using a USB/Bluetooth keyboard or on-screen buttons.
- Large word and emoji illustration, English speech and Hindi meaning.
- Number buttons 1–100; keyboard number keys 0–9.
- TV launcher entry (`LEANBACK_LAUNCHER`) and normal Android launcher entry.

## Open and build
1. Install Android Studio (Giraffe or newer) and Android SDK Platform 35.
2. Open this project folder in Android Studio.
3. Let Gradle sync finish. Use JDK 17 (Android Studio bundled JDK is recommended).
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Install on Google TV
- Enable Developer options and USB debugging / network debugging on the TV.
- Install the APK using Android Studio, `adb install app-debug.apk`, or a trusted sideloading method.
- Connect a USB or Bluetooth keyboard. The TV remote can navigate on-screen letter/number buttons.

## Notes
- Speech uses Android TextToSpeech. Install/download English and Hindi voice data in the TV's Text-to-Speech settings if speech is silent or a language is unavailable.
- This starter app uses emoji illustrations so it works offline and does not need image downloads. Emoji appearance depends on the TV's installed font.
- Hindi number names are fully supplied for 1–20 and round tens; other compound numbers currently show digits in the Hindi line. This can be extended.
- This is a starter project, not a Play Store-certified release. Test on the exact TV model and remote before publishing.
