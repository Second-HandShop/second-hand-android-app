package com.android.secondhand

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class ItemsPagerAdapter(fa: FragmentActivity): androidx.viewpager2.adapter.FragmentStateAdapter(fa) {
    var itemsFragments = mutableMapOf<Int, ItemsFragment>()
    override fun getItemCount(): Int {
        // 6 categories (tabs)
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        val itemsFragment = ItemsFragment.newInstance(position, "")
        itemsFragments[position] = itemsFragment
        return itemsFragment
    }

    fun getItem(position: Int): ItemsFragment? {
        return itemsFragments[position]
    }
}