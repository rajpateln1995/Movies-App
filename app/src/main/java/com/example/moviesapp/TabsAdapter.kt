package com.example.moviesapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MoviesListFragment(Constants.LATEST)
            1 -> MoviesListFragment(Constants.POPULAR)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }


}