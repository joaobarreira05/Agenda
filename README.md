# Chefe - Smart Personal Assistant

**Chefe** (formerly "Agenda") is a smart personal assistant for Android, designed to simplify and streamline the recording of tasks and reminders using natural voice commands.

Forget navigating through dozens of menus and typing dates; with Chefe, you just speak what you need to do, when, and for what context, and the system handles everything automatically.

## 🚀 Key Features

- **Natural Voice Recognition:** Press the microphone button and speak. The recognition engine converts your speech to text in real-time while also saving the original audio.
- **Smart Extraction (Local NLP):** The app reads your sentence and automatically extracts:
  - **Date and Time:** Supports natural expressions like "tomorrow", "at 2:30 PM", "around 11:30", "today".
  - **Category:** Analyzes the sentence looking for Categories using Fuzzy Matching (Levenshtein Distance). If you mention a category name, the app categorizes the task automatically.
  - **Description:** The remaining sentence is filtered (removing time expressions and categories) to create a clean and direct task description.
- **Manual Confirmation & Editing:** After analysis, a Confirmation screen is presented, allowing manual editing of the time, date, category, and text before saving.
- **Invasive Alarms (Full-Screen Intent):** When a reminder expires, the app wakes up the phone, playing the default Ringtone over the lock screen, requiring you to mark the task as seen or snooze it (10 min).
- **Custom Category Management:** Create, edit colors (via Hex codes), and delete categories directly in the settings.
- **Batch Completion & Overdue Alerts:** Tasks that have passed their deadline are marked in red, and the cards get an alert background. Select multiple tasks and mark them all as "Seen" at once.

## 🛠 Technology & Architecture

This project was built following the latest guidelines and best practices of the Modern Android ecosystem:

- **Language:** 100% Kotlin
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture (Presentation, Domain, and Data layers)
- **Dependency Injection:** Dagger Hilt
- **Database:** Room Database (Local SQLite)
- **Navigation:** Android Jetpack Navigation Component
- **Reminder Scheduling:** `AlarmManager` to ensure exact alarm execution and `BroadcastReceiver` + `Service` to display Full-Screen Intents.
- **UI Interface:** XML with Material Design 3 components and full Edge-to-Edge support (`fitsSystemWindows`).
- **Voice Recognition:** Android's native `SpeechRecognizer`.
- **Runtime Permissions:** Advanced support for Notifications, Microphone, and Exact Alarm permissions (API 33 and API 34+).

## 📱 How to Use

1. **Add Tasks:** On the main screen (Home), tap the floating Microphone button. Speak something like: *"Remind me to send an email about performance evaluation for the ULS category around 11:30 tomorrow"*.
2. **Review and Save:** The Confirmation screen will appear. The system has already selected the date (tomorrow), the time (11:30), the category (ULS), and cleaned the sentence. Check it, adjust if necessary (by tapping the clocks), and Save.
3. **Category Management:** Tap the "gear" icon (Settings) in the top right corner to create or edit the colors and names of your categories.
4. **Alarm:** When the time comes, the screen turns on. You have the options to "Complete" or "Snooze (10 min)".
5. **Complete Home Tasks:** Check the *checkbox* on the right side of pending tasks. You can select several. The bottom "Seen" button will appear. Tap it to clear your list!

## ⚙️ Installation (For Developers)

1. Clone this repository.
2. Open the project in **Android Studio**.
3. Sync Gradle.
4. Connect an emulator or a physical device.
5. Run the application (Shift + F10) or run the terminal command: `./gradlew installDebug`.

## License
This project is intended as a private personal assistant ("Chefe").
