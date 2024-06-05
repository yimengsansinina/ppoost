
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
buildscript {
//    repositories {
//        jcenter()
//        google()
//        mavenCentral()
//    }
    dependencies {
//        classpath("com.android.tools.build:gradle:7.3.0")
        classpath("io.objectbox:objectbox-gradle-plugin:4.0.0")
        // Add your other classpath dependencies here
    }
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}