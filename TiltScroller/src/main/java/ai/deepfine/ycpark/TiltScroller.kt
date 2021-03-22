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
    private lateinit var context: Context
    private lateinit var device: GlassDevice

    @JvmStatic
    var onTiltListener: OnTiltListener? = null

    // Gyroscope Variables
    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val gyroSensor: Sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private const val MANIPULATE= 100.toDouble()
    private const val NS2S        = 1.0F / 1000000000.0F
    private const val RAD2DGR   = (180 / PI)


    // For calculate values from gyroscope
    private var prevTimeStamp  = 0.0
    private var dt             = 0.0
    private var pitchX         = 0.0
    private var pitchY         = 0.0
    private var pitchZ         = 0.0

    private var horizontal     = 0
    private var vertical       = 0

    @JvmStatic
    val scrollable: MutableLiveData<Boolean> = MutableLiveData()

    private val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            with(event) {
                dt = (timestamp - prevTimeStamp) * NS2S
                pitchX = values[0].toDouble()
                pitchY = values[1].toDouble()
                pitchZ = values[2].toDouble()
            }

            if (dt - prevTimeStamp * NS2S != 0.0) {
                pitchX *= dt
                pitchY *= dt
                pitchZ *= dt

                when (device) {
                    GlassDevice.VUZIX, GlassDevice.REALWEAR -> {
                        horizontal = (pitchY * RAD2DGR * MANIPULATE).toInt()
                        vertical = (pitchX * RAD2DGR * MANIPULATE).toInt()
                    }

                    GlassDevice.GOOGLE_GLASS -> {
                        horizontal = (pitchZ * RAD2DGR * MANIPULATE).toInt()
                        vertical = (pitchX * RAD2DGR * MANIPULATE).toInt()
                    }
                }

                if (scrollable.value!!) {
                    onTiltListener?.onTilt(horizontal.toFloat(), vertical.toFloat())
                }
            }
        }
    }

    fun init(context: Context, device: GlassDevice) {
        this.context = context
        this.device = device
        scrollable.value = true
    }

    fun init(context: Context) {
        init(context, GlassDevice.REALWEAR)
    }

    fun registerSensor(onTiltListener: OnTiltListener) {
        sensorManager.registerListener(sensorListener, gyroSensor, SensorManager.SENSOR_DELAY_UI)
        this.onTiltListener = onTiltListener
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(sensorListener)
        this.onTiltListener = null
    }
}