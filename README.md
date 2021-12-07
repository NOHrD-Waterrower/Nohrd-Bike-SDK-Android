# NOHrD Bike SDK for Android

A library for reading data from a connected NOHrD Bike devices.
There are 2 types of NOHrD bikes:
- The initial version with a custom protocol (v1 Bike)
- The secondary version using the FTMS protocol (v2 Bike)

## NOHrD Bike SDK

The `com.nohrd.bike:sdkv1`artifact contains the sources for calculating bike data for a connected v1 NOHrD Bike device.
See [sdkv1/Readme](sdkv1/README.md) for usage instructions.

The `com.nohrd.bike:sdkv2`artifact contains the sources for retrieving bike data from a connected v2 NOHrD Bike device.
See [sdkv2/Readme](sdkv2/README.md) for usage instructions.

## NOHrD Bike SDK BLE

The `com.nohrd.bike:sdkv1-ble` artifact contains the UUIDs for connecting with a v1 NOHrD Bike device using BLE.
The `com.nohrd.bike:sdkv2-ble` artifact contains the UUIDs for connecting with a v2 NOHrD Bike device using BLE.
Theses artifacts has a transitive dependency on the `com.nohrd.bike:sdkv1` and `com.nohrd.bike:sdkv1` artifacts respectively.
See [sdkv1-ble/Readme](sdkv1-ble/README.md) for usage instructions.
See [sdkv2-ble/Readme](sdkv2-ble/README.md) for usage instructions.

## Sample app

A sample app that connects to the v1 NOHrD Bike using BLE can be found at
['sdkv1-ble/sample'](sdkv1-ble/sample).

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
+ implementation "com.nohrd.bike:sdkv1:x.x.x"
+ implementation "com.nohrd.bike:sdkv1-ble:x.x.x"
+ implementation "com.nohrd.bike:sdkv2:x.x.x"
+ implementation "com.nohrd.bike:sdkv2-ble:x.x.x"
}
```

## Development

This project uses Gradle to test and build the library:

 - `./gradlew test` builds and runs the tests
 - `./gradlew :sdkv1-ble-sample:installDebug` installs the sample application on
    a connected device.

## Releasing

See [RELEASING.md](RELEASING.md)