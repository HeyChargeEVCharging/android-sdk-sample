package com.heycharge.sdk.sample.app

import android.app.Application
import com.heycharge.androidsdk.core.HeyChargeSDK

class HeyChargeApp : Application() {

    //    dev
    private val testSdkKey = "KTrCsT64MbSBECjDejVNVKgu35n9t99G"
    //prod
//    private val testSdkKey = "399zzFUHpP6E6IWGcbbnR4st"

    override fun onCreate() {
        super.onCreate()
        HeyChargeSDK.initialize(applicationContext, testSdkKey)
    }
}