package ai.deepfine.ycpark

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
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
        mDevice  = device
        mContext = context
    }

    companion object {
        // Constructor parameters
        private lateinit var instance : TiltScroller
        private lateinit var mContext : Context
        private lateinit var mDevice  : GlassDevice

        // Callback
        @JvmStatic
        var onTiltListener: OnTiltListener? = null

        // Gyroscope Variables
        private val mSensorManager: SensorManager by lazy {
            mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        private val mGyroSensor: Sensor by lazy {
            mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        }

        private const val MANIPULATE= 100.toDouble()
        private const val NS2S        = 1.0F / 1000000000.0F
        private const val RAD2DGR   = (180 / PI)

        // Calculate Gyroscope
        private var mTimeStamp  = 0.0
        private var dt          = 0.0
        private var pitchX      = 0.0
        private var pitchY      = 0.0
        private var pitchZ      = 0.0

        private var horizontal  = 0
        private var vertical    = 0

        private var mRecyclerView: RecyclerView? = null

        @JvmStatic
        val scrollable: MutableLiveData<Boolean> = MutableLiveData()

        private val mSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                with(event) {
                    dt = (timestamp - mTimeStamp) * NS2S
                    pitchX = values[0].toDouble()
                    pitchY = values[1].toDouble()
                    pitchZ = values[2].toDouble()
                    mTimeStamp = timestamp.toDouble()
                }

                if (dt - mTimeStamp * NS2S != 0.0) {
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

                if (scrollable.value!!) {
                    onTiltListener?.onTilt(
                        horizontal.toFloat(), vertical.toFloat()
                    )
                    mRecyclerView?.smoothScrollBy(-horizontal, -vertical)
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }


        //==============================================================================================
        // Use
        //==============================================================================================
        @JvmStatic
        fun init(context: Context, device: GlassDevice) {
            instance =
                TiltScroller(context, device)

            scrollable.value = true
        }

        @JvmStatic
        fun init(context: Context) {
            init(context, GlassDevice.REALWEAR)
        }

        @JvmStatic
        fun registerSensorListener() {
            mSensorManager.registerListener(
                mSensorListener,
                mGyroSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        @JvmStatic
        fun destroy() {
            mSensorManager.unregisterListener(
                mSensorListener
            )
            init(mContext, mDevice)
        }

        @JvmStatic
        fun setRecyclerView(recyclerView: RecyclerView) {
            mRecyclerView = recyclerView
        }
    }
}