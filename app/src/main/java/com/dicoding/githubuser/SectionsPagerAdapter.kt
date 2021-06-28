package com.dicoding.githubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.fragment.FollowerFragment
import com.dicoding.githubuser.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        val mBundle = Bundle()
        mBundle.putString(DetailActivity.EXTRA_NAME, username)

        when (position) {
            0 -> {
                fragment = FollowerFragment()
                fragment.arguments = mBundle
            }
            1 -> {
                fragment = FollowingFragment()
                fragment.arguments = mBundle
            }

        }
        return fragment as Fragment
    }
}