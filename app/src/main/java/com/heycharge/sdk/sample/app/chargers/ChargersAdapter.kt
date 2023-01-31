package com.heycharge.sdk.sample.app.chargers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heycharge.androidsdk.core.ChargerSDK
import com.heycharge.androidsdk.domain.Charger
import com.heycharge.sdk.sample.app.R

class ChargersAdapter(
    private val chargerSDK: ChargerSDK,
    private val isAdminTab: Boolean,
    private val onButtonClick: (Charger) -> Unit
) :
    ListAdapter<Charger, ChargersAdapter.ChargerViewHolder>(ChargerDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.charger_item, parent, false)
        return ChargerViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ChargerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChargerViewHolder(private val chargerView: View) :
        RecyclerView.ViewHolder(chargerView) {
        fun bind(charger: Charger) = with(charger) {
            val chargerNameTextView = chargerView.findViewById<TextView>(R.id.chargerName)
            val chargerStatusTextView = chargerView.findViewById<TextView>(R.id.chargerStatus)
            val chargerButton = chargerView.findViewById<AppCompatButton>(R.id.chargerButton)
            var buttonText = "Not available"
            var statusText = "Not in range"
            var buttonEnabled = false
            chargerButton.visibility = View.GONE
            if (isAdminTab) {
                if (chargerSDK.isChargerRequiresSetup(charger)) {
                    statusText = "Not onboarded"
                    buttonText = "Complete setup"
                    buttonEnabled = true
                    chargerButton.visibility = View.VISIBLE
                } else if (chargerSDK.isChargerUpdateAvailable(charger)) {
                    statusText = "Update available"
                    buttonText = "Update"
                    buttonEnabled = true
                    chargerButton.visibility = View.VISIBLE
                } else {
                    statusText = charger.bluetoothState.toString()
                }
            } else {
                chargerButton.visibility = View.VISIBLE
                if (chargerSDK.isChargerAvailable(charger)) {
                    buttonText = "Star charging"
                    statusText = "Available"
                    buttonEnabled = true
                }
                if (chargerSDK.isChargerBusy(charger)) {
                    statusText = "In use"
                }
                if (chargerSDK.isChargingByUser(charger)) {
                    buttonText = "Stop charging"
                    statusText = "In use - self"
                    buttonEnabled = true
                }
                if (chargerSDK.isChargerRequiresSetup(charger)) {
                    statusText = "Not onboarded"
                    buttonText = "For admins only"
                    buttonEnabled = false
                }
            }
            chargerNameTextView.text = charger.name
            chargerStatusTextView.text = statusText
            chargerButton.text = buttonText
            chargerButton.isEnabled = buttonEnabled
            if (buttonEnabled) {
                chargerButton.setOnClickListener {
                    onButtonClick(charger)
                }
            }
        }
    }

    internal class ChargerDiff : DiffUtil.ItemCallback<Charger>() {
        override fun areItemsTheSame(oldItem: Charger, newItem: Charger) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Charger, newItem: Charger) =
            oldItem == newItem
    }
}