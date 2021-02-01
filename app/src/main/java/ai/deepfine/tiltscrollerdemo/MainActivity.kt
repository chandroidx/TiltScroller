package ai.deepfine.tiltscrollerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ai.deepfine.ycpark.TiltScroller
import com.ycpark.tiltscrollerdemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TiltScroller.init(this)

        TiltScroller.registerSensor()

    }


    override fun onPause() {
        super.onPause()
        TiltScroller.destroy()
    }

}