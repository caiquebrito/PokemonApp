# Preserve stack trace line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.coroutines.SafeContinuation { *; }

# Retrofit — keep service interfaces and their annotations
-keepattributes Signature, Exceptions, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson / JSON (if used by Retrofit converters)
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Domain and remote response models — must survive obfuscation so Retrofit/Gson
# can deserialise JSON into them.
-keep class com.ctb.domain.models.** { *; }
-keep class com.ctb.data_remote.response.** { *; }

# Koin — reflection-free; no extra rules needed.
-dontwarn org.koin.**

# Coil — image loading
-dontwarn coil.**

# Compose — the compiler plugin handles most of this; just silence known warns.
-dontwarn androidx.compose.**
