# Embedded Android Photo Picker

Ein Beispielprojekt, das zeigt, wie man den Embedded Photo Picker in Android 14+ (Upside Down Cake) mit Jetpack Compose integriert.

Dieses Projekt nutzt die neuesten Android SDK Extensions und Coil für das Laden von Bildern.

## 📋 Inhaltsverzeichnis

- [Über das Projekt](#über-das-projekt)
- [Voraussetzungen](#voraussetzungen)
- [Installation](#installation)
- [Verwendung](#verwendung)
- [Funktionen](#funktionen)
- [Code-Beispiele](#code-beispiele)
- [Mitwirken](#mitwirken)
- [Lizenz](#lizenz)

## 🎯 Über das Projekt

Dieses Projekt demonstriert:

- Verwendung von `EmbeddedPhotoPicker` in Jetpack Compose
- Unterstützung für Android 14+ mit SDK Extensions
- Dynamische Auswahl mehrerer Bilder
- Eigenes UI zur Darstellung der Anhänge
- Integration von Coil zur Bildanzeige

## 📱 Voraussetzungen

- Android Studio Flamingo oder neuer
- Kotlin 1.8+
- Min SDK 34 (Android 14)
- Jetpack Compose
- Coil Image Library
- Unterstützung für Android SDK Extensions >= 15

## 🚀 Installation

1. **Repository klonen:**

   ```bash
   git clone https://github.com/dein-benutzername/embedded-android-photo-picker.git
   ```

2. **Projekt in Android Studio öffnen**

3. **Abhängigkeiten synchronisieren**

4. **Auf einem Android 14+ Emulator oder Gerät ausführen**

## 📖 Verwendung

- Öffne die App und klicke auf **"Add from gallery"**, um Bilder auszuwählen
- Die ausgewählten Bilder werden in einem adaptiven Grid angezeigt
- Mit **"Send"** kannst du die ausgewählten Bilder weiterverarbeiten

## ✨ Funktionen

- **Embedded Photo Picker:** Auswahl mehrerer Bilder direkt im Compose UI
- **Attachment UI:** Eigene Oberfläche zur Anzeige und Entfernung von Bildern
- **Dynamische Picker-Features:** Maximal 5 Bilder, geordnete Auswahl, automatische Berechtigungen
- **Jetpack Compose UI:** Vollständig Compose-basiert für moderne Android-Apps

## 💻 Code-Beispiele

### PhotoPicker Launcher einrichten

```kotlin
val photoPickerLauncher = rememberLauncherForEmbeddedPhotoPicker(
    maxItems = 5,
    selectedItems = selectedUris
) { uris ->
    selectedUris = uris
}
```

### Picker öffnen

```kotlin
Button(onClick = { photoPickerLauncher.launch() }) {
    Text("Add from gallery")
}
```

## 🤝 Mitwirken

Beiträge sind willkommen! So kannst du mitwirken:

1. Forke das Projekt
2. Erstelle einen Feature-Branch (`git checkout -b feature/AmazingFeature`)
3. Committe deine Änderungen (`git commit -m 'Add some AmazingFeature'`)
4. Push zum Branch (`git push origin feature/AmazingFeature`)
5. Öffne einen Pull Request

## 📄 Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe die [LICENSE](LICENSE) Datei für Details.

---

**Hinweis:** Dieses Projekt erfordert Android 14 (API Level 34) oder höher, da der Embedded Photo Picker erst ab dieser Version verfügbar ist.
