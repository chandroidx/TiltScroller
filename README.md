# TiltScroller
Tilt Scrolling for Smart Glass

## Introduction
It makes smart glass possible to scroll and swipe by tilting

## Support
    Realwear
    Vuzix
    Google Glass
    
    maybe other glasses work but not tested

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
TiltScroller.init(context: Context, glass : Glass);

    - Support - 
    REALWEAR,
    VUZIX,
    GOOGLE_GLASS

If you use other devices, try put written above
```

Use
```Kotlin

TiltScroller.registerSensor(OnTiltListener)
            or
TiltScroller.registerSensor(()->Unit)

you must call TiltScroller.unregisterSensor()
```

enable & disable Scrolling
```java
TiltScroller.scrollable = (true|false);
```
