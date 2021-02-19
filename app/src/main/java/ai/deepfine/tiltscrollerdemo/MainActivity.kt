package ai.deepfine.tiltscrollerdemo

import ai.deepfine.ycpark.OnTiltListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ai.deepfine.ycpark.TiltScroller
import android.widget.TextView
import com.ycpark.tiltscrollerdemo.R

class MainActivity : AppCompatActivity(), OnTiltListener {
    private val textView: TextView by lazy {
        findViewById<TextView>(R.id.test)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TiltScroller.init(this)

        TiltScroller.registerSensorListener()

        TiltScroller.onTiltListener = this
    }


    override fun onPause() {
        super.onPause()
        TiltScroller.destroy()
    }

    override fun onTilt(horizontal: Float, vertical: Float) {
        textView.text = "horizontal : $horizontal\nvertical : $vertical"
    }

}