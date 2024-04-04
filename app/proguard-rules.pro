-keepattributes SourceFile,LineNumberTable

-keep class com.facebook.** {
   *;
}
-keep public class com.google.android.gms.ads.**{
   public *;
}
-keep public class com.google.ads.**{
   public *;
}



#-keep class com.google.android.gms.internal. { ; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#-keep class com.google.appengine. { ; }

-keepattributes Annotation
-keepattributes Signature
-dontwarn
-ignorewarnings

# Mapbox
#-keep class com.mapbox.mapboxsdk.maps. { ; }
-keep class com.mapbox.android.telemetry.**
-keep class com.mapbox.android.core.location.**
#-keep class android.arch.lifecycle. { ; }
#-keep class com.mapbox.android.core.location. { ; }
#-keep class com.mapbox.mapboxsdk. { ; }
-keep class com.mapbox.mapboxsdk.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel
-keepnames class com.mapbox.mapboxsdk.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel
-keepclassmembers class com.mapbox.mapboxsdk.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel.**{
  *;
    }
-dontwarn com.sun.xml.internal.ws.spi.db.*
#-keep class com.mapbox.mapboxsdk.maps.Telemetry. { *; }
#-keep class com.mapbox.mapboxsdk.plugins.locationlayer. { *; }
#-keep interface com.mapbox.mapboxsdk.plugins.locationlayer. { *; }

-dontwarn com.google.android.gms.**
#-keep class com.google.android.gms. { ; }

#-keep public class com.google.firebase. { public ; }
#-keep class com.google.firebase. { ; }
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
#-keep class org.xmlpull. { ; }
#-keepclassmembers class org.xmlpull. { ; }

 #Keep the Retrofit classes
-keep class retrofit.** { *; }

# Keep the OkHttp classes (if you're using OkHttp as the HTTP client)
-keep class okhttp3.** { *; }

# Keep the Gson classes (if you're using Gson for JSON parsing)
-keep class com.google.gson.** { *; }

# If you use other converters (e.g., Moshi), add rules for them too
-keep class com.squareup.moshi.** { *; }
#-keep class androidx.appcompat.widget. { ; }
# Keep your model classes
-keep class com.live.streetview.navigation.earthmap.compass.map.places.** { *; }


    # Add this global rule
    -keepattributes Signature

    # This rule will properly ProGuard all the model classes in
    # the package com.yourcompany.models. Modify to fit the structure
    # of your app.
    -keepclassmembers class com.live.streetview.navigation.earthmap.compass.map.** {
      *;



    }