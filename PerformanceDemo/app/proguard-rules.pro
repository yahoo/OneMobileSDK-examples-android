-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepparameternames
-renamesourcefileattribute SourceFile

-dontwarn java.lang.invoke.**
-dontwarn com.aol.mobile.sdk.annotations.**
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.duktape.**
-dontwarn com.google.android.gms.**

-dontnote com.aol.mobile.sdk.**
-dontnote com.google.android.exoplayer2.**
-dontnote com.squareup.picasso.**
-dontnote android.net.http.**
-dontnote android.support.**
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-keepattributes Exceptions

-keep public class com.aol.mobile.sdk.**

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class **.BuildConfig* {
    public static <fields>;
}
