# Daily Bleach Poems

An unofficial, fan-made Android application that delivers a daily poem from the *Bleach* manga series. Built with modern Android technologies, including Jetpack Compose, this app provides a clean, customizable, and immersive experience for fellow fans.

![App Screenshot](https://imgur.com/gallery/daily-bleach-poems-screenshots-llLBo30) 

## Features

- **Daily Poem & Cover:** Each day, a new random volume cover and its corresponding poem are selected and displayed on the main screen.
- **Interactive 3D Flip Animation:** Tap the cover image to reveal the poem on the back with a smooth and satisfying 3D flip animation.
- **Customizable Notifications:**
    - Enable or disable daily notifications.
    - Choose the exact time you want to receive your daily notification.
    - Select from three spoiler-level preferences for the notification text.
- **In-App Theming:**
    - Choose between a Light theme, a Dark theme, or a System Default theme that automatically syncs with your phone's settings.
- **Language Selection:**
    - Manually switch the app's language between English and Italian (more added soon), independent of your phone's language.
- **Modern & Clean UI:**
    - A minimalist user interface built entirely with Jetpack Compose.
    - Features a transparent status bar and a subtle gradient background for a modern look.
- **Self-Hosted Updates: (coming soon)**
    - The app includes a built-in update checker that notifies you when a new version is available for download.

## Disclaimer & Copyright Notice

**This is an unofficial, fan-made application created for fans, by a fan.** It is not affiliated with, endorsed, or supported by Shueisha Inc., Tite Kubo, or any other entity associated with the original *Bleach* work.

All images (manga covers), texts (poems), and names related to "Bleach" are registered trademarks and/or copyrights of **Shueisha Inc. and Tite Kubo**. The use of this material is intended for tribute and entertainment purposes only, with no intention of copyright infringement.

This app is, and will always be, **completely free**. No profit is being made from its distribution, and it contains no advertisements or in-app purchases.

If you are a copyright holder and believe that this application infringes upon your rights, please contact me at **[your_contact_email@example.com]**, and I will promptly remove the application from distribution.

## Built With

This project showcases a variety of modern Android development tools:

- **[Kotlin](https://kotlinlang.org/)**: The primary programming language.
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: For building the entire user interface declaratively.
- **[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)**: For scheduling the reliable daily background task that selects the poem.
- **[DataStore](https://developer.android.com/topic/libraries/architecture/datastore)**: For persisting all user preferences (theme, language, notification settings).
- **[Compose Navigation](https://developer.android.com/jetpack/compose/navigation)**: For handling in-app navigation between screens.
- **[ConstraintLayout for Compose](https://developer.android.com/jetpack/compose/layouts/constraintlayout)**: For positioning UI elements with complex relationships.
- **[Ktor Client](https://ktor.io/docs/client-overview.html)**: For handling networking to check for app updates.

