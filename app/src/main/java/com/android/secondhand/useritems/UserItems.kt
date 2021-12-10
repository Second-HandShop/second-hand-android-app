package com.android.secondhand.useritems

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.android.secondhand.R
import com.android.secondhand.editPage.ItemEditPageActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserItems : AppCompatActivity() {

    private lateinit var editButton: FloatingActionButton
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var tabNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_items)

        // set up Toolbar
        setSupportActionBar(findViewById(R.id.userItemToolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //configure tabs
        viewPager = findViewById(R.id.viewPager)
        addTabs()
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        //click handler for edit button
        editButton = findViewById(R.id.edit_button)
        editButton.setOnClickListener {
            val intent = Intent(this, ItemEditPageActivity::class.java)
            startActivity(intent)
        }
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