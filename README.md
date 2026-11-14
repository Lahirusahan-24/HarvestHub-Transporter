# HarvestHub-Transporter

Android app (Java + XML layouts) for HarvestHub's transport partner portal.

Implemented screens (from the Figma design "HarvestHub UI", file key `fmEOnxONufOFuBF63H0deN`, node `612-58`):

| Screen | File | Notes |
| --- | --- | --- |
| Driver login | `activity_login.xml` + `LoginActivity.java` | Mobile-or-email sign in with toggle, password visibility toggle, "stay signed in" persistence, forgot-password flow |
| Carrier registration | `activity_register.xml` + `RegisterActivity.java` | Full form (name, mobile +94, NIC with LK DMT verified badge, license, secure password) with input validation and terms consent |

## Opening / building the project

This repo is a standard Gradle Android project (`com.android.application` 8.2.2, `compileSdk 34`, `minSdk 24`).

1. Install Android Studio (Hedgehog or newer) and an SDK with platform 34.
2. `File → Open` this repository root, or `git clone` then open.
3. Android Studio will create `local.properties` pointing at your SDK and offer to generate the Gradle wrapper if it is missing (the wrapper jar is intentionally not committed).
4. Run the `app` configuration on an emulator or device. `LoginActivity` is the launcher activity; the two screens are linked by the segmented "Log In / Register Driver" toggle on each screen.

Design source assets / exports can be dropped into `design/` (see `design/README.md`).

## Project layout

```
app/src/main/
├── AndroidManifest.xml
├── java/com/harvesthub/transporter/
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── PasswordToggle.java
│   └── SimpleTextWatcher.java
└── res/
    ├── layout/            # activity_login.xml, activity_register.xml
    ├── drawable/          # vector icons + backgrounds (bg_login, bg_input, …)
    ├── color/             # checkbox_tint selector
    └── values/            # colors (design tokens), themes, strings
```
