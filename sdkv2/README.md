# `com.nohrd.bike:sdkv2`

The `com.nohrd.bike:sdkv2` artifact contains the sources for
reading data from a connected FTMS (FiTness Machine Service) Bike.

## Usage

The main entry point of this library is the `FTMSBike` class.
This class requires an implementation of the `BytesReader` interface,
see ['ServiceReader'](#servicereader).
Create an instance of the `FTMSBike` class as follows:

```kotlin
import com.nohrd.bike.sdkv2.*

val bike = FTMSBike.create(MyBytesReader())
```

### Calibration
The NOHrD Bike has a wheel to configure the peddling resistance, and the
minimum and maximum resistance measurement values vary from bike to bike.
These values are set during the production of a NOHrD Bike.
However, it is possible to calibrate the bike via bluetooth by sending the following packets:

Tell the bike the resistance is the highest:

`FE 01 01 00 00 00`

Tell the bike the resistance is the lowest:

`FE 01 02 00 00 00`

These packets need to be sent to this service & characteristic

service: `00001001-C042-66BA-1335-90118F542C77`

characteristic: `8EC92001-F315-4F60-9FB8-838830DAEA50`

This functionality is not included in this SDK to keep the FTMS implementation general to all Fitness Machine Indoor Bikes.

### Cycling data

Cycling data can be retrieved using callbacks.
Callbacks will be invoked on arbitrary threads, so it's the user's responsibility to properly switch to a main thread, if necessary.

```kotlin
val cancellable = device.bikeData(
  object : BikeDataListener {
      override fun onCadence(cadence: Cadence?) {
      }

      override fun onDistance(distance: Distance) {
      }

      override fun onEnergy(energy: Energy) {
      }

      override fun onPower(power: Power?) {
      }

      override fun onResistance(resistance: Resistance) {
      }

      override fun onSpeed(speed: Speed?) {
      }
  }
)

// Cancel listening when you're done
cancellable.cancel()
```

## ServiceReader

The `ServiceReader` interface plays the role of the input of the SDK.
The `FTMSBike` class will register itself with the implementation of this
interface to receive data.

An implementation of the `ServiceReader` can vary, depending on how you connect
with a NOHrD Bike machine.
Therefore, these implementations are not provided by the SDK.