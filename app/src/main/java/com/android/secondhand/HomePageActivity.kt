package com.android.secondhand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.cloudinary.android.MediaManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomePageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    companion object{
        var hasInitialized = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // set up Toolbar
        setSupportActionBar(findViewById(R.id.home_page_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        // set up Navigation Drawer Menu
        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState();

        // Set click listener on Navigation Drawer Menu
        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this);


        // set up cloudinary
        if(!hasInitialized){
            val config = hashMapOf<String, String>()
            config["cloud_name"] = "dqg4lzcl8"
            config["api_key"] = "736355668776457"
            config["api_secret"] = "ngcVM7KIwnAiFK9dQXlzkEAxm2I"
            MediaManager.init(this, config)
            hasInitialized = true
        }


        val categories = listOf<String>("All", "Household", "Furniture", "Books & Supplies", "Electronics", "Cars")
//        "Clothing & Shoes", "Sports & Outdoors", "Accessories", "Pet Supplies", "Musical Instruments", "Games & Toys", "Others")

        val tabs = findViewById<TabLayout>(R.id.tabs_container)

        // bind ViewPager and PostsPagerAdapter
        val viewPager = findViewById<ViewPager2>(R.id.recyclerview_container)
        viewPager.adapter = ItemsPagerAdapter(this)

        // bind ViewPager and TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
    }


    // click handlers for the icons on the Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    // when back button is pressed, close drawer (if open)
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START) }
        else super.onBackPressed()
    }

    // click handlers for the menu items on Navigation Drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.to_be_deleted -> {
                val intent = Intent(this, ItemEditPageActivity::class.java)
                startActivity(intent)
            }

        }
        return true
    }

}