package ai.deepfine.ycpark

import ai.deepfine.ycpark.Axis.AXIS_X
import ai.deepfine.ycpark.Axis.AXIS_Y
import ai.deepfine.ycpark.Axis.AXIS_Z

/**
 * @Description Class
 * @author yc.park (DEEP.FINE)
 * @since 2021-02-01
 * @version 1.0.0
 */
enum class Glass(val horizontal : Int, val vertical : Int) {
    REALWEAR(AXIS_Y, AXIS_X),
    VUZIX(AXIS_Y, AXIS_X),
    GOOGLE_GLASS(AXIS_Z , AXIS_X)
}

object Axis {
    const val AXIS_X = 0
    const val AXIS_Y = 1
    const val AXIS_Z = 2
}