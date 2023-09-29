package com.example.demovz.ui.event.adapter.eventsListAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class EventPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    fun getTabTitle(position : Int): String{
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}