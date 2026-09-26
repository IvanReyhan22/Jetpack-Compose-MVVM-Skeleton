# Android Template

A runnable multi-module Compose/MVVM skeleton following Field Officer v2's Land feature architecture. See [AGENTS.md](AGENTS.md) for the module map, data flow, package responsibilities, and feature/backend extension recipes.

- Demo login: `demo@example.com` / `password123`.
- Session persists across relaunches; the signed-in screen supports logout.
- Staging and production both use local demo authentication. No backend credentials are needed.
- Requires Android SDK 37, a compatible JDK (Android Studio's JBR works), and the checked-in Gradle wrapper.

```sh
./gradlew :app:assembleStagingDebug
./gradlew testStagingDebugUnitTest :app:lintStagingDebug
./gradlew :app:connectedStagingDebugAndroidTest
```

Choose `stagingDebug` in Android Studio. Staging package: `id.codemockup.template.staging`. Production package: `id.codemockup.template`. Release APKs are unsigned until project-specific signing is configured.
