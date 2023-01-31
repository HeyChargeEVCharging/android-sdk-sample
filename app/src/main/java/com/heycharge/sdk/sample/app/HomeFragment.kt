package com.heycharge.sdk.sample.app

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.heycharge.androidsdk.core.HeyChargeSDK

class HomeFragment : Fragment() {

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabs)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePermissions()
    }

    private fun handlePermissions() {
        val permissionsList = mutableListOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsList.add(android.Manifest.permission.BLUETOOTH_SCAN)
            permissionsList.add(android.Manifest.permission.BLUETOOTH_CONNECT)
        }
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions: Map<String, Boolean> ->
                val allGranted = !permissions.values.contains(false)
                if (allGranted) {
                    HeyChargeSDK.setUserId(
                        args.userId
                    )
                    initViews()
                }
            }
        requestPermissionLauncher.launch(permissionsList.toTypedArray())
    }

    private fun initViews() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Chargers"
                return@TabLayoutMediator
            }
            if (position == 1) {
                tab.text = "Sessions"
                return@TabLayoutMediator
            }
            if (position == 2) {
                tab.text = "Admin"
                return@TabLayoutMediator
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HeyChargeSDK.dispose()
    }


}