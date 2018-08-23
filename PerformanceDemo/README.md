<a name="head"></a>
# O2 Mobile SDK Performance Demo App - for Android 

The purpose of this demo application is to provide you with a fully functioning high performance demonstration of a video-centric app designed to showcase the SDK. Everything you need to know to get started with and use the O2 Mobile SDK, is here. However, please note, that this is not intended to replace the in-depth SDK Tutorial app we provide at the root of this repo. The audience for this demo application is an experienced Android Developer with in-depth knowledge of using Android Studio, the Java Programming Language, Android OS API programming, and Gradle build system.

<a name="requirements"></a>
## Starting Requirements

* **Android Studio 2+**
	* Java source code
	* Gradle build system
	* Includes dependency on ExoPlayer v2.7.3
* **Android mobile device running Android API level 16 or later** (Android OS v5.0/API 19 or later required for adaptive HLS rendering) – including Android TV or Amazon Fire TV set top box devices

<a name="build"></a>
## Build Notes

Open the directory in Android Studio, build and run.  That's it!

<a name="notes"></a>
## Specific Demo App Notes

1. Added `AspectFrameLayout` - container with custom and fixed aspect ratio. In sample app, this is set to 16x9.  
添加`AspectFrameLayout` -  这是一个可自定义的固定长宽比例的页面元素容器. 在sample app中比例被设置为16:9.

2. Main tricks are in the `middleware` package. Here are several classes – (`VideoBufferingRestriction`, `SinglePlaybackRestriction`, `ControlsBehavior`, `AdBufferedInBackground`) that update the view model and state to support some app needs:

  * **`AdBufferedInBackground`** - fix ad buffering issue of inactive video  
  * **`VideoBufferingRestriction`** - fix content buffering issue of inactive video  
  * **`SinglePlaybackRestriction`** - pause inactive video  
  * **`ControlsBehavior`** - update view model to show controls properly  

<p style="margin-left:20px;">主要的修改都在`middleware`包里面. 有几个典型的类 – (`VideoBufferingRestriction`, `SinglePlaybackRestriction`, `ControlsBehavior`, `AdBufferedInBackground`) 这几个类的修改了之前的视图模型和状态，用于提供以下几个功能:</p>

* **`AdBufferedInBackground`** - 用于修复处于非当前播放状态下的视频的广告缓冲问题  
* **`VideoBufferingRestriction`** - 用于修复处于非当前播放状态下的视频的内容缓冲问题  
* **`SinglePlaybackRestriction`** - 用于暂停非当前播放状态下的视频  
* **`ControlsBehavior`** - 修复视图模型以更好地显示播放控制界面  

> These middleware classes are added in `AolRecyclerViewAdapter` and the order of adding these cannot be changed.  
> 这几个`middleware`中介类是添加到`AolRecyclerViewAdapter`中的。添加这几个类的顺序请不要变更，否则会出问题。

3. Layouts of `AolRecyclerViewAdapter` are inflated asynchronously now (faster, less lags).  
AolRecyclerViewAdapter的视图页面现在是异步填充的了，不占用主UI线程，这样会更快，延迟更小。

4. Loading spinner is decorated with fancy background plate, for more details see [this resource](https://github.com/aol-public/OneMobileSDK-examples-android/blob/34ddb2050ac66293d4b37fc21d202fcc75b51985/PerformanceDemo/app/src/main/res/drawable/loading_bg.xml#L1) 
and [this layout](https://github.com/aol-public/OneMobileSDK-examples-android/blob/34ddb2050ac66293d4b37fc21d202fcc75b51985/PerformanceDemo/app/src/main/res/layout/aol_custom_controls.xml#L126)

5. No explicit [orientation](https://github.com/aol-public/OneMobileSDK-examples-android/blob/34ddb2050ac66293d4b37fc21d202fcc75b51985/PerformanceDemo/app/src/main/AndroidManifest.xml#L15) is set for main Activity, but when app goes fullscreen it is using 
[full sensor orientation](https://github.com/aol-public/OneMobileSDK-examples-android/blob/34ddb2050ac66293d4b37fc21d202fcc75b51985/PerformanceDemo/app/src/main/java/com/aol/mobile/sdk/testapp/AolFragment.java#L84) 
for convenience.