package com.android.secondhand.homepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.android.secondhand.editPage.ItemEditPageActivity
import com.android.secondhand.R
import com.android.secondhand.login.LoginActivity
import com.android.secondhand.showPage.ItemShowPageActivity
import com.android.secondhand.useritems.UserItems
import com.cloudinary.android.MediaManager
import com.firebase.client.Firebase
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth


class HomePageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    //Declare an instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth

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
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )

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
        val viewPagerAdapter = ItemsPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // bind ViewPager and TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()

        var searchBar: SearchView = findViewById(R.id.searchBar)

        searchBar.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    (viewPagerAdapter.getItem(viewPager.currentItem))?.updateData(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )

        auth = FirebaseAuth.getInstance()
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

            }
            R.id.userItems -> {
                val intent = Intent(this, UserItems::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                auth.signOut()
                Toast.makeText(this, "User Logged out Successfully.",
                    Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}