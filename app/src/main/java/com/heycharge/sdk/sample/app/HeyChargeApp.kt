package com.heycharge.sdk.sample.app

import android.app.Application
import com.heycharge.androidsdk.core.HeyChargeSDK

class HeyChargeApp : Application() {

    private lateinit var sdkKey: String

    override fun onCreate() {
        super.onCreate()
        sdkKey = "Insert you sdk key here"
        HeyChargeSDK.initialize(applicationContext, sdkKey)
    }
}