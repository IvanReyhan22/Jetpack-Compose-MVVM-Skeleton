# Android Template

A runnable Android skeleton following Field Officer v2's **Land feature architecture**: multi-module Compose UI, MVVM, Hilt, typed navigation, and a repository/use-case pipeline. It contains a local demo login and a minimal authenticated screen with persistent session and logout.

Start here for project orientation. [AGENTS.md](AGENTS.md) defines architecture and editing rules; each module guide explains package ownership.

## Quick start

1. Open the project in Android Studio and select `stagingDebug`.
2. Install Android SDK 37 and use a compatible Gradle JDK. Android Studio's bundled JBR works; shared Java/Kotlin bytecode targets Java 17.
3. Set the SDK location in untracked `local.properties`, then run the checked-in wrapper:

```sh
./gradlew :app:assembleStagingDebug
```

The app supports Android API 29 and newer. No backend, private Maven credentials, or Sentry DSN is required.

**Demo login:** `demo@example.com` / `password123`. Other credentials return an error after a 600 ms simulated request. Both flavors use the demo service. Passwords are never persisted.

## Project structure

```text
app/
  src/main/kotlin/id/codemockup/template/
    TemplateApplication.kt        Hilt application and guarded Sentry setup
    MainActivity.kt               Compose host and global error dialogs
    MainAppState.kt               Initial session state
    MainAppViewModel.kt            Startup, session-expiry handling and retry
    navigation/AppNavHost.kt      Feature graphs and cross-feature transitions
  src/main/res/                   Icons, app label, window theme and backup rules
  src/debug/kotlin/.../diagnostics/ Debug-only Hilt entry point for device tests
  src/test/kotlin/                App state/session tests
  src/androidTest/kotlin/         Login and diagnostics integration tests
core/
  common/                         State wrappers, validation, error/event managers, Sentry
  model/                          Shared User/UserSession application models
  data/                           Request/response DTOs and typed route objects
  network/                        Retrofit/OkHttp, interceptors, services and demo binding
  datastore/                      Preferences DataStore session contract and implementation
  domain/                         Repositories, DataSources, use cases, mappers and DI
  extensions/                     Shared navigation extension
  designsystem/                   Theme, bundled Onest typography, colors and reusable Compose controls
feature/
  login/                          Login state, ViewModel, screen, components and navigation
  main/                           Signed-in state, ViewModel, screen, logout and navigation
build-logic/
  convention/                     Application/library, Compose, Hilt and feature plugins
    src/main/kotlin/              Convention plugin classes
    src/main/kotlin/.../buildlogic/ Shared Android configuration and catalog helpers
  settings.gradle.kts             Included build and shared catalog import
gradle/
  libs.versions.toml              Dependency and plugin versions
  wrapper/                       Gradle wrapper configuration and bootstrap JAR
settings.gradle.kts              Module list, repositories and included build
build.gradle.kts                 Root plugin declarations
gradle.properties                Shared Gradle/Android settings
```

All modules use `src/main/kotlin/id/codemockup/template/...` packages. JVM tests mirror them under `src/test/kotlin`; Android tests live under `src/androidTest/kotlin`. Build outputs and caches (`**/build`, `.gradle`, `.kotlin`) are generated and ignored.

| Module | Main packages and responsibilities | Guide |
| --- | --- | --- |
| `app` | Root application/startup classes; `navigation` composes feature graphs. | [App](app/AGENTS.md) |
| `core:common` | Root state/error types and event managers; `validation`; `utils.sentry`. | [Common](core/common/AGENTS.md) |
| `core:model` | `session`: `User`, `UserSession`. | [Model](core/model/AGENTS.md) |
| `core:data` | `remote.request`, `remote.response.auth`, `remote.routes`; generic response envelope. | [Data](core/data/AGENTS.md) |
| `core:network` | Root interceptors/error mapper; `services`, `demo`, `di`. | [Network](core/network/AGENTS.md) |
| `core:datastore` | `BaseDataStore`, `PreferencesDataStore`, and `di`. | [DataStore](core/datastore/AGENTS.md) |
| `core:domain` | `repository.auth`, `usecase.auth`, `mapper`, `di`. | [Domain](core/domain/AGENTS.md) |
| `core:extensions` | Navigation extensions, including session-boundary back-stack clearing. | [Extensions](core/extensions/AGENTS.md) |
| `core:designsystem` | `theme` and generic `components`. | [Design system](core/designsystem/AGENTS.md) |
| `feature:login` | Screen/state/ViewModel/navigation; `components` contains stateless content and preview. | [Login](feature/login/AGENTS.md) |
| `feature:main` | Signed-in screen/state/ViewModel; `navigations` registers its typed destination. | [Main](feature/main/AGENTS.md) |
| `build-logic` | Convention plugins and shared SDK/flavor/dependency settings. | [Build logic](build-logic/AGENTS.md) / [Conventions](build-logic/convention/AGENTS.md) |

## Module dependencies

```mermaid
flowchart TD
    APP[app] --> FEATURES[feature:login / feature:main]
    APP --> DATA[core:data] & MODEL[core:model] & STORE[core:datastore] & DS[core:designsystem] & EXT[core:extensions] & COMMON[core:common]
    APP -. debug only .-> NETWORK[core:network]
    FEATURES --> DOMAIN[core:domain] & NETWORK & DATA & MODEL & STORE & DS & EXT & COMMON
    DOMAIN --> DATA & MODEL & NETWORK & STORE & COMMON
    NETWORK --> DATA & STORE & COMMON
    STORE --> MODEL & COMMON
    DS --> MODEL & EXT & COMMON
    EXT --> MODEL & COMMON
    DATA --> MODEL
    MODEL --> COMMON
```

The feature convention supplies all eight core modules, matching the reference. This does not authorize bypassing layers: screens and ViewModels call use cases and storage, not service/DataSource implementations. Core modules never depend on features; features do not depend on one another or on app.

`build-logic` is an included Gradle build, not a runtime module. It configures SDK levels, Java 17 bytecode, staging/production flavors, Compose, Hilt/KSP, and feature dependencies. AGP supplies built-in Kotlin compilation; do not add `org.jetbrains.kotlin.android`.

## MVVM and login data flow

```mermaid
flowchart LR
    UI[LoginScreen / LoginContent] --> VM[LoginViewModel]
    VM --> GROUP[AuthUseCase]
    GROUP --> UC[LoginUseCase]
    UC --> REPO[AuthRepository]
    REPO --> SOURCE[AuthDataSource]
    SOURCE --> SERVICE[AuthServices]
    SERVICE --> DEMO[DemoAuthServices by default]
    SERVICE -. backend binding .-> HTTP[Retrofit / OkHttp]
    SERVICE -. Response of LoginResponse .-> UC
    UC -. Flow of UiState .-> VM
    VM -. LoginState with DataState .-> UI
    VM --> STORE[BaseDataStore]
    UI -. navigation callback .-> NAV[AppNavHost]
```

Repository contracts **and their DataSource implementations live in `core:domain`**, matching LandRepository/LandDataSource. `core:data` owns wire DTOs and typed navigation routes. Use cases convert responses/failures into `UiState`; ViewModels reduce these results into immutable feature state and expose read-only StateFlow. Screens collect with `collectAsStateWithLifecycle`.

Login validates input and prevents duplicate submissions. It saves token/user atomically before emitting the signed-in effect. Logout clears session before navigation. Startup waits for persisted session resolution, preventing a login-screen flash. Passwords remain in memory and clear on success; session files are excluded from backup/transfer.

Serializable `Login` and `Main` routes live in `core:data/remote/routes`. Features expose `NavGraphBuilder` registration and `NavController` extensions. App supplies cross-feature callbacks; ViewModels never receive a NavController. Login, logout, and forced expiry clear the previous back stack.

## HTTP errors and expired sessions

OkHttp request order is `ResponseInterceptor`, `DiagnosticsInterceptor` (Chucker), then `SentryBreadcrumbInterceptor`. Responses return in reverse order, allowing Chucker to observe HTTP failures before the outer interceptor closes/converts them. Connect/read/write timeouts are 60 seconds.

Protect a Retrofit operation with the reference marker:

```kotlin
@Headers("@: Auth")
@GET("profile")
suspend fun profile(): Response<ProfileResponse>
```

The interceptor removes `@`, reads the current session, and attaches `Authorization: Bearer ...` only for protected calls. Login is public. It also carries `@Headers("X-Sensitive-Body: true")`, which makes diagnostics skip the entire call and removes that marker before transmission. Mark future credential/token endpoints the same way.

| Response/failure | Behavior |
| --- | --- |
| 2xx | Return body unchanged. |
| 400 | Use nonblank string `data` from reference error envelope; otherwise bad-request fallback. |
| Public 401 | Authentication error; do not clear an existing session. |
| Protected 401 with token | Atomically clear only the matching session, retain an expiry event, then return to login with a notice and cleared back stack. |
| 5xx | Use nonblank string `message`; otherwise server-error fallback. |
| Other unsuccessful status | Use nonblank string `message`; otherwise `Request failed (HTTP <code>).` |
| DNS/connect/timeout | Map to server-unreachable/no-connection/timeout and display one global connection dialog. |
| TLS failure | Return a trusted-connection error without disabling TLS verification. |

HTTP failures use `ApiException(statusCode, message)`, an IOException subtype. Error parsing reads at most 64 KiB, handles malformed/empty JSON, and closes failed responses. Original transport causes and coroutine cancellation are preserved. The existing use-case pipeline exposes feature errors via `UiState.Error`.

`NetworkErrorManager` and `SessionManager` retain pending state during backgrounding and deduplicate failures. The app acknowledges a network error after dismissal and expiry after navigation. A late 401 cannot delete a newer session: `clearSessionIfTokenMatches` checks inside the same DataStore edit. If clearing fails, a global storage-error dialog offers retry before navigation. Successful login resets old expiry state.

## Chucker

| Variant | Inspector |
| --- | --- |
| `stagingDebug` | Active |
| `productionDebug` | Active |
| `stagingRelease` | Active |
| `productionRelease` | No-op artifact |

Open Chucker from its notification (when notifications are permitted) or app launcher shortcut. It retains captures for one hour, limits body capture to 250 KB, and redacts Authorization, Cookie, and Set-Cookie headers. Sensitive calls are excluded entirely, so passwords and login tokens are not stored by the inspector.

**Demo login makes no HTTP requests**, so it creates no Chucker entries. Connecting the Retrofit service produces real captures; network tests exercise the same interceptor pipeline with local/synthetic responses. Chucker notifications are optional and do not gate app usage.

## Sentry: configured, disabled

Sentry helpers live in `core:common/utils/sentry`; `TemplateApplication` passes environment/release metadata. Every variant currently sets `SENTRY_ENABLED=false` and `SENTRY_DSN=""` in app BuildConfig. Manifest `io.sentry.auto-init=false` prevents automatic initialization. Disabled helpers are no-ops; the SDK is not manually initialized and sends no events.

To enable later, set a valid DSN and `SENTRY_ENABLED=true` for the intended flavor in `app/build.gradle.kts`. Keep auto-init false because initialization remains explicit. The SDK then handles unhandled errors; `SentryLogger.captureException` supports explicit reporting. Expected connection/cancellation failures are filtered. HTTP breadcrumbs contain method, status, and URL without query/fragment/user info; bodies, authorization headers, screenshots, and default PII are omitted. Tracing is disabled. There is no Sentry Gradle upload plugin or duplicate uncaught-exception handler.

## Build and test

| Flavor | Application ID |
| --- | --- |
| Staging | `id.codemockup.template.staging` |
| Production | `id.codemockup.template` |

```sh
# Complete project gate: APKs, JVM tests and lint
./gradlew build

# APKs
./gradlew :app:assembleStagingDebug :app:assembleProductionDebug
./gradlew :app:assembleStagingRelease :app:assembleProductionRelease

# Focused verification
./gradlew testStagingDebugUnitTest :app:lintStagingDebug
./gradlew :app:connectedStagingDebugAndroidTest

# Check the production artifact selection
./gradlew :core:network:dependencyInsight \
  --dependency com.github.chuckerteam.chucker \
  --configuration productionReleaseRuntimeClasspath
```

Connected tests require a running API 29+ emulator/device. They cover login validation, errors, persisted session, Activity recreation/relaunch, logout/back-stack behavior, global dialogs and background session expiry, plus disabled Sentry. JVM tests cover request delegation, HTTP/transport mapping, bounded response reads/closure, cancellation, capture exclusions, session token checks, event deduplication, storage failures, and retries.

APKs are under `app/build/outputs/apk/<flavor>/<buildType>/`; release files are unsigned and shrinking is disabled. Configure project-specific signing/shrinking when adopting the template. JVM/lint reports live under each module's `build/reports`; device reports under `app/build/reports/androidTests`.

## Extend or adopt the template

### Add a feature

1. Include `:feature:<name>` in settings and apply `template.android.feature` plus `template.android.library.compose`.
2. Add `<Name>Screen`, `<Name>State`, `<Name>ViewModel`, and navigation registration. Keep feature-only UI in `components`; generic controls belong in `core:designsystem`.
3. Add a serializable route in `core:data/remote/routes`, then wire callbacks in app's NavHost.
4. Add behavior tests and a module AGENTS guide. See [architecture rules](AGENTS.md#adding-a-feature-or-endpoint).

### Add an endpoint or real backend

1. Put request/response DTOs in `core:data`, service operations in `core:network/services`, repository contract/DataSource in `core:domain/repository/<area>`, and use cases in `core:domain/usecase/<area>`.
2. Bind repositories in RepositoryModule and grouped use cases in UseCaseModule. Add shared application models in `core:model` and transformations in `core:domain/mapper` when needed.
3. Set flavor HTTPS BASE_URL values in `core/network/build.gradle.kts`. Replace the demo service provider with `retrofit.create(AuthServices::class.java)`.
4. Adapt the sample `POST auth/login` contract, mapper, and tests together. Current request: `{ "email": "...", "password": "..." }`; response: `{ "data": { "token": "...", "userId": "...", "email": "..." } }`.
5. Apply protected/sensitive markers as appropriate. Token refresh, server-side logout, and backend-specific validation are intentionally left for the real API contract. Remove the demo credential hint and demo binding before using real authentication.

### Rename the project

Update rootProject.name, application ID, namespaces, source packages/imports, test packages, app label, and `template.*` convention plugin IDs together. Update build-logic package paths and documentation. Keep machine-specific SDK paths in local.properties and production secrets out of the repository. Dependencies stay centralized in `gradle/libs.versions.toml`.

## Design foundations

The app uses a fixed light palette from `temp/design_system.html`, with bundled Onest replacing the reference's Inter. Dynamic wallpaper colors and automatic dark mode are disabled. Feature layouts and authentication behavior are unchanged.

Shared tokens live in `core:designsystem/theme`: `AppColors`, `AppTypography`, `AppSpacing`, `AppRadius`, and `AppMotion`. The theme maps these tokens into Material colors, typography, and shapes. Spacing values are 2/4/8/12/16/24/32/48/64 dp; radii are 8/12/16/24 dp plus a pill shape. Motion durations are 140/220/320/480 ms with cubic-bezier (0.2, 0, 0, 1).

Use `AppText(text, style = AppTextStyle.Headline)` for named Onest styles. The available styles are Display, Headline, SectionTitle, Title, TitleSmall, Body, BodySmall, Label, Caption, and Meta. Meta applies locale-aware uppercase; Caption and Meta default to secondary ink. All styles support explicit color overrides and normal Compose text layout options.

`AppBackground(variant = AppBackgroundVariant.FieldGlow)` wraps bounded content; `Modifier.appBackground(...)` paints existing containers. Canvas, FieldGlow, FieldDots, FieldRuled, and FieldGrid are available. Patterns fade out by half the container height. Dots, rules, and grid use the documented 20/28/24 dp mobile spacing. Feature screens do not opt into patterned backgrounds automatically.

Open `FoundationPreviews.kt` for palette, typography, enlarged text, spacing, radius, and background previews. Use `AppMotion.tween<Float>(AppMotion.fast)` with Compose animations to retain system duration scaling.

Spacing uses grouped access: `AppSpacing.sm.sm2/sm4/sm8/sm12`, `AppSpacing.md.md16/md24`, and `AppSpacing.lg.lg32/lg48/lg64`. For example, use `Modifier.padding(AppSpacing.sm.sm8)` or `Arrangement.spacedBy(AppSpacing.md.md16)`. All values remain density-independent `Dp`.

Colors use grouped access: `AppColors.primary.onyx` and `.graphite`; `AppColors.secondary.spark`, `.sparkTint`, and `.onSparkTint`; and `AppColors.neutral` for canvas, surfaces, supporting ink, borders, dark surface colors, background patterns, and scrim. Status groups expose `AppColors.warning.solid/tint`, `AppColors.negative.solid/tint`, and `AppColors.success.solid/tint`. The negative group supplies Material error colors. Palette values and theme behavior are unchanged.


### App controls

Shared controls live in `components/buttons`, `components/inputs`, `components/text`, and
`components/backgrounds`, and `components/feedback`; previews live in `components/previews`.

`AppButton` replaces `TemplateButton`. It supports Primary, Spark, Secondary (outline),
Tonal, Text, Destructive, and DestructiveOutline variants, plus Small, Medium, and Large
sizes. Loading retains its label and active colors while blocking clicks; `loadingLabel`
overrides the label. Leading and trailing icon slots are optional.

`AppIconButton` provides filled, spark, tonal, outline, and ghost actions with circular
or square visuals and an optional notification dot. Supply an accessible description
that includes the badge meaning when relevant. `AppFloatingActionButton` supports small,
standard, and capture sizes with Spark or Onyx colors. `AppExtendedFloatingActionButton`
uses caller-owned `expanded` state. Controls reserve at least 48 dp for interaction.
Icons supplied to labeled controls should have null content descriptions to avoid
repeating the action label. Screen owners handle positioning and action behavior.

`AppTextField` replaces `TemplateTextField`. Labels sit above the input. It supports
helper/error text, success, read-only and disabled states, icons, suffixes, keyboard
options/actions, password transformations, and multiline text. Use
`shape = AppRadius.pill, filled = true` for a search field. Omitted labels require a
`contentDescription`. `characterLimit` displays a counter; it does not truncate input
or impose validation. Error takes precedence over success. Read-only input remains
selectable. Disabled styling takes precedence over interaction styling.

Control geometry lives in `AppControlTokens`; reference pressed/disabled colors extend
the existing AppColors groups. Typography uses Onest and AppTypography-derived styles.
Interaction colors and extended FAB sizing use AppMotion.fast. Existing foundation
values remain unchanged. Component previews cover variants and enlarged text.

Component enums live in `core:designsystem/common`. Import `AppButtonVariant`, `AppButtonSize`, `AppFabColor`, `AppFabSize`, `AppIconButtonVariant`, `AppIconButtonShape`, `AppBackgroundVariant`, and `AppTextStyle` from that package.
