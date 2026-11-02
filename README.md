# UHC — Uganda Health Connect

A complete starter Android Studio project built with Java, XML layouts, and AndroidX Fragments.

## Features
- Patient list with sample fictional records
- Patient detail screen with demographic details, medical history, and allergies
- Phone navigation with Back to patients
- Tablet two-panel layout using `res/layout-sw600dp/activity_main.xml`
- Blue UHC header and card-based interface

## Open the project
1. Extract this ZIP file.
2. In Android Studio, choose **Open** and select the extracted `UHC_Complete_Android_Project` folder (the folder containing `settings.gradle`).
3. Allow Gradle sync to finish.
4. Run the `app` configuration on an Android phone emulator or device.

## Package
The package and namespace are `com.example.uhc`.

## Notes
- The patient data is fictional demonstration data stored in `PatientListFragment.java`.
- This starter project does not connect to a real hospital database.
- Android Studio may ask to install Android SDK 35 or a compatible Gradle JDK. Use JDK 17 for this Gradle/Android Gradle Plugin setup.
- A Gradle wrapper is not included. Android Studio can configure/use a compatible Gradle installation when opening the project.
