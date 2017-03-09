# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in $ANDROID_SDK\tools\proguard\proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#阿里播放
-keep class com.alivc.player.**{*;}

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontwarn com.alipay.**

-keep class com.tcl.hyt.unionpay.plugin.** {*;}
-keep class com.tct.hz.unionpay.plugin.** {*;}
-keep class com.umpay.creditcard.android.** {*;}
-keep class com.unionpay.mobile.** {*;}
-dontwarn com.unionpay.mobile.android.**

-keep class com.tencent.** {*;}

-keep class com.netease.** {*;}
-keep public class * extends com.bluewhaledt.saylove.network.entities.BaseEntity

# Gson
-keep class com.google.gson.stream.** {*;}
-dontwarn sun.misc.Unsafe

# Retrofit2
-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

-keepclasseswithmembers class * {
  @retrofit2.http.* <methods>;
}

# Okio
-dontwarn okio.**

# OkHttp3
-keep class okhttp3.** {*;}
-keep interface okhttp3.** {*;}

# Rxjava
-dontwarn rx.internal.util.unsafe.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# RenderScript
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class android.support.v8.renderscript.** { *; }

# event-bus  http://greenrobot.org/eventbus/documentation/proguard/
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

# android.support.design
-keep class android.support.design.** {*;}
# android.support.design

-keep public class * implements java.io.Serializable {*;}


-keep class com.alipayzhima.**{*;}
-keep class com.android.moblie.zmxy.antgroup.creditsdk.**{*;}
-keep class com.antgroup.zmxy.mobile.android.container.**{*;}
-keep class com.megvii.livenessdetection.**{*;}
-keep class org.json.alipayzhima.**{*;}

#ijkplayer
-keep class tv.danmaku.ijk.media.player.** {*; }


-ignorewarnings