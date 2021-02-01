# TiltScroller
Tilt Scrolling for Smart Glass

## Introduction
It makes smart glass possible to scroll and swipe by tilting

## Support
- minSdkVersion 16
- targetSdkVersion 30

## Setup
Implementation (Latest Release : [![](https://jitpack.io/v/yc-park/TiltScroller.svg)](https://jitpack.io/#yc-park/TiltScroller) )
```javascript
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.yc-park:TiltScroller:Latest Release'
}
```

## How To Use
Initialize in Application class
```Kotlin
TiltScroller.init(context: Context, glassDevice: GlassDevice);

    - Support - 
    REALWEAR,
    VUZIX,
    GOOGLE_GLASS

If you use other devices, try put written above
```

Use
```Kotlin
class MainActivity : AppCompatActivity(), OnTiltListener, OnSwipeListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TiltScroller.onTiltListener = this
    }

    override fun onResume() {
        super.onResume();
        
        TiltScroller.registerSensor();
    }

    override fun void onPause() {
        super.onPause();
        
        TiltScroller.unregisterSensor();
    }

    override fun onTilt(horizontal: Float, vertical: Float) {
        TODO("Not yet implemented")
    }
}
```

enable & disable Scrolling
```java
TiltScroller.scrollable = (true|false);
```
