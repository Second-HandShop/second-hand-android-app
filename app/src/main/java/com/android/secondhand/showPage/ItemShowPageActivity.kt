package com.android.secondhand.showPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.secondhand.R

class ItemShowPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_page)

        // set up collapsing toolbar
        setSupportActionBar(findViewById(R.id.show_page_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }
}