# Care Crew – Volunteer Connection Android App

> _"Let's Make the World Beautiful Again"_

---

## 📜 Project Overview

**Care Crew** is a volunteer-driven Android application that connects compassionate individuals with real-time community needs like flood relief, blood donation, and neighborhood support activities.  
The app simplifies the process of signing up, finding local volunteer opportunities, creating groups, and collaborating with others — all while maintaining a seamless user experience with persistent logins.

---

## 📱 Key Features

- 🔐 **User Authentication**: Secure signup/login using Firebase Authentication.
- 🔁 **Persistent Sessions**: Auto-login unless manually logged out.
- 🗘️ **Volunteer Mapping**: Real-time map showing active areas and volunteers.
- 💬 **In-App Chat**: Communication between volunteers.
- 🏆 **Leaderboards**: Recognizing top contributors.
- 👥 **Volunteer Groups**: Create and manage local volunteer teams.
- 📲 **Push Notifications**: Alerts for urgent volunteer needs using Firebase Cloud Messaging.

---

## 🏗️ Technology Stack

- **Language**: Java
- **Development Tool**: Android Studio
- **Backend**: Firebase (Authentication + Realtime Database)
- **Other Services**: Google Maps API

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/care-crew.git
```

### 2. Open in Android Studio

- Open Android Studio → **Open Project** → Select the cloned project folder.

### 3. Firebase Setup

- Create a project on [Firebase Console](https://console.firebase.google.com/).
- Enable **Email/Password Authentication** and **Realtime Database**.
- Download the `google-services.json` file.
- Place it inside the `/app` directory (see `app/google-services.json.example` for the expected structure).

### 4. Configure API Keys

Add the following to your `local.properties` file (this file is git-ignored and should never be committed):

```properties
IMG_BB_API_KEY=your_imgbb_api_key_here
```

You can obtain an ImgBB API key from [https://api.imgbb.com/](https://api.imgbb.com/).

Alternatively, you can set `IMG_BB_API_KEY` as an environment variable.

> **For CI/CD**: Add the following repository secrets in GitHub Settings → Secrets and variables → Actions:
> - `GOOGLE_SERVICES_JSON` — The full contents of your `google-services.json` file
> - `IMG_BB_API_KEY` — Your ImgBB API key

### 5. Build and Run

- Connect your emulator or real device.
- Hit the **Run** ▶️ button in Android Studio.

---

## 📺 App Workflow

- **Splash Screen**: Checks if the user is already authenticated.
- **Login / Sign Up**: Only shown if no existing login session.
- **Home Screen**: Displays volunteer options, groups, and maps.
- **Profile Section**: Allows users to log out manually.

---

## 📲 Push Notifications

Care Crew includes Firebase Cloud Messaging (FCM) integration for real-time alerts about urgent volunteer needs.

**Key Features:**
- Automatic subscription to urgent notifications on login
- Topic-based messaging for scalable broadcasting
- Support for high-priority urgent notifications
- Notification channels for Android 8.0+

For detailed setup and usage instructions, see [Push Notifications Guide](docs/PUSH_NOTIFICATIONS.md).

---


## 🛃️ Future Plans

- 🌑 Dark Mode for better night usability.
- 📸 Profile picture uploads.
- 🫩 Event creation features.
- 📊 Admin Analytics Dashboard.
- 🔒 Two-Factor Authentication (2FA) for extra security.

---

## 🤝 How to Contribute

We welcome contributions to improve Care Crew!

### Contribution Steps

1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature/your-feature-name
    ```
3. Make your changes.
4. Commit with a descriptive message:
    ```bash
    git commit -m "Added: Short Description"
    ```
5. Push changes:
    ```bash
    git push origin feature/your-feature-name
    ```
6. Create a Pull Request.

**Please ensure** your code is clean, documented, and follows best practices.

---

## 📄 License

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
```

---

## 📬 Contact


Feel free to reach out for any queries, collaborations, or suggestions! 🚀

---

> **Care Crew – Empowering Volunteers. Connecting Communities.**

