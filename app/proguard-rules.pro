# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# ---- Hilt ----
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ---- Navigation Component ----
-keepnames class com.agenda.app.presentation.**
-keepnames class * implements android.os.Parcelable
-keepnames class * implements java.io.Serializable

# ---- Coroutines ----
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ---- Kotlin ----
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keep class kotlin.Metadata { *; }

# ---- App Models (keep for Parcelable/Serializable) ----
-keep class com.agenda.app.domain.model.** { *; }
-keep class com.agenda.app.data.local.database.entity.** { *; }
