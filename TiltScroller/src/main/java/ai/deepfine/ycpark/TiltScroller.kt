package ai.deepfine.ycpark

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.PI

/**
 * @Description Class설명
 * @author yc.park (DEEP.FINE)
 * @since 2021-02-01
 * @version 1.0.0
 */
class TiltScroller private constructor(context: Context, device: GlassDevice) {
    //==============================================================================================
    // Constant Define
    //==============================================================================================


    init {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mDevice = device
    }

    companion object {
        // Constructor parameters
        private lateinit var instance: TiltScroller
        private lateinit var context: Context
        private lateinit var mDevice: GlassDevice

        // Callback
        var onTiltListener: OnTiltListener? = null

        // Gyroscope Variables
        private lateinit var mSensorManager: SensorManager
        private lateinit var mGyroSensor: Sensor
        private const val MANIPULATE = 100.toDouble()
        private const val NS2S = 1.0F / 1000000000.0F
        private const val RAD2DGR = (180 / PI)

        // Calculate Gyroscope
        private var timestamp = 0.0
        private var dt = 0.0
        private var pitchX = 0.0
        private var pitchY = 0.0
        private var pitchZ = 0.0

        private var horizontal = 0
        private var vertical = 0

        private var mRecyclerView: RecyclerView? = null

        var scrollable = true


        private val mSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                dt = (event.timestamp - timestamp) * NS2S
                pitchX = event.values[0].toDouble()
                pitchY = event.values[1].toDouble()
                pitchZ = event.values[2].toDouble()
                timestamp = event.timestamp.toDouble()

                if (dt - timestamp * NS2S != 0.0) {
                    pitchX *= dt
                    pitchY *= dt
                    pitchZ *= dt

                    when (mDevice) {
                        GlassDevice.VUZIX, GlassDevice.REALWEAR -> {
                            horizontal = (pitchY * RAD2DGR * MANIPULATE).toInt()
                            vertical = (pitchX * RAD2DGR * MANIPULATE).toInt()
                        }

                        GlassDevice.GOOGLE_GLASS -> {
                            horizontal = (pitchZ * RAD2DGR * MANIPULATE).toInt()
                            vertical = (pitchX * RAD2DGR * MANIPULATE).toInt()
                        }
                    }
                }

                if (scrollable) {
                    onTiltListener?.onTilt(
                        horizontal.toFloat(), vertical.toFloat())
                    mRecyclerView?.smoothScrollBy(-horizontal, -vertical)
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }


        //==============================================================================================
        // Use
        //==============================================================================================
        fun init(context: Context, device: GlassDevice) {
            instance =
                TiltScroller(context, device)
        }

        fun init(context: Context) {
            instance =
                TiltScroller(
                    context,
                    GlassDevice.REALWEAR
                )
        }

        // Gyroscope Variables
        fun registerSensor() {
            mSensorManager.registerListener(
                mSensorListener,
                mGyroSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        fun destroy() {
            mSensorManager.unregisterListener(
                mSensorListener
            )
            scrollable = true
            instance =
                TiltScroller(
                    context,
                    mDevice
                )
        }

        fun setRecyclerView(recyclerView: RecyclerView) {
            mRecyclerView = recyclerView
        }
    }
}