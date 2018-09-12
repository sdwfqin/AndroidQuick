
#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#############################################
#
# 第三方开源库混淆配置
#
#############################################

-dontwarn android.arch.**
-keep class android.arch.** { *; }
-dontwarn com.alibaba.android.vlayout.**
-keep class com.alibaba.android.vlayout.** { *; }
-dontwarn android.support.**
-keep class android.support.** { *; }
-dontwarn androidx.constraintlayout.solver.widgets.**
-keep class androidx.constraintlayout.solver.widgets.** { *; }
-dontwarn com.blankj.utilcode.**
-keep class com.blankj.utilcode.** { *; }
-dontwarn com.bigkoo.pickerview.**
-keep class com.bigkoo.pickerview.** { *; }
-dontwarn com.contrarywind.**
-keep class com.contrarywind.** { *; }
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *; }
-dontwarn com.github.chrisbanes.photoview.**
-keep class com.github.chrisbanes.photoview.** { *; }
-dontwarn com.chad.library.**
-keep class com.chad.library.** { *; }
-dontwarn com.luck.picture.lib.**
-keep class com.luck.picture.lib.** { *; }
-dontwarn com.yalantis.ucrop.**
-keep class com.yalantis.ucrop.** { *; }
-dontwarn com.mcxtzhang.swipemenulib.**
-keep class com.mcxtzhang.swipemenulib.** { *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *; }
-dontwarn com.just.agentweb.**
-keep class com.just.agentweb.** { *; }
-dontwarn com.otaliastudios.cameraview.**
-keep class com.otaliastudios.cameraview.** { *; }
-dontwarn com.qmuiteam.qmui.**
-keep class com.qmuiteam.qmui.** { *; }
-dontwarn com.scwang.smartrefresh.layout.**
-keep class com.scwang.smartrefresh.layout.** { *; }
-dontwarn me.imid.swipebacklayout.lib.**
-keep class me.imid.swipebacklayout.lib.** { *; }
-dontwarn org.reactivestreams.**
-keep class org.reactivestreams.** { *; }
-dontwarn com.tencent.**
-keep class dagger.** { *; }
-dontwarn dagger.**
-keep class com.tencent.** { *; }
-dontwarn com.alipay.**
-keep class com.alipay.** { *; }
-keep class com.sdwfqin.widget.** { *; }
-keep class com.sdwfqin.quicklib.** { *; }
-keep class com.sdwfqin.qrscan.** { *; }
-keep class com.sdwfqin.paylib.** { *; }
-keep class com.sdwfqin.imageloader.** { *; }

#============================Retrofit===========================
# Retrofit does reflection on generic parameters and InnerClass is required to use Signature.
-keepattributes Signature, InnerClasses
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

#============================EventBus===========================
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#============================butterknife===========================
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#============================RxJava===========================
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
-dontwarn io.reactivex.**
-keep class io.reactivex.**{*;}
#============================OkHttp===========================
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
#============================OkIo===========================
-dontwarn okio.**
-keep class okio.**{*;}