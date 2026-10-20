# Dark Souls Android Launcher & Container

An Android container launcher and touch HUD interface for running and configuring Dark Souls with hardware profiles (Mali Helio G99 / Qualcomm Snapdragon Turnip), custom on-screen controls, graphics pipeline settings, and save file management.

## Features
- **GPU Driver Profiles**: Profiles for ARM Mali (Helio G99 smooth pipeline) and Qualcomm Snapdragon (Adreno Turnip 60 FPS).
- **Custom Touch Overlay HUD**: Fully customizable touch controls with opacity, sensitivity, and layout adjustments.
- **Save File Manager**: Backup and restore game save states.
- **Automated CI/CD**: Automatic APK build and GitHub Releases artifact generation on push via GitHub Actions.

## Building the APK
To build the debug APK locally:
```bash
./gradlew assembleDebug
```
The resulting APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`
