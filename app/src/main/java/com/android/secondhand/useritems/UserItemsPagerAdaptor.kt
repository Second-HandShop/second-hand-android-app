package com.android.secondhand.useritems

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
class UserItemsPagerAdaptor(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private var fragmentList : ArrayList<UserItemFragment> = arrayListOf()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: UserItemFragment) {
        this.fragmentList.add(fragment)
    }
}