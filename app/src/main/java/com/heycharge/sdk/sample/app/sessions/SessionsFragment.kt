package com.heycharge.sdk.sample.app.sessions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.heycharge.androidsdk.core.HeyChargeSDK
import com.heycharge.androidsdk.data.GetDataCallback
import com.heycharge.androidsdk.domain.Session
import com.heycharge.sdk.sample.app.R
import java.text.SimpleDateFormat
import java.util.*

class SessionsFragment : Fragment(), GetDataCallback<List<Session>> {

    private lateinit var adapter: SessionsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var pickPeriodButton: AppCompatButton
    private lateinit var pickedPeriod: AppCompatTextView
    private val simpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_sessions, container, false)
        recyclerView = view.findViewById(R.id.sessionsRecyclerView)
        pickPeriodButton = view.findViewById(R.id.pickPeriodButton)
        pickedPeriod = view.findViewById(R.id.pickedPeriod)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SessionsAdapter()
        recyclerView.adapter = adapter
        pickPeriodButton.setOnClickListener {
            openDatePicker()
        }
        onDatePicked(getInitialStartDate())
    }

    private fun openDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, pickedYear, pickedMonthOfYear, pickedDayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, pickedDayOfMonth)
                calendar.set(Calendar.MONTH, pickedMonthOfYear)
                calendar.set(Calendar.YEAR, pickedYear)
                val periodFrom = calendar.time.time
                onDatePicked(periodFrom)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun onDatePicked(date: Long) {
        HeyChargeSDK.sessions().observeSessionsFromDate(
            date,
            this
        )
        val periodText = simpleDateFormat.format(Date(date))
        pickedPeriod.text = "Start date is $periodText"
    }

    private fun getInitialStartDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -31)
        return calendar.time.time
    }

    override fun onGetDataSuccess(data: List<Session>) {
        adapter.submitList(data)
    }

    override fun onGetDataFailure(exception: Exception) {
        Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HeyChargeSDK.sessions().removeSessionsObserver(this)
    }
}