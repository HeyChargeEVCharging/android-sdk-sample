package com.heycharge.sdk.sample.app.admin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.heycharge.androidsdk.core.HeyChargeSDK
import com.heycharge.androidsdk.data.OTACallback
import com.heycharge.androidsdk.domain.Charger
import com.heycharge.sdk.sample.app.R

class OTADialog : DialogFragment(), OTACallback {

    private val args: OTADialogArgs by navArgs()

    private lateinit var otaProgress: TextView
    private lateinit var otaProgressIndicator: LinearProgressIndicator
    private val handler = Handler(Looper.getMainLooper())
    private val goBackRunnable = Runnable {
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_ota, container, false)
        otaProgress = view.findViewById(R.id.otaProgress)
        otaProgressIndicator = view.findViewById(R.id.otaProgressIndicator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chargerJson = args.chargerJson
        val charger = Charger.fromJson(chargerJson)
        if (charger == null) {
            findNavController().popBackStack()
            return
        }
        HeyChargeSDK.chargers().startOtaUpdate(charger, this)
    }

    override fun onProgressUpdated(progress: Int) {
        otaProgress.text = "$progress%"
        otaProgressIndicator.progress = progress
    }

    override fun onUpdateFinished() {
        findNavController().popBackStack()
    }

    override fun onError(e: Exception) {
        Toast.makeText(context, e.toString(), LENGTH_SHORT).show()
        handler.postDelayed(goBackRunnable, 2000)
    }

    override fun onDetach() {
        super.onDetach()
        handler.removeCallbacks(goBackRunnable)
    }
}