# NOHrD Bike SDK for Android

A library for reading data from a connected NOHrD Bike device.

## NOHrD Bike SDK

The `com.nohrd.bike:sdk` artifact contains the sources for calculating
bike data for a connected NOHrD Bike device.
See [sdk/Readme](sdk/README.md) for usage instructions.

## NOHrD Bike SDK BLE

The `com.nohrd.bike:sdk-ble` artifact contains the UUIDs for connecting
with a NOHrD Bike device using BLE.
This artifact has a transitive dependency on the `com.nohrd.bike.:sdk` artifact.
See [sdk-ble/Readme](sdk-ble/README.md) for usage instructions.

## Sample app

A sample app that connects to the NOHrD Bike using BLE can be found at
['sdk-ble/sample'](sdk-ble/sample).

## Setup

This library is available on Maven Central.

Include the following in your project's `build.gradle` file:

```diff
buildscript {
  // ...
}

+ subprojects {
+   repositories {
+     mavenCentral()
+   }
+ }
```

Include one of the the following in the `build.gradle` file of the module you wish to add
the dependency to, replacing `x.x.x` with the latest version:

```diff
dependencies {
+ implementation "com.nohrd.bike:sdk:x.x.x"
+ implementation "com.nohrd.bike:sdk-ble:x.x.x"
}
```

## Development

This project uses Gradle to test and build the library:

 - `./gradlew test` builds and runs the tests
 - `./gradlew :sdk-ble-sample:installDebug` installs the sample application on
    a connected device.

## Releasing

See [RELEASING.md](RELEASING.md)