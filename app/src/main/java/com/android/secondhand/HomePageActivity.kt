package com.android.secondhand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomePageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // set up Toolbar
        setSupportActionBar(findViewById(R.id.home_page_toolbar))

        val categories = listOf<String>("All", "Household", "Furniture", "Books & Supplies", "Electronics", "Cars")
//        "Clothing & Shoes", "Sports & Outdoors", "Accessories", "Pet Supplies", "Musical Instruments", "Games & Toys", "Others")

        val tabs = findViewById<TabLayout>(R.id.tabs_container)

        // bind ViewPager and PostsPagerAdapter
        val viewPager = findViewById<ViewPager2>(R.id.recyclerview_container)
        viewPager.adapter = PostsPagerAdapter(this)

        // bind ViewPager and TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
    }
}