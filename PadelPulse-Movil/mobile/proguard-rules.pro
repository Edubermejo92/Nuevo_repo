# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# La clase puente se llama AndroidBridge (antes WebAppInterface). Sin esta regla
# correcta, activar minify romperia en silencio TODA la comunicacion JS <-> Kotlin.
-keep class padelpulseapp2.netlify.app.MainActivity$AndroidBridge { *; }
-keepclassmembers class padelpulseapp2.netlify.app.MainActivity$AndroidBridge {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class * extends androidx.activity.ComponentActivity { *; }
-keepclassmembers class * extends androidx.activity.ComponentActivity {
    <init>(...);
}

-dontwarn android.webkit.**
