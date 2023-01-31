package com.heycharge.sdk.sample.app.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.heycharge.androidsdk.core.HeyChargeSDK
import com.heycharge.androidsdk.data.ChargingCommandCallback
import com.heycharge.androidsdk.data.GetDataCallback
import com.heycharge.androidsdk.domain.Charger
import com.heycharge.sdk.sample.app.HomeFragmentDirections
import com.heycharge.sdk.sample.app.R
import com.heycharge.sdk.sample.app.chargers.ChargersAdapter


class AdminChargersFragment : Fragment(), GetDataCallback<List<Charger>> {

    private lateinit var adapter: ChargersAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_chargers, container, false)
        recyclerView = view.findViewById(R.id.chargersRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChargersAdapter(HeyChargeSDK.chargers(), true) { onButtonClick(it) }
        recyclerView.adapter = adapter
        HeyChargeSDK.chargers()
            .observeChargers(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HeyChargeSDK.chargers().removeChargersObserver(this)
    }

    override fun onGetDataSuccess(data: List<Charger>) {
        adapter.submitList(data)
    }

    override fun onGetDataFailure(exception: Exception) {
        Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG)
            .show()
    }


    private fun onButtonClick(charger: Charger) {
        val sdk = HeyChargeSDK.chargers()
        val updateAvailable = sdk.isChargerUpdateAvailable(charger)
//        val onboardingRequired = sdk.isChargerRequiresSetup(charger)
        val onboardingRequired = false
        if (!onboardingRequired && !updateAvailable) {
            return
        }
        if (onboardingRequired) {
            val dialog =
                AlertDialog.Builder(requireContext()).setTitle("Setting up the charger").create()
            dialog.show()
            val callback = object : ChargingCommandCallback {
                override fun onChargingCommandExecuted() {
                    dialog.hide()
                }

                override fun onChargingCommandFailure(exception: java.lang.Exception) {
                    dialog.hide()
                    Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            }
            sdk.startOnboarding(charger, callback)
            return
        }
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToOtaDialog(
                Charger.toJson(charger)
            )
        )
    }
}