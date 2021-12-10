package com.android.secondhand.useritems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.android.secondhand.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserItems : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var tabNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_items)

        //configure tabs
        viewPager = findViewById(R.id.viewPager)
        addTabs()
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }

    private fun addTabs() {
        val adapter = UserItemsPagerAdaptor(this)

        //user's "in the market items"
        tabNames.add("In Market")
        adapter.addFragment(UserItemFragment.newInstance("IN_THE_MARKET"))

        //user's bought items
        tabNames.add("Bought")
        adapter.addFragment(UserItemFragment.newInstance("BOUGHT"))

        //user's sold items
        tabNames.add("Sold")
        adapter.addFragment(UserItemFragment.newInstance("SOLD"))

        viewPager.adapter = adapter
    }

}