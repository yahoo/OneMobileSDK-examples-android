<a name="head"></a>
# O2 Mobile SDK Tutorial - for Android

Welcome to the O2 Mobile SDK (OMSDK or SDK). The purpose of this document is to provide you with everything you need to know to get started with and use the O2 Mobile SDK. The audience for this document is an experienced Android Developer with in-depth knowledge of using Android Studio, the Java Programming Language, Android OS API programming, and Gradle build system.

This document will describe basic concepts and then will link you to sample projects and code, that are kept up-to-date with the latest versions of the the developer tools, API, and language we support.

As always, we highly appreciate, welcome, and value all feedback on this documentation or the OMSDK in any way, shape, or form. If you have any suggestions for corrections, additions, or any further clarity, please don’t hesitate to email the [Video Support Team](mailto:video.support@oath.com).

If you want to see the code - go to this [section](#qs)!

## Table of Contents

[O2 Mobile SDK Tutorial - for Android](#head)
1. [What is the O2 Mobile SDK?](#what)
2. [Main SDK Features](#features)
3. [Why would I use the O2 Mobile SDK?](#why)
4. [Advertising Info and User Tracking](#privacy)
5. [Starting Requirements](#requirements)
6. [Onboarding your Apps for SDK Authentication](#onboarding)
7. [High-Level Architecture Overview](#architecture)
8. [How the SDK works](#how)
9. [Default (Player) Controls UX](#ux)
10. [TLDR: Quick Start](#qs)
11. [Tutorial 1 – Playing Videos](#t1)
	1. [Setting default player controls’ tint color](#t11)
	2. [Playing with AutoPlay on/off](#t12)
	3. [Playing Muted](#t13)
	4. [Disabling HLS (or forcing MP4 playback)](#t14)
12. [Tutorial 2 – Customizing the Default Controls UX](#t2)
	1. [Hiding Various Controls buttons](#t21)
	2. [Closed Captioning / SAP Settings button](#t22)
	3. [Using the 4 Custom Sidebar buttons](#t23)
	4. [Setting the LIVE indicator’s tint color](#t24)
13. [Tutorial 3 – Observing the Player](#t3)
	1. [Pausing or Resume Playback and Seeking](#t31)
	2. [Looping Playback](#t32)
	3. [LIVE, VOD, or 360°?](#live-vod-or-360)
	4. [Manually Hooking up Previous or Next Videos](#t34)
14. [Tutorial 4 – Error Handling in the SDK](#t4)
	1. [SDK Initialization Errors](#t41)
	2. [Player Initialization Errors](#t42)
	3. [Restricted Videos](#t43)
15. [Specific Notes for Android TV and Amazon Fire TV Apps](#firetv)
	1. [Tutorial 5 – Remote Control Support for TV Consoles](#t5)
16. [Next Steps](#go2)
	1. [Getting O2 Video/Playlist IDs into your apps](#getting-o2-videoplaylist-ids-into-your-apps)
	2. [Controlling Ads via your O2 Portal Account](#controlling-ads-via-your-o2-portal-account)



<a name="what"></a>
## What is the O2 Mobile SDK?

The O2 Mobile SDK (OMSDK or SDK) is a native Android SDK with the sole purpose for playing and monetizing videos from the Oath O2 video platform in your app. The OMSDK is written in Swift and is delivered as a framework. You can include this in your app projects either via your Gradle build script.

As part of playing videos, the OMSDK also handles video ads (pre-roll, mid-roll, and post-roll) and associated videos and ads playback and performance analytics. Analytics are focused on tracking what is played, how far it is played (e.g., deciles, quartiles), and details about the actual device or network. For more details on the analytics supported or to access the analytics data, you will work with the [Video Support Team](mailto:video.support@oath.com) to build reports that focus specifically on your app’s video and ads performance.

The SDK includes a complete default video player controls UX (user experience), which includes a limited albeit robust set of customization options. The controls implementation is fully open source, and the SDK architecture allows for you to include your own fully customized controls UX, should you not be interested in the built-in default one.

<a name="features"></a>
## Main SDK Features

* Playback of one or more individual videos or a single playlist of videos
* Video playback of VOD (video on demand), 360°, and LIVE streaming video types
* Supports either .mp4 or .m3u8 (HLS) formats
* Video ads (VAST support of .mp4 format only)
* Tap an ad to access ad URL (more information) via an in-app-browser
* Full video and ads analytics
* Default video player controls UX (full source code open sourced)
* HLS support for multiple closed captioning (CC) and SAP audio languages
* Mute/Unmute support
* Automatic filtering of geo-restricted videos
* Complete apps control of the frame where videos play
* Google ChromeCast support

<a name="why"></a>
## Why would I use the O2 Mobile SDK?

The O2 Mobile SDK is used to natively play O2 videos. If you have a native app, you should use the SDK. The main reason as to why you’d want to use the SDK, is because you get all the ads and analytics for free. The ads are important for monetization. Analytics are important for tracking your app’s video and video ads performance and usage. This helps you understand what your users are watching with your app, and how much.

There are several technical advantages to using the native OMSDK over a web player-based solution. We won’t go into these in depth in this document, but here are some of the advantages:
* Performance
* Mobile network awareness
* Frugal memory, thread, and device resource usage
* Security – webviews / embedded browsers are known weak points
* Fine-grained control, less limits
* Some platforms don’t have webviews or webviews are inconsistent and unreliable (e.g., Fire TV)
* More customization options

<a name="privacy"></a>
## Advertising Info and User Tracking

The O2 Mobile SDK does not track anything that is not related to playing videos or video ads. We use the IDFA (ID for advertisers) value and respect the user's settings for Limit Ad Tracking. The device geolocation is determined by our backend video servers based on IP address, for the purposes of determining and filtering out content that is geo-restricted by content owners. The SDK does not explicitly use the built-in Location Services APIs, and thus does not require your users to grant access to device location data.

<a name="requirements"></a>
## Starting Requirements

* **Android Studio 2+**
	* **Java source code** \*
	* **Gradle build system**
	* **Includes dependency on ExoPlayer v2.5.2**
* **Android mobile device running Android API level 16 or later (Android OS v5.0/API 19 or later required for adaptive HLS rendering) – including Android TV or Amazon Fire TV set top box devices**
* **Onboarded application package name**

**\* Note**: Future Kotlin support is currently being evaluated.

<a name="onboarding"></a>
## Onboarding your Apps for SDK Authentication

In order for the OMSDK to authenticate itself for video playback within your app, we pass the containing app’s unique App Store package name to our back end service. You need to email the [Video Support Team](mailto:video.support@oath.com) to register all of your app package names for OMSDK usage. You can also register multiple package names against your same app entity. Possible reasons to do this, is to allow for a dev/test app package name or an enterprise package name, that can co-exist on a device alongside your production app. Also, both iOS (bundle ID) and Android app package names can either be the same or different – for the same app. Registration not only authenticates your application, but it ensures your backend video and ads analytics are all configured properly. In addition, this registration information also defines all the video content your app is allowed to playback through the SDK.

The sample projects are all set up to use the following test-only package name:

`com.aol.mobile.one.testapp`

<a name="architecture"></a>
## High-Level Architecture Overview

At a high-level, the OMSDK architecture is composed of the following components:
* SDK Core
* O2 VRM (video rights management) VAST Ads Engine
* Video and Ads Analytics module
* A set of Video Renderers that are either built-in (e.g., flat 2D and RYOT 360°) or external plugins (e.g., Verizon Envrmnt 360°, custom, etc.)
* ChromeCast module
* Default video player controls UX implementation

Our modular approach makes it easy for us (or you) to add new renderers in the future, or for you to add your own custom video player controls UX implementation. Under the hood, we rely on Google’s ExoPlayer to handle the actual video playback.

<a name="how"></a>
## How the SDK works

At a very basic level, the OMSDK controls only the video frame. Because of this, you are completely in control of your app’s design and UX (look and feel). You can control whether videos play in a small view, in-place over a thumbnail image, or at full-screen. Your app also has complete control over device rotation, use of view/navigation controllers, scrollers, and any transitions between them. The SDK does not dictate any overall visual design or behavior on your app.

If you choose to use the SDK’s built-in default player controls UX implementation, then that part of the video UX is imposed on you. All controls rendering is also done within the frame you provide for the video. Regardless of which controls UX you use, we currently do not allow any customization or overriding of the ads playback UX (which is different from the normal video playback UX), so that visual interface is dictated, and you cannot override it. Future customization options are planned here.

To play a video, you follow these very basic steps:
1. Initialize an instance of the OMSDK
2. Initialize a new `Player` object with a video ID
3. Bind it with a `PlayerView`
4. Play it

**That’s it!**

Behind the scenes, what happens is this … the initialization of an instance of the SDK takes your app’s bundle ID, and passes it to our back-end micro services to authenticate your application for video playback within the O2 video platform. The server passes back all the necessary playback and authentication information down to that instance of the SDK, for use during it’s lifespan. When you construct a new `Player` object from that SDK instance, it communicates with our micro services to obtain all the necessary video metadata (e.g., thumbnails and video URLs, duration, etc.). This `Player` object will play and replay the associated video until deinitialized.

More specifically, before a video plays, the SDK’s Ads Engine tries to fulfill a pre-roll ad. While the request for an ad is being processed, the video starts buffering in the background. If an ad is returned in a reasonable amount of time, the `Player` plays the ad using the built-in ads UX. When the ad finishes, the video playback begins. If no ad is to be shown, or the ad request times out, the video playback begins directly.

The runtime circumstances and algorithm for getting an ad or not, are not in the scope of this documentation. Suffice to say, there are many considerations to this (e.g., content owner/seller rules, geolocation, frequency capping, etc.). For more information and details on how ads are served to the OMSDK, please email the [Video Support Team](mailto:video.support@oath.com).

**Note**: The SDK only operates with an active network connection – without it, you will not be able to do anything.

<a name="ux"></a>
## Default (Player) Controls UX

The default player controls UX contains the following elements:
* Play/Pause/Replay button (with loading animation)
* ± 10 second skip buttons
* Previous and Next buttons
* Seekbar
* Video title
* Elapsed time
* Video duration
* LIVE indicator
* 360° View Orientation Compass / Orientation Reset button
* Closed Captioning/SAP Settings button
* ChromeCast button
* 4 app-custom sidebar buttons

This video controls implementation allows for a few runtime customizations, that you can set on a player by player basis. This includes:
* Setting the tint color for the controls (e.g., matching your app’s brand)
* Hiding various elements of the controls (useful for smaller view versus full-screen playback)
* Setting any of the 4 app-custom sidebar buttons

The built-in tint color of the default video player controls UX is <span style="color:magenta">pink/magenta</span>. This is deliberate; feel free to keep it or change it to mesh with your app’s design or brand theme. The built-in tint color of the ad’s UX is <span style="color:gold">yellow/gold</span>. This cannot be changed at this time. Because of this, we advise that you don’t tint your main controls yellow as well. You also don’t want to use black or gray, because of video contrast, your controls will not be very visible. White, on the other hand is fair game. There is a slightly darkened canvas that is layered above the video, but below the controls layer, that helps make the controls pop out more.

The player controls are shown under several well-established circumstances. This includes whenever the video is paused, such as before a video starts (with AutoPlay off) or while buffering, after any video finishes (with AutoPlay off), or after all videos linked to the player finish. They also will display (fade in) on demand whenever a video is tapped. If a video is actively playing, the controls will automatically hide (fade out) after a predetermined number of seconds. At any time the controls are shown, they can be quickly hidden by tapping on the video (not over a button, of course).

The default player controls UX implementation includes 4 optional app-specific sidebar buttons. You can set any or all of these to use as you see fit. This was built to allow for app-specific video overlay customization in anticipation for up to 4 new behaviors. Because these 4 sidebar buttons are built right into the default controls UX, they will automatically fade in/out with the rest of the video controls. There is no need to handle any of that logic or attempt to synchronize to the animation timings.

The complete implementation of the default player controls UX is open-source and provided to serve as an implementation sample of its own. Feel free to inspect it, copy it, modify it at will.

The default Android Custom Controls UX implementation repo can be found here: 
[O2 Mobile SDK Controls for Android](https://github.com/aol-public/OneMobileSDK-controls-android)

<a name="qs"></a>
## TLDR: Quick Start

Want to dive right in, quickly and directly, you can jump here to get started using our legacy documentation: 
[Getting Started for Android](https://github.com/aol-public/OneMobileSDK-releases-android/blob/maven/README.md)

<a name="t1"></a>
## Tutorial 1 – Playing Videos

This tutorial sample shows you how to quickly init the OMSDK and play videos using all the default options and behaviors, with very little code.  Playing a single video, a list of individual videos, or videos from an O2 Playlist are all done the same way.  The only difference between playing a single video or multiple videos is that the SDK strings multiple videos together, connects up the previous and next player controls UX buttons, and if AutoPlay is on - plays them straight through.

##### _Tutorial Samples:_

> [Play Video](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/PlayVideoActivity.java)
>
> [Play Array of Videos](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/PlayArrayActivity.java)
>
> [Play Video Playlist](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/PlayPlaylistActivity.java)

<a name="t11"></a>
#### Setting default player controls’ tint color

The built-in tint color of the default video player controls UX is pink/magenta.  This is deliberate.  You set the main and accent tint colors of the default player controls by setting these in the `PlayerView` along with the `PlayerControlsView` inside it.  In this sample, you’ll find a code block that shows you how to override the default controls colors.

##### _Tutorial Sample:_

> [Setting Controls’ Tint Color](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/SetTintColorActivity.java)

<a name="t12"></a>
#### Playing with AutoPlay on/off

By default, the SDK plays videos with AutoPlay mode on.  This means, that as soon as you construct a player, the first video queues to play immediately (first, calling for an ad, of course).  In this case, no further user action is required.  As soon as the ad or the video is ready to play, it will.  To override this behavior and turn off AutoPlay, look for the alternate way to construct the Player in this sample.

If AutoPlay mode is off, the user will have to tap the play button to start the playback process. Alternatively, you can programmatically do this by controlling the Player object.

##### _Tutorial Sample:_

> [Turning Off AutoPlay](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/AutoplayOffActivity.java)

<a name="t13"></a>
#### Playing Muted

You can easily control the mute state of the `Player` object. In this sample, you’ll find a code block that shows you how to set the mute state of the `Player` object.
```java
player.setMute(true);
player.setMute(false);
```

##### _Tutorial Sample:_

> [Controlling Mute State](https://github.com/aol-public/OneMobileSDK-examples-android/blob/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/one/PlayMutedActivity.java)

<a name="t14"></a>
#### Disabling HLS (or forcing MP4 playback)

Many (but not all) of the videos in the O2 video platform, have multiple renditions. There may be some set of circumstances where you do not want to use HLS (.m3u8) renditions, and therefore, want to force the alternate high resolution .mp4 rendition. As a result, our SDK has the ability to override or disable getting the default HLS rendition. Look for this alternate initialization code in this tutorial sample for an example of how to programmatically control this.

<a name="t2"></a>
## Tutorial 2 – Customizing the Default Controls UX

This tutorial sample shows you how to further modify the default controls UX.

<a name="t21"></a>
#### Hiding Various Controls buttons

You can change the look of the default controls UX on a player-by-player basis to suit your app design needs. The elements that can be hidden include:
* ± 10 second skip buttons
* Previous and Next buttons
* Seekbar
* Video title
* Elapsed time
* Video duration
* Closed Captioning/SAP Settings button
* ChromeCast button

If you hide the title, and bottom element buttons such as CC/SAP and ChromeCast, the seekbar will fall down closer to the bottom of the video frame, to cover the gap usually left for those elements. See this tutorial for examples of how to hide/show these elements.

##### _Tutorial Sample:_

> [Hide Buttons](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/HiddenButtonsControlsActivity.java)

<a name="t22"></a>
#### Closed Captioning / SAP Settings button

This new feature of the OMSDK is generally dependent on having this information in the HLS stream. There are ways to filter out what CC languages and SAP audio tracks are available. Also, there’s a way to control what the choices are for a given video. One reason to control this may be to implement a “sticky” closed captioning setting. By default, turning CC on only applies the the current playing video. A next or previous video would not have CC on by default. If you wanted your app to support a sticky setting for this, you would do it yourself. This part of this tutorial will show you how to accomplish this.

##### _Tutorial Sample:_

> [Modify CC/SAP Settings Button](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/ModifiedCcSapActivity.java)

<a name="t23"></a>
#### Using the 4 Custom Sidebar buttons

Use this sample to see how to add custom code and behaviors to one of the 4 sidebar buttons. The Sidebar buttons are part of the default player controls UX and are there for you to add up to 4 different overlays/behaviors to your player. You provide the button graphics – icons for normal, selected, and highlighted modes, and you provide a handler to be called in the case of a button tap. The SDK will handle showing/hiding the buttons along with the other player controls.

##### _Tutorial Samples:_

> [Sidebar Buttons](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/SidebarButtonsActivity.java)
>
> [Sidbar Volume Controls](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/SidebarVolumeActivity.java)
>
> [Sidbar Fullscreen Button](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/SidebarFullscreenActivity.java)

<a name="t24"></a>
#### Setting the LIVE indicator’s tint color

The LIVE indicator only appears during a LIVE streaming video playback. This will not appear for a video on demand video. Part of the LIVE indicator is the ability to colorize the • that appears next to the LIVE indicator. In general, you may want to use a standard pure-red color. However, it’s possible that you want to use your app’s brand color or while here instead. You can use black or any dark-gray color, but that is ill advised, because of the general nature of video to have lots of blacks in it. The sample code in this example shows how to set this.

##### _Tutorial Sample:_

> [Tinting the LIVE Indicator](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/two/LiveIndicatorTintActivity.java)

<a name="t3"></a>
## Tutorial 3 – Observing the Player

This tutorial sample shows you how to observe just about everything you can observe from OMSDK `Player` objects. As you would suspect, many properties that can be observed, can also be set or manipulated.

<a name="t31"></a>
#### Current Playback State and Position

Determining the current state of the `Player` is a key need for apps … most app-level video playback logic starts here. In addition to the play/pause state, also includes the current position. Once you can query for these property values, you can also programmatically modify them.

##### _Tutorial Sample:_

> [Getting Current Playback State](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/three/CurrentStateActivity.java)

#### Pausing or Resume Playback and Seeking #####

##### _Tutorial Sample:_

> [Pause / Seek / Play](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/three/PauseSeekPlayActivity.java)

<a name="t32"></a>
#### Looping Playback

If your app has some need to loop a `Player` (one video or many), such as running a kiosk-style interface, for example. This is an easy operation to accomplish with the OMSDK. Look in this example, to see how to determine when playback finishes, and how to reset the video index back to the first video and start it over.

##### _Tutorial Sample:_

> [Looping Playback](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/three/LoopPlaybackActivity.java)

<a name="live-vod-360"></a>
#### LIVE, VOD, or 360°?

You may need to inspect some more metadata on the video, such as what type of video this is – LIVE, video on demand, or 360°. This tutorial sample shows how to inspect this. You may need to make certain app design or UX decisions accordingly, based on the type of video that’s currently playing.

##### _Tutorial Sample:_

> [Inspecting Video Type](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/three/InspectVideoTypeActivity.java)

<a name="t34"></a>
#### Manually Hooking up Previous or Next Videos

There are many legitimate app UX circumstance, that can dictate the dynamicness of a video player – meaning, that not every app design will simply be setup to operate off fixed playlists or lists of videos. As such, the Player can be modified on the fly to dynamically handle what video is played when the previous or next buttons are tapped. This example tutorial has sample code that shows you precisely how to do this. However, be judicious with the usage of this behavior, and make sure it matches a natural flow of content for the user.

##### _Tutorial Sample:_

> _sample coming soon_

<a name="t4"></a>
## Tutorial 4 – Error Handling in the SDK

This tutorial sample shows you how to handle various different types of errors that can occur when using the OMSDK and how to catch and identify them. How you handle these in your app is up to you. The SDK is designed to either return a valid SDK or Player instance otherwise it returns an error. There is no middle ground. If you don’t get a valid instance, you should look at the error result instead to determine why. This section describes some common issues.

<a name="t41"></a>
#### SDK Initialization Errors

For various reasons, the SDK may fail to initialize. The most common reason for this, is you’re trying to use the OMSDK without first having [onboarded your app’s bundle ID](). In this case, you’ll get an error that looks like something like this:
```
{
  "error": "Not found - com.company.ungregisteredapp"
} 
```

##### _Tutorial Sample:_

> [SDK Initialization Errors](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/four/SDKErrorsActivity.java)

<a name="t42"></a>
#### Player Initialization Errors

For various reasons, the `Player` may fail to initialize. 

##### _Tutorial Sample:_

> [Player Initialization Errors](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/four/PlayerErrorsActivity.java)

<a name="t43"></a>
#### Restricted Videos

Videos can be restricted for playback in two very distinct ways. The first is geo restricted content. The second is device restricted content. If you’re attempting to initialize a `Player` with content that’s restricted against your device or geolocation, that content is automatically filtered out. Only valid, playable video IDs are accepted, and have their metadata pulled into the `Player` instance. If you end up with no `Player` instance, it’s because there are no valid video IDs for it to operate on. So, you get an error this this effect.

##### _Tutorial Sample:_

> [Handline Restricted Videos](https://github.com/aol-public/OneMobileSDK-examples-android/tree/master/app/src/main/java/com/aol/mobile/sdk/testapp/tutorials/four/RestrictedVideoActivity.java)

<a name="firetv"></a>
## Specific Notes for Android TV and Amazon Fire TV Apps

The OMSDK supports Android TV and Amazon Fire TV devices with the same source library as Android.  Besides not having a finger to tap on the screen, the biggest difference for Android TV or Fire TV is that you have to deal with remote-control support.

Because there is no way to *tap* on the screen, you cannot access the ad URL.  And if you could, you may not even have a webview available on the device that would support properly rendering the ad URL.

<a name="t5"></a>
### Tutorial 5 – Remote Control Support for TV Consoles

Since there is no SDK difference for mobile devices vs. TV console devices, all the capabilities described above in [Tutorial 1](#t1) are still valid. However, there are a couple subtle differences that are required for proper remote control support on the Android TV and Amazon Fire TV smart TVs or console devices.

##### _Tutorial Sample:_

> _sample coming soon_

<a name="go2"></a>
## Next Steps

### Getting O2 Video/Playlist IDs into your apps

The OMSDK operates on O2 video and playlist IDs. That said, it is the application’s responsibility to dynamically acquire video IDs and/or playlist IDs either via your own CMS (content management system) or perhaps via a direct O2 Search API call. Since apps are generally dynamic in their content (video or otherwise), you need to figure out how to deliver these content IDs into your app, so they can be passed to the SDK to play against. Although unadvised, the easiest possible approach is to hardcode one or more playlist ID[s] into an app, and let those playlists dynamically change their content via the O2 Portal. The upside to this is you don’t need a CMS or further server communications on your end to get video information into your app, and thus to the SDK. The downside, is that if any of those IDs are ever deleted, the app will virtually be useless in terms of O2 video playback.

For more information about the O2 Search API, the O2 Portal, or creation and manipulation of playlists, please email the [Video Support Team](mailto:video.support@oath.com).

### Controlling Ads via your O2 Portal Account

You have some options with respect to ads and the OMSDK.  During early development, your developers are going to want ads disabled because they’re intrusive to the development process, and unnecessary.  Before you launch, you will likely want to see test or Public Service Announcement (PSA) ads enabled all the time, so you can get a feel for how ads will impact your users in various parts of your app.  And, as you launch, you’ll want to enable live production ads for your app, so you’re ready to go as soon as your app passes through the App Store submission process.

To make changes to the ads settings for your app, please contact [Video Support Team](mailto:video.support@oath.com) and they’ll promptly assist you.
