package ai.deepfine.ycpark

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import kotlin.math.PI

/**
 * @Description TiltScroller
 * @author yc.park (DEEP.FINE)
 * @since 2021-03-22
 * @version 1.0.0
 */
object TiltScroller {
    private lateinit var device: Glass

    private var onTiltListener: OnTiltListener? = null
    private var onTilt: ((horizontal: Float, vertical: Float) -> Unit?)? = null

    // Gyroscope Variables
    private lateinit var sensorManager: SensorManager

    private val gyroSensor: Sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private const val MANIPULATE    = 100.toDouble()
    private const val NS2S          = 1.0F / 1000000000.0F
    private const val RAD2DGR       = (180 / PI)


    // Calculating values from gyroscope
    private var prevTimeStamp   = 0.0
    private var dt              = 0.0
    private var pitchHorizontal = 0.0
    private var pitchVertical   = 0.0

    private var horizontal      = 0
    private var vertical        = 0

    var scrollable = true

    private val sensorListener by lazy {
        object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent) {
                with(event) {
                    dt = (timestamp - prevTimeStamp) * NS2S
                    pitchHorizontal = values[device.horizontal].toDouble()
                    pitchVertical = values[device.vertical].toDouble()
                    prevTimeStamp = timestamp.toDouble()
                }

                if (dt - prevTimeStamp * NS2S != 0.0) {
                    pitchHorizontal *= dt
                    pitchVertical *= dt

                    horizontal = (pitchHorizontal * RAD2DGR * MANIPULATE).toInt()
                    vertical = (pitchVertical * RAD2DGR * MANIPULATE).toInt()

                    if (scrollable) {
                        onTiltListener?.onTilt(horizontal.toFloat(), vertical.toFloat())
                        onTilt?.let { it(horizontal.toFloat(), vertical.toFloat()) }
                    }
                }
            }
        }
    }

    fun init(context: Context, device: Glass) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.device = device
        scrollable = true
    }

    fun init(context: Context) {
        init(context, Glass.REALWEAR)
    }

    fun registerSensor(onTiltListener: OnTiltListener) {
        sensorManager.registerListener(sensorListener, gyroSensor, SensorManager.SENSOR_DELAY_UI)
        this.onTiltListener = onTiltListener
    }

    fun registerSensor(onTilt: ((horizontal: Float, vertical: Float) -> Unit)?) {
        sensorManager.registerListener(sensorListener, gyroSensor, SensorManager.SENSOR_DELAY_UI)
        this.onTilt = onTilt
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(sensorListener)
        this.onTiltListener = null
    }
}