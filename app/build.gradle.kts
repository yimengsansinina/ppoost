plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    alias(libs.plugins.jetbrainsKotlinAndroidd)
//    id("kotlin-android") // Only for Kotlin projects.
//    id("kotlin-kapt") // Only for Kotlin projects.
//    id("io.objectbox") // Apply
//    id("com.android.application")
//    id("kotlin-android") // Only for Kotlin projects.
//    id("kotlin-kapt") // Only for Kotlin projects.
//    id("io.objectbox") // Apply last.
    id("io.objectbox") // Apply last.
}


android {
    namespace = "com.exp.post"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.exp.post"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.jaeger.statusbarutil:library:1.5.1") {	//所加的第三方框架
        exclude (
        group="com.android.support",
        module="support-compat")
        exclude (
        group="com.android.support",
        module="support-media-compat")
    }
    implementation ("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.14")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.16")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.ogaclejapan.smarttablayout:library:2.0.0@aar")
    implementation ("com.ogaclejapan.smarttablayout:utils-v4:2.0.0@aar")
    implementation ("cn.jzvd:jiaozivideoplayer:7.7.0")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.19.1")
    implementation ("com.tencent:mmkv:1.3.4")
    implementation ("com.google.code.gson:gson:2.2.4")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.13")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("me.gujun.android.taggroup:library:1.4@aar")
    implementation ("org.greenrobot:eventbus:3.2.0")
    implementation  ("com.android.billingclient:billing:7.0.0")
    implementation  ("com.android.billingclient:billing-ktx:7.0.0")
    implementation ("com.wrapp.floatlabelededittext:library:0.0.6")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation("io.objectbox:objectbox-gradle-plugin:4.0.0")

}