package ai.deepfine.tiltscrollerdemo

import ai.deepfine.ycpark.OnTiltListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ai.deepfine.ycpark.TiltScroller
import android.util.Log
import com.ycpark.tiltscrollerdemo.R

class MainActivity : AppCompatActivity(), OnTiltListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TiltScroller.init(this)

        TiltScroller.registerSensor()

        TiltScroller.onTiltListener = this
    }


    override fun onPause() {
        super.onPause()
        TiltScroller.destroy()
    }

    override fun onTilt(horizontal: Float, vertical: Float) {
        Log.d("PYC", "horizontal : " + horizontal + "\t" + "vertical : " + vertical)
    }

}