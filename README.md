# Care Crew – Volunteer Connection Android App

> _"Let's Make the World Beautiful Again"_

---

## 📜 Project Overview

**Care Crew** is a volunteer-driven Android application that connects compassionate individuals with real-time community needs like flood relief, blood donation, and neighborhood support activities.  
The app simplifies the process of signing up, finding local volunteer opportunities, creating groups, and collaborating with others — all while maintaining a seamless user experience with persistent logins.

---

## 📱 Key Features

- 🔐 **User Authentication**: Secure signup/login using Firebase Authentication.
- 🔒 **Two-Factor Authentication (2FA)**: Enhanced security with phone-based verification.
- 🔁 **Persistent Sessions**: Auto-login unless manually logged out.
- 🗘️ **Volunteer Mapping**: Real-time map showing active areas and volunteers.
- 💬 **In-App Chat**: Communication between volunteers.
- 🏆 **Leaderboards**: Recognizing top contributors.
- 👥 **Volunteer Groups**: Create and manage local volunteer teams.
- 📲 **Notifications**: (Coming Soon) Alerts for urgent help opportunities.

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
- Enable **Phone Authentication** for Two-Factor Authentication (2FA).
- Download the `google-services.json` file.
- Place it inside the `/app` directory.

### 4. Build and Run

- Connect your emulator or real device.
- Hit the **Run** ▶️ button in Android Studio.

---

## 📺 App Workflow

- **Splash Screen**: Checks if the user is already authenticated.
- **Login / Sign Up**: Only shown if no existing login session.
- **Two-Factor Authentication**: Optional phone-based 2FA setup after signup for enhanced security.
- **Home Screen**: Displays volunteer options, groups, and maps.
- **Profile Section**: Allows users to manage 2FA settings and log out manually.

---


## 🔒 Two-Factor Authentication (2FA)

Care Crew now supports Two-Factor Authentication to provide an extra layer of security to user accounts.

### How to Enable 2FA

1. **During Signup**: After creating a new account, you'll be prompted to set up 2FA. You can either:
   - Enter your phone number to enable 2FA immediately
   - Skip the setup and enable it later from your profile

2. **From Profile Settings**: Existing users can enable 2FA by:
   - Navigate to your Profile
   - Click on the "Enable Two-Factor Authentication" button
   - Enter your phone number (with country code, e.g., +1234567890)
   - Click "Send Verification Code"
   - Enter the 6-digit code sent to your phone
   - Click "Verify Code" to complete the setup

### How 2FA Works

- Once enabled, your phone number is linked to your account
- The 2FA status is stored in Firebase Realtime Database under your user profile
- Users can manage their 2FA settings from the Profile page
- The button label changes to "Disable Two-Factor Authentication" when 2FA is active

### Firebase Configuration for 2FA

To use 2FA in your deployment:
1. Enable **Phone Authentication** in Firebase Console
2. Configure your app's SHA-1 fingerprint in Firebase project settings
3. Ensure your Firebase project has the Phone Auth provider enabled

---

## 🛃️ Future Plans

- 🔔 Push Notifications for emergency events.
- 🌑 Dark Mode for better night usability.
- 📸 Profile picture uploads.
- 🫩 Event creation features.
- 📊 Admin Analytics Dashboard.

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

