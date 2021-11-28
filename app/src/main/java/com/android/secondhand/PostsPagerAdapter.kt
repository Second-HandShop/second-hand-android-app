package com.android.secondhand

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class PostsPagerAdapter(fa: FragmentActivity): androidx.viewpager2.adapter.FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        // 6 categories (tabs)
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return PostsFragment.newInstance(position)
    }
}