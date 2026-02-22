# Changelog

All notable changes to the CareCrew project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-02-22

### Added
- User authentication with Firebase (email/password sign-up and login)
- Persistent login sessions with auto-login support
- Real-time volunteer mapping with Google Maps integration
- In-app chat system for volunteer communication
- Volunteer groups — create, join, manage, and leave groups
- Push notifications via Firebase Cloud Messaging for urgent alerts
- User profiles with bio editing and profile picture uploads (via ImgBB)
- Community posts — create and view posts with image support
- Leaderboard to recognize top contributors
- Splash screen and welcome onboarding flow
- Material Design 3 UI with Lottie animations
- CI/CD pipelines (lint, build, test) via GitHub Actions
- Firebase security rules for database access control
- Secure credential management via `BuildConfig` and `local.properties`

### Security
- Removed hardcoded API keys from source code
- Added `google-services.json` to `.gitignore`
- API keys loaded from `local.properties` at build time
