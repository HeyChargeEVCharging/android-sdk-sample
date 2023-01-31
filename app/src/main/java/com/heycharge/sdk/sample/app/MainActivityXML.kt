package com.heycharge.sdk.sample.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.heycharge.androidsdk.core.HeyChargeSDK

class MainActivityXML : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        HeyChargeSDK.onResume()
    }

    override fun onPause() {
        super.onPause()
        HeyChargeSDK.onPause()
    }

}