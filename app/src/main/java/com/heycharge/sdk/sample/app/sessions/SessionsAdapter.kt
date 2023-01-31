package com.heycharge.sdk.sample.app.sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heycharge.androidsdk.domain.Session
import com.heycharge.sdk.sample.app.R
import java.text.SimpleDateFormat
import java.util.*

class SessionsAdapter :
    ListAdapter<Session, SessionsAdapter.SessionViewHolder>(SessionDiff()) {

    private val simpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session_item, parent, false)
        return SessionViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SessionViewHolder(private val sessionView: View) :
        RecyclerView.ViewHolder(sessionView) {

        fun bind(session: Session) = with(session) {
            val sessionDateTextView = sessionView.findViewById<AppCompatTextView>(R.id.sessionDate)
            val sessionChargerNameTextView =
                sessionView.findViewById<AppCompatTextView>(R.id.sessionChargerName)
            val sessionChargedTextView =
                sessionView.findViewById<AppCompatTextView>(R.id.sessionCharged)
            val time = simpleDateFormat.format(Date(endDate))
            sessionDateTextView.text = time
            sessionChargerNameTextView.text = session.chargerName
            sessionChargedTextView.text = String.format("%.2f", (session.chargeAmount / 1000))
        }
    }

}


internal class SessionDiff : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem == newItem
    }
}