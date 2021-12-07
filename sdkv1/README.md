# `com.nohrd.bike:sdk`

The `com.nohrd.bike:sdk` artifact contains the sources for
reading data from a connected NOHrD Bike device.

The NOHrD Bike sends a stream of bytes that is interpreted and
transformed into cycling data by the library.


## Usage

The main entry point of this library is the `NohrdBike` class.
This class requires an implementation of the `BytesReader` interface,
see ['BytesReader'](#bytesreader).
Create an instance of the `NohrdBike` class as follows:

```kotlin
import com.nohrd.bike.sdk.*

val bike = NohrdBike.create(MyBytesReader())
```

Data can be obtained using callbacks, as described in ['Calibration'](#calibration)
and ['Cycling data'](#cycling-data).
Callbacks will be invoked on arbitrary threads, so it's the user's responsibility
to properly switch to a main thread, if necessary.

### Calibration

Before you can retrieve cycling data, the NOHrD Bike needs to be calibrated.
The NOHrD Bike has a wheel to configure the peddling resistance, and the
minimum and maximum resistance measurement values vary from bike to bike.
It is therefore important that these minimum and maximum values are configured
to obtain correct cycling data.

To calibrate the bike, use the `NohrdBike.resistanceMeasurements` function.
This function will provide you with the raw resistance measurement values of
the connected NOHrD Bike.
Generally, it is a good idea to ask the user to turn the resistance all the way
down and all the way up.
This gives you the opportunity to store the minimum and maximum resistance measurement
value of the bike.

```kotlin
val cancellable = device.resistanceMeasurements(
    object : ResistanceMeasurementsListener {
        override fun onResistanceMeasurement(measurement: ResistanceMeasurement) {
            // Store the minimum and maximum values
        }
    }
)

/* ... */

// Cancel listening when you're done
cancellable.cancel()
```

### Cycling data

Cycling data can be retrieved in a similar manner as resistance measurements,
but this requires an additional `Calibration` parameter.
Obtain this value as described in ['Calibration'](#calibration).

```kotlin
val calibration = Calibration(
    lowValue = ResistanceMeasurement(...),
    highValue = ResistanceMeasurement(...)
)

val cancellable = device.bikeData(
  calibration,
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

## BytesReader

The `BytesReader` interface plays the role of the input of the SDK.
The `NohrdBike` class will register itself with the implementation of this
interface to receive data.

An implementation of the `BytesReader` can vary, depending on how you connect
with a NOHrD Bike machine.
Therefore, these implementations are not provided by the SDK.

An example implementation is listed below, which shows how you could implement
the interface when reading from a `UsbDeviceConnection` on Android:

```kotlin
class UsbBytesReader(
    private val readEndpoint: UsbEndpoint,
    private val connection: UsbDeviceConnection
) : BytesReader {

    override fun start(callback: Callback): Cancellable {
        var cancelled = false
        val buffer = ByteArray(256)
        while (!cancelled) {
            val read = connection.bulkTransfer(readEndpoint, buffer, buffer.size, 1000)
            if (read > 0) {
                val bytes = buffer.take(read).toByteArray()
                callback.onBytesRead(bytes)
            }
        }

        return Cancellable { cancelled = true }
    }
}
```