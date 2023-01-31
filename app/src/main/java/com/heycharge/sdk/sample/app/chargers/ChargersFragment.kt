package com.heycharge.sdk.sample.app.chargers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.heycharge.androidsdk.core.HeyChargeSDK
import com.heycharge.androidsdk.data.ChargingCommandCallback
import com.heycharge.androidsdk.data.GetDataCallback
import com.heycharge.androidsdk.domain.Charger
import com.heycharge.sdk.sample.app.R


class ChargersFragment : Fragment(), GetDataCallback<List<Charger>> {

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
        adapter = ChargersAdapter(HeyChargeSDK.chargers(), false) { onButtonClick(it) }
        recyclerView.adapter = adapter
        HeyChargeSDK.chargers()
            .observeChargers(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HeyChargeSDK.chargers()
            .removeChargersObserver(this)
    }

    override fun onGetDataSuccess(data: List<Charger>) {
        adapter.submitList(data)
    }

    override fun onGetDataFailure(exception: Exception) {
        Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT)
            .show()
    }

    private fun onButtonClick(charger: Charger) {
        val sdk = HeyChargeSDK.chargers()
        val isAvailable = sdk.isChargerAvailable(charger)
        val isChargingByUser = sdk.isChargingByUser(charger)
        if (!isAvailable && !isChargingByUser) {
            return
        }
        var title = "Starting charge session"
        if (isChargingByUser) {
            title = "Stopping charge session"
        }
        val dialog = AlertDialog.Builder(requireContext()).setTitle(title).create()
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
        if (isAvailable) {
            HeyChargeSDK.chargers().startCharging(charger, callback)
        } else {
            HeyChargeSDK.chargers().stopCharging(charger, callback)
        }
    }
}