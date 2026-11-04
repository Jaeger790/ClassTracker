# Class Tracker

Android coursework app for tracking terms, courses, instructors and assessments. Built with Java, Android views and Room.

## Status

Coursework project. The entry screen opens the term list; it does not authenticate a user. The current Android Gradle plugin is 7.4.2, with compile/target SDK 33 and minimum SDK 26.

## Build

Open the repository in Android Studio with a compatible JDK and Android SDK. Let Gradle sync, then run the app on an emulator or Android device. From a configured shell:

```sh
./gradlew assembleDebug
```

Windows: use `gradlew.bat assembleDebug`. Configure the SDK path locally; do not commit `local.properties`.

## Layout

- `app/src/main/java/tech/school/schoolapp/activity/`: screens.
- `DAO/`, `model/`, `adapter/` and `viewmodel/` under the same package: Room storage and display models.
- `app/src/main/res/`: layouts, themes and icons.
- `app/src/test/` and `app/src/androidTest/`: existing example tests.

## Maintenance

Build caches, local IDE settings, APK output and the signing keystore are removed from the tracked tree in this cleanup. The previously committed signing key remains in history. Replace it before using it for new releases; follow the app store's key-change process if it was used for a published app. The existing example tests do not establish coverage of the app's workflows.
## Related projects

See the [project index](https://github.com/Jaeger790#projects) for the rest of my work.
