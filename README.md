# Score
<p align="center"><img width="148" height="148" alt="score_icon" src="https://github.com/user-attachments/assets/77b0cd4d-3a4a-440d-9f2e-c71fd202bafb" /><p align="center">

Score is Cornell’s all-in-one sports discovery hub that makes Cornell sports easily accessible and engaging for the entire community. A free and open-source app, Score helps facilitate the seamless discovery, tracking, and engagement of Cornell sports, allowing students to conveniently stay informed on sports games and feel motivated to actively participate in the campus sports culture.


## Getting Started
1. Clone the repository
2. Download the following two files (AppDev members can find it pinned in `#score-android` in the Slack)
    - `google-services.json`
    - `secrets.properties`
4. Add `secrets.properties` to the project root and `google-services.json` to the app level in Android Studio
5. Build and run!

## Authentication

Google sign-in uses Credential Manager and Firebase Authentication, then exchanges the Firebase ID token for Score backend tokens. Enable Google as a sign-in provider in Firebase, register the Android signing SHA-1 and SHA-256 fingerprints, and install the updated `app/google-services.json`. The file must include the web OAuth client so the Google Services plugin generates `default_web_client_id`. The backend Firebase Admin configuration must use the same Firebase project.

The backend must support `loginUser(idToken:)`, `signupUser(idToken:, name:)`, `refreshAccessToken`, and `logoutUser`. The Android schema matches the local backend's token-only responses. It does not require the user and favorite-team response fields from the iOS PR. Cornell email filtering is also required on the backend to enforce the restriction outside this client.

The welcome screen's Sign Up and Log In buttons both start Google sign-in. The app first calls `loginUser`; only a missing-user response triggers `signupUser`. Successful authentication enters the existing app, and Sign Up Later keeps guest browsing available.

`AuthRepository.state` records the backend session. A Firebase session alone does not sign the user into Score. Backend sessions are encrypted using Android Keystore and stored outside backups. Session restoration refreshes expired access tokens; network failures preserve the saved session, while rejected refresh tokens clear it.

Auth is currently limited to the welcome flow. Profile screens, profile and sports onboarding, and authorization headers on the app's existing API client are not connected to it.

Run `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` with Java 17 to verify the app. End-to-end Google sign-in needs a device or emulator with Google Play services and a backend configured for the updated Firebase project.
