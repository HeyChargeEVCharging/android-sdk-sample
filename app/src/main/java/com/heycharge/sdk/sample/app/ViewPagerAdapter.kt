package com.heycharge.sdk.sample.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.heycharge.sdk.sample.app.admin.AdminChargersFragment
import com.heycharge.sdk.sample.app.chargers.ChargersFragment
import com.heycharge.sdk.sample.app.sessions.SessionsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3


    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return ChargersFragment()
        }
        if (position == 1) {
            return SessionsFragment()
        }
        return AdminChargersFragment()
    }
}